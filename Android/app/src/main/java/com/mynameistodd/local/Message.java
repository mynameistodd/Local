package com.mynameistodd.local;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Todd DeLand on 12/16/2014.
 */
@ParseClassName("Message")
public class Message extends ParseObject {
    private String text;

    public String getText() {
        return getString("text");
    }

    public void setText(String text) {
        put("text", text);
    }
}
