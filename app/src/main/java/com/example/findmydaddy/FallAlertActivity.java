package com.example.findmydaddy;

/**
 * Created by Diamond on 2015/12/25.
 */

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.view.Window;
import android.widget.Toast;
import android.os.Handler;
import android.os.Vibrator;

import org.mcnlab.lib.smscommunicate.CommandHandler;
import org.mcnlab.lib.smscommunicate.Recorder;

public class FallAlertActivity extends Activity {
    Button btn_confirm;
    Button btn_cancel;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keyguard = km.newKeyguardLock("fall_alert_dialog");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        keyguard.disableKeyguard();

        setContentView(R.layout.fall_alert_dialog);
        myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

        btn_confirm = (Button)findViewById(R.id.btn_confirm);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        text_close = (TextView)findViewById(R.id.text_close);
        text_title  = (TextView)findViewById(R.id.text_title);
        text_content = (TextView)findViewById(R.id.text_content);

        text_content.setText("請問您是否需要幫助？\n\n若沒有回應，系統將在10秒後自動聯繫您的1號緊急聯絡人：" + countDownSec);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm = true;
//                Toast.makeText(FallAlertActivity.this, "you click confirm!", Toast.LENGTH_SHORT).show();

                handler.postDelayed(sendSMS, 0);
                keyguard.reenableKeyguard();
                //finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View v) {
                confirm = true;
                //Toast.makeText(FallAlertActivity.this,"you click cancel!", Toast.LENGTH_SHORT).show();
                keyguard.reenableKeyguard();
                finish();
            }
        });
        text_close.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View v) {
                confirm = true;
                //Toast.makeText(FallAlertActivity.this,"you click close!",Toast.LENGTH_SHORT).show();
                keyguard.reenableKeyguard();
                finish();
            }
        });
        handler.postDelayed(countDown, 1000);
        myVibrator.vibrate(500);

        FileManager fm = new FileManager();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(fm.GetDaddyDB(), null, null);

//        db.execSQL("UPDATE contacter SET phone='" + EPhoneNum.getText().toString() + "' WHERE contacterID=" + pos + "");
//        db.execSQL("INSERT INTO contacter(contacterID, phone) VALUES(" + pos + ", '" + EPhoneNum.getText().toString() + "')");
        //db.execSQL("SELECT COUNT(*) FROM table_name");
        Cursor cursor = db.rawQuery("select * from contacter", null);
        while (cursor.moveToNext()) {

            phoneNumberTable[contacterNum] = cursor.getString(1);//获取第二列的值
            contacterNum = contacterNum+1;
        }
        db.close();

    }
    Runnable countDown=new Runnable(){
        @Override
        public void run() {
            countDownSec = countDownSec - 1;

            if((countDownSec > 0) && (confirm == false)) {
                text_content.setText("請問您是否需要幫助？\n\n若沒有回應，系統將在10秒後自動聯繫您的"+current_contacter +"號緊急聯絡人：" + countDownSec);
                handler.postDelayed(countDown, 1000);
                myVibrator.vibrate(500);
            }
            else if((countDownSec <= 0) && (confirm == false)) {
                handler.postDelayed(sendSMS, 0);
                countDownSec = 0;
            }
        }
    };

    Runnable sendSMS=new Runnable(){
        @Override
        public void run() {
            text_content.setText("已聯繫您的" + current_contacter +"號緊急聯絡人！");


            //聯絡第current_contacter個緊急聯絡人
            Recorder rec = Recorder.getSharedRecorder();
            CommandHandler hdlr = CommandHandler.getSharedCommandHandler();
            SQLiteDatabase db = rec.getWritableDatabase();
            //int device_id = rec.getDeviceIdByPhonenumberOrCreate(db, "0983201211");
            int device_id = rec.getDeviceIdByPhonenumberOrCreate(db, phoneNumberTable[current_contacter-1]);
            db.close();
            hdlr.execute("ALARM", device_id, 0, null);


            current_contacter = current_contacter + 1;
            if ((current_contacter <= contacterNum) && (confirm == false)) {//若還有聯絡人，準備聯絡下一個
                handler.postDelayed(countDown, 5000);//五秒後開始倒數
                countDownSec = 10;
            }
        }
    };
}
