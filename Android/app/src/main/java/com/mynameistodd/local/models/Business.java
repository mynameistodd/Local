package com.mynameistodd.local.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by todd on 10/7/14.
 */
@ParseClassName("Business")
public class Business extends ParseObject {
    private String name;
    private String snippet;
    private ParseGeoPoint location;
    private String channelId;
    private ParseFile logo;

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getSnippet() {
        return getString("snippet");
    }

    public void setSnippet(String snippet) {
        put("snippet", snippet);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint location) {
        put("location", location);
    }

    public String getChannelId() {
        return getString("channelId");
    }

    public void setChannelId(String channelId) {
        put("channelId", channelId);
    }

    public ParseFile getLogo() {
        return getParseFile("logo");
    }

    public void setLogo(ParseFile logo) {
        put("logo", logo);
    }
}
