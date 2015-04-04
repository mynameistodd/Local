package com.mynameistodd.local.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by todd on 10/7/14.
 */
@ParseClassName("Geofence")
public class Geofence extends ParseObject {
    private String text;
    private String placeId;

    public String getText() {
        return getString("text");
    }

    public void setText(String text) {
        put("text", text);
    }

    public String getPlaceId() {
        return getString("placeId");
    }

    public void setPlaceId(String placeId) {
        put("placeId", placeId);
    }

}
