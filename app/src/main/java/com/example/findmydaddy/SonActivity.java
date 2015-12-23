package com.example.findmydaddy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class SonActivity extends AppCompatActivity {
    private FileManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.son);

        fm = new FileManager();
        ArrayList<HashMap<String, Object>> elder_arr = new ArrayList<HashMap<String, Object>>();

        // 從資料庫取得監控人資料放入ArrayList
        for(int i=0; i < 3; i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("Name", "Mom " + Integer.toString(i));
            item.put("PhoneNumber", "0911111111");
            elder_arr.add(item);
        }

        ListView elder_list = (ListView)findViewById(R.id.elder_list);
        Elder_Adapter EldAdapter = new Elder_Adapter(this, elder_arr);
        elder_list.setAdapter(EldAdapter);
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
        intent.setClass(SonActivity.this, LoginActivity.class);
        startActivity(intent);
        SonActivity.this.finish();
    }
}
