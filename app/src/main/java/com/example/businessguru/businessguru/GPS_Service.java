package com.example.businessguru.businessguru;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

/**
 * Created by filipp on 6/16/2016.
 */
public class GPS_Service extends Service {
/*
    private double longitude = 0;
    private double lantitude = 0;
    private double distance = 1;
*/
    private double longitude = -97.94256000;
    private double lantitude = 29.88931000;
    private double distance = 40;

    private LocationListener listener;
    private LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i = new Intent("location_update");
                i.putExtra("coordinates",location.getLongitude()+" "+location.getLatitude());

                //double euclideanDistance = Math.sqrt((location.getLongitude() - longitude) * (location.getLongitude() - longitude) + (location.getLatitude() - lantitude) * (location.getLatitude() - lantitude));
                float[] result = new float[3];
                Location.distanceBetween(location.getLatitude(), location.getLongitude(), lantitude, longitude, result);
                //   Intent light = new Intent("light_switch");
                if (result[0] < distance){
                    i.putExtra("light_signal","On");
                    //   sendBroadcast(light);
                } else if (result[0] > distance) {
                    i.putExtra("light_signal","Off");
                }
                else {
                    i.putExtra("light_signal",result[0]);
                    //   sendBroadcast(light);
                }

                //for testing
                /*
                longitude = location.getLongitude();
                lantitude = location.getLatitude();
                */
                sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,listener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }
}
