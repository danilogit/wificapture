package com.apecalc.wificapture.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import com.apecalc.wificapture.R;
import com.apecalc.wificapture.dao.CaptureDao;
import com.apecalc.wificapture.db.DatabaseHelper;
import com.apecalc.wificapture.models.Capture;
import com.j256.ormlite.stmt.ColumnArg;

import java.sql.SQLException;
import java.util.List;


public class WifiReceiver extends BroadcastReceiver
{

    public WifiManager getMainWifi() {
        return mainWifi;
    }

    public void setMainWifi(WifiManager mainWifi) {
        this.mainWifi = mainWifi;
    }

    public WifiManager mainWifi;
    private DatabaseHelper dh;
    private CaptureDao captureDao;
    LocationManager locationManager;
    Context myContext;
    MediaPlayer mediaPlayer;



    public void onReceive(Context c, Intent intent)
    {
            locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
            myContext = c;

            mediaPlayer = MediaPlayer.create(c, R.raw.galaga);

            dh = new DatabaseHelper(c);

            // passive, gps, network
            Location location = (Location) locationManager.getLastKnownLocation("passive");
            getBestLocation();

            List<ScanResult> wifiList;

            wifiList = mainWifi.getScanResults();

            for (int i = 0; i < wifiList.size(); i++) {
                ScanResult r = wifiList.get(i);
                Capture capture = new Capture();
                capture.setBSSID(r.BSSID);
                capture.setSSID(r.SSID);
                capture.setTimestamp(r.timestamp);
                capture.setLat(String.valueOf(location.getLatitude()));
                capture.setLon(String.valueOf(location.getLongitude()));
                capture.setFrequency(r.frequency);
                capture.setLevel(r.level);
                capture.setWifiSecurityType(r.capabilities);
                capture.setAccurancy(location.getAccuracy());
                capture.setProvider(location.getProvider());

                try {
                    captureDao = new CaptureDao(dh.getConnectionSource());

                    List<Capture> captureList =
                            captureDao.queryBuilder()
                                    .limit(1)
                                    .where()
                                    .eq("BSSID", capture.getBSSID())
                                    .query();

                    if (captureList.size() == 0) {
                       // mediaPlayer.start();
                        captureDao.create(capture);
                   }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    public Location getBestLocation()
    {

        locationManager = (LocationManager) myContext.getSystemService(Context.LOCATION_SERVICE);

        final Location bestLocation = locationManager.getLastKnownLocation("passive");


        Log.i("BestLocationHasSpeed", String.valueOf(bestLocation.hasSpeed()));
        Log.i("BestLocationAccuracy", String.valueOf(bestLocation.getAccuracy()));


        LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Log.i("BestLocation", "Location changed:" + location.getAccuracy());
                if(location.getAccuracy() < 100)
                {
                    locationManager.removeUpdates(this);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("BestLocation", "Status changed:" + provider + " Status: " + status);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("BestLocation", "Provider Enabled:" + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("BestLocation", "Provider Disabled:" + provider);
            }
        };


        if(bestLocation.getAccuracy() > 100)
        {
            locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
        }



        return null;
    }


}