package com.servir.lulcmapper;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.servir.lulcmapper.Utils.ApplicationContextor;
import com.servir.lulcmapper.Utils.AsyncTaskCompleteListener;
import com.servir.lulcmapper.Utils.Constantori;
import com.servir.lulcmapper.Utils.DatabaseHandler;
import com.servir.lulcmapper.Utils.Locatori;
import com.servir.lulcmapper.Utils.NetPost;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class Loginno extends AppCompatActivity implements AsyncTaskCompleteListener {

    EditText logemailA, logpassA;
    TextView logsignupA, logforgotA;
    Button logsigninA;
    View View;
    String mail="";
    public Context context = this;
    //locTrak2 locii =  new locTrak2(context);
    Locatori loka = new Locatori(this, Loginno.this);
    String pass="";
    DatabaseHandler db = DatabaseHandler.getInstance(this);
    String statt = "";
    String pswd = "";
    String myil = "";
    String nisa = "";
    String refo = "";
    String sax = "0.0";
    String say = "0.0";
    String naniask = "";
    private final static int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 13;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loggah);

        overridePendingTransition(0,0);

        if (Build.VERSION.SDK_INT < 23) {

        }else {
            reqPermission("top");
        }

        logsigninA = (Button) findViewById(R.id.logingia);
        logpassA = (EditText) findViewById(R.id.logpass);
        logemailA = (EditText) findViewById(R.id.logemail);
        logsignupA = (TextView) findViewById(R.id.logreg);
        logforgotA = (TextView) findViewById(R.id.logforgot);

        //this.deleteDatabase(db.getDatabaseName());
        //db.resetAllTables();
        //db.emptyTable(Constantori.TABLE_DAT);

        if (db.CheckIsDataAlreadyInDBorNot(Constantori.TABLE_REGISTER, Constantori.KEY_USERACTIVE, Constantori.USERACTIVE) && !db.CheckIsDataAlreadyInDBorNot(Constantori.TABLE_REGISTER, Constantori.KEY_USERREF, Constantori.USERREFNULL)) {

            List<HashMap<String, String>> regData = db.GetAllData(Constantori.TABLE_REGISTER,"","");
            HashMap<String, String> UserDetails = regData.get(0);

            mail = UserDetails.get(Constantori.KEY_USEREMAIL);
            pass = UserDetails.get(Constantori.KEY_USERPASS);
            nisa = UserDetails.get(Constantori.KEY_USERORG);
            refo = UserDetails.get(Constantori.KEY_USERREF);

            logemailA.setText(mail);

        } else {
            statt = Constantori.USERUNREG;
        }


        /*

        ALTERMATIVE ACCESS METHOD

        App.getGoogleApiHelper().setConnectionListener(new GoogleApiHelper.ConnectionListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }

            @Override
            public void onConnectionSuspended(int i) {

            }

            @Override
            public void onConnected(Bundle bundle, GoogleApiClient googleApiClient) {
                //this function will call whenever google api connected or already connected when setting listener
                //You are connected do what ever you want
                //Like i get last known location
                Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            }
        });*/




        Timer timer = new Timer("swcha");
        timer.schedule(new TimerTask() {

            public void run() {

                runOnUiThread(new Runnable() {


                    @Override
                    public void run() {


                        if(ApplicationContextor.getGoogleApiHelper().isConnected())
                        {

                            if ( Build.VERSION.SDK_INT >= 23 &&
                                    ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(Loginno.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        Constantori.REQUEST_LOCATION);

                                ActivityCompat.requestPermissions(Loginno.this,
                                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                        Constantori.REQUEST_LOCATION);
                            } else {

                                GoogleApiClient client = ApplicationContextor.getGoogleApiHelper().getGoogleApiClient();

                                Location ll = LocationServices.FusedLocationApi.getLastLocation(client);

                                if (ll != null) {
                                    sax = String.valueOf(ll.getLongitude());
                                    say = String.valueOf(ll.getLatitude());
                                }
                            }

                        }



                        Log.e("wapi", String.format("LAT : %s \n LON : %s", say, sax));


                    }

                });

            }

        }, 0, 2000);




        logsigninA.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {

                if (Build.VERSION.SDK_INT < 23) {
                    anaingia();
                } else {

                    if (
                            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED

                            ) {

                        anaingia();

                    } else {
                     reqPermission("anaingia");

                    }


                }


            }

        });

    }



    public void onClick_su(View v)
    {


        if (Build.VERSION.SDK_INT < 23) {
            anareg();
        } else {

            if (
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED

                    ) {

                anareg();

            } else {

                reqPermission("anareg");

            }


        }

    }


    public void onClick_fd(View v)
    {
     //
    }




    public void diambaid_newuser(View v) {
        final Dialog ingia = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        ingia.setContentView(R.layout.mbaind_newuser);
        ingia.setCanceledOnTouchOutside(false);
        ingia.setCancelable(false);
        WindowManager.LayoutParams lp = ingia.getWindow().getAttributes();
        lp.dimAmount=0.85f;
        ingia.getWindow().setAttributes(lp);
        ingia.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ingia.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ingia.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Button regno = (Button) ingia.findViewById(R.id.sawa);
        Button regyes = (Button) ingia.findViewById(R.id.rback);

        regyes.setOnClickListener(new android.view.View.OnClickListener(){
            @Override
            public void onClick(View v){

                ingia.dismiss();


                Map<String,String> map = new HashMap<String, String>();
                map.put("pswd", pswd);
                map.put("mail", myil);

                if (Constantori.isConnectedToInternet()) {
                    new NetPost(context, "login_CheckJSON", Constantori.getJSON(map), "Authenticating. Make sure internet connection is active", Constantori.TABLE_REGISTER, Constantori.KEY_USERACTIVE, Loginno.this).execute(new String[]{Constantori.URL_GEN});
                }else{
                    Toast.makeText(context,Constantori.ERROR_NO_INTERNET,Toast.LENGTH_LONG).show();
                }



            }
        });
        regno.setOnClickListener(new android.view.View.OnClickListener(){
            @Override
            public void onClick(View v){

                Toast.makeText(context, "No active user account exists on the phone. Please Sign Up first ", Toast.LENGTH_LONG).show();
                ingia.dismiss();
            }
        });
        ingia.show();
    }


    @SuppressLint("NewApi")
    private void reqPermission(final String nini) {

        naniask = nini;

        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();

        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Location");
        if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Storage");
        if (!addPermission(permissionsList, android.Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (!addPermission(permissionsList, android.Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Cell Network");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "This application needs to access your " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        return;

    }

    @SuppressLint("NewApi")
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        ) {
                    // All Permissions Granted
                    if (naniask.equals("anaingia")) {
                        anaingia();
                    }

                    if (naniask.equals("anareg")) {
                        anareg();
                    }


                    Toast.makeText(context, "Permissions enabled", Toast.LENGTH_LONG).show();

                } else {
                    // Permission Denied
                    Toast.makeText(context, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    private void anaingia(){

        if (logemailA.getText().toString().equals("") || logpassA.getText().toString().equals("")) {

            Toast.makeText(context, "Please fill in the login detail", Toast.LENGTH_LONG).show();

        } else {

            pswd = logpassA.getText().toString().trim();
            myil = logemailA.getText().toString().trim();

            if (statt.equals(Constantori.USERUNREG)){
                diambaid_newuser(View);
            }else{

                if (pswd.equals(pass) && myil.equals(mail)) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                } else if (!pswd.equals(pass) && myil.equals(mail)) {
                    Toast.makeText(context, "Please insert the right password for the user account.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, "New user? If registered, click on Yes.", Toast.LENGTH_LONG).show();
                    diambaid_newuser(View);

                }


                logpassA.setText("");
            }
        }

    }

    private void anareg(){
        finish();

        Intent intent = new Intent(context, Regista_new.class);
        intent.putExtra("reggo","login");
        intent.putExtra("lattt", say);
        intent.putExtra("lonnn", sax);
        startActivity(intent);

    }

    @Override
    public void AsyncTaskCompleteListener(String result, String sender, String TableName, String FieldName)
    {
        switch (sender){
            case "login_CheckJSON":

                if (result.equals("202")) {
                    Toast.makeText(context, Constantori.ERROR_APPROVAL, Toast.LENGTH_LONG).show();
                }else if(result.equals("101")) {
                    Toast.makeText(context, Constantori.ERROR_PASSWORD, Toast.LENGTH_LONG).show();
                }else if(result.equals(null)) {
                    Toast.makeText(context, Constantori.ERROR_SERVER_ISSUE, Toast.LENGTH_LONG).show();
                 }else if(result.equals("Issue")) {
                    Constantori.diambaidno(View, context);

                }else{

                    try {
                        JSONArray storesArray = new JSONArray(result);
                        storesArray.getJSONObject(0).put(Constantori.KEY_USERPASS, pswd);
                        storesArray.getJSONObject(0).put(Constantori.KEY_USERACTIVE, Constantori.USERACTIVE);

                        if (db.CheckIsDataAlreadyInDBorNot(Constantori.TABLE_REGISTER, Constantori.KEY_USERACTIVE, Constantori.USERACTIVE)) {
                            db.RegisterUser_Update(storesArray);
                        }else {
                            db.RegisterUser_Insert(storesArray);
                        }

                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);


                    }catch (Exception xx){
                        Log.e(Constantori.APP_ERROR_PREFIX + "_LoginnoJSON", xx.getMessage());
                        xx.printStackTrace();
                    }

                }

                break;

            default:

                break;

        }

    }





}
