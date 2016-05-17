package com.example.tothe.myapplication;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.Toast;


import com.example.tothe.myapplication.common.HttpCommunicator;
import com.example.tothe.myapplication.models.SessionData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    public static LogfileManager writer;
    public static String android_id;
    static Boolean isActive = false;
    private static Context context;
    protected String latestHdop;
    protected String latestPdop;
    protected String latestVdop;
    protected String geoIdHeight;
    protected String ageOfDgpsData;
    protected String dgpsId;
    protected int satellitesUsedInFix;
    SessionData dataFiles;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                logToServer();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTitle("Gps Logger");
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        stats = (TableLayout) findViewById(R.id.stats);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //myToolbar.h

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        buttonLogger = (Button) findViewById(R.id.logger);
        if (buttonLogger != null) {
            buttonLogger.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!isActive) {
                        // buttonLogger.setText("Stop logging!");
                        startGpsListen();
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
                        startGpsListen();
                    } else {
                        buttonLogger.setText("Start logging!");
                        stopGpsListen();
                    }
                    isActive = !isActive;
                }
            });
        }

        buttonSave = (Button) findViewById(R.id.saver);


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


    public void startGpsListen() {

        locListener = new GpsLocationListener(new String("fileLogger"), (LocationManager) getSystemService(Context.LOCATION_SERVICE));
        locListener.startListening();


    }

    private void stopGpsListen() {

        stats.setVisibility(View.VISIBLE);
        buttonSave.setVisibility(View.VISIBLE);
        buttonLogger.setVisibility(View.GONE);
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

                logManager.setStats(jsonStats());
            dataFiles = logManager.sessionData();

        }
        stats.setVisibility(View.GONE);
        buttonSave.setVisibility(View.GONE);
        buttonLogger.setVisibility(View.VISIBLE);

        Toast.makeText(MainActivity.getAppContext(), dataFiles.getLogged().getName() + " is saved", Toast.LENGTH_SHORT).show();
    }

    private JSONObject jsonStats() {
        JSONObject res = new JSONObject();
        try {
            res.put("challenging", getStatStatus(R.id.challenging, R.id.challengingT, R.id.challengingF));
            res.put("safe", getStatStatus(R.id.safe, R.id.safeT, R.id.safeF));
            res.put("crowded", getStatStatus(R.id.crowded, R.id.crowdedT, R.id.crowdedF));
            res.put("fast", getStatStatus(R.id.fast, R.id.fastT, R.id.fastF));
            res.put("recreational", getStatStatus(R.id.recreational, R.id.recreationalT, R.id.recreationalF));
            res.put("touristy", getStatStatus(R.id.touristy, R.id.touristyT, R.id.touristyF));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }


    private void logToServer() {

        if (dataFiles != null) {
            HttpCommunicator communicator = new HttpCommunicator();
            try {
                communicator.multipartPost(dataFiles);
            } catch (Exception e) {
                System.err.print(e.getStackTrace());
            }
        }

    }

}
