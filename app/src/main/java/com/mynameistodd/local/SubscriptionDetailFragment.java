package com.mynameistodd.local;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
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
    private static final String ARG_OBJECTID = "";

    private String mObjectId;

    private OnFragmentInteractionListener mListener;

    private Business mBusiness;
    private ImageView mDetailBusinessStaticMap;
    private TextView mDetailBusinessName;
    private TextView mDetailBusinessSnippet;

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
    public SubscriptionDetailFragment() {
        // Required empty public constructor
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

        mDetailBusinessStaticMap = (ImageView) view.findViewById(R.id.detail_map_static);
        mDetailBusinessName = (TextView) view.findViewById(R.id.detail_business_name);
        mDetailBusinessSnippet = (TextView) view.findViewById(R.id.detail_business_snippet);

        if (mBusiness != null) {
            setData();
        }
        return view;
    }

    private void setData() {
        if (mBusiness != null) {
            Picasso.with(getActivity())
                    .load("https://maps.googleapis.com/maps/api/staticmap?center="+mBusiness.getLocation().getLatitude()+","+mBusiness.getLocation().getLongitude()+"&zoom=15&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:C%7C"+mBusiness.getLocation().getLatitude()+","+mBusiness.getLocation().getLongitude()+"")
                    .into(mDetailBusinessStaticMap);
            mDetailBusinessName.setText(mBusiness.getName());
            mDetailBusinessSnippet.setText(mBusiness.getSnippet());
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
    }

}
