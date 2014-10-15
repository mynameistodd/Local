package com.mynameistodd.local;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;

/**
 * Created by todd on 10/14/14.
 */
public class Util {
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = "MYNAMEISTODD.LOCAL";

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
        // Build a new Geofence object
        return new Geofence.Builder()
                .setRequestId(business.getObjectId())
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER) //TODO: Should come from parse entry
                .setCircularRegion(business.getLocation().getLatitude(), business.getLocation().getLongitude(), 1) //TODO: Should come from parse entry
                .setExpirationDuration(86400000)
                .build();
    }
}
