package com.example.findmydaddy;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.mcnlab.lib.smscommunicate.CommandHandler;
import org.mcnlab.lib.smscommunicate.Recorder;

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


    @Override    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

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


        //找出傳來求救訊號的人的divice id & phone number
        Recorder rec = Recorder.getSharedRecorder();
        SQLiteDatabase db = rec.getReadableDatabase();
        int dev_id = 0;
        String phone = "";
        if(db.isOpen()) {
            Cursor c = db.rawQuery("SELECT * FROM Commands WHERE role='0' and command='CALLME'", null);
            c.moveToLast();
            dev_id = c.getInt(5);
            c = db.rawQuery("SELECT * FROM Devices WHERE id='" + dev_id + "'", null);
            phone = c.getString(3);
            c.close();
            db.close();
        }

        text_content.setText("手機號碼：" +phone + "\n\n向您發出了跌倒警訊，您可以採取以下行動來幫助他！");

        //按下查看地點按鈕
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm = true;
                keyguard.reenableKeyguard();
            }
        });
        //按下強制通話按鈕
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View v) {
                confirm = true;
                keyguard.reenableKeyguard();
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


}
