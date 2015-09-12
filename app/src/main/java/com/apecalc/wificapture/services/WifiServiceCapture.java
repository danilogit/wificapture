package com.apecalc.wificapture.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.apecalc.wificapture.receivers.WifiReceiver;

import java.util.List;

public class WifiServiceCapture extends Service
{

    WifiReceiver wifiReceiver;
    public boolean capture = false;
    private WifiManager wifiManager;
    private LocationManager locationManager;


    public WifiServiceCapture() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;
    }


    @Override
    public void onCreate() {
        registerWifiReceiver();
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        registerWifiReceiver();
        return super.onStartCommand(intent, flags, startId);

    }


    public void registerWifiReceiver()
    {

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

            if (wifiReceiver == null) {
                wifiReceiver = new WifiReceiver();
                wifiReceiver.setMainWifi(wifiManager);
            }

            registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(wifiReceiver);
        Log.i("Script", "onDestroy()");
        super.onDestroy();
    }


}
