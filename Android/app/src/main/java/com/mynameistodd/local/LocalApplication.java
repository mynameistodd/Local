package com.mynameistodd.local;

import android.app.Application;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

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


        ParseQuery<Business> query = ParseQuery.getQuery(Business.class);
        query.getInBackground("53tjjL5sLh", new GetCallback<Business>() {
            @Override
            public void done(Business business, ParseException e) {
                ParseUser user = ParseUser.getCurrentUser();
                ParseRelation<Business> relation = user.getRelation("Business");
                relation.add(business);
                user.saveInBackground();
            }
        });
    }
}
