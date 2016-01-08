package com.example.findmydaddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sony on 2015/12/16.
 */
public class Contacter_Adapter extends BaseAdapter {
    /* Region Private Fields*/
    private ArrayList<HashMap<String, Object> > contacter_list;
    private LayoutInflater LInflater;
    private Context context;
    private int NumOfShowingData;
    private ViewHolder viewHolder;
    private class ViewHolder{
        public TextView phoneNum;
        public ImageView ARbtn;
    }

    /* Constructor */
    public Contacter_Adapter(Context c, ArrayList<HashMap<String, Object> > list, int n){
        context = c;
        contacter_list = list;
        LInflater = LayoutInflater.from(context);
        NumOfShowingData = n;
    }

    /* Override BaseAdapter's methods */
    @Override
    public int getCount(){
        return NumOfShowingData;
    }

    @Override
    public Object getItem(int position){
        if(contacter_list.size() > 0)
            return contacter_list.get(position);
        else
            return new Object();
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
            convertView = LInflater.inflate(R.layout.list_item_contacter, null);
            viewHolder = new ViewHolder();
            viewHolder.phoneNum = (TextView) convertView.findViewById(R.id.contacter_phone);
            viewHolder.ARbtn = (ImageView) convertView.findViewById(R.id.ARContacter);
            convertView.setTag(viewHolder);
        }

        // Fill in the value of that list item
        if(position < contacter_list.size()){
            HashMap<String, Object> info = contacter_list.get(position);
            String number = (String) info.get("PhoneNumber");
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
        }else{
            viewHolder.phoneNum.setText("0900-000-000");
            viewHolder.ARbtn.setImageDrawable(viewHolder.ARbtn.getResources().getDrawable(R.drawable.add));
            viewHolder.ARbtn.setTag(position);
            viewHolder.ARbtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    CreateEditDialog(pos);
                }
            });

        }
        return convertView;
    }

    /* Function CreateEditDialog which is called when Add/Remove button is clicked */
    protected void CreateEditDialog(final int pos){
        View EditView = LInflater.inflate(R.layout.edit_contacter_dialog, null);
        AlertDialog.Builder EditDialog = new AlertDialog.Builder(context);
        EditDialog.setView(EditView);
        EditDialog.setTitle("編輯緊急聯絡人");
        final EditText EPhoneNum = (EditText) EditView.findViewById(R.id.EContacterPhone);

        if(pos < contacter_list.size()){
            // Get the information of that list item
            HashMap<String, Object> info = (HashMap<String, Object>) this.getItem(pos);
            EPhoneNum.setText((String) info.get("PhoneNumber"));
        }
        EditDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 將新的電話號碼存入資料庫
                FileManager fm = new FileManager();
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(fm.GetDaddyDB(), null, null);
                if(pos < contacter_list.size()){
                    db.execSQL("UPDATE contacter SET phone='" + EPhoneNum.getText().toString() + "' WHERE contacterID=" + pos + "");
                }else{
                    db.execSQL("INSERT INTO contacter(contacterID, phone) VALUES(" + pos + ", '" + EPhoneNum.getText().toString() + "')");
                }
                RefreshPage();
            }
        });

        EditDialog.show();
    }

    public void RefreshPage(){
        Intent intent = new Intent();
        intent.setClass(context, DaddyActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    /*
    *  falling alarm
    * when we calculate the falling condition, we send SMS with location to Contactor
    *
        Recorder rec = Recorder.getSharedRecorder();
        CommandHandler hdlr = CommandHandler.getSharedCommandHandler();
        SQLiteDatabase db = rec.getWritableDatabase();
        int device_id = rec.getDeviceIdByPhonenumberOrCreate(db, phone);
        db.close();
        hdlr.execute("ALARM", device_id, 0, null);
    *
    * */
}
