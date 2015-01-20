package com.mynameistodd.local.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mynameistodd.local.models.Business;
import com.mynameistodd.local.R;
import com.mynameistodd.local.utils.Util;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditBusinessFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditBusinessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditBusinessFragment extends Fragment {
    private static final String ARG_OBJECTID = "ARG_OBJECTID";

    private String mObjectId;

    private OnFragmentInteractionListener mListener;

    private Business mBusiness;
    private ImageView mEditDetailBusinessStaticMap;
    private ImageView mEditDetailBusinessLogo;
    private EditText mEditDetailBusinessName;
    private EditText mEditDetailBusinessSnippet;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param objectId The Parse Business objectId.
     * @return A new instance of fragment EditBusinessFragment.
     */
    public static EditBusinessFragment newInstance(String objectId) {
        EditBusinessFragment fragment = new EditBusinessFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OBJECTID, objectId);
        fragment.setArguments(args);
        return fragment;
    }

    public EditBusinessFragment() {
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
        View view = inflater.inflate(R.layout.fragment_edit_business, container, false);

        mEditDetailBusinessStaticMap = (ImageView) view.findViewById(R.id.edit_detail_business_map);
        mEditDetailBusinessLogo = (ImageView) view.findViewById(R.id.edit_detail_business_logo);
        mEditDetailBusinessName = (EditText) view.findViewById(R.id.edit_detail_business_name);
        mEditDetailBusinessSnippet = (EditText) view.findViewById(R.id.edit_detail_business_snippet);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.action_edit);
        inflater.inflate(R.menu.edit_business, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        if (mBusiness != null) {
            int w = mEditDetailBusinessStaticMap.getWidth() / 2;
            int h = mEditDetailBusinessStaticMap.getHeight() / 2;
            String uri = Util.MAP_BASE_URI;
            uri = uri.concat("&center=" + mBusiness.getLocation().getLatitude() + "," + mBusiness.getLocation().getLongitude() + "");
            uri = uri.concat("&size=" + w + "x" + h + "&markers=color:green%7Clabel:P%7C" + mBusiness.getLocation().getLatitude() + "," + mBusiness.getLocation().getLongitude()+"");

            Log.d(Util.TAG, "Static Map: " + uri);

            ParseFile file = mBusiness.getLogo();
            String logoUrl = file.getUrl();

            Picasso.with(getActivity()).load(uri).into(mEditDetailBusinessStaticMap);
            Picasso.with(getActivity()).load(logoUrl).into(mEditDetailBusinessLogo);
            mEditDetailBusinessName.setText(mBusiness.getName());
            mEditDetailBusinessSnippet.setText(mBusiness.getSnippet());
        }
    }

    private void saveData() {
        if (mBusiness != null) {
            mBusiness.setName(mEditDetailBusinessName.getText().toString());
            mBusiness.setSnippet(mEditDetailBusinessSnippet.getText().toString());
            mBusiness.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                    }
                }
            });
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
