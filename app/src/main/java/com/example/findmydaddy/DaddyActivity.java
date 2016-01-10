package com.example.findmydaddy;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;
import org.mcnlab.lib.smscommunicate.CommandHandler;
import org.mcnlab.lib.smscommunicate.Recorder;
import org.mcnlab.lib.smscommunicate.UserDefined;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;
import org.mcnlab.lib.smscommunicate.CommandHandler;
import org.mcnlab.lib.smscommunicate.Recorder;
import org.mcnlab.lib.smscommunicate.UserDefined;

import java.util.ArrayList;
import java.util.HashMap;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.SensorEvent;
public class DaddyActivity extends AppCompatActivity {
    private FileManager fm;
    private int TotalNumOfData = 3;
    private SensorManager sm;
    private Sensor aSensor;
    private Sensor mSensor;
    private Integer acc_recording = 0;//0 idle 1 waiting 2 recording
    TextView Acc_Info;
    float[] accelerometerValues = new float[3];
    float[] prev_accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    private static final String TAG = "sensor";
    Handler handler=new Handler();
    float[] accRecord =  new float[3];
    Integer recordCount = 0;
    float accChange;
    Context context = this;
    double max_acc = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daddy);

        View backgroundimage = (View) findViewById(R.id.daddy_background);
        backgroundimage.getBackground().setAlpha(110);

        fm = new FileManager();
        ArrayList<HashMap<String, Object>> contacter_arr = new ArrayList<HashMap<String, Object>>();

        // 從資料庫取得緊急連絡人資料放入ArrayList
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(fm.GetDaddyDB(), null, null);
        Cursor c = db.rawQuery("SELECT * FROM contacter ORDER BY contacterID", null);
        if(c.getCount() != 0){
            while(c.moveToNext()){
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("PhoneNumber", c.getString(1));
                contacter_arr.add(item);
            }
        }


        ListView contacter_list = (ListView)findViewById(R.id.contacter_list);
        Contacter_Adapter ContAdapter = new Contacter_Adapter(this, contacter_arr, TotalNumOfData);
        contacter_list.setAdapter(ContAdapter);

        //FindMe init
        UserDefined.filter = "$FINDME$";

        Recorder.init(this, "DaddyActivity");
        CommandHandler.init(this);

        CommandHandler.getSharedCommandHandler().addExecutor("WHERE", new ExecutorWhere() {
            @Override
            public JSONObject execute(Context context, int device_id, int count, JSONObject usr_json) {
                return super.execute(context, device_id, count, usr_json);
            }
        });

        CommandHandler.getSharedCommandHandler().addExecutor("ALARM", new ExecutorAlarm() {
            @Override
            public JSONObject execute(Context context, int device_id, int count, JSONObject usr_json) {
                return super.execute(context, device_id, count, usr_json);
            }
        });

        CommandHandler.getSharedCommandHandler().addExecutor("CALLME", new ExecutorCallMe() {
            @Override
            public JSONObject execute(Context context, int device_id, int count, JSONObject usr_json) {
                return super.execute(context, device_id, count, usr_json);
            }
        });

        //Acc_Info = (TextView) findViewById(R.id.acc_info);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(myListener, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.logout){
            if(fm.IsDaddyFile()){
                if(fm.RemoveDaddyFile()) GoToLoginPage();
            }
            if(fm.IsSonFile()) {
                if(fm.RemoveSonFile()) GoToLoginPage();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /* Function used to jump to login page */
    protected void GoToLoginPage(){
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setClass(DaddyActivity.this, LoginActivity.class);
        startActivity(intent);
        DaddyActivity.this.finish();
    }
    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {

            // if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            //    magneticFieldValues = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = sensorEvent.values;
            calculateOrientation();
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        //recordCount = recordCount+1;
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);
        double cur_acc = Math.sqrt(Math.abs(accelerometerValues[0])*Math.abs(accelerometerValues[0])+Math.abs(accelerometerValues[1])*Math.abs(accelerometerValues[1])+Math.abs(accelerometerValues[2])*Math.abs(accelerometerValues[2]));
        if( max_acc > cur_acc)
            max_acc = cur_acc;
        //Acc_Info.setText(accChange + "  " + cur_acc + " " + max_acc);
        if ((cur_acc<=4) && (acc_recording == 0)) {
            acc_recording = 1;
            handler.postDelayed(startRecording, 2500);
        }
        if(acc_recording == 2) {
            accChange = accChange + Math.abs(prev_accelerometerValues[0]-accelerometerValues[0]) +  Math.abs(prev_accelerometerValues[1]-accelerometerValues[1]) +  Math.abs(prev_accelerometerValues[2]-accelerometerValues[2]);
            prev_accelerometerValues[0] = accelerometerValues[0];
            prev_accelerometerValues[1] = accelerometerValues[1];
            prev_accelerometerValues[2] = accelerometerValues[2];
        }
    }
    Runnable fallJudgement=new Runnable(){
        @Override
        public void run() {
            if (accChange <= 2.5) {//活動量太少，判斷為跌倒
                //切換到跌倒警示頁面 FallAlertActivity
                Intent intent = new Intent(DaddyActivity.this,FallAlertActivity.class);
                startActivity(intent);
            }

            //發出震動警訊
            // TODO Auto-generated method stub
            acc_recording = 0;
            accChange = 0;

            //

        }
    };
    Runnable startRecording=new Runnable(){
        @Override
        public void run() {
            acc_recording = 2;
            recordCount = 0;
            accChange = 0;
            prev_accelerometerValues[0] = accelerometerValues[0];
            prev_accelerometerValues[1] = accelerometerValues[1];
            prev_accelerometerValues[2] = accelerometerValues[2];
            handler.postDelayed(fallJudgement, 2000);
        }
    };
}
