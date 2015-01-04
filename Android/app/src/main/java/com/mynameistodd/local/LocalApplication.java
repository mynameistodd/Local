package com.mynameistodd.local;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
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

//        Parse.enableLocalDatastore(this);
//        ParseUser.enableAutomaticUser();

        ParseInstallation.getCurrentInstallation().saveInBackground();
//        ParseUser.getCurrentUser().saveInBackground();

        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);


//        ParseQuery<Business> query = ParseQuery.getQuery(Business.class);
//        query.getInBackground("53tjjL5sLh", new GetCallback<Business>() {
//            @Override
//            public void done(Business business, ParseException e) {
//                ParseUser user = ParseUser.getCurrentUser();
//                ParseRelation<Business> relation = user.getRelation("Business");
//                relation.add(business);
//                user.saveInBackground();
//            }
//        });
    }
}
