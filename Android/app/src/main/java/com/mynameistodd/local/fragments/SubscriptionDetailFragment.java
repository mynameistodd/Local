package com.mynameistodd.local.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mynameistodd.local.LocalApplication;
import com.mynameistodd.local.R;
import com.mynameistodd.local.models.Business;
import com.mynameistodd.local.utils.MyRequestHandler;
import com.mynameistodd.local.utils.Util;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubscriptionDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubscriptionDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionDetailFragment extends Fragment {
    private static final String ARG_OBJECTID = "ARG_OBJECTID";

    private String mPlaceId;
    private Boolean mEditable = false;

    private OnFragmentInteractionListener mListener;

    private Place mPlace;
    private ImageView mDetailBusinessStaticMap;
    private ImageView mDetailBusinessLogo;
    private TextView mDetailBusinessName;
    private TextView mDetailBusinessSnippet;
    private Button mDetailBusinessUnsubscribe;

    public SubscriptionDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param objectId The Parse Business objectId.
     * @return A new instance of fragment SubscriptionDetailFragment.
     */
    public static SubscriptionDetailFragment newInstance(String objectId) {
        SubscriptionDetailFragment fragment = new SubscriptionDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OBJECTID, objectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlaceId = getArguments().getString(ARG_OBJECTID);
            new PlaceAsyncTask().execute(mPlaceId);

            Map<String, String> details = new HashMap<>();
            details.put("detail", mPlaceId);
            ParseAnalytics.trackEventInBackground("view", details);

            // Get tracker.
            Tracker t = ((LocalApplication) getActivity().getApplication()).getTracker(LocalApplication.TrackerName.APP_TRACKER);

            // Set screen name.
            t.setScreenName(SubscriptionDetailFragment.class.getSimpleName());

            // Send a screen view.
            t.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    private class PlaceAsyncTask extends AsyncTask<String, Void, Place> {
        @Override
        protected Place doInBackground(String... params) {
            GooglePlaces client = new GooglePlaces(Util.PLACES_API_KEY, new MyRequestHandler());
            Place place = client.getPlaceById(params[0]);
            return place;
        }

        @Override
        protected void onPostExecute(Place place) {
            super.onPostExecute(place);
            mPlace = place;
            setData();
            setEditable();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription_detail_cardview, container, false);

        mDetailBusinessStaticMap = (ImageView) view.findViewById(R.id.detail_business_map);
        mDetailBusinessLogo = (ImageView) view.findViewById(R.id.detail_business_logo);
        mDetailBusinessName = (TextView) view.findViewById(R.id.detail_business_name);
        mDetailBusinessSnippet = (TextView) view.findViewById(R.id.detail_business_snippet);
        mDetailBusinessUnsubscribe = (Button) view.findViewById(R.id.detail_business_unsubscribe);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mEditable) {
            inflater.inflate(R.menu.subscription_detail, menu);
            ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(R.string.my_business);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, EditBusinessFragment.newInstance(mPlace.getPlaceId()))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack("editBusinessFragment")
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        if (mPlace != null) {
            int w = mDetailBusinessStaticMap.getWidth() / 2;
            int h = mDetailBusinessStaticMap.getHeight() / 2;
            String uri = Util.MAP_BASE_URI;
            uri = uri.concat("&center=" + mPlace.getLatitude() + "," + mPlace.getLongitude() + "");
            uri = uri.concat("&size=" + w + "x" + h + "&markers=color:red%7Clabel:L%7C" + mPlace.getLatitude() + "," + mPlace.getLongitude() + "");

            Log.d(Util.TAG, "Static Map: " + uri);

            String logoUrl = mPlace.getIconUrl();
            Picasso.with(getActivity()).load(logoUrl).into(mDetailBusinessLogo);


            Picasso.with(getActivity()).load(uri).into(mDetailBusinessStaticMap);
            mDetailBusinessName.setText(mPlace.getName());
            mDetailBusinessSnippet.setText(mPlace.getVicinity());

            final String channelId = mPlace.getPlaceId();
            mDetailBusinessUnsubscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParsePush.unsubscribeInBackground(channelId, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d(Util.TAG, "Successfully unsubscribed from " + channelId);
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                    if (mListener != null) {
                        mListener.onSubscribe(false, channelId);
                    }
                    getFragmentManager().popBackStack();
                }
            });
        }
    }

    private void setEditable() {
        ParseRelation<Business> businesses = ParseUser.getCurrentUser().getRelation("Business");
        businesses.getQuery().getFirstInBackground(new GetCallback<Business>() {
            @Override
            public void done(Business business, ParseException e) {
                if (business != null) {
                    if (mPlace.getPlaceId().equals(business.getObjectId())) {
                        mEditable = true;
                        getActivity().invalidateOptionsMenu();
                    }
                } else if (e != null) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);

        public void onSubscribe(Boolean subscribe, String channelId);
    }

}
