package com.minyan.avigailfohrman.candelighting;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

  StringBuilder sunsetApiUrlStringBuilder;
  String urlBase = "https://api.sunrise-sunset.org/json?";
  RequestQueue requestQueue;
  URL sunsetUrl;
  String latitudeName = "lat";
  String longitudeName = "lng";
  String defaultDate = "date=today";
  TextView candleLightingTimeTextView;

  String sunsetUtc;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    sunsetApiUrlStringBuilder = new StringBuilder(urlBase);
    candleLightingTimeTextView = findViewById(R.id.candlelighting_time);

    buildSunsetUrlString();
    requestJsonSunsetData();

  }

  private void requestJsonSunsetData() {
    setUpRequestQueue();
    handleJsonRequest();
  }

  private void setUpRequestQueue() {
    requestQueue = Volley.newRequestQueue(this);
  }

  private void populateCandleLightingTimeTextView(String sunsetUtc) {
    if (sunsetUtc != null) {
      String sunsetLocalTime = convertUtcToLocalTime(sunsetUtc);
      candleLightingTimeTextView.setText(sunsetLocalTime);
    } else {
      candleLightingTimeTextView.setText("unknown");
    }
  }

  private String convertUtcToLocalTime(String sunsetUtc) {
    String sunsetLocalTime = sunsetUtc;
    Date sunsetLocalTimeAsDate = new Date();
    sunsetLocalTimeAsDate.setTime(Calendar.getInstance().getTime().getTime());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH : mm : ss a");
    simpleDateFormat.format(sunsetLocalTimeAsDate);
    return sunsetLocalTime;
  }

  private String convertUtcToLocal(String sunsetUtc) {
    String sunsetLocal = sunsetUtc;
    // Get the desired time zone.

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss a");
    simpleDateFormat.setTimeZone(TimeZone.getDefault());

    Date sunsetDate = new Date();
    sunsetDate.setTime(Long.parseLong(sunsetUtc));

    sunsetLocal = simpleDateFormat.format(sunsetDate);

    // Convert from UTC to local.
    return sunsetLocal;
  }

  private String convertFromUtcToLocal(String sunsetUtc) {
    long gmtOffset = TimeZone.getDefault().getRawOffset();
    long sunsetLong = getTimeInLong(sunsetUtc);
    long sunsetCorrected = sunsetLong + gmtOffset;
    return new String(sunsetCorrected + "");
  }

  private long getTimeInLong(String sunsetUtc) {
    StringBuilder sb = new StringBuilder();

    for (Character c : sunsetUtc.toCharArray()) {
      if (Character.isDigit(c)) {
        sb.append(c);
      }
    }
    long sunset = Long.parseLong(sb.toString());
    return sunset;
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
    String sampleUrl = "https://api.sunrise-sunset.org/json?lat=40.629435&lng=-73.709537&date=today";
    sunsetApiUrlStringBuilder = new StringBuilder(sampleUrl);

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
        sunsetApiUrlStringBuilder.toString(), null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        try {
          sunsetUtc = response.getJSONObject("results").getString("sunset");
          populateCandleLightingTimeTextView(sunsetUtc);
        } catch (JSONException e) {
          e.printStackTrace();
          sunsetUtc = "Unknown";
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        sunsetUtc = "Error";
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
