package com.tothe.tothe.bikeLogger.models;

import android.os.Environment;

import com.tothe.tothe.bikeLogger.common.GpxHelper;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by tothe on 5/17/16.
 */
public class SessionData {

    public static final String GPSLOGS = "GPSLogs";
    public static final String LOG_GPX = "-log.gpx";
    public static final String LOG_JSON = "-log.json";
    public static String LOGGING_DIR;
    File logFile;
    File descriptor;

    public SessionData(String loggingDir) {
        LOGGING_DIR = loggingDir;
        String isoDateTime = GpxHelper.getIsoDateTime(new Date());
        logFile = newFileFormat(isoDateTime, LOG_GPX);
        descriptor = newFileFormat(isoDateTime, LOG_JSON);


    }

    public SessionData(File logFile, File descriptor) {
        this.logFile = logFile;
        this.descriptor = descriptor;
    }


    private File newFileFormat(String rootTitle, String format) {
        File dirFile = new File(Environment.getExternalStorageDirectory() + File.separator + GPSLOGS);
        if (!dirFile.exists() || !dirFile.isDirectory()) {

            dirFile.mkdir();

        }
        File physFile = new File(Environment.getExternalStorageDirectory() + File.separator + GPSLOGS + File.separator + rootTitle + format);
        try {
            physFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GpxHelper.addToMediaDatabase(physFile);

        return physFile;
    }

    public File getLogged() {
        return logFile;
    }

    public void setLogged(File logged) {
        this.logFile = logged;
    }

    public File getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(File descriptor) {
        this.descriptor = descriptor;
    }
}
