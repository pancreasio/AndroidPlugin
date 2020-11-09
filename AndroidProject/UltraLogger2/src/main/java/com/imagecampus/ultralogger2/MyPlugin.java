package com.imagecampus.ultralogger2;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CacheRequest;
import java.util.Scanner;

import static java.security.AccessController.getContext;

public class MyPlugin {
    private static final MyPlugin ourInstance = new MyPlugin();

    private static final String LOGTAG = "ImageCampus";
    private static final String FILENAME = "logs.txt";
    private long startTime;
    private String rootPath;
    public static Activity mainActivity;

    public static MyPlugin getInstance() {return ourInstance;}

    public interface AlertViewCallback{
        public void onButtonTapped(int id);
    }

    private MyPlugin() {
        Log.i(LOGTAG, "Created MyPlugin");
        startTime = System.currentTimeMillis();
        rootPath = System.getProperty("user.dir");
    }

    public double GetElapsedTime(){
        return (System.currentTimeMillis() - startTime)/1000;
    }

    public void RegisterLog(String logLevel, String logContent){

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mainActivity.openFileOutput(FILENAME, Context.MODE_APPEND));
            outputStreamWriter.write(logLevel + ": "+ logContent);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public boolean DeleteLogs(){
        File dir = mainActivity.getFilesDir();
        File file = new File(dir, FILENAME);
        boolean res = file.delete();
        if (res) {
            System.out.println("Logs deleted ");
        } else {
            System.out.println("Logfile does not exist");
        }
        return res;
    }

    public String GetLogs(){
        String ret = "";

        try {
            InputStream inputStream = mainActivity.openFileInput(FILENAME);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public void ShowLogs(final AlertViewCallback callback){
        ShowAlertView(new String[] {"Registered Logs", GetLogs() ,"Done" }, callback);
    }

    public void ShowAlertView(String[] strings, final AlertViewCallback callback){
        if(strings.length<3) {
            Log.i(LOGTAG, "Error: expected >=3 strings, got " + strings.length);
            return;
        }
        DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int id){
                dialogInterface.dismiss();
                Log.i(LOGTAG, "Tapped: "+ id);
                callback.onButtonTapped(id);
            }
        };

        AlertDialog alertDialog = new AlertDialog.Builder(mainActivity)
                .setTitle(strings[0])
                .setMessage(strings[1])
                .setCancelable(false)
                .create();
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, strings[2], myClickListener);
        if(strings.length>3)
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, strings[3], myClickListener);
        if(strings.length>4)
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, strings[4], myClickListener);
        alertDialog.show();
    }
}
