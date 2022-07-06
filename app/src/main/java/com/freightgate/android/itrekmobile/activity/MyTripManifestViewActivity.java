package com.freightgate.android.itrekmobile.activity;

import java.util.ArrayList;
import java.util.List;

import com.freightgate.android.itrekmobile.apphelper.ItrekMobileSingelton;
import com.freightgate.android.itrekmobile.data.DatabaseManager;
import com.freightgate.android.itrekmobile.server.ServerException;
import com.freightgate.android.itrekmobile.server.ServerManager;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyTripManifestViewActivity extends ListActivity{
   
    private ProgressDialog m_ProgressDialog = null;
    private ArrayList<String> m_mytripmanifest = null;
    private ArrayList<String> m_mytripmanifest_sort = new ArrayList<String>();
    private MyTripManifestAdapter m_adapter;
    private Runnable viewOrders;
      
    private DatabaseManager dbManager;
    ServerManager sManager;
    
    private int textlength = 0;   
    private EditText edittext; 
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_trip_manifest);
        
        ActionBar ab = getActionBar(); 

        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.MAIN_COLOR));     
        ab.setBackgroundDrawable(colorDrawable);
        
		dbManager = new DatabaseManager(MyTripManifestViewActivity.this);
		sManager = new ServerManager(this, dbManager);
        
		m_mytripmanifest = new ArrayList<String>();
        this.m_adapter = new MyTripManifestAdapter(this, R.layout.my_trip_manifest_row, m_mytripmanifest);
        setListAdapter(this.m_adapter);
//        refreshListAdapter();
        
        
        /////////////////////////////////////////////////////////////////////////////////////////////////
        //search function (show only the selected names)
        edittext = (EditText) findViewById(R.id.et_mytripmanifest_search);       
		edittext.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

				textlength = edittext.getText().length();
				m_mytripmanifest_sort.clear();

				for (int i = 0; i < m_mytripmanifest.size(); i++) {
					
					String tripmanifest = m_mytripmanifest.get(i);
					if (textlength <= tripmanifest.length()) {
						if (edittext.getText().toString().equalsIgnoreCase((String) tripmanifest.subSequence(0,textlength))) {
							m_mytripmanifest_sort.add(tripmanifest);
						}
					}
				}
			
		        m_adapter = new MyTripManifestAdapter(MyTripManifestViewActivity.this, R.layout.my_trip_manifest_row, m_mytripmanifest_sort);
		        setListAdapter(m_adapter);
			}
		});

    }
     
    
    ////////////////////////////////////////////////

    // /////////////////// MENU ////
 	@Override
 	public boolean onCreateOptionsMenu(Menu menu) {
 		MenuInflater inflater = getMenuInflater();
 		inflater.inflate(R.menu.menu_main, menu);
 		return true;
 	}
    
 	// NEW
 	@Override
 	public boolean onOptionsItemSelected(MenuItem item) {
 		switch (item.getItemId()) {
 		case R.id.action_refresh:
 			
 			// try to send all offline saved requests
 			try {
 						sManager.syncOpenRequests();
 						
			} catch (ServerException e) {
				e.printStackTrace();
				changeTobBarStatus();
				showAlert(e.getMessage());		
			}
 						
 			break;
 		case R.id.action_logout:
 			ItrekMobileSingelton.getInstance().setSessionID("");
 			Intent intent = new Intent(this, LoginActivity.class);
 			startActivityForResult(intent, 0);
 			

 		default:
 			break;
 		}

 		return true;
 	}
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	     
	    String selectedValue = (String) getListAdapter().getItem(position);
		
		Intent intent = new Intent( this ,TripManifestViewActivity.class);
		intent.putExtra("TripManifest",selectedValue);

		startActivity(intent);     
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	refreshListView();
        m_adapter.notifyDataSetChanged();   	
        
        ///// check if something is in offline table and if yes set icon to change
        
        changeTobBarStatus();
        
        
    }
    
    
    private void changeTobBarStatus(){
        
    	ActionBar ab = getActionBar(); 
        if(checkInternetConnection() && !ItrekMobileSingelton.getInstance().getSessionID().isEmpty()){
        	ab.setTitle(Html.fromHtml("<font color='#ffffff '>My Tripmanifest - online modus</font>"));
        }else{
        	ab.setTitle(Html.fromHtml("<font color='#ff0000'><b>My Tripmanifest - offline modus</b></font>"));
        }
        
               
        boolean status = dbManager.areOfflineDataAvailable();
        
        if(status){
        	Drawable myIcon = getResources().getDrawable( R.drawable.warning );
            ab.setIcon(myIcon);
            
            Toast.makeText(this, "Please try to re-sync the data !", Toast.LENGTH_LONG).show();
        }else{
        	Drawable myIcon = getResources().getDrawable( R.drawable.app_icon );
            ab.setIcon(myIcon);
        }
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        //mDatabase.close();
    }

    
    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if(m_mytripmanifest != null && m_mytripmanifest.size() > 0){
                m_adapter.notifyDataSetChanged();
                m_adapter.clear();
                for(int i=0;i<m_mytripmanifest.size();i++)
                m_adapter.add(m_mytripmanifest.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };
    
    private void getTripManifests(){
    	try {
    		m_mytripmanifest = new ArrayList<String>();  
    		List<String> gList = dbManager.getTripManifests();
    		
			for (String gm : gList) {
				insertOrdered(gm);
			}
		
		    Thread.sleep(1000); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		runOnUiThread(returnRes);
    }
    
    private void insertOrdered(String tripmanifest){
    	
    	String tripmanifest_old = new String();
    	int arraySize = m_mytripmanifest.size();
		if (arraySize==0){
			m_mytripmanifest.add(0,tripmanifest);
		}
		else {          	  
			for (int i=0;i<arraySize;i++){
				tripmanifest_old = (String) m_mytripmanifest.get(i);
				String surname_old = tripmanifest_old;
				  
				if (surname_old.compareTo(tripmanifest)>0){
					m_mytripmanifest.add(i,tripmanifest);
					i = arraySize;
				}
				else if (i==arraySize-1){
					m_mytripmanifest.add(i+1,tripmanifest);
				}
			}		  
		}
    }
    
    public void refreshListView() {
    	m_mytripmanifest.clear();
    	m_adapter.clear();
    	refreshListAdapter();
    }
    
    public void refreshListAdapter() {
    	
    	changeTobBarStatus();
    	
        viewOrders = new Runnable(){
            @Override
            public void run() {
                getTripManifests();
            }
        };
        Thread thread =  new Thread(null, viewOrders, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(MyTripManifestViewActivity.this,"Please wait...", "Retrieving data ...", true);
    }

       
    private class MyTripManifestAdapter extends ArrayAdapter<String> {

        private ArrayList<String> items;

        public MyTripManifestAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                
        	View v = convertView;
        	
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.my_trip_manifest_row, null);
            }
            
            String o = items.get(position);
            if (o != null) {
                
            	TextView tt = (TextView) v.findViewById(R.id.toptext);

                if (tt != null) {
                      tt.setText(o);
                }
            		
            }                            
           
            return v;
        }
    }
    
    
    public boolean checkInternetConnection(){
		
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		
		if (netinfo != null && netinfo.isConnectedOrConnecting()){
			return true;
		}
		else
			return false;
	}
    
    public void refreshList(){
    	refreshListAdapter();
    }
    
    @SuppressWarnings("deprecation")
	private void showAlert(String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(MyTripManifestViewActivity.this).create();

		// Setting Dialog Title
		alertDialog.setTitle("Alert Dialog");
		// Setting Dialog Message
		alertDialog.setMessage(message);
		// Setting Icon to Dialog
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {
		
				
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

}