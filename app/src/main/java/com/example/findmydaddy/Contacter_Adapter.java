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
public class Contacter_Adapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object> > contacter_list;
    private LayoutInflater LInflater;
    private Context context;
    private ViewHolder viewHolder;

    private class ViewHolder{
        public TextView phoneNum;
        public ImageView ARbtn;
    }
    public Contacter_Adapter(Context c, ArrayList<HashMap<String, Object> > list){
        context = c;
        contacter_list = list;
        LInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return contacter_list.size();
    }

    @Override
    public Object getItem(int position){
        return contacter_list.get(position);
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
            convertView = LInflater.inflate(R.layout.list_item_contacter, null);
            viewHolder = new ViewHolder();
            viewHolder.phoneNum = (TextView) convertView.findViewById(R.id.contacter_phone);
            viewHolder.ARbtn = (ImageView) convertView.findViewById(R.id.ARContacter);
            convertView.setTag(viewHolder);
        }

        HashMap<String, Object> info = contacter_list.get(position);
        if(info != null){
            String number = (String) info.get("PhoneNumber");
            viewHolder.phoneNum.setText(number);
            viewHolder.ARbtn.setImageDrawable(viewHolder.ARbtn.getResources().getDrawable(R.drawable.remove));
            //viewHolder.ARbtn.setOnClickListener(new ARButton_Click(position));
        }
        return convertView;
    }

    /*class ARButton_Click implements OnClickListener{
        private int position;

        ARButton_Click(int pos){
            position = pos;
        }

        @Override
        public void OnClick(View v){
            //do_something()
        }
    }*/
}
