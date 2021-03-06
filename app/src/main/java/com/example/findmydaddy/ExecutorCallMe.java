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
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

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
    /*自動擴音
          TelephonyManager manager;
          StatePhoneReceiver myPhoneStateListener;
          boolean callFromApp=false; // To control the call has been made from the application
          boolean callFromOffHook=false;
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public JSONObject execute(Context context, final int device_id, int count, JSONObject usr_json) {
        Log.d("EXECUTOR", "COUNT = " + count);

        switch (count) {
            case 0:
                Toast.makeText(context, "WHERE case 0", Toast.LENGTH_SHORT).show();
                return new JSONObject();

            default:

                //Toast.makeText(context, "test", Toast.LENGTH_LONG).show();
                //get phone number of the contacter
                Recorder rec = Recorder.getSharedRecorder();
                SQLiteDatabase db = rec.getReadableDatabase();
                int dev_id = 0;
                String phone = "";

                if(db.isOpen()) {
                    Cursor c = db.rawQuery("SELECT device_id FROM Commands WHERE role = 0 and command = \"CALLME\"", null);
                    if(c.getCount() != 0) {
                        c.moveToLast();
                        dev_id = c.getInt(0);
                        //role = c.getInt(0);
                        //Toast.makeText(context, String.format("%d", dev_id), Toast.LENGTH_SHORT).show();
                        c = db.rawQuery("SELECT phonenumber FROM Devices WHERE id='" + dev_id + "'", null);
                        c.moveToFirst();
                        phone = c.getString(0);
                        Toast.makeText(context, phone, Toast.LENGTH_SHORT).show();
                        c.close();
                        db.close();
                        /*自動擴音
                                                    manager.listen(myPhoneStateListener,
                                                    PhoneStateListener.LISTEN_CALL_STATE);
                                                    callFromApp=true;
                                                */
                        Intent dial = new Intent();
                        dial.setAction("android.intent.action.CALL");
                        dial.setData(Uri.parse("tel:" + phone));
                        context.startActivity(dial);
                    }
                    else
                        Toast.makeText(context, "NO phone number", Toast.LENGTH_SHORT).show();
                }
                return null;
        }
    }
    /*自動擴音
       // Monitor for changes to the state of the phone
   public class StatePhoneReceiver extends PhoneStateListener {
       Context context;
       public StatePhoneReceiver(Context context) {
           this.context = context;
       }
       @Override
       public void onCallStateChanged(int state, String incomingNumber) {
           super.onCallStateChanged(state, incomingNumber);

           switch (state) {
               case TelephonyManager.CALL_STATE_OFFHOOK: //Call is established
                   if (callFromApp) {
                       callFromApp=false;
                       callFromOffHook=true;
                       try {
                           Thread.sleep(500); // Delay 0,5 seconds to handle better turning on loudspeaker
                       } catch (InterruptedException e) {
                       }

                       //Activate loudspeaker
                       AudioManager audioManager = (AudioManager)
                               getSystemService(Context.AUDIO_SERVICE);
                       audioManager.setMode(AudioManager.MODE_IN_CALL);
                       audioManager.setSpeakerphoneOn(true);
                   }
                   break;
               case TelephonyManager.CALL_STATE_IDLE: //Call is finished
                   if (callFromOffHook) {
                       callFromOffHook=false;
                       AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                       audioManager.setMode(AudioManager.MODE_NORMAL); //Deactivate loudspeaker
                       manager.listen(myPhoneStateListener, // Remove listener
                               PhoneStateListener.LISTEN_NONE);
                   }
                   break;
           }
       }
   }
     */
}
