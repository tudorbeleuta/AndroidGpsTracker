package com.tothe.tothe.bikeLogger;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by tothe on 5/3/16.
 */
public class GpsLocationListener implements LocationListener {
    protected String latestHdop;
    protected String latestPdop;
    protected String latestVdop;
    protected String geoIdHeight;
    protected String ageOfDgpsData;
    protected String dgpsId;
    protected int satellitesUsedInFix;
    protected LogfileManager writer;
    //private static final Logger LOG = new Logg;
    String listenerName;


    LocationManager locManager;

    public GpsLocationListener(String listenerName, LocationManager manager) {
        locManager = manager;
        this.listenerName = listenerName;
        writer = new LogfileManager();
    }

    @Override
    public void onLocationChanged(Location loc) {

        try {
            if (loc != null) {
                Bundle b = new Bundle();
                b.putString("HDOP", this.latestHdop);
                b.putString("PDOP", this.latestPdop);
                b.putString("VDOP", this.latestVdop);
                b.putString("GEOIDHEIGHT", this.geoIdHeight);
                b.putString("AGEOFDGPSDATA", this.ageOfDgpsData);
                b.putString("DGPSID", this.dgpsId);
                b.putBoolean("PASSIVE", listenerName.equalsIgnoreCase("PASSIVE"));
                b.putString("LISTENER", listenerName);
                b.putInt("SATELLITES_FIX", satellitesUsedInFix);

                loc.setExtras(b);
                writer.logCoords(loc);
                this.latestHdop = "";
                this.latestPdop = "";
                this.latestVdop = "";
            }

        } catch (Exception ex) {
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(MainActivity.getAppContext(), "Gps changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider != null) {
            Toast.makeText(MainActivity.getAppContext(), "Gps enabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider != null) {
            Toast.makeText(MainActivity.getAppContext(), "Gps disabled", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean checkGpsPermission() {
        int perm = MainActivity.getAppContext().checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION");
        if (perm != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.getAppContext(), "no gps permissions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean startListening() {
        writer.initGPX();
        if (!checkGpsPermission()) {
            return false;
        }
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
        return true;
    }

    public LogfileManager stopGpsListen() {

        checkGpsPermission();

        //File loggedFile = writer.getLogFile();

        locManager.removeUpdates(this);

        locManager = null;

        writer.terminateGPX();

        return writer;

    }


}
