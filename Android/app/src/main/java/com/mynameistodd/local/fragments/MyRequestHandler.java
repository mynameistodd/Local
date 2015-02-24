package com.mynameistodd.local.fragments;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import se.walkercrou.places.RequestHandler;

/**
 * Created by tdeland on 2/23/15.
 */
class MyRequestHandler implements RequestHandler {
    public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
    private HttpURLConnection client;
    private String characterEncoding;

    public MyRequestHandler(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public MyRequestHandler() {
        this(DEFAULT_CHARACTER_ENCODING);
    }

    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    @Override
    public void setCharacterEncoding(String s) {
        this.characterEncoding = s;
    }

    @Override
    public InputStream getInputStream(String s) throws IOException {
        URL url = new URL(s);
        try {
            client = (HttpsURLConnection) url.openConnection();
            return client.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        } finally {
            client.disconnect();
        }
    }

    @Override
    public String get(String s) throws IOException {
        URL url = new URL(s);
        try {
            client = (HttpsURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(client.getInputStream());
            return IOUtils.toString(in);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        } finally {
            client.disconnect();
        }
    }

    @Override
    public String post(HttpPost httpPost) throws IOException {
        URL url = httpPost.getURI().toURL();
        try {
            client = (HttpsURLConnection) url.openConnection();
            client.setDoOutput(true);
            client.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(client.getOutputStream());
            httpPost.getEntity().writeTo(out);
            out.flush();
            out.close();

            int responseCode = client.getResponseCode();

            InputStream in = new BufferedInputStream(client.getInputStream());
            return IOUtils.toString(in);
        } finally {
            client.disconnect();
        }
    }
}
