package com.example.findmydaddy;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class SonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.son);

        ArrayList<HashMap<String, Object>> elder_arr = new ArrayList<HashMap<String, Object>>();

        // 從資料庫取得監控人資料放入ArrayList
        for(int i=0; i < 3; i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("Name", "Mom");
            item.put("PhoneNumber", "0911111111");
            elder_arr.add(item);
        }

        ListView elder_list = (ListView)findViewById(R.id.elder_list);
        Elder_Adapter EldAdapter = new Elder_Adapter(this, elder_arr);
        elder_list.setAdapter(EldAdapter);
    }
}
