package com.example.findmydaddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.app.Activity;
import android.widget.Toast;

import org.mcnlab.lib.smscommunicate.CommandHandler;
import org.mcnlab.lib.smscommunicate.Recorder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sony on 2015/12/16.
 */
public class Elder_Adapter extends BaseAdapter {
    /* Region Private Fields */
    private ArrayList<HashMap<String, Object> > elder_list;
    private LayoutInflater LInflater;
    private Context context;
    private ViewHolder viewHolder;
    private class ViewHolder{
        public TextView name;
        public TextView phoneNum;
        public ImageView ARbtn;
        public ImageView Locationbtn;
    }

    /* Constructor */
    public Elder_Adapter(Context c, ArrayList<HashMap<String, Object> > list){
        context = c;
        elder_list = list;
        LInflater = LayoutInflater.from(context);
    }

    /* Override BaseAdapter's methods */
    @Override
    public int getCount() { return elder_list.size(); }

    @Override
    public Object getItem(int position){
        return elder_list.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Find view components of a list item
        if(convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        }else{
            convertView = LInflater.inflate(R.layout.list_item_elder, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.elder_name);
            viewHolder.phoneNum = (TextView) convertView.findViewById(R.id.elder_phone);
            viewHolder.ARbtn = (ImageView) convertView.findViewById(R.id.ARElder);
            viewHolder.Locationbtn = (ImageView) convertView.findViewById(R.id.location_btn);
            convertView.setTag(viewHolder);
        }

        // Fill in the value of that list item
        HashMap<String, Object> info = elder_list.get(position);
        if(info != null){
            String name = (String) info.get("Name");
            String number = (String) info.get("PhoneNumber");

            viewHolder.name.setText(name);
            viewHolder.phoneNum.setText(number);
            viewHolder.ARbtn.setImageDrawable(viewHolder.ARbtn.getResources().getDrawable(R.drawable.remove));
            viewHolder.ARbtn.setTag(position);
            viewHolder.ARbtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    CreateEditDialog(pos);
                }
            });
            viewHolder.Locationbtn.setTag(position);
            viewHolder.Locationbtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    GetElderLocation(pos);
                }
            });
        }
        return convertView;
    }

    /* Function CreateEditDialog which is called when Add/Remove button is clicked */
    protected void CreateEditDialog(int pos){
        // Get the information of that list item
        HashMap<String, Object> info = (HashMap<String, Object>) this.getItem(pos);

        View EditView = LInflater.inflate(R.layout.edit_elder_dialog, null);
        AlertDialog.Builder EditDialog = new AlertDialog.Builder(context);
        EditDialog.setView(EditView);
        EditDialog.setTitle("編輯監控人");
        EditText EName = (EditText) EditView.findViewById(R.id.EElderName);
        EditText EPhoneNum = (EditText) EditView.findViewById(R.id.EElderPhone);

        if(info != null){
            EName.setText((String) info.get("Name"));
            EPhoneNum.setText((String) info.get("PhoneNumber"));
        }
        EditDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 將新的名字和電話號碼存入資料庫
                //Log.e("Test", which to string);
            }
        });

        EditDialog.show();
    }

    protected void GetElderLocation(int pos){
        // Get the information of that list item
        HashMap<String, Object> info = (HashMap<String, Object>) this.getItem(pos);

        // Send an SMS to this phone number to get elder's location
        String phone = (String) info.get("PhoneNumber");

        //FindMe : get elder's location (first, send a SMS with cnt = 0)
        Recorder rec = Recorder.getSharedRecorder();
        CommandHandler hdlr = CommandHandler.getSharedCommandHandler();
        SQLiteDatabase db = rec.getWritableDatabase();
        int device_id = rec.getDeviceIdByPhonenumberOrCreate(db, phone);
        db.close();
        hdlr.execute("WHERE", device_id, 0, null);

        /* 格式待修改
        // Jump to map page
        Intent intent = new Intent();
        intent.setClass(context, MapsActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
        */
    /*
    *
    * //FindMe : ask elder to Call Me
        Recorder rec = Recorder.getSharedRecorder();
        CommandHandler hdlr = CommandHandler.getSharedCommandHandler();
        SQLiteDatabase db = rec.getWritableDatabase();
        int device_id = rec.getDeviceIdByPhonenumberOrCreate(db, phone);
        db.close();
        hdlr.execute("CALLME", device_id, 0, null);
    *
    * */
    }
}


