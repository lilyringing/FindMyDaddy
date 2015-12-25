package com.example.findmydaddy;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.mcnlab.lib.smscommunicate.CommandHandler;
import org.mcnlab.lib.smscommunicate.Executor;

import java.text.DecimalFormat;

/**
 * Created by sally on 2015/12/24.
 */
public class ExecutorAlarm implements Executor {
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public JSONObject execute(Context context, final int device_id, int count, JSONObject usr_json) {
        Log.d("EXECUTOR", "COUNT = " + count);

        switch (count) {
            case 0:
                final int device_id_closure = device_id;
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return null;
                }
                LocationListener myloclis = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        JSONObject new_usr_json = new JSONObject();
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        DecimalFormat df = new DecimalFormat("#.####");

                        try{
                            new_usr_json.put("lat", df.format(latitude)).put("lon", df.format(longitude));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CommandHandler.getSharedCommandHandler().execute("ALARM", device_id_closure, 1, new_usr_json);

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
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, myloclis, null);
                return null;

            case 1:
                return usr_json;

            default:
                try{
                    double lat = usr_json.getDouble("lat");
                    double lon = usr_json.getDouble("lon");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
        }
    }
}
