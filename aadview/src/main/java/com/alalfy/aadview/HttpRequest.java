package com.alalfy.aadview;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpRequest extends AsyncTask<String ,  Void , String> {

    @Override
    protected String doInBackground(String... urls) {

        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urls[0]+"/api-aadview-request-ad.php?id="+urls[1]);
            urlConnection = (HttpURLConnection) url.openConnection();

            int code = urlConnection.getResponseCode();

            if (code !=  200) {
                throw new IOException("Invalid response from server: " + code);
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            onFinish(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void onFinish(String result) throws JSONException {}
}
