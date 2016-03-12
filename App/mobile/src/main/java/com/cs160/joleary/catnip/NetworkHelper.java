package com.cs160.joleary.catnip;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mmzhai on 3/10/16.
 */
public class NetworkHelper extends AsyncTask<String, Void, JSONObject> {
    JSONObject json;
    @Override
    protected JSONObject doInBackground(String... urlStr) {

        try {
            URL url = new URL(urlStr[0]);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String repData = "";
            String str;
            while ((str = in.readLine()) != null)
                repData += str;
            in.close();
            json = new JSONObject(repData);
            return json;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }



}
