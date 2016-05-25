package com.tothe.tothe.bikeLogger.common;


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
