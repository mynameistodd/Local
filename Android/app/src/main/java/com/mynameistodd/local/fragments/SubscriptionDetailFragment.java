package com.mynameistodd.local.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
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

import com.mynameistodd.local.R;
import com.mynameistodd.local.models.Business;
import com.mynameistodd.local.utils.Util;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubscriptionDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubscriptionDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SubscriptionDetailFragment extends Fragment {
    private static final String ARG_OBJECTID = "ARG_OBJECTID";

    private String mObjectId;
    private Boolean mEditable = false;

    private OnFragmentInteractionListener mListener;

    private Business mBusiness;
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
            mObjectId = getArguments().getString(ARG_OBJECTID);
        }

        ParseQuery<Business> query = ParseQuery.getQuery(Business.class);
        query.getInBackground(mObjectId, new GetCallback<Business>() {
            @Override
            public void done(Business business, ParseException e) {
                if (e == null) {
                    mBusiness = business;
                    setData();
                    setEditable();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription_detail, container, false);

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
            getActivity().getActionBar().setTitle(R.string.my_business);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, EditBusinessFragment.newInstance(mBusiness.getObjectId()))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack("editBusinessFragment")
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        if (mBusiness != null) {
            int w = mDetailBusinessStaticMap.getWidth() / 2;
            int h = mDetailBusinessStaticMap.getHeight() / 2;
            String uri = Util.MAP_BASE_URI;
            uri = uri.concat("&center=" + mBusiness.getLocation().getLatitude() + "," + mBusiness.getLocation().getLongitude() + "");
            uri = uri.concat("&size=" + w + "x" + h + "&markers=color:green%7Clabel:P%7C" + mBusiness.getLocation().getLatitude() + "," + mBusiness.getLocation().getLongitude()+"");

            Log.d(Util.TAG, "Static Map: " + uri);

            ParseFile file = mBusiness.getLogo();
            if (file != null) {
                String logoUrl = file.getUrl();
                Picasso.with(getActivity()).load(logoUrl).into(mDetailBusinessLogo);
            }


            Picasso.with(getActivity()).load(uri).into(mDetailBusinessStaticMap);
            mDetailBusinessName.setText(mBusiness.getName());
            mDetailBusinessSnippet.setText(mBusiness.getSnippet());

            final String channelId = mBusiness.getChannelId();
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
                    if (mBusiness.getObjectId().equals(business.getObjectId())) {
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
     * <p>
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
