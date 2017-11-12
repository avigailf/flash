package com.minyan.avigailfohrman.candelighting;

import android.location.Location;
import android.location.LocationProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    StringBuilder sunsetApiUrlStringBuilder;
    String urlBase = "https://api.sunrise-sunset.org/json?";
    RequestQueue requestQueue;
    URL sunsetUrl;
    String latitudeName = "lat";
    String longitudeName = "lng";
    String defaultDate = "date=today";
    TextView candleLightingTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sunsetApiUrlStringBuilder = new StringBuilder(urlBase);
        candleLightingTimeTextView = findViewById(R.id.candlelighting_time);
        candleLightingTimeTextView.setText("6:45");
        buildSunsetUrlString();

        requestJsonSunsetData();

        //populateCandleLightingTimeTextView(candleLightingTimeTextView);
    }

    private void requestJsonSunsetData() {
        setUpRequestQueue();
        handleJsonRequest();
    }

    private void setUpRequestQueue() {
        requestQueue = Volley.newRequestQueue(this);
    }

    private void populateCandleLightingTimeTextView(TextView candleLightingTimeTextView) {
        StringBuilder candleLightingTime = new StringBuilder();
        candleLightingTime.append("6:34");
        candleLightingTimeTextView.setText(candleLightingTime.toString());
    }

    private Date calculateCandlelighting() {
        Date date = new Date();
        return date;
    }

    private Date getSunset() {
        buildSunsetUrlString();
        // Call to the API.
        Date date = new Date();

        return calculateCandelightingFromSunset(date);
    }

    private Date calculateCandelightingFromSunset(Date sunsetTime) {
        sunsetTime.setTime(sunsetTime.getTime() - 18);
        return sunsetTime;
    }

    private void buildSunsetUrlString() {
        double latitudeValue = getLocation().getLatitude();
        double longitudeValue = getLocation().getLongitude();

        sunsetApiUrlStringBuilder.append(latitudeName).append("=").append(latitudeValue);
        sunsetApiUrlStringBuilder.append("&");
        sunsetApiUrlStringBuilder.append(longitudeName).append("=").append(longitudeValue);
        sunsetApiUrlStringBuilder.append("&");
        sunsetApiUrlStringBuilder.append(defaultDate);
    }

    private void handleJsonRequest() {
        String sampleUrl = "https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400&date=today";
        sunsetApiUrlStringBuilder = new StringBuilder(sampleUrl);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, sunsetApiUrlStringBuilder.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                StringBuilder time = new StringBuilder();
                try {
                    JSONObject jsonObject = response.getJSONObject("results");
                    time.append(jsonObject.getString("sunset"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                candleLightingTimeTextView.setText(time);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public Location getLocation() {
        Location location = new Location("");

        location.getLatitude();

        return location;
    }
}
