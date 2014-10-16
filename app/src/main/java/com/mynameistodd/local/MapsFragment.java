package com.mynameistodd.local;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import android.support.v4.app.FragmentActivity;

public class MapsFragment extends Fragment implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private final static int SUBSCRIBE_DIALOG_FRAGMENT_REQUEST = 1000;
    private final static int UNSUBSCRIBE_DIALOG_FRAGMENT_REQUEST = 2000;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private HashMap<Marker, String> mMarkers;
    private LocationClient mLocationClient;
    private List<String> mSubscribedChannels;
    private Fragment mThisFragment;

    public MapsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMarkers = new HashMap<Marker, String>();
        mLocationClient = new LocationClient(getActivity(), this, this);
        mSubscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");
        if (mSubscribedChannels == null) {
            mSubscribedChannels = new ArrayList<String>();
        }
        mThisFragment = this;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.servicesConnected(getActivity(), mThisFragment)) {
            mLocationClient.connect();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View myLayout = inflater.inflate(R.layout.activity_maps, container, false);
        return myLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMap != null) {
            getFragmentManager()
                    .beginTransaction()
                    .remove(getFragmentManager().findFragmentById(R.id.map))
                    .commit();
        }
    }

    @Override
    public void onStop() {
        if (mLocationClient.isConnected()) {
            mLocationClient.disconnect();
        }
        super.onStop();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String channelId = mMarkers.get(marker);
                String markerId = marker.getId();

                Bundle args = new Bundle();
                args.putString("channelId", channelId);
                args.putString("markerId", markerId);
                args.putBoolean("subscribed", mSubscribedChannels.contains(channelId));

                SubscribeDialogFragment subscribeDialogFragment = new SubscribeDialogFragment();
                subscribeDialogFragment.setArguments(args);
                subscribeDialogFragment.setTargetFragment(mThisFragment, mSubscribedChannels.contains(channelId) ? UNSUBSCRIBE_DIALOG_FRAGMENT_REQUEST : SUBSCRIBE_DIALOG_FRAGMENT_REQUEST);
                subscribeDialogFragment.show(getFragmentManager(), "SubscribeDialogFragment");
            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                updateMap(cameraPosition.target);
            }
        });
    }

    private void updateMap(LatLng latLng) {
        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(latLng.latitude, latLng.longitude);

        ParseQuery<Business> query = ParseQuery.getQuery(Business.class);
        //query.whereNear("location", parseGeoPoint);
        query.whereWithinMiles("location", parseGeoPoint, 1.0); //TODO Distance needs to be a preference
        query.findInBackground(new FindCallback<Business>() {
            @Override
            public void done(List<Business> businesses, ParseException e) {
                if (businesses != null) {
                    mMarkers.clear();
                    mMap.clear();
                    for (Business business : businesses) {
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(business.getLocation().getLatitude(), business.getLocation().getLongitude()))
                                .title(business.getName())
                                .snippet(business.getSnippet());

                        if (mSubscribedChannels.contains(business.getChannelId())) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        }

                        Marker marker = mMap.addMarker(markerOptions);
                        mMarkers.put(marker, business.getChannelId());
                    }
                } else if (e != null) {
                    e.printStackTrace();
                }

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Util.CONNECTION_FAILURE_RESOLUTION_REQUEST:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //try to get location again and update map.
                        break;
                }
                break;
            case SUBSCRIBE_DIALOG_FRAGMENT_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    final String channelId = data.getStringExtra("channelId");
                    String markerId = data.getStringExtra("markerId");

                    for (Marker marker : mMarkers.keySet()) {
                        if (marker.getId().equals(markerId)) {
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            break;
                        }
                    }
                    ParsePush.subscribeInBackground(channelId, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d(Util.TAG, "Successfully subscribed to " + channelId);
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                    mSubscribedChannels.add(channelId);
                }
                break;
            case UNSUBSCRIBE_DIALOG_FRAGMENT_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    final String channelId = data.getStringExtra("channelId");
                    String markerId = data.getStringExtra("markerId");

                    for (Marker marker : mMarkers.keySet()) {
                        if (marker.getId().equals(markerId)) {
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            break;
                        }
                    }
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
                    mSubscribedChannels.remove(channelId);
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = mLocationClient.getLastLocation();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));

        //mLocationClient.disconnect();
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        getActivity(),
                        Util.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            // Get the error code
            int errorCode = connectionResult.getErrorCode();
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    errorCode,
                    getActivity(),
                    Util.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(
                        getFragmentManager(),
                        "CurrentLocationFailed");
            }
        }
    }
}
