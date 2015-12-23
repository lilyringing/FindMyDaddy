package com.example.findmydaddy;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by sony on 2015/12/21.
 */
public class FileManager {
    private String path = Environment.getExternalStorageDirectory().toString();
    private File daddy_file = new File(path + "/daddy.txt");
    private File son_file = new File(path + "/son.txt");

    public Boolean CreateDaddyFile(){
        if(!IsDaddyFile() && !IsSonFile()){
            try{
                daddy_file.createNewFile();
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
            return true;
        }else{
            return false;
        }

    }

    public Boolean RemoveSonFile(){
        if(IsSonFile()){
            son_file.delete();
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
}
