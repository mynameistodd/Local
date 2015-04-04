package com.mynameistodd.local.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Todd DeLand on 12/16/2014.
 */
@ParseClassName("Message")
public class Message extends ParseObject {
    private String userId;
    private String placeId;
    private String text;

    public String getUserId() {
        return getString("userId");
    }

    public void setUserId(String userId) {
        put("userId", userId);
    }

    public String getPlaceId() {
        return getString("placeId");
    }

    public void setPlaceId(String placeId) {
        put("placeId", placeId);
    }

    public String getText() {
        return getString("text");
    }

    public void setText(String text) {
        put("text", text);
    }
}
