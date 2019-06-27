package com.team3jp.MoneyMan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FindATMnear extends AsyncTask<Object,String,Boolean> {
    GoogleMap map;
    String url;
    InputStream is;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String data;
    @SuppressLint("StaticFieldleak")
    Activity activity;
    public FindATMnear(MapsActivity mapActivity) {
        activity=mapActivity;
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        map = (GoogleMap) objects[0];
        url = (String) objects[1];

        try {
            URL murl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) murl.openConnection();
            httpURLConnection.connect();
            is = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            data = stringBuilder.toString();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // Handle your exceptions
            return false;
        }


    }

    @Override
    protected void onPostExecute(Boolean s) {
//       Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
        if (s) {
            try {
                JSONObject parentObject = new JSONObject(data);
                JSONArray resultArray = parentObject.getJSONArray("results");
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject jsonObject = resultArray.getJSONObject(i);
                    JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");

                    String latitude = locationObj.getString("lat");
                    String longtitude = locationObj.getString("lng");

                    JSONObject nameObject = resultArray.getJSONObject(i);
                    String name_ATM = nameObject.getString("name");
                    String vicinity = nameObject.getString("vicinity");

                    LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(name_ATM);
                    markerOptions.snippet(vicinity);
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.atm_icon));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                    map.addMarker(markerOptions);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(activity,"Vui long bat internet",Toast.LENGTH_SHORT).show();
        }
    }
}
