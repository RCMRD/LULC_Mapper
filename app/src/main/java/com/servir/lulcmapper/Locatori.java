package com.servir.lulcmapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Locatori implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private LocationRequest mlr;
    private GoogleApiClient mgac;
    private Location cl;
    private final static int REQUEST_LOCATION = 2;
    private final static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
    private Context extCtx;
    private Activity extAct;
    private View View;
    private Bundle connectionBundle;
    private ConnectionListener connectionListener;
    private static final int confail = 9000;

    static double longitude = 0.0;
    static double latitude = 0.0;

    private void createlocreq(){
        mlr = new LocationRequest();
        mlr.setInterval(2000);
        mlr.setFastestInterval(1000);
        mlr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    public Locatori(Context context) {

        this.extCtx = context;

        createlocreq();

        buildGoogleApiClient();
        onStart();

    }

    public Locatori(Context context, Activity activity) {

        this.extCtx = context;
        this.extAct = activity;

        createlocreq();

        buildGoogleApiClient();
        onStart();
    }

    public GoogleApiClient getGoogleApiClient() {
        return this.mgac;
    }

    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
        if (this.connectionListener != null && isConnected()) {
            connectionListener.onConnected(connectionBundle);
        }
    }

    private void buildGoogleApiClient(){
        mgac = new GoogleApiClient.Builder(ApplicationContextor.getAppContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // TODO Auto-generated method stub

        startLocupdates();

        connectionBundle = bundle;
        if (connectionListener != null) {
            connectionListener.onConnected(bundle);
        }

    }



    @SuppressWarnings({"MissingPermission"})
    private void startLocupdates() {
        // TODO Auto-generated method stub

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(extCtx, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( extCtx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(extAct,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);

            ActivityCompat.requestPermissions(extAct,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
        } else {

            PendingResult<Status> pr = LocationServices.FusedLocationApi.requestLocationUpdates(mgac, mlr, this);

        }

    }



    public boolean isConnected() {
        return mgac != null && mgac.isConnected();
    }

    public void onDisconnected() {
        // TODO Auto-generated method stub
        updateUI();
        if (mgac != null && mgac.isConnected()) {
            mgac.disconnect();
        }
    }


    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub
        cl = arg0;
        updateUI();
    }

    public Location getLastLocation() {



        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(extCtx, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( extCtx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(extAct,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);

            ActivityCompat.requestPermissions(extAct,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
        } else {

            cl = LocationServices.FusedLocationApi.getLastLocation(
                    mgac);
        }
        return cl;
    }



    private void updateUI() {
        // TODO Auto-generated method stub


        if (null != cl){
            latitude = cl.getLatitude();
            longitude = cl.getLongitude();
        }
    }



    public void onResume(){

        if (mgac.isConnected()){
            startLocupdates();
        }

    }

    public void onStart(){

        if(mgac != null) {
            mgac.connect();
        }

    }

    public void onPause(){

        try{

            if (mgac.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mgac, this);
                mgac.disconnect();
            }
        }catch(Exception x){

        }
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RECOVER_PLAY_SERVICES) {
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mgac.isConnecting() &&
                        !mgac.isConnected()) {
                    mgac.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(extCtx, "Google Play Services must be installed.",
                        Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }


    @SuppressWarnings({"MissingPermission"})
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                PendingResult<Status> pr = LocationServices.FusedLocationApi.requestLocationUpdates(mgac, mlr, this);


            } else {
                Toast.makeText(extCtx, "GPS Location services must be enabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }




    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        Log.e("ConnLULC", "onConnectionSuspended: googleApiClient.connect()");
        mgac.connect();

        if (connectionListener != null) {
            connectionListener.onConnectionSuspended(arg0);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

        Log.e("ConnLULC", "onConnectionFailed: connectionResult = " + arg0);

        if (connectionListener != null) {
            connectionListener.onConnectionFailed(arg0);
        }


        if (arg0.hasResolution()){

            try{
                arg0.startResolutionForResult(extAct, confail);
            } catch (IntentSender.SendIntentException e){
                e.printStackTrace();
            }

        }else{
            Constantori.diambaidno(View, extCtx);
        }
    }

    public interface ConnectionListener {
        void onConnectionFailed(@NonNull ConnectionResult connectionResult);

        void onConnectionSuspended(int i);

        void onConnected(Bundle bundle);
    }


}