package com.servir.lulcmapper;


import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Constantori {

    //DATABASE//////////////////////////////////////////////////////////////

    public static final String APP_ERROR_PREFIX = "LULC";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LULCDB";
    public static  final Context DATABASE_Context = ApplicationContextor.getAppContext();

        //Tables
        public static final String TABLE_DAT = "datTBL";
        public static final String TABLE_REGISTER = "userTBL";
        public static final String TABLE_PIC = "picTBL";

        //Fields - Register table
        public static final String KEY_USERREF = "_userid";
        public static final String KEY_USERACTIVE = "_useractive";
        public static final String KEY_USERNEM = "_username";
        public static final String KEY_USERTEL = "_usertel";
        public static final String KEY_USERCNTRY = "_usercntry";
        public static final String KEY_USEREMAIL = "_useremail";
        public static final String KEY_USERPASS  = "_userpass";
        public static final String KEY_USERORG = "_userorg";
        public static final String KEY_USERROLE = "_userrole";
        public static final String KEY_USERLAT = "_userlat";
        public static final String KEY_USERLON = "_userlon";


        //Fields - KOLEK Dat table
        public static final String KEY_RID = "_datindex";
        public static final String KEY_DATNO = "_datno";
        public static final String KEY_DATFEATURE = "_datftrname";
        public static final String KEY_DATCOMMENT = "_datcom";
        public static final String KEY_DATLON = "_datlon";
        public static final String KEY_DATLAT = "_datlat";
        public static final String KEY_DATPICNAME = "_datpicnm";
        public static final String KEY_DATSTATUS = "_datstatus";

        //Fields - Pic table
        public static final String KEY_USERPIC = "_userpic";
        public static final String KEY_USERPICPATH = "_userpicpath";
        public static final String KEY_SENDSTAT = "_sendstat";

        //For user status
        public static final String USERACTIVE = "1";
        public static final String USERINACTIVE = "0";
        public static final String USERREFNULL = "None";
        public static final String USERUNREG = "unreg";
        public static final String POST_RESPONSEKEY = "resp";
        public static final String POST_RESPONSEVAL = "success";
        public static final String POST_DATSTATUS = "1";
        public static final String SAVE_DATSTATUS = "0";
        public static final String SAVE_PICSTATUS = "pending";

    //////////////////////////////////////////////////////////////////////////////

    public static final String URL_LOGIN = "http://mobiledata.rcmrd.org/lulc/checka.php";
    public static final String URL_REGISTER = "http://mobiledata.rcmrd.org/lulc/spatiareg.php";
    public static final String URL_MAINPOST = "http://mobiledata.rcmrd.org/lulc/senda.php";
    public static final String URL_PIC = "http://mobiledata.rcmrd.org/lulc/sendapic.php";








    public static JSONArray getJSON(Map<String, String> x){

        JSONArray allData_multi = new JSONArray();
        JSONObject allData_single = new JSONObject(x);
        allData_multi.put(allData_single);
        Log.e(APP_ERROR_PREFIX + "_LogSendJSON", allData_multi.toString());

        return allData_multi;
    }



    public static void diambaidno(View v) {
        final Dialog mbott = new Dialog(ApplicationContextor.getAppContext(), android.R.style.Theme_Translucent_NoTitleBar);
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


}
