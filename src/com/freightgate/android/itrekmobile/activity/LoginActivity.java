package com.freightgate.android.itrekmobile.activity;


import java.util.UUID;

import com.freightgate.android.itrekmobile.apphelper.Errormessage;
import com.freightgate.android.itrekmobile.apphelper.ItrekMobileSingelton;
import com.freightgate.android.itrekmobile.data.DatabaseManager;
import com.freightgate.android.itrekmobile.server.ServerException;
import com.freightgate.android.itrekmobile.server.ServerManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
    
    boolean asyncTaskActive = false;
//    private Builder builder;
//    private View inflatedView;
    DatabaseManager dbManager;
    
    ServerManager sManager;
    private ItrekMobileSingelton service = ItrekMobileSingelton.getInstance(); 
	
	EditText et_company, et_username,et_password;
    String username, password;
	TextView error;
    Button ok;
//    Hasher hash = new Hasher();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);        

        et_company=(EditText)findViewById(R.id.et_company);
        et_username=(EditText)findViewById(R.id.et_username);
        et_password=(EditText)findViewById(R.id.et_password);
//        et_company.setText("awest");
        et_company.setText("fgdemo");
        
        SharedPreferences sharedpref = getApplicationContext().getSharedPreferences("USERNAME", 0);
        if(sharedpref != null){
        	et_username.setText(sharedpref.getString("USERNAME", ""));
        }
        
//        et_username.setText("2001");
//        et_password.setText("706997");
//        et_username.setText("shack");
//        et_password.setText("12345");

        dbManager = new DatabaseManager(this);
        
        sManager = new ServerManager(this, dbManager);
       
    }
    
    //disable back button
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mDatabase.close();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
            
    public void wechsleActivity(final View view){

    	switch(view.getId()){
    	case R.id.btn_login:
			
    		et_company=(EditText)findViewById(R.id.et_company);
    		et_username=(EditText)findViewById(R.id.et_username);    	
    		et_password=(EditText)findViewById(R.id.et_password);
    		error=(TextView)findViewById(R.id.tv_error);
    		
    		saveCredentials(et_company.getText().toString(), et_username.getText().toString());
    		
    		try {
    			
    			//// try to login online and set sessionid
				sManager.webLogin(et_company.getText().toString(), et_username.getText().toString(), et_password.getText().toString());
	    				
    		}
    		catch (Exception e){  
    			
    			if(dbManager.localLogin(et_username.getText().toString(), et_password.getText().toString())){
    				startMyTripManifestViewActivity();
    			}else{
    				startDialogBox("Username not local or wrong credentials");
    			}
    			
    			
    		}
    		break;
    		
    	case R.id.btn_cancel:   		
    		Intent intent = new Intent(Intent.ACTION_MAIN);
    		intent.addCategory(Intent.CATEGORY_HOME);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(intent);
    		break;
	
    	}	
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) {
            finish();
        }
    }
    
    public void saveCredentials(String host, String un) {
    	
		// save host
		SharedPreferences sharedpref = getApplicationContext().getSharedPreferences("SYSTEM", 0);
		SharedPreferences.Editor editor = sharedpref.edit();
		editor.putString("SYSTEM", host);
		editor.commit();
		
		// save username
		sharedpref = getApplicationContext().getSharedPreferences("USERNAME", 0);
		editor = sharedpref.edit();
		editor.putString("USERNAME", un);
		editor.commit();
    }

    public String createUniqueID(){
    	
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String deviceImei, deviceSerial,deviceModel;
        deviceImei = "" + tm.getDeviceId();
        deviceSerial = "" + android.os.Build.SERIAL;
        deviceModel = android.os.Build.MODEL;
        String deviceId = "";    
     
        try{
        UUID deviceUuid = new UUID(deviceImei.hashCode(), ((long)deviceSerial.hashCode() << 32) | deviceModel.hashCode());
        deviceId = deviceUuid.toString();
        
        return deviceId;
        
        }catch(NullPointerException e){
        	
//        	error=(TextView)findViewById(R.id.tv_error);
//        	error.setText(new Errormessage("1000" ,getApplicationContext()).get_errorText());
        	
        }
        return deviceId;        
    }


	public void showErrorDialogBox(String errorCode) {
		error=(TextView)findViewById(R.id.tv_error);
    	error.setText(new Errormessage(errorCode ,getApplicationContext()).get_errorText());		
	}
	
    public void OnCheckConnection(View v) {

    	if(service.checkInternetConnection(this)){
    		Toast.makeText(this, getResources().getString(R.string.loginview_connect), Toast.LENGTH_SHORT).show();
    	}
    	else {
    		Toast.makeText(this, getResources().getString(R.string.loginview_noconnect), Toast.LENGTH_SHORT).show();
    	}
    }
    
    public void startDialogBox(String msg){
        new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("InfoBox")
        .setMessage(msg)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })
        .show(); 	    	
    }
    
    public void startMyTripManifestViewActivity(){
    	
    	Intent intent= new Intent(this,MyTripManifestViewActivity.class);
    	startActivity(intent);
    }

	public void refreshManifest() {
		try {
			sManager.syncOpenRequests();
//			sManager.getMyTipManifests();
		} catch (ServerException e) {
			e.printStackTrace();
		}
	}
	
       
}

