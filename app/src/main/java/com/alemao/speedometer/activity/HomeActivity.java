package com.alemao.speedometer.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alemao.speedometer.R;
import com.alemao.speedometer.helper.DegressFormater;
import com.alemao.speedometer.helper.Permissao;
import com.alemao.speedometer.location.LocAvgFilter;

import java.text.DecimalFormat;
import java.util.Locale;

import im.delight.android.location.SimpleLocation;

public class HomeActivity extends AppCompatActivity {

    private LocationManager locManager;
    private LocationListener locListener = new MyLocationListener();

    LocAvgFilter locAvgFilter;

    private TextView txtLon;
    private TextView txtLat;
    private TextView txtAlt;
    private TextView txtAltError;
    private TextView txtInc;
    private TextView txtIncError;
    private TextView txtSpeed;
    private TextView txtSpeedError;
    private TextView txtAcu;
    private TextView txtTime;

    private String[] permission = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtLon = findViewById(R.id.home_lon);
        txtLat = findViewById(R.id.home_lat);
        txtAlt = findViewById(R.id.home_alt);
        txtAltError = findViewById(R.id.home_alt_error);
        txtInc = findViewById(R.id.home_inc);
        txtIncError = findViewById(R.id.home_inc_error);
        txtSpeed = findViewById(R.id.home_speed);
        txtSpeedError = findViewById(R.id.home_speed_error);
        txtAcu = findViewById(R.id.home_acu);
        txtTime = findViewById(R.id.home_time);

        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startUpdates();
    }

    @Override
    protected void onPause() {
        locManager.removeUpdates(locListener);
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
        }else {
            Toast.makeText(this, "Despulpe, mas e imposivel calcular a velocidade  por GPS sem ter acesso a localizacao!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void startUpdates(){
        if( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
        }else{
            Permissao.validaPermissoes(1, this, permission);
        }
    }

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                if(locAvgFilter==null) locAvgFilter = new LocAvgFilter(5, location);
                else locAvgFilter.addLoc(location);

                txtLon.setText(DegressFormater.format(locAvgFilter.getLongitude()));
                txtLat.setText(DegressFormater.format(locAvgFilter.getLatitude()));
                txtAcu.setText(String.format(getResources().getString(R.string.distance_error_formater), locAvgFilter.getAccuracy()));

                txtAlt.setText(String.format(getResources().getString(R.string.distance_formater), locAvgFilter.getAltitude()));
                txtAltError.setText(String.format(getResources().getString(R.string.distance_error_formater), locAvgFilter.getVerticalAccuracyMeters()));

                txtInc.setText(String.format(getResources().getString(R.string.degress_formater),locAvgFilter.getBearing(),  (char)0x00B0));
                txtIncError.setText(String.format(getResources().getString(R.string.degress_error_formater),locAvgFilter.getBearingAccuracyDegrees(),  (char)0x00B0));

                txtSpeed.setText(String.format(getResources().getString(R.string.speed_formater), locAvgFilter.getSpeed()*3.6));
                txtSpeedError.setText(String.format(getResources().getString(R.string.speed_error_formater), locAvgFilter.getSpeedAccuracyMetersPerSecond()*3.6));

                txtTime.setText(String.format(getResources().getString(R.string.time_formater),location.getTime()/1000));
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(HomeActivity.this, "GPS was turned off!!!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(HomeActivity.this, "GPS was turned on", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
}
