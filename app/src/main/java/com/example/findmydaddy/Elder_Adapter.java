package com.example.findmydaddy;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sony on 2015/12/16.
 */
public class Elder_Adapter extends BaseAdapter {
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

    OnClickListener ARButton_Click = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public Elder_Adapter(Context c, ArrayList<HashMap<String, Object> > list){
        context = c;
        elder_list = list;
        LInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return elder_list.size();
    }

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

        HashMap<String, Object> info = elder_list.get(position);
        if(info != null){
            String name = (String) info.get("Name");
            String number = (String) info.get("PhoneNumber");

            viewHolder.name.setText(name);
            viewHolder.phoneNum.setText(number);
            viewHolder.ARbtn.setImageDrawable(viewHolder.ARbtn.getResources().getDrawable(R.drawable.remove));
            viewHolder.ARbtn.setOnClickListener(ARButton_Click);
            //viewHolder.Locationbtn.setOnClickListener(new LocationButton_Click(position));
        }
        return convertView;
    }
}


