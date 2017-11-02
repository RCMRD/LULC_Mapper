package com.servir.lulcmapper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, AsyncTaskCompleteListener {

    TextSwitcher textSwitcher;
    Animation slide_in_left, slide_out_right;
    LinearLayout btnreg;
    View View;
    static double longitude = 0.0;
    static double latitude = 0.0;
    DatabaseHandler db = DatabaseHandler.getInstance(this);
    LinearLayout btnview, btnniko;
    public static final int confail = 9000;
    public Context context = this;
    String zipfilo;
    LocationRequest mlr;
    GoogleApiClient mgac;
    Location cl;
    List<String> stora  = new ArrayList<String>();
    List<String> storapic  = new ArrayList<String>();
    TextView title, sitato;
    String lefile;
    String say = "0.0";
    String sax = "0.0";
    String refo="";
    ImageView taftaa;
    private final static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
    private final static int REQUEST_LOCATION = 2;


    protected void createlocreq(){
        mlr = new LocationRequest();
        mlr.setInterval(2000);
        mlr.setFastestInterval(1000);
        mlr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    String[] TextToSwitched = { "...map your world", "...easy visualisation", "...land cover mapper", "...convenient system", "...ready information",
            "...stay connected", "...access resources","...land use mapper" };

    int curIndex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(0,0);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.lulc_launcher);

        textSwitcher = (TextSwitcher) findViewById(R.id.textswitcher);
        btnreg = (LinearLayout) findViewById(R.id.btn1);
        btnview = (LinearLayout) findViewById(R.id.btn2);
        btnniko = (LinearLayout) findViewById(R.id.btn3);
        title = (TextView)findViewById(R.id.title);
        taftaa = (ImageView)findViewById(R.id.procss);
        sitato = (TextView)findViewById(R.id.sitato);

        createlocreq();




        slide_in_left = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        textSwitcher.setInAnimation(slide_in_left);
        textSwitcher.setOutAnimation(slide_out_right);



        if (db.CheckIsDataAlreadyInDBorNot(Constantori.TABLE_REGISTER, Constantori.KEY_USERACTIVE, Constantori.USERACTIVE)) {

            List<HashMap<String, String>> regData = db.GetAllData(Constantori.TABLE_REGISTER,"","");
            HashMap<String, String> UserDetails = regData.get(0);

            refo = UserDetails.get(Constantori.KEY_USERREF);
        } else {
            diambaidweni(View);
        }


        textSwitcher.setFactory(new ViewFactory(){

            @Override
            public View makeView() {
                TextView textView = new TextView(context);
                textView.setTextSize(16);
                textView.setTextColor(Color.rgb(0,0,0));
                //textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
                // textView.setShadowLayer(10, 10, 10, Color.rgb(255,204,51));
                return textView;
            }});

        curIndex = 0;
        textSwitcher.setText(TextToSwitched[curIndex]);



        mgac = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        	  /*final Animation in = new AlphaAnimation(0.0f, 1.0f);
        	  in.setDuration(3000);

        	  final Animation out = new AlphaAnimation(1.0f, 0.0f);
        	  out.setDuration(3000);

        	  AnimationSet as = new AnimationSet(true);
        	  as.addAnimation(out);
        	  in.setStartOffset(3000);
        	  as.addAnimation(in);*/
        taftaa.setVisibility(android.view.View.GONE);

        Timer timer = new Timer("swcha");
        timer.schedule(new TimerTask() {

            public void run() {

                runOnUiThread(new Runnable() {


                    @Override
                    public void run() {

                        updateUI();

                        //Toast.makeText(context,"tLat :"+  latitude + "\n" + "tLon : "+longitude ,Toast.LENGTH_LONG).show();


                        if(curIndex == TextToSwitched.length-1){
                            curIndex = 0;
                            textSwitcher.setText(TextToSwitched[curIndex]);
                        }else{
                            textSwitcher.setText(TextToSwitched[++curIndex]);
                        }

                        if (latitude!=0.0 && longitude!=0.0 ){

                            sitato.setText("Locator Status : Detected");

                        }else{
                            sitato.setText("Locator Status : Scanning");

                        }

                        // Toast.makeText(context,"LAT:"+String.valueOf(latitude)+"\n"+"LON:"+String.valueOf(longitude),Toast.LENGTH_SHORT).show();

                    }



                });

            }

        }, 0, 2000);

        if (!isgpsa(this)){
            Toast.makeText(this, "Google Play Services is disabled on your phone", Toast.LENGTH_LONG).show();
        }



        btnreg.setOnClickListener(new OnClickListener(){

            public void onClick(View view){



                if (sax.equals("0.0") || say.equals("0.0")){
                    updateUI();
                    Toast.makeText(context,"Please turn on GPS then try again",Toast.LENGTH_LONG).show();
                }else{

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", java.util.Locale.getDefault());
                    String datno = refo + "_" + dateFormat.format(new Date());

                    Intent intent = new Intent (context, Selekta.class);
                    intent.putExtra("lattt", String.valueOf(latitude));
                    intent.putExtra("lonnn", String.valueOf(longitude));
                    intent.putExtra("datno", datno);
                    startActivity(intent);

                }

            }
        });

        btnniko.setOnClickListener(new OnClickListener(){

            public void onClick(View view){

                if (latitude!=0.0 && longitude!=0.0 ){

                    Intent intent = new Intent (context, Mapper.class);
                    startActivity(intent);

                }else{

                    updateUI();
                    Toast.makeText(context,"Please turn on GPS then try again",Toast.LENGTH_LONG).show();

                }


            }
        });

        btnview.setOnClickListener(new OnClickListener(){

            public void onClick(View view){

                if (refo.equals(Constantori.USERREFNULL)){
                    diambaidweni(View);
                }else{

                    int hexa = 0;


                    if (db.getRowCount(Constantori.TABLE_DAT,Constantori.KEY_DATSTATUS,Constantori.SAVE_DATSTATUS) > 0) {

                        JSONArray sendo = db.PostDataArray_Alldata(Constantori.TABLE_DAT, Constantori.KEY_DATSTATUS, Constantori.SAVE_DATSTATUS);

                        if (Constantori.isConnectedToInternet()) {
                            new NetPost(context, "maindata_PostJSON", sendo, "Sending... Make sure internet connection is active", Constantori.TABLE_DAT, Constantori.KEY_DATSTATUS, MainActivity.this).execute(new String[]{Constantori.URL_GEN});
                        }else{
                            Toast.makeText(context,Constantori.ERROR_NO_INTERNET,Toast.LENGTH_LONG).show();
                        }

                    }else{
                        hexa++;
                    }


                    if(db.getRowCount(Constantori.TABLE_PIC, "","") > 0){

                        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss", java.util.Locale.getDefault());
                        zipfilo = "LC_"  +  refo + "_" + dateFormat.format(new Date()) + ".zip";
                        lapica();

                        try {

                            JSONArray picArray = new JSONArray();
                            JSONObject allImages = new JSONObject();

                            allImages.put("zipfile", lefile);
                            allImages.put("zipname", zipfilo);

                            picArray.put(allImages);

                            if (Constantori.isConnectedToInternet()) {
                                new NetPost(context, "maindata_PostImages", picArray, "Sending Images... Make sure internet connection is active", Constantori.TABLE_PIC, "", MainActivity.this).execute(new String[]{Constantori.URL_GEN});
                            }else{
                                Toast.makeText(context,Constantori.ERROR_NO_INTERNET,Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception xx){


                        }

                    }else{

                        hexa++;
                    }

                    if (hexa > 0){
                        Toast.makeText(getBaseContext(), "No pending data in internal database", Toast.LENGTH_LONG).show();
                    }


                }

            }
        });

        updateUI();

    }

    public Bitmap ShrinkBitmap(String file, int width, int height)
    {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if(heightRatio > 1 || widthRatio > 1)
        {
            if(heightRatio > widthRatio)
            {
                bmpFactoryOptions.inSampleSize = heightRatio;
            }
            else
            {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }


    private boolean isgpsa(Activity activity) {
        // TODO Auto-generated method stub
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }





    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub

        startLocupdates();

    }

    @SuppressWarnings({"MissingPermission"})
    private void startLocupdates() {
        // TODO Auto-generated method stub

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
        } else {

            PendingResult<Status> pr = LocationServices.FusedLocationApi.requestLocationUpdates(mgac, mlr, this);

            if (latitude!=0.0 && longitude!=0.0 ){
                sitato.setText("Locator Status : Detected");

            }else{
                sitato.setText("Locator Status : Scanning");
            }

        }

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        mgac.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
        if (arg0.hasResolution()){

            try{
                arg0.startResolutionForResult(this, confail);
            } catch (IntentSender.SendIntentException e){
                e.printStackTrace();
            }

        }else{
            Constantori.diambaidno(View, context);
        }
    }


    public void onDisconnected() {
        // TODO Auto-generated method stub
        updateUI();
    }


    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub
        cl = arg0;
        updateUI();
    }


    private void updateUI() {
        // TODO Auto-generated method stub


        if (null != cl){
            latitude = cl.getLatitude();
            longitude = cl.getLongitude();

            sax = String.valueOf(longitude);
            say = String.valueOf(latitude);
        }
    }

    // @Override
    // public void onBackPressed(){

    // }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    public void diambaidweni(View v) {
        final Dialog mbott = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        mbott.setContentView(R.layout.mbaind_nowe);
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

                Intent intent = new Intent (context, Regista_new.class);
                intent.putExtra("lattt", String.valueOf(latitude));
                intent.putExtra("lonnn", String.valueOf(longitude));
                intent.putExtra("reggo","main");
                startActivity(intent);

                mbott.dismiss();
            }
        });
        mbott.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        if (id == R.id.itop1) {
            Intent intent = new Intent (context, Regista_new.class);
            intent.putExtra("lattt", String.valueOf(latitude));
            intent.putExtra("lonnn", String.valueOf(longitude));
            intent.putExtra("reggo","main");
            startActivity(intent);
            return true;
        }

        if (id == R.id.itop2) {
            Intent intent = new Intent (context, AboutUs.class);
            startActivity(intent);
            return true;
        }




        return super.onOptionsItemSelected(item);
    }




    public void onResume(){
        super.onResume();

        if (mgac.isConnected()){
            startLocupdates();
        }

        //new HttpAsyncTask0().execute(new String[]{URL2});
    }

    public void onStart(){
        super.onStart();
        mgac.connect();
        //new HttpAsyncTask0().execute(new String[]{URL2});
    }

    public void onPause(){
        super.onPause();
        try{

            if (mgac.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mgac, this);
                mgac.disconnect();
            }
        }catch(Exception x){

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RECOVER_PLAY_SERVICES) {
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mgac.isConnecting() &&
                        !mgac.isConnected()) {
                    mgac.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(context, "Google Play Services must be installed.",
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

                if (latitude!=0.0 && longitude!=0.0 ){
                    sitato.setText("Locator Status : Detected");

                }else{
                    sitato.setText("Locator Status : Scanning");
                }


            } else {
                Toast.makeText(context, "GPS Location services must be enabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }





    private void lapica(){

        storapic.clear();
        if (db.getRowCount(Constantori.TABLE_PIC,"","") > 0) {

            List<HashMap<String, String>> picList = db.GetAllData(Constantori.TABLE_PIC, "", "");

            for (HashMap<String, String> picData: picList){

                String picopk = picData.get(Constantori.KEY_USERPICPATH);

                Bitmap iopic = ShrinkBitmap(picopk, 200, 200);
                File file = new File(picopk);
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    iopic.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();

                }
                catch (Exception e) {
                    e.printStackTrace();
                }


                storapic.add(picopk);



            }

        }

        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IMGLULC";
        File myDir = new File(root,zipfilo);
        if (!myDir.exists()) {
            myDir.getParentFile().mkdirs();
        }else{
            myDir.getParentFile().delete();
            myDir.getParentFile().mkdirs();
        }
        String zippath = myDir.getAbsolutePath();

        try  {

            Log.e("Progress", "1");
            BufferedInputStream origin = null;
            Log.e("Progress", "2");
            FileOutputStream dest = new FileOutputStream(zippath);
            Log.e("Progress", "3");
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            Log.e("Progress", "4");
            byte data[] = new byte[2048];
            Log.e("Progress", "6");
            for(int i=0; i < storapic.size(); i++) {
                //Log.e("Compress", "Adding: " + storapic.get(i));
                Log.e("ProgressR", storapic.get(i));
                //File ioso = new File(storapic.get(i));
                //ioso.setReadable(true);
                FileInputStream fi = new FileInputStream(storapic.get(i));
                Log.e("ProgressR", "72");
                origin = new BufferedInputStream(fi, 2048);
                Log.e("ProgressR", "73");
                ZipEntry entry = new ZipEntry(storapic.get(i).substring(storapic.get(i).lastIndexOf("/") + 1));
                Log.e("ProgressR", "74");
                out.putNextEntry(entry);
                Log.e("ProgressR", "75");
                int count;
                while ((count = origin.read(data, 0, 2048)) != -1) {
                    Log.e("ProgressR", "81");
                    out.write(data, 0, count);
                }
                Log.e("ProgressR", "82");
                origin.close();
            }

            out.close();

            Log.e("Progress", "9");
            String encodeFileToBase64Binary = encodeFileToBase64Binary(myDir);
            Log.e("Progress", "10");
            lefile = encodeFileToBase64Binary;

        } catch(Exception e) {
            Log.e(" error ---- ", "exception", e);
            Toast.makeText(context,"Image(s) not found at this time." ,Toast.LENGTH_LONG).show();


        }
    }

    private static String encodeFileToBase64Binary(File fileName) throws IOException {
        //byte[] bytes = loadFile(fileName);

        FileInputStream fileInputStream=null;


        byte[] bFile = new byte[(int) fileName.length()];

        try {
            //convert file into array of bytes

            fileInputStream = new FileInputStream(fileName);
            fileInputStream.read(bFile);
            fileInputStream.close();

            /*for (int i = 0; i < bFile.length; i++) {
                System.out.print((char)bFile[i]);
            }*/

        }catch(Exception e){
            e.printStackTrace();
        }

        //below byte to string
        //String str = new String(bFile, StandardCharsets.UTF_8);

        String encodedString  = Base64.encodeToString(bFile, Base64.DEFAULT);
        return encodedString;
    }




    @Override
    public void AsyncTaskCompleteListener(String result, String sender, String TableName, String FieldName)
    {
        switch (sender){
            case "maindata_PostJSON":

                if(result.equals(null)) {
                    Toast.makeText(context, Constantori.ERROR_SERVER_ISSUE, Toast.LENGTH_LONG).show();
                }else if(result.equals("Issue")) {
                    Constantori.diambaidno(View, context);
                }else{

                    try {
                        JSONArray storesArray = new JSONArray(result);

                        db.DataPost_Status(storesArray, Constantori.TABLE_DAT);

                        Constantori.diambaidsent(View, context);

                    }catch (Exception xx){
                        Log.e(Constantori.APP_ERROR_PREFIX + "_LoginnoJSON", xx.getMessage());
                        xx.printStackTrace();
                    }

                }

                break;

            case "maindata_PostImages":



                if(result.equals(null)) {
                    Toast.makeText(context, "Server updating, please wait and try again", Toast.LENGTH_LONG).show();
                }else if(result.equals("Issue")) {
                    Constantori.diambaidno(View, context);

                }else if (result.contains("inflating")){

                    Log.e("matiangi","hajakam");

                    db.emptyTable(Constantori.TABLE_PIC);

                }

                break;

            default:

                break;

        }

    }

}
