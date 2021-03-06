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
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONObject;
import org.mcnlab.lib.smscommunicate.CommandHandler;
import org.mcnlab.lib.smscommunicate.Recorder;
import org.mcnlab.lib.smscommunicate.UserDefined;

import java.util.ArrayList;
import java.util.HashMap;

public class SonActivity extends AppCompatActivity {
    //private Button googleMap;
    private double latitude = 25.016738;
    private double longitude = 121.533638;

    private FileManager fm;
    private int TotalNumOfData = 3;
    public final static String LOG_TAG = "SonActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.son);

        View backgroundimage = (View) findViewById(R.id.son_background);
        backgroundimage.getBackground().setAlpha(110);

        /*googleMap = (Button)findViewById(R.id.map_button);

        googleMap.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToMapPage();
            }
        });*/

        fm = new FileManager();
        ArrayList<HashMap<String, Object>> elder_arr = new ArrayList<HashMap<String, Object>>();

        // 從資料庫取得監控人資料放入ArrayList
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(fm.GetSonDB(), null, null);
        Cursor c = db.rawQuery("SELECT * FROM elder ORDER BY elderID", null);
        if(c.getCount() != 0){
            while(c.moveToNext()){
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("Name", c.getString(1));
                item.put("PhoneNumber", c.getString(2));
                elder_arr.add(item);
            }
        }

        ListView elder_list = (ListView)findViewById(R.id.elder_list);
        Elder_Adapter EldAdapter = new Elder_Adapter(this, elder_arr, TotalNumOfData);
        elder_list.setAdapter(EldAdapter);

        //FindMe init
        UserDefined.filter = "$FINDME$";

        Recorder.init(this, "SonActivity");
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

    /* Function used to jump to map page */
    protected void GoToMapPage(){
        Intent intent = new Intent();
        intent.setClass(SonActivity.this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        intent.putExtras(bundle);
        startActivity(intent);
        //  SonActivity.this.finish();
    }
}
