package com.example.findmydaddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    /* Region Private Fields */
    private Button daddy;
    private Button son;
    private FileManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        // Check whether user logged in or not
        fm = new FileManager();
        if(fm.IsDaddyFile()){
            GoToDaddyPage();
        }else if(fm.IsSonFile()) {
            GoToSonPage();
        }

        // Get Button and set OnClick Event
        daddy = (Button)findViewById(R.id.daddy);
        son = (Button)findViewById(R.id.son);

        daddy.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fm.CreateDaddyFile()){
                    GoToDaddyPage();
                }else{
                    Log.e("File", "error!");
                }
            }
        });

        son.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fm.CreateSonFile()){
                    GoToSonPage();
                }else{
                    Log.e("File", "error!");
                }
            }
        });

        /*倒數計時
        new CountDownTimer(5000,1000){
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            @Override
            public void onFinish() {
                r.stop();
                // 撥電話
                Intent dial = new Intent();
                dial.setAction("android.intent.action.CALL");
                dial.setData(Uri.parse("tel:0917961689"));
                startActivity(dial);
            }
            @Override
            public void onTick(long millisUntilFinished) {
            //警示音
                r.play();
            }
        }.start();
        */
    }

    /* Function used to jump to page: daddy */
    protected void GoToDaddyPage(){
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, DaddyActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    /* Function used to jump to page: son */
    protected void GoToSonPage(){
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, SonActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }
}
