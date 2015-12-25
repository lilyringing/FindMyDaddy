package com.example.findmydaddy;


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

public class DaddyActivity extends AppCompatActivity {
    private FileManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daddy);

        fm = new FileManager();
        ArrayList<HashMap<String, Object>> contacter_arr = new ArrayList<HashMap<String, Object>>();

        // 從資料庫取得緊急連絡人資料放入ArrayList
        for(int i=0; i < 3; i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("PhoneNumber", "0911111111");
            contacter_arr.add(item);
        }

        ListView contacter_list = (ListView)findViewById(R.id.contacter_list);
        Contacter_Adapter ContAdapter = new Contacter_Adapter(this, contacter_arr);
        contacter_list.setAdapter(ContAdapter);

        //FindMe init
        UserDefined.filter = "$FINDME$";

        Recorder.init(this, "DaddyActivity");
        CommandHandler.init(this);

        CommandHandler.getSharedCommandHandler().addExecutor("ALARM", new ExecutorAlarm() {
            @Override
            public JSONObject execute(Context context, int device_id, int count, JSONObject usr_json) {
                return super.execute(context, device_id, count, usr_json);
            }
        });
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
}
