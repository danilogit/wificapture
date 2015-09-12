package com.apecalc.wificapture.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apecalc.wificapture.R;
import com.apecalc.wificapture.adapters.ListViewCaptureAdapter;
import com.apecalc.wificapture.dao.CaptureDao;
import com.apecalc.wificapture.db.DatabaseHelper;
import com.apecalc.wificapture.models.Capture;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    DatabaseHelper dh;
    CaptureDao captureDao;
    LocationManager locationManager;
    Location myLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // -- Mock Location --
        Location mockLocation = locationManager.getLastKnownLocation("passive");

        mockLocation.setAccuracy(10);
        mockLocation.setElapsedRealtimeNanos(new Date().getTime());
        mockLocation.setTime(new Date().getTime());
        mockLocation.setSpeed(50);

        // -- End Mock Location



        ListViewCaptureAdapter listViewCaptureAdapter = new ListViewCaptureAdapter();
        ListView lvCaptures = (ListView) findViewById(R.id.lvCaptures);
        TextView txtTotal = (TextView) findViewById(R.id.txtTotal);

        dh = new DatabaseHelper(getApplicationContext());
        List<String> captures = new ArrayList<String>();

        try {

            captureDao = new CaptureDao(dh.getConnectionSource());

            long total = captureDao.queryBuilder().countOf();
            txtTotal.setText("Total: " + String.valueOf(total) + " records");


            List<Capture> listCaptures = captureDao.queryBuilder()
                    .orderBy("timestamp", false)
                    .limit(1000)
                    .query();



            for (int i = 0; i < listCaptures.size(); i++)
            {
                captures.add(
                                " SSID: " +listCaptures.get(i).getSSID() + "\n" +
                                " Security: " +listCaptures.get(i).getWifiSecurityType() + "\n" +
                                " LOC: " + listCaptures.get(i).getLat() + ", " + listCaptures.get(i).getLon() + "\n" +
                                " ACC: " + listCaptures.get(i).getAccurancy() + "\n" +
                                " VIA: " + listCaptures.get(i).getProvider() + "\n" +
                                " DATE: " + new Date(listCaptures.get(i).getTimestamp()).toString()

                );
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> captureArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_view_item_captures, R.id.txtSSID ,captures);
        lvCaptures.setAdapter(captureArrayAdapter);


    }

    public void startCapture(View view)
    {
        Intent intent = new Intent("SERVICE_CAPTURE");
        intent.setPackage("com.apecalc.wificapture");
        startService(intent);
    }

    public void stopCapture(View view)
    {
        Intent intent = new Intent("SERVICE_CAPTURE");
        intent.setPackage("com.apecalc.wificapture");
        stopService(intent);
    }


    public void setCaptureOff()
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            exportCapturesToFile();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void enableGPS(View view)
    {

    }

    public void disableGPS(View view)
    {

    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    public void exportCapturesToFile()
    {

        TextView txtExporting = (TextView) findViewById(R.id.txtExporting);

        Date today = new Date();
        String filename = "file_" +  today.getTime() + ".csv";
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WifiCaptures" ;

        try {
            Toast.makeText(getApplicationContext(), "Start saving file in " + file_path, Toast.LENGTH_LONG).show();

            File fileDir = new File(file_path);
            fileDir.mkdirs();

            File file = new File(fileDir, filename);

            FileOutputStream outputStream = new FileOutputStream(file);


            List<Capture> captureList = captureDao.queryForAll();


            for (int i = 0; i < captureList.size(); i++)
            {
                String csv_line = captureList.get(i).toString() + "\n";
                outputStream.write(csv_line.getBytes());
                txtExporting.setText("Exporting: " + (i+1) + "/" + captureList.size());
            }

            outputStream.close();
            Toast.makeText(getApplicationContext(), "Captures saved in " + file_path, Toast.LENGTH_LONG).show();
        }

        catch (IOException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
