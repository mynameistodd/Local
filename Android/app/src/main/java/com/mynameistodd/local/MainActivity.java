package com.mynameistodd.local;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.mynameistodd.local.fragments.AboutFragment;
import com.mynameistodd.local.fragments.EditBusinessFragment;
import com.mynameistodd.local.fragments.ErrorDialogFragment;
import com.mynameistodd.local.fragments.MessageFragment;
import com.mynameistodd.local.fragments.NavigationDrawerFragment;
import com.mynameistodd.local.fragments.SubscriptionDetailFragment;
import com.mynameistodd.local.fragments.SubscriptionFragment;
import com.mynameistodd.local.models.Business;
import com.mynameistodd.local.services.ReceiveTransitionsIntentService;
import com.mynameistodd.local.utils.MyRequestHandler;
import com.mynameistodd.local.utils.Util;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.List;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;


public class MainActivity extends ActionBarActivity
        implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        SubscriptionFragment.OnFragmentInteractionListener,
        SubscriptionDetailFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener,
        MessageFragment.OnFragmentInteractionListener,
        EditBusinessFragment.OnFragmentInteractionListener,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        ResultCallback {

    List<Geofence> mGeofenceList;
    // Store the list of geofence Ids to remove
    List<String> mGeofencesToRemove;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;
    // Holds the location client

    private GoogleApiClient mGoogleApiClient;
    // Stores the PendingIntent used to request geofence monitoring
    private PendingIntent mGeofenceRequestIntent;
    private REQUEST_TYPE mRequestType;
    // Flag that indicates if a request is underway.
    private boolean mInProgress;
    private Business mMyBusiness;

    int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser() == null) {
            ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
            startActivityForResult(builder.build(), 0);
        }

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        mInProgress = false;
        mRequestType = REQUEST_TYPE.UNKNOWN;
        mGeofenceList = new ArrayList<Geofence>();

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        // Create a GoogleApiClient instance
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
//                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);

        List<String> channelIds = ParseInstallation.getCurrentInstallation().getList("channels");
        addGeofence(channelIds);

//        if (ParseUser.getCurrentUser() != null) {
//            ParseRelation<Business> businesses = ParseUser.getCurrentUser().getRelation("Business");
//            businesses.getQuery().getFirstInBackground(new GetCallback<Business>() {
//                @Override
//                public void done(Business business, ParseException e) {
//                    if (business != null) {
//                        mMyBusiness = business;
//                    } else if (e != null) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                switch (resultCode) {
                    case RESULT_OK:
                        Toast.makeText(this, "Logged in!", Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, this);

                    final String channelId = place.getId();
                    ParsePush.subscribeInBackground(channelId, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                onSubscribe(true, channelId);
                                Log.d(Util.TAG, "Successfully subscribed to " + channelId);
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });

                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(String tab) {
        FragmentManager fragmentManager = getFragmentManager();

        if (tab.equals(getString(R.string.subscribed))) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, SubscriptionFragment.newInstance("", ""))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        } else if (tab.equals(getString(R.string.map))) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Context context = getApplicationContext();
            try {
                startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        } else if (tab.equals(getString(R.string.my_business))) {
            if (mMyBusiness != null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, SubscriptionDetailFragment.newInstance(mMyBusiness.getObjectId()))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack("subscriptionDetailFragment")
                        .commit();
            }
        } else if (tab.equals(getString(R.string.send_message))) {
            if (mMyBusiness != null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MessageFragment.newInstance("", ""))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack("messageFragment")
                        .commit();
            }
        } else if (tab.equals(getString(R.string.about))) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, AboutFragment.newInstance("", ""))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack("aboutFragment")
                    .commit();
        } else if (tab.equals(getString(R.string.logout))) {
            ParseUser.getCurrentUser().logOut();
            ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
            startActivityForResult(builder.build(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            //restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

//    public void restoreActionBar() {
//        ActionBar actionBar = getActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setTitle(mTitle);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSubscriptionItemClick(Place place) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, SubscriptionDetailFragment.newInstance(place.getPlaceId()))
                .addToBackStack("subscriptionDetailFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSubscribe(Boolean subscribe, String channelId) {
        if (subscribe) {
            addGeofence(channelId);
        } else {
            removeGeofence(channelId);
        }
    }

    /*
     * Provide the implementation of ConnectionCallbacks.onConnected()
     * Once the connection is available, send a request to add the
     * Geofences
     */
    @Override
    public void onConnected(Bundle bundle) {
        switch (mRequestType) {
            case ADD:
                // Send a request to add the current geofences
                if (mGeofenceList.size() > 0) {
                    //mLocationClient.addGeofences(mGeofenceList, mGeofenceRequestIntent, this);
                    LocationServices.GeofencingApi.addGeofences(
                            mGoogleApiClient,
                            getGeofencingRequest(),
                            getTransitionPendingIntent()
                    ).setResultCallback(this);
                }
                break;
            case REMOVE_INTENT:
                //mLocationClient.removeGeofences(mGeofenceRequestIntent, this);
                LocationServices.GeofencingApi.removeGeofences(
                        mGoogleApiClient,
                        getTransitionPendingIntent()
                ).setResultCallback(this);
                break;
            case REMOVE_LIST:
                //mLocationClient.removeGeofences(mGeofencesToRemove, this);
                LocationServices.GeofencingApi.removeGeofences(
                        mGoogleApiClient,
                        mGeofencesToRemove
                ).setResultCallback(this);
                break;
        }
    }

    @Override
    public void onResult(Result result) {
        Log.d(Util.TAG, "Geofence Result: " + GeofenceStatusCodes.getStatusCodeString(result.getStatus().getStatusCode()));
        mInProgress = false;
        mGoogleApiClient.disconnect();
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Turn off the request flag
        mInProgress = false;
        // Destroy the current location client
//        mLocationClient = null;
        Log.d(Util.TAG, "GoogleApiClient Suspended");
    }

//    @Override
//    public void onDisconnected() {
//        // Turn off the request flag
//        mInProgress = false;
//        // Destroy the current location client
//        mLocationClient = null;
//    }

//    @Override
//    public void onAddGeofencesResult(int statusCode, String[] strings) {
//        // If adding the geofences was successful
//        if (LocationStatusCodes.SUCCESS == statusCode) {
//            for (String fence : strings) {
//                Log.d(Util.TAG, "Successfully added geofence: " + fence);
//            }
//        } else {
//            Log.e(Util.TAG, "Error adding geofence: " + statusCode);
//        }
//        // Turn off the in progress flag and disconnect the client
//        mInProgress = false;
//        //mLocationClient.disconnect();
//        mGoogleApiClient.disconnect();
//    }

//    /**
//     * When the request to remove geofences by PendingIntent returns,
//     * handle the result.
//     *
//     * @param statusCode    the code returned by Location Services
//     * @param requestIntent The Intent used to request the removal.
//     */
//    @Override
//    public void onRemoveGeofencesByPendingIntentResult(int statusCode, PendingIntent requestIntent) {
//        // If removing the geofences was successful
//        if (statusCode == LocationStatusCodes.SUCCESS) {
//            Log.d(Util.TAG, "Successfully removed geofences with request intent. ");
//        } else {
//            Log.e(Util.TAG, "Error removing geofence: " + statusCode);
//        }
//        /*
//         * Disconnect the location client regardless of the
//         * request status, and indicate that a request is no
//         * longer in progress
//         */
//        mInProgress = false;
//        //mLocationClient.disconnect();
//        mGoogleApiClient.disconnect();
//    }

//    /**
//     * When the request to remove geofences by IDs returns, handle the
//     * result.
//     *
//     * @param statusCode         The code returned by Location Services
//     * @param geofenceRequestIds The IDs removed
//     */
//    @Override
//    public void onRemoveGeofencesByRequestIdsResult(int statusCode, String[] geofenceRequestIds) {
//        // If removing the geocodes was successful
//        if (LocationStatusCodes.SUCCESS == statusCode) {
//            for (String fence : geofenceRequestIds) {
//                Log.d(Util.TAG, "Successfully removed geofence: " + fence);
//            }
//        } else {
//            Log.e(Util.TAG, "Error removing geofence: " + statusCode);
//        }
//        // Indicate that a request is no longer in progress
//        mInProgress = false;
//        // Disconnect the location client
////        mLocationClient.disconnect();
//        mGoogleApiClient.disconnect();
//    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Turn off the request flag
        mInProgress = false;
        Log.d(Util.TAG, "GoogleApiClient Connection Failed");
        /*
         * If the error has a resolution, start a Google Play services
         * activity to resolve it.
         */
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, Util.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
            // If no resolution is available, display an error dialog
        } else {
            // Get the error code
            int errorCode = connectionResult.getErrorCode();
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    errorCode,
                    this,
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
                        "Geofence Detection");
            }
        }
    }

    /*
     * Create a PendingIntent that triggers an IntentService in your
     * app when a geofence transition occurs.
     */
    private PendingIntent getTransitionPendingIntent() {
        if (mGeofenceRequestIntent != null) {
            return mGeofenceRequestIntent;
        }

        // Create an explicit Intent
        Intent intent = new Intent(this, ReceiveTransitionsIntentService.class);

        mGeofenceRequestIntent = PendingIntent.getService(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofenceRequestIntent;
    }

    public void addGeofence(String channelId) {
        List<String> channelIds = new ArrayList<String>();
        channelIds.add(channelId);
        addGeofence(channelIds);
    }

    public void addGeofence(List<String> channelIds) {
        // Start a request to add geofences
        mRequestType = REQUEST_TYPE.ADD;
        /*
         * Test for Google Play services after setting the request type.
         * If Google Play services isn't present, the proper request
         * can be restarted.
         */
        if (!Util.servicesConnected(this, getFragmentManager())) {
            return;
        }
        /*
         * Create a new location client object. Since the current
         * activity class implements ConnectionCallbacks and
         * OnConnectionFailedListener, pass the current activity object
         * as the listener for both parameters
         */
//        mLocationClient = new LocationClient(this, this, this);

        if (channelIds != null && !mInProgress) {
            // Indicate that a request is underway
            mInProgress = true;

            new PlaceAsyncTask().execute(channelIds);

        } else {
        /*
         * A request is already underway. You can handle
         * this situation by disconnecting the client,
         * re-setting the flag, and then re-trying the
         * request.
         */
        }
    }

    private class PlaceAsyncTask extends AsyncTask<List<String>, Void, List<Place>> {
        @Override
        protected List<Place> doInBackground(List<String>... params) {
            List<Place> results = new ArrayList<>();
            GooglePlaces client = new GooglePlaces(Util.PLACES_API_KEY, new MyRequestHandler());
            for (String channelId : params[0]) {
                Place place = client.getPlaceById(channelId);
                results.add(place);
            }

            return results;
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);
            for (Place place : places) {
                mGeofenceList.add(Util.getGeofence(place));
            }

//            mLocationClient.connect();
            mGoogleApiClient.connect();
        }
    }

//    public void removeGeofence(PendingIntent requestIntent) {
//        // Record the type of removal request
//        mRequestType = REQUEST_TYPE.REMOVE_INTENT;
//        /*
//         * Test for Google Play services after setting the request type.
//         * If Google Play services isn't present, the request can be
//         * restarted.
//         */
//        if (!Util.servicesConnected(this, getFragmentManager())) {
//            return;
//        }
//        // Store the PendingIntent
//        mGeofenceRequestIntent = requestIntent;
//        /*
//         * Create a new location client object. Since the current
//         * activity class implements ConnectionCallbacks and
//         * OnConnectionFailedListener, pass the current activity object
//         * as the listener for both parameters
//         */
////        mLocationClient = new LocationClient(this, this, this);
//        // If a request is not already underway
//        if (!mInProgress) {
//            // Indicate that a request is underway
//            mInProgress = true;
//            // Request a connection from the client to Location Services
////            mLocationClient.connect();
//            mGoogleApiClient.connect();
//        } else {
//            /*
//             * A request is already underway. You can handle
//             * this situation by disconnecting the client,
//             * re-setting the flag, and then re-trying the
//             * request.
//             */
//        }
//    }

    public void removeGeofence(String channelId) {
        List<String> channelIds = new ArrayList<String>();
        channelIds.add(channelId);
        removeGeofence(channelIds);
    }

    public void removeGeofence(List<String> channelIds) {
        // If Google Play services is unavailable, exit
        // Record the type of removal request
        mRequestType = REQUEST_TYPE.REMOVE_LIST;
        /*
         * Test for Google Play services after setting the request type.
         * If Google Play services isn't present, the request can be
         * restarted.
         */
        if (!Util.servicesConnected(this, getFragmentManager())) {
            return;
        }
        // Store the list of geofences to remove
        mGeofencesToRemove = channelIds;
        /*
         * Create a new location client object. Since the current
         * activity class implements ConnectionCallbacks and
         * OnConnectionFailedListener, pass the current activity object
         * as the listener for both parameters
         */
//        mLocationClient = new LocationClient(this, this, this);
        // If a request is not already underway
        if (!mInProgress) {
            // Indicate that a request is underway
            mInProgress = true;
            // Request a connection from the client to Location Services
//            mLocationClient.connect();
            mGoogleApiClient.connect();
        } else {
            /*
             * A request is already underway. You can handle
             * this situation by disconnecting the client,
             * re-setting the flag, and then re-trying the
             * request.
             */
        }
    }

    // Defines the allowable request types.
    public enum REQUEST_TYPE {
        ADD,
        REMOVE_INTENT,
        REMOVE_LIST,
        UNKNOWN
    }
}
