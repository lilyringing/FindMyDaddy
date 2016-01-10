package com.example.findmydaddy;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.mcnlab.lib.smscommunicate.CommandHandler;
import org.mcnlab.lib.smscommunicate.Recorder;
import org.mcnlab.lib.smscommunicate.UserDefined;

/**
 * Created by Diamond on 2016/1/9.
 */



public class SonAlertActivity extends Activity {
    Button btn_map;
    Button btn_call;
    TextView text_content;
    TextView text_title;
    TextView text_close;
    Handler handler=new Handler();
    Integer current_contacter = 1;
    Integer contacterNum = 0;
    String [] phoneNumberTable =  new String[3];
    Integer countDownSec = 10;
    Boolean confirm = false;
    Vibrator myVibrator;
    KeyguardManager.KeyguardLock keyguard;
    double lat;
    double lon;
    public final static String LOG_TAG = "SonAlertActivity";

    String phone = "";

    @Override    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keyguard = km.newKeyguardLock("son_alert_dialog");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        keyguard.disableKeyguard();

        setContentView(R.layout.son_alert_dialog);
        myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

        btn_map = (Button)findViewById(R.id.btn_map);
        btn_call = (Button)findViewById(R.id.btn_call);
        text_close = (TextView)findViewById(R.id.text_close);
        text_title  = (TextView)findViewById(R.id.text_title);
        text_content = (TextView)findViewById(R.id.text_content);

        //FindMe init
        UserDefined.filter = "$FINDME$";

        Recorder.init(this, "SonAlertActivity");
        CommandHandler.init(this);

        CommandHandler.getSharedCommandHandler().addExecutor("CALLME", new ExecutorCallMe() {
            @Override
            public JSONObject execute(Context context, int device_id, int count, JSONObject usr_json) {
                return super.execute(context, device_id, count, usr_json);
            }
        });

        //找出傳來求救訊號的人的divice id & phone number
        Recorder rec = Recorder.getSharedRecorder();
        SQLiteDatabase db = rec.getReadableDatabase();
        int dev_id = 0;
        if(db.isOpen()) {
            Cursor c = db.rawQuery("SELECT device_id FROM Commands WHERE role = 0 and command = \"CALLME\"", null);
            if(c.getCount() != 0) {
                c.moveToLast();
                dev_id = c.getInt(0);
                c = db.rawQuery("SELECT phonenumber FROM Devices WHERE id='" + dev_id + "'", null);
                c.moveToFirst();
                phone = c.getString(0);
                c.close();
                db.close();
            }
            else
                Toast.makeText(this, "NO phone number", Toast.LENGTH_SHORT).show();
        }
        //取得位置資訊
        Bundle bundle = getIntent().getExtras();
        lat = bundle.getDouble("lat");
        lon = bundle.getDouble("lon");

        text_content.setText("手機號碼：" + phone + "\n\n向您發出了跌倒警訊，您可以採取以下行動來幫助他！");

        //按下查看地點按鈕
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm = true;
                keyguard.reenableKeyguard();
                GoToMapPage();
            }
        });
        //按下強制通話按鈕
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View v) {
                confirm = true;
                keyguard.reenableKeyguard();
                //FindMe : ask elder to Call Me
                Recorder rec = Recorder.getSharedRecorder();
                CommandHandler hdlr = CommandHandler.getSharedCommandHandler();
                SQLiteDatabase db = rec.getWritableDatabase();
                int device_id = rec.getDeviceIdByPhonenumberOrCreate(db, phone);
                db.close();
                hdlr.execute("CALLME", device_id, 0, null);
            }
        });
        //按下關閉視窗X
        text_close.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View v) {
                confirm = true;
                keyguard.reenableKeyguard();
                finish();
            }
        });
        handler.postDelayed(alarm, 1000);
        myVibrator.vibrate(500);

    }
    Runnable alarm=new Runnable(){
        @Override
        public void run() {
            if(confirm == false) {
                handler.postDelayed(alarm, 1000);
                myVibrator.vibrate(500);
            }
        }
    };

    /* Function used to jump to map page */
    protected void GoToMapPage(){
        Intent intent = new Intent();
        intent.setClass(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", lat);
        bundle.putDouble("longitude", lon);
        intent.putExtras(bundle);
        startActivity(intent);
        //  SonActivity.this.finish();
    }


}
