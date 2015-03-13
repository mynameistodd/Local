package com.mynameistodd.local.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.mynameistodd.local.MainActivity;
import com.mynameistodd.local.R;
import com.mynameistodd.local.utils.MyRequestHandler;
import com.mynameistodd.local.utils.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class ReceiveTransitionsIntentService extends IntentService {
    /**
     * Sets an identifier for the service
     */
    public ReceiveTransitionsIntentService() {
        super("ReceiveTransitionsIntentService");
    }
    /**
     * Handles incoming intents
     *@param intent The Intent sent by Location Services. This
     * Intent is provided
     * to Location Services (inside a PendingIntent) when you call
     * addGeofences()
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // First check for errors
        if (LocationClient.hasError(intent)) {
            // Get the error code with a static method
            int errorCode = LocationClient.getErrorCode(intent);
            // Log the error
            Log.e("ReceiveTransitionsIntentService",
                    "Location Services error: " + Integer.toString(errorCode));
            /*
             * You can also send the error code to an Activity or
             * Fragment with a broadcast Intent
             */
            /*
             * If there's no error, get the transition type and the IDs
             * of the geofence or geofences that triggered the transition
             */
        } else {
            // Get the type of transition (entry or exit)
            int transitionType = LocationClient.getGeofenceTransition(intent);
            // Test that a valid transition was reported
            if ((transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) || (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) || (transitionType == Geofence.GEOFENCE_TRANSITION_DWELL)) {
                List<Geofence> triggerList = LocationClient.getTriggeringGeofences(intent);

                List<String> triggerIds = new ArrayList<String>();

                for (Geofence geofence : triggerList) {
                    triggerIds.add(geofence.getRequestId());
                }

                new PlaceAsyncTask().execute(triggerIds);
            }
            // An invalid transition was reported
            else {
                Log.e("ReceiveTransitionsIntentService", "Geofence transition error: " + Integer.toString(transitionType));
            }
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
            final NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            final TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            for (Place place : places) {
                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(place.getName())
                        .setContentText(place.getVicinity())
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                String logoUrl = place.getIconUrl();

                Picasso.with(getApplicationContext()).load(logoUrl).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mBuilder.setLargeIcon(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

                mNotificationManager.notify(1, mBuilder.build());
            }
        }
    }
}