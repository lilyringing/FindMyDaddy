package com.example.findmydaddy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by sony on 2015/12/21.
 */
public class FileManager {
    private String path = Environment.getExternalStorageDirectory().toString();
    private String directory = "/FindMyDaddy";
    private File daddy_file;
    private File son_file;

    FileManager(){
        File dir = new File(path + directory);
        if(!dir.exists()){
            dir.mkdir();
        }
        path = path + directory;
        daddy_file = new File(path + "/daddy.txt");
        son_file = new File(path + "/son.txt");
    }

    public Boolean CreateDaddyFile(){
        if(!IsDaddyFile() && !IsSonFile()){
            try{
                daddy_file.createNewFile();
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path + "/DaddyDB.db", null, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS contacter(contacterID INT, phone VARCHAR, PRIMARY KEY(contacterID));");
            }catch (IOException e){
                e.toString();
                return false;
            }
            return true;
        }else{
            return false;
        }

    }

    public Boolean CreateSonFile(){
        if(!IsDaddyFile() && !IsSonFile()) {
            try{
                son_file.createNewFile();
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path + "/SonDB.db", null, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS elder(elderID INT, name VARCHAR, phone VARCHAR, PRIMARY KEY(elderID));");
            }catch (IOException e){
                e.toString();
                return false;
            }
            return true;
        }else {
            return false;
        }
    }

    public Boolean RemoveDaddyFile(){
        if(IsDaddyFile()){
            daddy_file.delete();
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path + "/DaddyDB.db", null, null);
            db.execSQL("DROP TABLE IF EXISTS contacter");
            return true;
        }else{
            return false;
        }

    }

    public Boolean RemoveSonFile(){
        if(IsSonFile()){
            son_file.delete();
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path + "/SonDB.db", null, null);
            db.execSQL("DROP TABLE IF EXISTS elder");
            return true;
        }else{
            return false;
        }

    }

    public Boolean IsDaddyFile(){
        return daddy_file.exists();
    }

    public Boolean IsSonFile(){
        return son_file.exists();
    }
    public String GetDaddyDB(){ return path + "/DaddyDB.db";}
    public String GetSonDB(){ return path + "/SonDB.db";}
}
