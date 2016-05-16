package com.example.tothe.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.Toast;


import com.example.tothe.myapplication.common.HttpCommunicator;

import org.json.JSONException;
import org.json.JSONObject;

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
    Button buttonLogger;
    Button buttonSave;
    Button buttonUploader;
    TableLayout stats;
    LogfileManager logManager;

    public static Context getAppContext() {
        return MainActivity.context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTitle("Gps Logger");
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        stats = (TableLayout) findViewById(R.id.stats);


        buttonLogger = (Button) findViewById(R.id.logger);
        if (buttonLogger != null) {
            buttonLogger.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!isActive) {
                        // buttonLogger.setText("Stop logging!");
                        stasrtGpsListen();
                    } else {
                        //buttonLogger.setText("Start logging!");
                        stopGpsListen();
                    }
                    isActive = !isActive;
                }
            });
        }

        buttonSave = (Button) findViewById(R.id.saver);
        //buttonSave.setVisibility(View.INVISIBLE);

        if (buttonSave != null) {
            buttonSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    saveSession();
                }
            });
        }


        buttonUploader = (Button) findViewById(R.id.uploader);
        if (buttonUploader != null) {
            buttonUploader.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    logToServer();
                }
            });
        }
        // initViewComponents();

    }

    private void initViewComponents() {
        stats = (TableLayout) findViewById(R.id.stats);


        buttonLogger = (Button) findViewById(R.id.logger);
        if (buttonLogger != null) {
            buttonLogger.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!isActive) {
                        buttonLogger.setText("Stop logging!");
                        stasrtGpsListen();
                    } else {
                        buttonLogger.setText("Start logging!");
                        stopGpsListen();
                    }
                    isActive = !isActive;
                }
            });
        }

        buttonSave = (Button) findViewById(R.id.saver);
        //buttonSave.setVisibility(View.INVISIBLE);

        if (buttonLogger != null) {
            buttonLogger.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    saveSession();
                }
            });
        }


        buttonUploader = (Button) findViewById(R.id.uploader);
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

        stats.setVisibility(View.VISIBLE);
        buttonSave.setVisibility(View.VISIBLE);
        buttonLogger.setVisibility(View.INVISIBLE);
        //loggedFile = locListener.stopGpsListen();
        logManager = locListener.stopGpsListen();

        locListener = null;

    }

    private int getStatStatus(int grid, int tr, int fl) {

        RadioGroup statGroup = (RadioGroup) findViewById(grid);
        int statId = statGroup.getCheckedRadioButtonId();
        statGroup.clearCheck();
        return statId == tr ? 1 : (fl == statId ? -1 : 0);
    }

    private void saveSession() {

        if (logManager != null) {
            try {
                logManager.setStats(jsonStats());
                loggedFile = logManager.getLogFile();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        stats.setVisibility(View.INVISIBLE);
        buttonSave.setVisibility(View.INVISIBLE);
        buttonLogger.setVisibility(View.VISIBLE);

        Toast.makeText(MainActivity.getAppContext(), loggedFile.getName() + " saved", Toast.LENGTH_SHORT).show();
    }

    private JSONObject jsonStats() throws JSONException {
        JSONObject res = new JSONObject();

        res.put("challenging", getStatStatus(R.id.challenging, R.id.challengingT, R.id.challengingF));
        res.put("safe", getStatStatus(R.id.safe, R.id.safeT, R.id.safeF));
        res.put("crowded", getStatStatus(R.id.crowded, R.id.crowdedT, R.id.crowdedF));
        res.put("fast", getStatStatus(R.id.fast, R.id.fastT, R.id.fastF));
        res.put("recreational", getStatStatus(R.id.recreational, R.id.recreationalT, R.id.recreationalF));
        res.put("touristy", getStatStatus(R.id.touristy, R.id.touristyT, R.id.touristyF));
        return res;
    }


    private void logToServer() {
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        if (loggedFile != null) {
            HttpCommunicator communicator = new HttpCommunicator();
            try {
                progress.show();
                communicator.multipartPost(loggedFile);
            } catch (Exception e) {
                System.err.print(e.getStackTrace());
            }
            progress.dismiss();
            Toast.makeText(MainActivity.getAppContext(), communicator.getStatus().toString(), Toast.LENGTH_SHORT).show();
        }

    }

}
