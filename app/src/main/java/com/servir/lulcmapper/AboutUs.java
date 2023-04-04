package com.servir.lulcmapper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView; 

public class AboutUs extends ActionBarActivity {
 
	Button tokaa;
	Context context = this;
	TextView tvStn;
    
 
      @SuppressLint("NewApi") @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
 
            setContentView(R.layout.aboutus);
            overridePendingTransition(0,0);
            tokaa = (Button) findViewById(R.id.rback);

		  getSupportActionBar().setDisplayShowHomeEnabled(true);
		  getSupportActionBar().setIcon(R.mipmap.lulc_launcher);
            
            
        		
    		tvStn = (TextView) findViewById(R.id.txte);

    		
    		 tvStn.setMovementMethod(new ScrollingMovementMethod());
            
                		
            tokaa.setOnClickListener(new OnClickListener(){
      	    	
      	    	public void onClick(View view){
      	      	Intent intent = new Intent (context, MainActivity.class);
      	    	startActivity(intent);
      	    	finish();
      	    	}
      	      });  
      
          }
      
      
      
    
  	public void onBackPressed(){
    	 
  	}		
      
      
  }