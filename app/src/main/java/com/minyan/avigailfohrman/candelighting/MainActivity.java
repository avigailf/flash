package com.minyan.avigailfohrman.candelighting;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    StringBuilder sunsetApiUrl;
    String urlBase = "https://api.sunrise-sunset.org/json?";
    String latitudeName = "lat";
    String longitudeName = "lng";
    String defaultDate = "date=today";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sunsetApiUrl = new StringBuilder(urlBase);
    }

    private Date calculateCandlelighting() {
        Date date = new Date();
        return date;
    }

    private Date getSunset() {
        buildSunsetUrlString();
        // Call to the
        Date date = new Date();


        //date.setTime();
        return date;
    }

    private void buildSunsetUrlString() {
        double latitudeValue = getLocation().getLatitude();
        double longitudeValue = getLocation().getLongitude();

        sunsetApiUrl.append(latitudeName).append("=").append(latitudeValue);
        sunsetApiUrl.append("&");
        sunsetApiUrl.append(longitudeName).append("=").append(longitudeValue);
        sunsetApiUrl.append(defaultDate);
    }

    public Location getLocation() {
        Location location = getLocation();

        return location;
    }
}
