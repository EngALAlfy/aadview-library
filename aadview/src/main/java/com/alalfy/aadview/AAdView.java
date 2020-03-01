package com.alalfy.aadview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.IdRes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * name : AAView
 * ALALfy AdView Library
 * Made by Islam AlAlfy
 * web : AlAlfy.com
 */
public class AAdView {
    private Activity parent;
    private String adHtml;
    private String adUrl;
    private String adSize;
    private String baseUrl;

    public AAdView(Activity parent, String baseUrl) {
        this.parent = parent;
        this.baseUrl = baseUrl;
    }

    /**
     * request specific ad by refer the ad id
     * @param adID the ad id for load
     */
    public void requestAd(String adID , @IdRes final int adResID){
        @SuppressLint("StaticFieldLeak") HttpRequest request = new HttpRequest(){
            @Override
            void onFinish(String result) throws JSONException {
                super.onFinish(result);

                Toast.makeText(parent, result, Toast.LENGTH_SHORT).show();

                JSONObject json = new JSONObject(result);
                adHtml = json.getString("adHtml");
                //adSize = json.getString("adSize");
                adUrl = json.getString("adUrl");
                view(adResID);
            }
        };
        request.execute(baseUrl , adID);
    }

    /**
     * show the ad on the view id loaded
     * @param adResID the WebView layout id
     */
    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    public void view(@IdRes int adResID) {
        WebView view = (WebView) parent.findViewById(adResID);

        // settings
        view.getSettings().setAllowFileAccess(true);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setDefaultTextEncodingName("utf-8");
        view.getSettings().setLoadWithOverviewMode(true);
        view.getSettings().setBuiltInZoomControls(false);


        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(adUrl));
                    parent.startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        view.loadDataWithBaseURL("" , adHtml, "text/html", "UTF-8" , "");

    }
}


