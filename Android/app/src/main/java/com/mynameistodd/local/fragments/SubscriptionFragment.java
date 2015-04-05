package com.mynameistodd.local.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mynameistodd.local.LocalApplication;
import com.mynameistodd.local.R;
import com.mynameistodd.local.adapters.SubscriptionRecyclerAdapter;
import com.mynameistodd.local.utils.MyRequestHandler;
import com.mynameistodd.local.utils.Util;
import com.parse.ParseInstallation;

import java.util.ArrayList;
import java.util.List;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class SubscriptionFragment extends Fragment implements SubscriptionRecyclerAdapter.IAdapterClicks {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private SubscriptionRecyclerAdapter.IAdapterClicks mAdapterClicks;

    private List<String> mSubscribedChannels;
    private List<Place> mPlaces;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mItemDecoration;

    private ProgressBar mProgressBar;
    private TextView mEmptyText;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SubscriptionFragment() {
    }

    // TODO: Rename and change types of parameters
    public static SubscriptionFragment newInstance(String param1, String param2) {
        SubscriptionFragment fragment = new SubscriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAdapterClicks = (SubscriptionRecyclerAdapter.IAdapterClicks) this;

        // Get tracker.
        Tracker t = ((LocalApplication) getActivity().getApplication()).getTracker(LocalApplication.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName(SubscriptionFragment.class.getSimpleName());

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mEmptyText = (TextView) view.findViewById(R.id.empty);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);

        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.addItemDecoration(mItemDecoration);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(R.string.subscribed);
        mSubscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");
        mPlaces = new ArrayList<>();

        if (mSubscribedChannels != null && !mSubscribedChannels.isEmpty()) {
            new PlaceAsyncTask().execute(mSubscribedChannels);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyText.setText(getString(R.string.empty));
        }
    }

    private class PlaceAsyncTask extends AsyncTask<List<String>, Void, List<Place>> {
        @Override
        protected List<Place> doInBackground(List<String>... params) {
            List<Place> results = new ArrayList<>();
            GooglePlaces client = new GooglePlaces(Util.PLACES_API_KEY, new MyRequestHandler());
            for (String placeId : params[0]) {
                Place place = client.getPlaceById(placeId);
                results.add(place);
            }
            return results;
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);

            mPlaces = places;
            mAdapter = new SubscriptionRecyclerAdapter(getActivity(), mPlaces, mAdapterClicks);
            mRecyclerView.setAdapter(mAdapter);
            mProgressBar.setVisibility(View.GONE);
        }
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

    @Override
    public void onItemClick(int position) {
        mListener.onSubscriptionItemClick(mPlaces.get(position));
    }

    public interface OnFragmentInteractionListener {
        public void onSubscriptionItemClick(Place place);
    }

}
