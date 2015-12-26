package com.example.findmydaddy;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.mcnlab.lib.smscommunicate.CommandHandler;
import org.mcnlab.lib.smscommunicate.Executor;
import org.mcnlab.lib.smscommunicate.Recorder;

import java.text.DecimalFormat;

/**
 * Created by sally on 2015/12/26.
 */
public abstract class ExecutorCallMe implements Executor {
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public JSONObject execute(Context context, final int device_id, int count, JSONObject usr_json) {
        Log.d("EXECUTOR", "COUNT = " + count);

        switch (count) {
            case 0:
                return new JSONObject();

            default:
                //get phone number of the contacter
                Recorder rec = Recorder.getSharedRecorder();
                SQLiteDatabase db = rec.getReadableDatabase();
                int dev_id = 0;
                String phone = "";
                if(db.isOpen()) {
                    Cursor c = db.rawQuery("SELECT * FROM Commands WHERE role='0' and command='CALLME'", null);
                    c.moveToLast();
                    c.moveToPrevious();
                    dev_id = c.getInt(5);
                    c = db.rawQuery("SELECT * FROM Devices WHERE id='"+dev_id+"'", null);
                    phone = c.getString(3);
                    Toast.makeText(context, phone, Toast.LENGTH_LONG).show();
                    c.close();
                    db.close();
                }
                //Call
                if(phone != "") {
                    Intent dial = new Intent();
                    dial.setAction("android.intent.action.CALL");
                    dial.setData(Uri.parse("tel:" + phone));
                    context.startActivity(dial);
                }
                return null;
        }
    }
}
