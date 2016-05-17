package com.example.tothe.myapplication.common;

import android.os.Environment;

import com.example.tothe.myapplication.models.SessionData;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tothe on 5/17/16.
 */
public class FilesystemManager {


    private String filterRootName(String filename) {
        int index = filename.lastIndexOf(SessionData.LOG_JSON);
        if (index >= 0) {
            return new StringBuilder(filename).replace(index, index + SessionData.LOG_JSON.length(), "").toString();
        }
        return null;
    }


    private File getAssociatedGpx(File dir, String rootname) {
        File f = new File(dir.getAbsolutePath() + File.separator + rootname + SessionData.LOG_GPX);
        if (f.exists() && !f.isDirectory()) {
            return f;
        }

        return null;
    }

    //gets all sessions that have an associated file descriptor
    public List<SessionData> getAllSessionData(File... descriptors) {

        List<SessionData> sessions = new ArrayList<SessionData>();

        for (File desc : descriptors) {


            String rootName = filterRootName(desc.getName());
            if (rootName != null) {
                File gpx = getAssociatedGpx(desc.getParentFile(), rootName);
                if (gpx != null) {
                    sessions.add(new SessionData(gpx, desc));
                }
            }
        }
        return sessions;
    }


    //gets all sessions from the logging dir
    public List<SessionData> getAllSessions() {

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + SessionData.GPSLOGS);

        //get only the json descriptors
        File[] listOfFiles = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(SessionData.LOG_JSON);
            }
        });

        return getAllSessionData(listOfFiles);

    }

    public void deleteAllLogData() {
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + SessionData.GPSLOGS);

        for (File f : folder.listFiles()) {
            f.delete();
        }
    }
}
