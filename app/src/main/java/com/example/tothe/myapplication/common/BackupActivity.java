package com.example.tothe.myapplication.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tothe.myapplication.MainActivity;
import com.example.tothe.myapplication.R;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;


import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;


/**
 * Created by tothe on 5/4/16.
 */
public class BackupActivity {


      /*  final String EVENT_NAME="buttonMessage";
        private Socket mSocket;
        {
            try {
                mSocket = IO.socket("http://routetracker-tudorb.rhcloud.com:8000");
            } catch (URISyntaxException e) {}
        }


        private EditText mInputMessageView;

        private static Context context;

        public static Context getAppContext() {
            return BackupActivity.context;
        }*/
/*

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            MainActivity.context = getApplicationContext();

            mSocket.connect();



            LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    String msg="lat-long:" + String.valueOf(lat) + "-" + String.valueOf(lon);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    attemptSend(msg);
                    Toast.makeText(getApplicationContext(),"sent!",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {
                    Toast.makeText(getApplicationContext(), "Gps enabled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Toast.makeText(getApplicationContext(), "Gps disabled", Toast.LENGTH_SHORT).show();

                }
            };

            int perm=getApplicationContext().checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION");
            if(perm!= PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),"no gps permissions",Toast.LENGTH_SHORT).show();
            }
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locListener);
        }




        private void attemptSend(String message ) {

            if (TextUtils.isEmpty(message)) {
                return;
            }

            mSocket.emit("buttonMessage", message);
        }

*/

}
