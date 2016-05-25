package com.tothe.tothe.bikeLogger.common;

import android.os.Environment;

import com.tothe.tothe.bikeLogger.models.SessionData;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tothe on 5/17/16.
 */
public class FilesystemManager {


    public static final int MIN_GPX_FILESIZE = 345;

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

    //gets all sessions that have an associated file descriptor and are above a certain size in bytes

    public List<SessionData> getAllSessionData(int fileByteSize, File... descriptors) {

        List<SessionData> sessions = new ArrayList<SessionData>();

        for (File desc : descriptors) {


            String rootName = filterRootName(desc.getName());
            if (rootName != null) {
                File gpx = getAssociatedGpx(desc.getParentFile(), rootName);
                if (gpx != null && gpx.length() > fileByteSize) {
                    sessions.add(new SessionData(gpx, desc));
                } else {
                    //gpx.delete();
                    //will delete the descriptor for noncorresponding file
                    desc.delete();
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

        return getAllSessionData(MIN_GPX_FILESIZE, listOfFiles);

    }

    public void deleteAllLogData() {
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + SessionData.GPSLOGS);
        if (folder.exists()) {
            for (File f : folder.listFiles()) {
                f.delete();
            }
        }

    }
}
