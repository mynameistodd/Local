package com.mynameistodd.local.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.mynameistodd.local.MainActivity;
import com.mynameistodd.local.R;
import com.mynameistodd.local.models.Business;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

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

                final NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                final TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);

                ParseQuery<Business> query = ParseQuery.getQuery(Business.class);
                query.whereContainedIn("channelId", triggerIds);
                query.findInBackground(new FindCallback<Business>() {
                    @Override
                    public void done(List<Business> businesses, ParseException e) {
                        if (businesses != null) {
                            for (Business business : businesses) {
                                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                        .setSmallIcon(R.drawable.ic_launcher)
                                        .setContentTitle(business.getName())
                                        .setContentText(business.getSnippet())
                                        .setContentIntent(pendingIntent)
                                        .setAutoCancel(true);


                                String logoUrl = business.getLogo().getUrl();

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
                        } else if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            // An invalid transition was reported
            else {
                Log.e("ReceiveTransitionsIntentService", "Geofence transition error: " + Integer.toString(transitionType));
            }
        }
    }
}