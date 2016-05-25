package com.tothe.tothe.bikeLogger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.tothe.tothe.bikeLogger.common.FilesystemManager;
import com.tothe.tothe.bikeLogger.common.HttpCommunicator;
import com.tothe.tothe.bikeLogger.models.SessionData;
import com.tothe.tothe.bikeLogger.models.User;
import com.tothe.tothe.bikeLogger.storage.DbHelper;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    public static LogfileManager writer;
    public static String android_id;
    static Boolean listens = false;
    static boolean waitingForSave = false;
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
    DbHelper database;
    //AlertDialog.Builder dialog;
    AlertDialog profileDialog;
    AlertDialog dialog;
    public static Context getAppContext() {
        return MainActivity.context;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload_delete:
                logToServer(true);
                return true;

            case R.id.upload:
                logToServer(false);
                return true;

            case R.id.clear_all:
                deleteAll();
                return true;

            case R.id.about:
                dialog.show();
                //infoDialog.show();
                return true;
            case R.id.profile:
                profileDialog.show();
                //infoDialog.show();
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
        super.setTitle("Route on bike");
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        stats = (TableLayout) findViewById(R.id.stats);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        dialog = initDialog();
        profileDialog = initProfileDialog();
        //infoDialog=new InfoDialog().getDialog();
//        initDialog();
        //myToolbar.h
        database = new DbHelper(this);


        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        buttonLogger = (Button) findViewById(R.id.logger);
        buttonLogger.setText("Start logging!");
        if (buttonLogger != null) {
            buttonLogger.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    listens = !listens;
                    if (listens) {
                        buttonLogger.setText("Listening, tap to stop...");
                        startGpsListen();
                    } else {

                        buttonLogger.setText("Start logging!");
                        stopGpsListen();
                    }

                }


            });
        }

        buttonSave = (Button) findViewById(R.id.saver);
        //buttonSave.setVisibility(View.INVISIBLE);

        if (buttonSave != null) {
            buttonSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    waitingForSave = false;
                    saveSession();
                }
            });
        }


        // initViewComponents();

    }


    protected void onDestroy() {
        if (listens) {
            stopGpsListen();
            saveSession();
        }
        super.onDestroy();
    }

    public void startGpsListen() {

        locListener = new GpsLocationListener(new String("fileLogger"), (LocationManager) getSystemService(Context.LOCATION_SERVICE));
        locListener.startListening();


    }

    private void stopGpsListen() {
        waitingForSave = true;
        stats.setVisibility(View.VISIBLE);
        buttonSave.setVisibility(View.VISIBLE);
        buttonLogger.setVisibility(View.GONE);

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


    private void logToServer(boolean deleteUploads) {
        if (waitingForSave) {
            saveSession();
        }
        HttpCommunicator communicator = new HttpCommunicator();
        FilesystemManager fsManager = new FilesystemManager();


        try {

                communicator.postMultipleSessions(fsManager.getAllSessions(), deleteUploads);
                //communicator.postSingleSession(dataFiles);
            } catch (Exception e) {
                System.err.print(e.getStackTrace());
            }

    }

    private void deleteAll() {
        FilesystemManager fsManager = new FilesystemManager();
        fsManager.deleteAllLogData();
    }


    private AlertDialog initProfileDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        // 2. Chain together various setter methods to set the dialog characteristics
        final View profileView = inflater.inflate(R.layout.dialog_details, null);
        builder.setView(profileView)
                .setTitle("Your profile");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                TextView nametv = (TextView) profileView.findViewById(R.id.name);

                TextView emailtv = (TextView) profileView.findViewById(R.id.email);

                database.addUser(new User(nametv.getText().toString(), emailtv.getText().toString()));
                Toast.makeText(MainActivity.getAppContext(), database.getAllContacts().toString(), Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel,", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        // 3. Get the AlertDialog from create()
        return builder.create();

    }


    private AlertDialog initDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.app_description)
                .setTitle("About");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        // 3. Get the AlertDialog from create()
        return builder.create();

    }

}
