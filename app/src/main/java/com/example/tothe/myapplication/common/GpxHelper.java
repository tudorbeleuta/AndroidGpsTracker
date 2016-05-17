package com.example.tothe.myapplication.common;

import android.location.Location;
import android.media.MediaScannerConnection;

import com.example.tothe.myapplication.BuildConfig;
import com.example.tothe.myapplication.MainActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GpxHelper {

    public static void writeToFile(File logFile, String text) throws IOException {
        BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));

        buf.append(text);
        buf.newLine();
        buf.flush();
        buf.close();
    }

    public static void addToMediaDatabase(File file) {

        MediaScannerConnection.scanFile(MainActivity.getAppContext(),
                new String[]{file.getPath()},
                new String[]{"text/plain"},
                null);
    }

    public static String getIsoDateTime(Date dateToFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.format(dateToFormat);
    }


    public static String getDateTimeString(Location loc) {
        long time = loc.getTime();
        if (time <= 0) {
            time = System.currentTimeMillis();
        }
        return getIsoDateTime(new Date(time));
    }

    public static String getGpxHeader() {

        StringBuilder initialXml = new StringBuilder();
        initialXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        initialXml.append("<gpx version=\"1.0\" creator=\"TudorBeleuta \" ");
        initialXml.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
        initialXml.append("xmlns=\"http://www.topografix.com/GPX/1/0\" ");
        initialXml.append("xsi:schemaLocation=\"http://www.topografix.com/GPX/1/0 ");
        initialXml.append("http://www.topografix.com/GPX/1/0/gpx.xsd\">");
        initialXml.append("<time>").append(getIsoDateTime(new Date())).append("</time>").append("<trk><trkseg>");
        return initialXml.toString();
    }

    public static String locationToGpx(Location loc) {

        String dateTimeString = getDateTimeString(loc);

        StringBuilder track = new StringBuilder();

        track.append("<trkpt lat=\"")
                .append(String.valueOf(loc.getLatitude()))
                .append("\" lon=\"")
                .append(String.valueOf(loc.getLongitude()))
                .append("\">");
        if (loc.hasSpeed()) {
            track.append("<speed>").append(String.valueOf(loc.getSpeed())).append("</speed>");
        }

        track.append("</trkpt>\n");

        //track.append("</trkseg>");

        return track.toString();
    }

    public static String getGpxFooter() {
        return "</trkseg></trk></gpx>";
    }

    public static JSONObject getTrackJson(Location loc) {

        String dateTimeString = getDateTimeString(loc);


        JSONObject itemPoint = new JSONObject();
        try {
            itemPoint.put("time", dateTimeString);
            itemPoint.put("lat", loc.getLatitude());
            itemPoint.put("long", loc.getLongitude());
            if (loc.hasSpeed()) {
                itemPoint.put("speed", loc.getSpeed());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return itemPoint;
    }

    public static void clearFile(File file) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");

            randomAccessFile.setLength(randomAccessFile.length() - 1);
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}