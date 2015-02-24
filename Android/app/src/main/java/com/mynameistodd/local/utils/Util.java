package com.mynameistodd.local.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.mynameistodd.local.R;
import com.mynameistodd.local.fragments.ErrorDialogFragment;
import com.mynameistodd.local.models.Business;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Created by todd on 10/14/14.
 */
public class Util {
    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = "MYNAMEISTODD.LOCAL";
    public static final String MAP_STATIC_API_KEY = "AIzaSyAxuSUZ7-cXhCDi--yo-XLxy3WtpBHb4bU"; //TODO all this map stuff should be in a static method to use the resource value.
    public static final String MAP_BASE_URI = "https://maps.googleapis.com/maps/api/staticmap?key=" + MAP_STATIC_API_KEY + "&maptype=roadmap&zoom=16&scale=2";
    public static final String PLACES_API_KEY = "AIzaSyC-0QDSRvVHsX5T8ysLIN5Farm75xXheRM";

    public static boolean servicesConnected(Context context, Fragment fragment) {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    fragment.getActivity(),
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(fragment.getFragmentManager(), "Location Updates");
            }
            return false;
        }
    }

    public static boolean servicesConnected(Activity activity, FragmentManager fragmentManager) {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    activity,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(fragmentManager, "Location Updates");
            }
            return false;
        }
    }

    /**
     * Creates a Location Services Geofence object from a
     * Business object.
     *
     * @return A Geofence object
     */
    public static Geofence getGeofence(Business business) {
        return new Geofence.Builder()
                .setRequestId(business.getChannelId())
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL) //TODO: Should come from parse entry
                .setCircularRegion(business.getLocation().getLatitude(), business.getLocation().getLongitude(), 50) //TODO: Should come from parse entry
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(60000)
                .setNotificationResponsiveness(60000)
                .build();
    }

    public static String[] getTabs(Context context) {
        if (ParseUser.getCurrentUser() != null) {
            ParseRelation<Business> businesses = ParseUser.getCurrentUser().getRelation("Business");
            try {
                int count = businesses.getQuery().count();
                if (count > 0) {
                    return new String[]{
                            context.getString(R.string.subscribed),
                            context.getString(R.string.map),
                            context.getString(R.string.my_business),
                            context.getString(R.string.send_message),
                            context.getString(R.string.about),
                            context.getString(R.string.logout)
                    };
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return new String[]{
                context.getString(R.string.subscribed),
                context.getString(R.string.map),
                context.getString(R.string.about),
                context.getString(R.string.logout)
        };
    }
}
