package com.example.tothe.myapplication;

import android.location.Location;

import com.example.tothe.myapplication.common.GpxHelper;
import com.example.tothe.myapplication.models.SessionData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

public class LogfileManager {

    public static final String LOG_REF = "log-ref";
    public static final String DEVICE_NAME = "device_name";
    public static final String GICA = "gica";
    SessionData sessionFiles;


    public LogfileManager() {
        sessionFiles = new SessionData(SessionData.GPSLOGS);
    }

    public LogfileManager(SessionData data) {
        this.sessionFiles = data;
    }


    public void initGPX() {

        try {
            GpxHelper.writeToFile(sessionFiles.getLogged(), GpxHelper.getGpxHeader());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void logCoords(Location loc) throws IOException {

        GpxHelper.writeToFile(sessionFiles.getLogged(), GpxHelper.locationToGpx(loc));

    }


    public void terminateGPX() {

        if (sessionFiles.getLogged().getFreeSpace() != sessionFiles.getLogged().getTotalSpace())
            try {
                GpxHelper.writeToFile(sessionFiles.getLogged(), GpxHelper.getGpxFooter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //write session description data to json file
    public void setStats(JSONObject stats) {

        JSONObject fileDesc = null;
        try {
            fileDesc = addLoggerToDescriptor();
            fileDesc.put(DEVICE_NAME, MainActivity.android_id);
            fileDesc.put("stats", stats);
            fileDesc.put("time", new Date());
            //....? should add some others
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            GpxHelper.writeToFile(sessionFiles.getDescriptor(), fileDesc.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject addLoggerToDescriptor() throws JSONException {
        JSONObject resp = new JSONObject();
        resp.put(LOG_REF, sessionFiles.getLogged().getName());
        return resp;
    }


    public SessionData sessionData() {
        return sessionFiles;
    }


}

