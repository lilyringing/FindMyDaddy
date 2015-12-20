package com.example.findmydaddy;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private String path = Environment.getExternalStorageDirectory().toString();
    private File daddy_file = new File(path + "/daddy.txt");
    private File son_file = new File(path + "/son.txt");
    private Button daddy;
    private Button son;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        // Check whether user logged in or not
        if(daddy_file.exists()){
            GoToDaddyPage();
        }else if(son_file.exists()) {
            GoToSonPage();
        }

        // Get Button and set OnClick Event
        daddy = (Button)findViewById(R.id.daddy);
        son = (Button)findViewById(R.id.son);

        daddy.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    daddy_file.createNewFile();
                }catch(IOException e){
                    e.toString();
                }

                GoToDaddyPage();
            }
        });

        son.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    son_file.createNewFile();
                }catch(IOException e){
                    e.toString();
                }

                GoToSonPage();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void GoToDaddyPage(){
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, DaddyActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }
    protected void GoToSonPage(){
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, SonActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }
}
