package com.example.tothe.myapplication;

import android.location.Location;
import android.os.Environment;

import com.example.tothe.myapplication.common.GpxHelper;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

public class LogfileManager {
    protected final static Object lock = new Object();


    JSONArray log_data;
    File logFile;
    boolean init;

    public LogfileManager() {

        logFile = new File(Environment.getExternalStorageDirectory() + File.separator + "GPSLogs" + File.separator + GpxHelper.getIsoDateTime(new Date()) + "-log.json");
        log_data = new JSONArray();
        init = false;
    }

    public void appendLog(Location loc) {

    }

    public void writeToFile(Location loc) {
        if (!logFile.exists()) {
            try {

                logFile.createNewFile();
                GpxHelper.addToMediaDatabase(logFile);
                // GpxHelper.writeToFile(logFile,GpxHelper.fileInit());
                GpxHelper.writeToFile(logFile, "data=[");
                init = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            if (init) {

            }
            GpxHelper.writeToFile(logFile, GpxHelper.getTrackJson(loc).toString() + ",\n");
            //GpxHelper.writeToFile(logFile,GpxHelper.getTrackPointXml(loc));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public File getLogFile() {
        return logFile;
    }


}

