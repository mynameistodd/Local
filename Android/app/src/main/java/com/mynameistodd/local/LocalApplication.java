package com.mynameistodd.local;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.mynameistodd.local.models.Business;
import com.mynameistodd.local.models.Message;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;

import io.fabric.sdk.android.Fabric;

/**
 * Created by todd on 10/7/14.
 */
public class LocalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        ParseObject.registerSubclass(Business.class);
        ParseObject.registerSubclass(Message.class);

        ParseCrashReporting.enable(this);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
        ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret));

        ParseInstallation.getCurrentInstallation().saveInBackground();

        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
    }
}
