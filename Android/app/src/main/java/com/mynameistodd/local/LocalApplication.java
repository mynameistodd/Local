package com.mynameistodd.local;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

/**
 * Created by todd on 10/7/14.
 */
public class LocalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Business.class);
        Parse.initialize(this, "m5dzHOXkMFC9BHPEbmprX02KM2GoVv2NBBPC5eUN", "tdKfe6bJDPpstLEigKmnhyRyBhSV7vy94IA1SVHM");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUser.enableAutomaticUser();
        ParseUser.getCurrentUser().saveInBackground();
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
    }
}
