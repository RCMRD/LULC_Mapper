package com.servir.lulcmapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler sInstance;

    public static synchronized DatabaseHandler getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DatabaseHandler(Constantori.DATABASE_Context.getApplicationContext());
        }
        return sInstance;
    }


    private DatabaseHandler(Context context) {
        super(context, Constantori.DATABASE_NAME, null, Constantori.DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_REGISTER_TABLE = "CREATE TABLE " + Constantori.TABLE_REGISTER + "("
                + Constantori.KEY_USERACTIVE + " VARCHAR,"
                + Constantori.KEY_USERNEM + " VARCHAR,"
                + Constantori.KEY_USERTEL + " VARCHAR,"
                + Constantori.KEY_USERCNTRY + " VARCHAR,"
                + Constantori.KEY_USEREMAIL + " VARCHAR,"
                + Constantori.KEY_USERPASS + " VARCHAR,"
                + Constantori.KEY_USERORG + " VARCHAR,"
                + Constantori.KEY_USERROLE + " VARCHAR,"
                + Constantori.KEY_USERLAT + " VARCHAR,"
                + Constantori.KEY_USERLON + " VARCHAR,"
                + Constantori.KEY_USERREF + " VARCHAR"
                + ")";
        db.execSQL(CREATE_REGISTER_TABLE);


        String CREATE_PIC_TABLE = "CREATE TABLE " + Constantori.TABLE_PIC + "("
                + Constantori.KEY_USERPIC + " TEXT PRIMARY KEY,"
                + Constantori.KEY_USERPICPATH + " VARCHAR,"
                + Constantori.KEY_SENDSTAT + " VARCHAR"
                + ")";
        db.execSQL(CREATE_PIC_TABLE);


        String CREATE_KOLEKDAT_TABLE = "CREATE TABLE " + Constantori.TABLE_DAT + "("
                + Constantori.KEY_RID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Constantori.KEY_DATNO + " VARCHAR,"
                + Constantori.KEY_DATFEATURE + " VARCHAR,"
                + Constantori.KEY_DATCOMMENT + " VARCHAR,"
                + Constantori.KEY_DATLON + " VARCHAR,"
                + Constantori.KEY_DATLAT + " VARCHAR,"
                + Constantori.KEY_DATPICNAME + " VARCHAR,"
                + Constantori.KEY_DATSTATUS + " VARCHAR,"
                + Constantori.KEY_USERREF + " VARCHAR"
                + ")";
        db.execSQL(CREATE_KOLEKDAT_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Constantori.TABLE_REGISTER);
        db.execSQL("DROP TABLE IF EXISTS " + Constantori.TABLE_DAT);
        db.execSQL("DROP TABLE IF EXISTS " + Constantori.TABLE_PIC);

        // Create tables again
        onCreate(db);
    }


    public boolean CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue) {

        SQLiteDatabase db = this.getInstance(ApplicationContextor.getAppContext()).getWritableDatabase();

        String sql =  "SELECT * FROM " + TableName + " WHERE " + dbfield + " = ? ";

        Cursor query = db.rawQuery(
                sql,
                new String[] {fieldValue}
        );

        if(query.getCount() <= 0){
            query.close();
            db.close();
            return false;
        }
        query.close();
        db.close();
        return true;
    }

    public void KolekData_UpdateAll(JSONArray storesArray) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransactionNonExclusive();

        try {

            for (int i = 0; i < storesArray.length(); i++) {

                JSONObject store = storesArray.getJSONObject(i);

                String sref = store.getString(Constantori.KEY_USERREF);
                String sdat = store.getString(Constantori.KEY_DATNO);

                ContentValues values = new ContentValues();

                values.put(Constantori.KEY_DATFEATURE, store.getString(Constantori.KEY_DATFEATURE));
                values.put(Constantori.KEY_DATCOMMENT, store.getString(Constantori.KEY_DATCOMMENT));
                values.put(Constantori.KEY_DATLON, store.getString(Constantori.KEY_DATLON));
                values.put(Constantori.KEY_DATLAT, store.getString(Constantori.KEY_DATLAT));
                values.put(Constantori.KEY_DATPICNAME, store.getString(Constantori.KEY_DATPICNAME));
                values.put(Constantori.KEY_DATSTATUS, store.getString(Constantori.KEY_DATSTATUS));

                db.update(Constantori.TABLE_DAT, values, Constantori.KEY_USERREF + "=? AND " + Constantori.KEY_DATNO + "=?", new String[]{sref,sdat});

            }

            db.setTransactionSuccessful();


        } catch(Exception xx){

            Log.e(Constantori.APP_ERROR_PREFIX + "_KolUpSQL", xx.getMessage());
            xx.printStackTrace();

        }finally{

            db.endTransaction();

        }
        //adb.close();

    }



    public void KolekData_InsertAll(JSONArray storesArray) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransactionNonExclusive();

        try {

            for (int i = 0; i < storesArray.length(); i++) {

                JSONObject store = storesArray.getJSONObject(i);

                ContentValues values = new ContentValues();

                values.put(Constantori.KEY_DATFEATURE, store.getString(Constantori.KEY_DATFEATURE));
                values.put(Constantori.KEY_DATCOMMENT, store.getString(Constantori.KEY_DATCOMMENT));
                values.put(Constantori.KEY_DATLON, store.getString(Constantori.KEY_DATLON));
                values.put(Constantori.KEY_DATLAT, store.getString(Constantori.KEY_DATLAT));
                values.put(Constantori.KEY_DATPICNAME, store.getString(Constantori.KEY_DATPICNAME));
                values.put(Constantori.KEY_DATSTATUS, store.getString(Constantori.KEY_DATSTATUS));
                values.put(Constantori.KEY_DATNO, store.getString(Constantori.KEY_DATNO));
                values.put(Constantori.KEY_USERREF, store.getString(Constantori.KEY_USERREF));


                db.insert(Constantori.TABLE_DAT, null, values);

            }

            db.setTransactionSuccessful();


        } catch(Exception xx){

            Log.e(Constantori.APP_ERROR_PREFIX + "_KolInSQL", xx.getMessage());
            xx.printStackTrace();

        }finally{

            db.endTransaction();

        }
        //adb.close();
    }



    public void PicData_InsertAll(JSONArray storesArray) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransactionNonExclusive();

        try {

            for (int i = 0; i < storesArray.length(); i++) {

                JSONObject store = storesArray.getJSONObject(i);

                ContentValues values = new ContentValues();

                values.put(Constantori.KEY_USERPIC, store.getString(Constantori.KEY_USERPIC));
                values.put(Constantori.KEY_USERPICPATH, store.getString(Constantori.KEY_USERPICPATH));
                values.put(Constantori.KEY_SENDSTAT, store.getString(Constantori.KEY_SENDSTAT));

                db.insert(Constantori.TABLE_PIC, null, values);
            }

            db.setTransactionSuccessful();


        } catch(Exception xx){

            Log.e(Constantori.APP_ERROR_PREFIX + "_PICinSQL", xx.getMessage());
            xx.printStackTrace();

        }finally{

            db.endTransaction();

        }
        
    }



    public void RegisterUser_Update(JSONArray storesArray) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransactionNonExclusive();

        try {

            for (int i = 0; i < storesArray.length(); i++) {

                JSONObject store = storesArray.getJSONObject(i);

                String sref = store.getString(Constantori.KEY_USERACTIVE);


                ContentValues values = new ContentValues();


                values.put(Constantori.KEY_USERNEM, store.getString(Constantori.KEY_USERNEM));
                values.put(Constantori.KEY_USERTEL, store.getString(Constantori.KEY_USERTEL));
                values.put(Constantori.KEY_USERCNTRY, store.getString(Constantori.KEY_USERCNTRY));
                values.put(Constantori.KEY_USEREMAIL, store.getString(Constantori.KEY_USEREMAIL));
                values.put(Constantori.KEY_USERPASS, store.getString(Constantori.KEY_USERPASS));
                values.put(Constantori.KEY_USERORG, store.getString(Constantori.KEY_USERORG));
                values.put(Constantori.KEY_USERROLE, store.getString(Constantori.KEY_USERROLE));
                values.put(Constantori.KEY_USERLAT, store.getString(Constantori.KEY_USERLAT));
                values.put(Constantori.KEY_USERLON, store.getString(Constantori.KEY_USERLON));
                values.put(Constantori.KEY_USERREF, store.getString(Constantori.KEY_USERREF));

                db.update(Constantori.TABLE_REGISTER, values, Constantori.KEY_USERACTIVE + "=?", new String[]{sref});
            }

            db.setTransactionSuccessful();


        } catch(Exception xx){

            Log.e(Constantori.APP_ERROR_PREFIX + "_RegUpSQL", xx.getMessage());
            xx.printStackTrace();

        }finally{

            db.endTransaction();

        }
        //adb.close();
    }



    public void RegisterUser_Insert(JSONArray storesArray) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransactionNonExclusive();

        try {

            for (int i = 0; i < storesArray.length(); i++) {

                JSONObject store = storesArray.getJSONObject(i);

                ContentValues values = new ContentValues();

                values.put(Constantori.KEY_USERACTIVE, store.getString(Constantori.KEY_USERACTIVE));
                values.put(Constantori.KEY_USERNEM, store.getString(Constantori.KEY_USERNEM));
                values.put(Constantori.KEY_USERTEL, store.getString(Constantori.KEY_USERTEL));
                values.put(Constantori.KEY_USERCNTRY, store.getString(Constantori.KEY_USERCNTRY));
                values.put(Constantori.KEY_USEREMAIL, store.getString(Constantori.KEY_USEREMAIL));
                values.put(Constantori.KEY_USERPASS, store.getString(Constantori.KEY_USERPASS));
                values.put(Constantori.KEY_USERORG, store.getString(Constantori.KEY_USERORG));
                values.put(Constantori.KEY_USERROLE, store.getString(Constantori.KEY_USERROLE));
                values.put(Constantori.KEY_USERLAT, store.getString(Constantori.KEY_USERLAT));
                values.put(Constantori.KEY_USERLON, store.getString(Constantori.KEY_USERLON));
                values.put(Constantori.KEY_USERREF, store.getString(Constantori.KEY_USERREF));

                db.insert(Constantori.TABLE_REGISTER, null, values);
            }

            db.setTransactionSuccessful();


        } catch(Exception xx){

            Log.e(Constantori.APP_ERROR_PREFIX + "_RegInSQL", xx.getMessage());
            xx.printStackTrace();

        }finally{

            db.endTransaction();

        }
        //adb.close();
    }


    /*//get index of sub table - incase of multi-insert for delete
    public int getSubKey(String TableName, String TelVal){

        SQLiteDatabase db = getWritableDatabase();
        int dakey = 0;
        db.beginTransaction();
        try {

        String searchQuery = "SELECT  * FROM " + TableName + " WHERE " + KEY_NURSERYTEL + "=?";
        Cursor cursor = db.rawQuery(searchQuery, new String [] {TelVal});



        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            dakey ++;
            cursor.moveToNext();
        }
        cursor.close();
        db.setTransactionSuccessful();
    } catch(Exception xx){
        Log.e("MKS_NURSKEY_ERROR", xx.getMessage());
        xx.printStackTrace();
    }finally{
        db.endTransaction();
        Log.e("MKS_NURSKEY_DONE", TableName + " : " + String.valueOf(dakey));
        return dakey;
    }
    }*/


    //get all data values from any table
    public List<HashMap<String, String>> GetAllData(String TableName, String FieldName, String Value){

        SQLiteDatabase db = getWritableDatabase();
        List<HashMap<String, String>>  All_data = new ArrayList<HashMap<String, String>>();

        db.beginTransaction();
        try {

        String searchQuery = "";
        Cursor cursor = null;


        if (FieldName.equals("") && Value.equals("")) {
            searchQuery = "SELECT  * FROM " + TableName;
            cursor = db.rawQuery(searchQuery, null );

        }else{
            searchQuery = "SELECT  * FROM " + TableName + " WHERE " + FieldName + " = ? ";;
            cursor = db.rawQuery(
                    searchQuery,
                    new String[] {Value}
            );
        }



        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            HashMap<String, String> tempHash = new HashMap<String, String>();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                            Log.e(Constantori.APP_ERROR_PREFIX + "_alldataSQL", cursor.getString(i) );
                            tempHash.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            tempHash.put(cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d(Constantori.APP_ERROR_PREFIX + "_alldataerror", "Retrieve_"+TableName);
                    }
                }
            }
            All_data.add(tempHash);
            cursor.moveToNext();
        }
        cursor.close();
        db.setTransactionSuccessful();
    } catch(Exception xx){
        Log.e(Constantori.APP_ERROR_PREFIX + "_AllData_ERROR", xx.getMessage());
        xx.printStackTrace();
    }finally{
        db.endTransaction();
        Log.e(Constantori.APP_ERROR_PREFIX+"_AllData_DONE", TableName + " : " + All_data.toString());
        return All_data;
    }
    }


    //Delete Row
    public void deleteRow (String TableName, String FieldName, String KeyValue){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
        db.delete(TableName, FieldName + "=?", new String [] {KeyValue});
            db.setTransactionSuccessful();
        } catch(Exception xx){
            Log.e(Constantori.APP_ERROR_PREFIX+"_CLEARTBL_ERROR", xx.getMessage());
            xx.printStackTrace();
        }finally{
            db.endTransaction();
            Log.e(Constantori.APP_ERROR_PREFIX+"_CLEARTBL_DONE", "All Tables Deleted");
        }
    }


    public int getRowCount(String TableName, String FieldName, String Value) {

        SQLiteDatabase db = getWritableDatabase();


        String searchQuery = "";
        Cursor cursor = null;


        if (FieldName.equals("") && Value.equals("")) {
            searchQuery = "SELECT  * FROM " + TableName;
            cursor = db.rawQuery(searchQuery, null );

        }else{
            searchQuery = "SELECT  * FROM " + TableName + " WHERE " + FieldName + " = ? ";;
            cursor = db.rawQuery(
                    searchQuery,
                    new String[] {Value}
            );
        }



        int rowCount = 0;

        db.beginTransaction();

        try {

        rowCount = cursor.getCount();
        cursor.close();
            db.setTransactionSuccessful();
        } catch(Exception xx){
            Log.e(Constantori.APP_ERROR_PREFIX+"_ROWcnt_ERROR", xx.getMessage());
            xx.printStackTrace();
        }finally{
            db.endTransaction();
            Log.e(Constantori.APP_ERROR_PREFIX+"_ROWcnt_DONE", TableName + " : " + String.valueOf(rowCount));
            return rowCount;
        }

    }

    /**
     * Re create database Delete and recreate it
     * */

    public void emptyAllTables() {

        SQLiteDatabase db = getWritableDatabase();
        // Delete All Rows

        db.beginTransaction();

        try {

            db.delete(Constantori.TABLE_REGISTER, null, null);
            db.delete(Constantori.TABLE_DAT, null, null);
            db.delete(Constantori.TABLE_PIC, null, null);
            db.setTransactionSuccessful();
        } catch(Exception xx){
            Log.e(Constantori.APP_ERROR_PREFIX+"_CLEARDB_ERROR", xx.getMessage());
            xx.printStackTrace();
        }finally{
            db.endTransaction();
            Log.e(Constantori.APP_ERROR_PREFIX+"_CLEARDB_DONE", "All Tables Deleted");
        }

    }

    public void emptyTable(String TableName) {
        //delete all data in it
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
        db.delete(TableName, null, null);
            db.setTransactionSuccessful();
        } catch(Exception xx){
            Log.e(Constantori.APP_ERROR_PREFIX+"_DelTBL_ERROR", xx.getMessage());
            xx.printStackTrace();
        }finally{
            db.endTransaction();
            Log.e(Constantori.APP_ERROR_PREFIX+"_DelTBL_DONE", TableName);
                    }
    }



    public JSONArray PostDataArray_Alldata(String TableName, String FieldName, String Value)
    {

        SQLiteDatabase db = getWritableDatabase();

        JSONArray resultSet = new JSONArray();

        db.beginTransaction();

        try {


            String searchQuery = "";
            Cursor cursor = null;


            if (FieldName.equals("") && Value.equals("")) {
                searchQuery = "SELECT  * FROM " + TableName;
                cursor = db.rawQuery(searchQuery, null );

            }else{
                searchQuery = "SELECT  * FROM " + TableName + " WHERE " + FieldName + " = ? ";;
                cursor = db.rawQuery(
                        searchQuery,
                        new String[] {Value}
                );
            }


        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                            Log.e(Constantori.APP_ERROR_PREFIX+"_Post_i", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d(Constantori.APP_ERROR_PREFIX+"_Post_Error", "Post_"+TableName);
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

            cursor.close();

            db.setTransactionSuccessful();

        } catch(Exception xx){

            Log.e(Constantori.APP_ERROR_PREFIX+"_PostR_ERROR", xx.getMessage());
            xx.printStackTrace();

        }finally{

            db.endTransaction();
            Log.e(Constantori.APP_ERROR_PREFIX+"_PostR_JSON", TableName + " : " +resultSet.toString() );
            return resultSet;

        }


    }



    public void DataPost_Status(JSONArray storesArray, String TableName) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {

            for (int i = 0; i < storesArray.length(); i++) {

                JSONObject store = storesArray.getJSONObject(i);

                String kref = store.getString(Constantori.KEY_RID);

                ContentValues values = new ContentValues();

                values.put(Constantori.KEY_DATSTATUS, store.getString(Constantori.KEY_DATSTATUS));

                db.update(TableName, values, Constantori.KEY_RID+ "=?", new String[]{kref});
            }

            db.setTransactionSuccessful();


        } catch(Exception xx){

            Log.e(Constantori.APP_ERROR_PREFIX+"_EDTUPD_ERROR", xx.getMessage());
            xx.printStackTrace();

        }finally{
            Log.e(Constantori.APP_ERROR_PREFIX+"_EDTUPD_ERROR", "Uploaded/Edited filled");
            db.endTransaction();

        }

        //adb.close();
    }



    public void UserApproved(JSONArray storesArray) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {

            for (int i = 0; i < storesArray.length(); i++) {

                JSONObject store = storesArray.getJSONObject(i);

                String uref = store.getString(Constantori.KEY_USERACTIVE);

                ContentValues values = new ContentValues();

                values.put(Constantori.KEY_USERREF, store.getString(Constantori.KEY_USERREF));

                db.update(Constantori.TABLE_REGISTER, values, Constantori.KEY_USERACTIVE+ "=?", new String[]{uref});
            }

            db.setTransactionSuccessful();


        } catch(Exception xx){

            Log.e(Constantori.APP_ERROR_PREFIX+"_EDTUPD_ERROR", xx.getMessage());
            xx.printStackTrace();

        }finally{
            Log.e(Constantori.APP_ERROR_PREFIX+"_EDTUPD_ERROR", "Uploaded/Edited filled");
            db.endTransaction();

        }

        //adb.close();
    }






}