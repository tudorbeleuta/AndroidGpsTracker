package com.example.tothe.myapplication;

import android.content.Context;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.tothe.myapplication.common.HttpCommunicator;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    public static LogfileManager writer;
    static Boolean isActive = false;
    private static Context context;
    protected String latestHdop;
    protected String latestPdop;
    protected String latestVdop;
    protected String geoIdHeight;
    protected String ageOfDgpsData;
    protected String dgpsId;
    protected int satellitesUsedInFix;
    File loggedFile;
    //private static final Logger LOG = new Logg;
    String listenerName = "test";
    GpsLocationListener locListener;

    public static Context getAppContext() {
        return MainActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();


       /* HttpCommunicator comm = new HttpCommunicator();
        try {
            comm.send();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        final Button buttonLogger = (Button) findViewById(R.id.logger);
        if (buttonLogger != null) {
            buttonLogger.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!isActive) {
                        stasrtGpsListen();
                    } else {
                        stopGpsListen();

                    }
                    isActive = !isActive;
                }
            });
        }

        final Button buttonUploader = (Button) findViewById(R.id.uploader);
        if (buttonUploader != null) {
            buttonUploader.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    logToServer();
                }
            });
        }



    }


    public void stasrtGpsListen() {

        locListener = new GpsLocationListener(new String("fileLogger"), (LocationManager) getSystemService(Context.LOCATION_SERVICE));
        locListener.startListening();
    }

    private void stopGpsListen() {

        loggedFile = locListener.stopGpsListen();
        locListener = null;
        Toast.makeText(MainActivity.getAppContext(), loggedFile.getName(), Toast.LENGTH_SHORT).show();
    }


    private void logToServer() {
        if (loggedFile != null) {
            HttpCommunicator communicator = new HttpCommunicator();
            communicator.execute(loggedFile);
            Toast.makeText(MainActivity.getAppContext(), communicator.getStatus().toString(), Toast.LENGTH_SHORT).show();
        }

    }
}
