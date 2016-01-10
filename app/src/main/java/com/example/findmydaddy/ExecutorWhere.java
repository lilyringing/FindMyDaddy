package com.example.findmydaddy;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.mcnlab.lib.smscommunicate.CommandHandler;
import org.mcnlab.lib.smscommunicate.Executor;

import java.text.DecimalFormat;


/**
 * Created by IMlab304 on 2015/12/14.
 */
public abstract class ExecutorWhere implements Executor {
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public JSONObject execute(Context context, final int device_id, int count, JSONObject usr_json) {
        Log.d("EXECUTOR", "COUNT = " + count);

        switch (count) {
            case 0:
                Toast.makeText(context, "WHERE case 0", Toast.LENGTH_SHORT).show();
                return new JSONObject();
            case 1:
                final int device_id_closure = device_id;
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                Toast.makeText(context, "WHERE case 1", Toast.LENGTH_SHORT).show();

                LocationListener myloclis = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        JSONObject new_usr_json = new JSONObject();
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        DecimalFormat df = new DecimalFormat("#.####");

                        try {
                            new_usr_json.put("lat", df.format(latitude)).put("lon", df.format(longitude));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CommandHandler.getSharedCommandHandler().execute("WHERE", device_id_closure, 2, new_usr_json);

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                try {
                    locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, myloclis, null);
                }
                catch (SecurityException se){
                    Toast.makeText(context, "No Permission !", Toast.LENGTH_SHORT).show();
                }
                return null;

            case 2:
                Toast.makeText(context, "WHERE case 2", Toast.LENGTH_SHORT).show();
                return usr_json;

            default:
                try{
                    double lat = usr_json.getDouble("lat");
                    double lon = usr_json.getDouble("lon");
                    Toast.makeText(context, Double.toString(lat) + "," + Double.toString(lon), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(context, MapsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("latitude", lat);
                    bundle.putDouble("longitude", lon);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
        }
    }
}