package com.example.findmydaddy;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class DaddyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daddy);

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

    }
}
