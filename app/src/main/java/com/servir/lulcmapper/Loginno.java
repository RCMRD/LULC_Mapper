package com.servir.lulcmapper;


import android.*;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class Loginno extends AppCompatActivity {

    EditText logemailA, logpassA;
    TextView logsignupA, logforgotA;
    Button logsigninA;
    View View;
    String mail="";
    locTrak2 locii =  new locTrak2(Loginno.this);;
    String pass="";
    private SQLiteDatabase spatiadb;
    String huyu = "user";
    String statt = "";
    String pswd = "";
    String myil = "";
    String sax = "0.0";
    String say = "0.0";

    public static final String URL1 = "http://mobiledata.rcmrd.org/lulc/checka.php";


    List<String> smalla2 = new ArrayList<String>();

    ProgressDialog mpd;

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

        spatiadb=openOrCreateDatabase("LULCDB", Context.MODE_PRIVATE, null);
        spatiadb.execSQL("CREATE TABLE IF NOT EXISTS userTBL(userno VARCHAR,usernem VARCHAR,usertel VARCHAR,usercntry VARCHAR,useremail VARCHAR,userpass VARCHAR);");

        Cursor c=spatiadb.rawQuery("SELECT * FROM userTBL WHERE userno='"+huyu+"'", null);
        if(c.moveToFirst())
        {
            mail = c.getString(4);
            pass = c.getString(5);
        }else{
            statt = "unreg";
        }


        Timer timer = new Timer("swcha");
        timer.schedule(new TimerTask() {

            public void run() {

                runOnUiThread(new Runnable() {


                    @Override
                    public void run() {

                            //locii = new locTrak2(Loginno.this);
                            if (locii.canGetLocation()) {
                                if (locii.getLatitude() != 0.0 && locii.getLongitude() != 0.0) {

                                    double slatitude = locii.getLatitude();
                                    double slongitude = locii.getLongitude();
                                    sax = String.valueOf(slongitude);
                                    say = String.valueOf(slatitude);


                                }
                            }
                    }

                });

            }

        }, 0, 2000);




        logsigninA.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {

                //Toast.makeText(Loginno.this, "aaaaaaa ", Toast.LENGTH_LONG).show();


                if (Build.VERSION.SDK_INT < 23) {
                    anaingia();
                } else {

                    if (
                            ContextCompat.checkSelfPermission(Loginno.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(Loginno.this, android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(Loginno.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(Loginno.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED

                            ) {

                        anaingia();
                        //Toast.makeText(Loginno.this, "111aaaaaaa ", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(Loginno.this, "222aaaaaaa ", Toast.LENGTH_LONG).show();

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
                    ContextCompat.checkSelfPermission(Loginno.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(Loginno.this, android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(Loginno.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(Loginno.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED

                    ) {

                anareg();
                //Toast.makeText(Loginno.this, "111aaaaaaa ", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(Loginno.this, "222aaaaaaa ", Toast.LENGTH_LONG).show();

                reqPermission("anareg");

            }


        }

    }


    public void onClick_fd(View v)
    {
     //
    }





    public void diambaidno(View v) {
        final Dialog mbott = new Dialog(Loginno.this, android.R.style.Theme_Translucent_NoTitleBar);
        mbott.setContentView(R.layout.mbaind_nonet3);
        mbott.setCanceledOnTouchOutside(false);
        mbott.setCancelable(false);
        WindowManager.LayoutParams lp = mbott.getWindow().getAttributes();
        lp.dimAmount=0.85f;
        mbott.getWindow().setAttributes(lp);
        mbott.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mbott.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Button mbaok = (Button) mbott.findViewById(R.id.mbabtn1);
        mbaok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mbott.dismiss();
            }
        });
        mbott.show();
    }



    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mpd = new ProgressDialog(Loginno.this);
            mpd.setMessage("Authenticating. Make sure internet connection is active");
            mpd.setIndeterminate(true);
            mpd.setCanceledOnTouchOutside(false);
            mpd.setMax(100);
            mpd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mpd.show();
        }



        @Override
        protected String doInBackground(String... urls) {

            String output1=null;
            for (String url:urls){
                output1 = getOutputFromUrl(url);
            }

            return output1;
        }

        private String getOutputFromUrl(String url){
            String output1=null;
            StringBuilder sb1 = new StringBuilder();


            try {

                HttpClient httpclient1 = new DefaultHttpClient();
                HttpPost httppost1 = new HttpPost(url);
                List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(2);
                nameValuePairs1.add(new BasicNameValuePair("mail", myil));
                nameValuePairs1.add(new BasicNameValuePair("pswd", pswd));

                httppost1.setEntity(new UrlEncodedFormEntity(nameValuePairs1, "utf-8"));
                HttpResponse httpr1 = httpclient1.execute(httppost1);

                if (httpr1.getStatusLine().getStatusCode() != 200) {
                    Log.d("this ndio hii", "Server encountered an error");
                }


                BufferedReader reader1 = new BufferedReader(new InputStreamReader(httpr1.getEntity().getContent(), "UTF8"));
                sb1 = new StringBuilder();
                sb1.append(reader1.readLine() + "\n");
                String line1 = null;

                while ((line1 = reader1.readLine()) != null) {
                    sb1.append(line1 + "\n");
                }

                output1 = sb1.toString();

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return output1;

        }

        @SuppressWarnings("unused")
        protected void onProgressUpdate(int...progress) {
            mpd.setProgress(progress[0]);

        }



        @Override
        protected void onPostExecute(String output1) {

            try {
                mpd.dismiss();

                String mekam2 = output1.toString().trim();
                Log.e("Login_output", mekam2);

                if (mekam2.length() >= 13){

                    smalla2 = Arrays.asList(mekam2.split(","));

                    Cursor chk=spatiadb.rawQuery("SELECT * FROM userTBL WHERE userno='"+huyu+"'", null);
                    if(chk.moveToFirst())
                    {
                        spatiadb.execSQL("UPDATE userTBL SET usernem='"+smalla2.get(0)+"',usertel='"+smalla2.get(1)+"',usercntry='"+smalla2.get(2)+"',useremail='"+smalla2.get(3)+"', userpass='"+smalla2.get(4)+"' WHERE userno='"+huyu+"'");
                    }
                    else
                    {
                        spatiadb.execSQL("INSERT INTO userTBL VALUES('"+huyu+"','"+smalla2.get(0)+"','"+smalla2.get(1)+"','"+smalla2.get(2)+"','"+smalla2.get(3)+"','"+smalla2.get(4)+"');");
                    }
                    chk.close();

                    Intent intent = new Intent(Loginno.this, MainActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(Loginno.this, "You have either not registered or been approved yet. Contact the administrator.", Toast.LENGTH_LONG).show();

                        logemailA.setText("");
                        logpassA.setText("");
                }

            }catch(Exception x){
                //Log.e("FM_login_error_auth",x.toString());
                diambaidno(View);
            }


        }

    }


    public void diambaidnet(View v) {
        final Dialog ingia = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        ingia.setContentView(R.layout.mbaind_net);
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
                new HttpAsyncTask2().execute(new String[]{URL1});

            }
        });
        regno.setOnClickListener(new android.view.View.OnClickListener(){
            @Override
            public void onClick(View v){

                Toast.makeText(Loginno.this, "No active user account exists on the phone. Please Sign Up first ", Toast.LENGTH_LONG).show();
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
        new AlertDialog.Builder(Loginno.this)
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


                    Toast.makeText(Loginno.this, "Permissions enabled", Toast.LENGTH_LONG).show();

                } else {
                    // Permission Denied
                    Toast.makeText(Loginno.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
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

            Toast.makeText(Loginno.this, "Please fill in the login detail", Toast.LENGTH_LONG).show();

        } else {

            pswd = logpassA.getText().toString().trim();
            myil = logemailA.getText().toString().trim();

            if (statt.equals("unreg")){
                diambaidnet(View);
            }else{

                if (pswd.equals(pass) && myil.equals(mail)) {
                    Intent intent = new Intent(Loginno.this, MainActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(Loginno.this, "If registered, click on Yes.", Toast.LENGTH_LONG).show();
                    diambaidnet(View);

                }

                logemailA.setText("");
                logpassA.setText("");
            }
        }

    }

    private void anareg(){
        finish();

        Intent intent = new Intent(Loginno.this, Regista.class);
        intent.putExtra("reggo","login");
        intent.putExtra("lattt", say);
        intent.putExtra("lonnn", sax);
        startActivity(intent);

    }
}
