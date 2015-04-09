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
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.mynameistodd.local.MainActivity;
import com.mynameistodd.local.R;
import com.mynameistodd.local.models.Business;
import com.mynameistodd.local.utils.MyRequestHandler;
import com.mynameistodd.local.utils.Util;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class ReceiveTransitionsIntentService extends IntentService {
    /**
     * Sets an identifier for the service
     */
    public ReceiveTransitionsIntentService() {
        super("ReceiveTransitionsIntentService");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    /**
     * Handles incoming intents
     *
     * @param intent The Intent sent by Location Services. This
     *               Intent is provided
     *               to Location Services (inside a PendingIntent) when you call
     *               addGeofences()
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e(Util.TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        Map<String, String> details = new HashMap<>();
        details.put("transitionType", String.valueOf(geofenceTransition));
        details.put("transitionTime", String.valueOf(Calendar.getInstance().getTimeInMillis()));
        ParseAnalytics.trackEventInBackground("geofence", details);

        // Test that the reported transition was of interest.
        if ((geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) ||
            (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) ||
            (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL)) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            List<String> triggerIds = new ArrayList<>();
            for (Geofence geofence : triggeringGeofences) {
                triggerIds.add(geofence.getRequestId());
            }

            new PlaceAsyncTask().execute(triggerIds);
        } else {
            // Log the error.
            Log.e(Util.TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            final PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            for (final Place place : places) {
                ParseQuery<com.mynameistodd.local.models.Geofence> query = ParseQuery.getQuery(com.mynameistodd.local.models.Geofence.class);
                query.whereEqualTo("placeId", place.getPlaceId());
                query.getFirstInBackground(new GetCallback<com.mynameistodd.local.models.Geofence>() {
                    @Override
                    public void done(com.mynameistodd.local.models.Geofence geofence, ParseException e) {
                        if (geofence != null) {

                            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle(place.getName())
                                    .setContentText(geofence.getText())
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
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}