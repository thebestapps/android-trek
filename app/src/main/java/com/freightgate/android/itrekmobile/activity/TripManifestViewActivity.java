package com.freightgate.android.itrekmobile.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.freightgate.android.itrekmobile.apphelper.ItrekMobileSingelton;
import com.freightgate.android.itrekmobile.data.DatabaseManager;
import com.freightgate.android.itrekmobile.model.Shipment;
import com.freightgate.android.itrekmobile.server.ServerException;
import com.freightgate.android.itrekmobile.server.ServerManager;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TripManifestViewActivity extends ListActivity implements LocationListener {

	private String tripmanifest;
	MyCustomAdapter m_adapter = null;
	public ProgressDialog progrDialog;
	private ArrayList<Shipment> shipments = null;
	private Runnable viewOrders;
	private String locationTxt = "";
	
	private DatabaseManager dbManager;
	private ServerManager sManager;
	
	private Location location;
	private LocationManager locationManager;
	private String  provider;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_manifest);
	
		tripmanifest = (String) getIntent().getSerializableExtra("TripManifest");
		
		
		ActionBar ab = getActionBar(); 
		//add back-button to icon
		ab.setDisplayHomeAsUpEnabled(true);
		//change title name
		ab.setTitle("Tripmanifest:"+tripmanifest);
		
		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.MAIN_COLOR));     
        ab.setBackgroundDrawable(colorDrawable);
		
		dbManager = new DatabaseManager(TripManifestViewActivity.this);
		sManager = new ServerManager(this, dbManager);
		
		shipments = new ArrayList<Shipment>();
        this.m_adapter = new MyCustomAdapter(this, R.layout.trip_manifest_row,	shipments);
        setListAdapter(this.m_adapter);
//		refreshListAdapter(tripmanifest);
		
		
		//////// Location /////
		
		
		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the locatioin provider -> use
	    // default
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    location = locationManager.getLastKnownLocation(provider);
	    try {
			locationTxt = getLocationAddress(location);
		} catch (ServerException e) {
			e.printStackTrace();
		}

	}
	
	@Override
    protected void onResume() {
    	super.onResume();
    	
    	refreshListView();
        m_adapter.notifyDataSetChanged();  
        locationManager.requestLocationUpdates(provider, 20000, 100, this);
    }
	
	@Override
    protected void onPause() {
    	super.onPause();
    	locationManager.removeUpdates(this);
    }
	
			
	// /////////////////// MENU ////
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_trip_manifest, menu);
		return true;
	}
	
	
	////////////////////// CLICK EVENTS /////////////////////////
	
	
	public void onButtonClick(final View view) {

		switch (view.getId()) {	
		case R.id.btn_tripmanifest_multi_submit:
				ArrayList<Shipment> stateList = m_adapter.stateList;
				ArrayList<Shipment> shipmentLists = new ArrayList<Shipment>();
				for (int i = 0; i < stateList.size(); i++) {
					Shipment shipment = stateList.get(i);
	
					if (shipment.isSelected()) {
						// collect data						
						shipmentLists.add(shipment);
					}
				}
				
				if(shipmentLists.size() > 0){
				
				
				Intent intent = new Intent( this ,SubmitShipmentActivity.class);
				intent.putExtra("Shipment",shipmentLists);
				startActivity(intent); 
				}else{
					startDialogBox("You have to check minimum one item");
				}
			break;

		}

	}

	// NEW
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh_location:
			
			try {

				locationTxt = getLocationAddress(location);
			
			} catch (ServerException e) {
				e.printStackTrace();
				showMessage("No Location found");
			}
			
			openLocationDialog(locationTxt);
			
						
			break;
		case android.R.id.home:
			finish();

		default:
			break;
		}

		return true;
	}
	
	public void openLocationDialog(String location) {

		final View inflatedView = getLayoutInflater().inflate(R.layout.location_dialog, null);

		EditText locationTxt = (EditText) inflatedView.findViewById(R.id.et_location_dialog_location);

		locationTxt.setText(location);

		Builder builder = new Builder(this);
		builder.setTitle("Update Location")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {

								if(checkInternetConnection() && ItrekMobileSingelton.getInstance().getSessionID() != ""){
									// //// do submit to system

									EditText locationTxt = (EditText) inflatedView.findViewById(R.id.et_location_dialog_location);
									try {
										
										sManager.updateLocation(tripmanifest, locationTxt.getText().toString().trim());

										progrDialog = ProgressDialog.show(TripManifestViewActivity.this,"Please wait...", "Retrieving data ...", true);
									
									} catch (ServerException e) {
										e.printStackTrace();
										showAlert(e.getMessage());
									}
								}else{
									showMessage("System is offline. You can not do a location update right now. Please try it to a later time.");
								}
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {

								
							}
						}).setView(inflatedView).setCancelable(false);

		builder.show();

	}
	
	public String getLocationAddress(Location location) throws ServerException {
						
				
			    // Initialize the location fields
				if (location != null) {
					
					Geocoder geocoder;
					List<Address> addresses;
					geocoder = new Geocoder(this, Locale.getDefault());
					try {
						String city = "";
						if(checkInternetConnection()){
							addresses = geocoder.getFromLocation(location.getLatitude(),  location.getLongitude() , 1);
	//						String address = addresses.get(0).getAddressLine(0);
							city = addresses.get(0).getAddressLine(1);
	//						String country = addresses.get(0).getAddressLine(2);
						}else{
							city = location.getLatitude()+" "+  location.getLongitude();
						}
						
						return city ;
					
					} catch (IOException e) {
						e.printStackTrace();
						showMessage(e.getLocalizedMessage());
						return null;
					}
				}
				
				return null;
	}
	


	public void refreshListView() {
    	shipments.clear();
    	m_adapter.clear();
    	refreshListAdapter(tripmanifest);
    }
    
    public void refreshListAdapter(String manifest) {
    	
        viewOrders = new Runnable(){
            @Override
            public void run() {
                getTripManifests();
            }
        };
        Thread thread =  new Thread(null, viewOrders, "MagentoBackground");
        thread.start();
        progrDialog = ProgressDialog.show(TripManifestViewActivity.this,"Please wait...", "Retrieving data ...", true);
    }
    
    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if(shipments != null && shipments.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<shipments.size();i++)
                m_adapter.add(shipments.get(i));
            }
            progrDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };
    
    private void getTripManifests(){
    	try {
    		shipments = new ArrayList<Shipment>();  
    		shipments = dbManager.getShipmentsByManifest(tripmanifest);

		
		    Thread.sleep(1000); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		runOnUiThread(returnRes);
    }
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    
	    //// get checked shipments and selected
	    //// create Manifest object
	    
	    ArrayList<Shipment> shipmentLists = new ArrayList<Shipment>();
	    
	    Shipment selectedValue = (Shipment) getListAdapter().getItem(position);
	    shipmentLists.add(selectedValue);
						
		Intent intent = new Intent( this ,SubmitShipmentActivity.class);
		intent.putExtra("Shipment",shipmentLists);

		startActivity(intent);       
    }
	
	private static class AccessoriesViewHolder {
		public Button btn;
		public CheckBox checked;
        public ImageView image;
        public TextView trip_mani_bill_no;
        public TextView position;
        public TextView name;
        public TextView street;
        public TextView city;
        public TextView phone;
    }

	private class MyCustomAdapter extends ArrayAdapter<Shipment> {

		private ArrayList<Shipment> stateList;

		public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Shipment> stateList) {
			super(context, textViewResourceId, stateList);
			this.stateList = stateList;
//			this.stateList.addAll(stateList);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			AccessoriesViewHolder holder = null;
			
			Shipment o = stateList.get(position);
           

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.trip_manifest_row, parent, false);

                holder = new AccessoriesViewHolder();
                holder.btn = (Button) findViewById(R.id.btn_tripmanifest_multi_submit);
                holder.checked = (CheckBox) convertView.findViewById(R.id.tip_mani_checked);
                holder.image = (ImageView) convertView.findViewById(R.id.list_image);
                holder.trip_mani_bill_no = (TextView) convertView.findViewById(R.id.trip_mani_bill_no);
                holder.position = (TextView) convertView.findViewById(R.id.trip_mani_position);
                holder.name = (TextView) convertView.findViewById(R.id.txt_name);
                holder.street = (TextView) convertView.findViewById(R.id.txt_street);
                holder.city = (TextView) convertView.findViewById(R.id.txt_city);
                holder.phone = (TextView) convertView.findViewById(R.id.txt_phone);

                ((ImageButton) convertView.findViewById(R.id.btn_navigate)).setOnClickListener(mNavigateButtonClickListener);

                convertView.setTag(holder);
            } else {
                holder = (AccessoriesViewHolder) convertView.getTag();
            }

            /*
             * The Android API provides the OnCheckedChangeListener interface
             * and its onCheckedChanged(CompoundButton buttonView, boolean
             * isChecked) method. Unfortunately, this implementation suffers
             * from a big problem: you can't determine whether the checking
             * state changed from code or because of a user action. As a result
             * the only way we have is to prevent the CheckBox from callbacking
             * our listener by temporary removing the listener.
             */
            
            holder.position.setText(String.valueOf(o.getPosition()));
            holder.trip_mani_bill_no.setText(o.getBillno()+((o.getDeliveryagent_ref().equalsIgnoreCase(""))? "" : "  , "+o.getDeliveryagent_ref()));
            
            // empty normal delivery
            if(o.getAgent_num().isEmpty()){
            	holder.image.setImageResource(R.drawable.box);
            	holder.checked.setVisibility(View.GONE);
            	holder.btn.setVisibility(View.GONE); 
            	
            	
            	holder.name.setText(o.getCsg_name());
            	holder.street.setText(o.getCsg_add1());
            	String city = o.getCsg_city() +","+o.getCsg_state()+" "+o.getCsg_postal();
                holder.city.setText(city.replaceAll("null", ""));
                holder.phone.setText("phone: "+o.getCsg_phone());
            	
            }else{
            	// not empty agent dropof
            	holder.name.setText(o.getAgent_name());
            	holder.street.setText(o.getAgent_add1());
            	String city = o.getAgent_city() +","+o.getAgent_state()+" "+o.getAgent_postal();
                holder.city.setText(city.replaceAll("null", ""));
                holder.phone.setText("phone: "+o.getAgent_phone());

            	
            	holder.image.setImageResource(R.drawable.agent);
            	holder.checked.setVisibility(View.VISIBLE);
            	holder.checked.setOnCheckedChangeListener(null);
                holder.checked.setChecked(false);
                holder.checked.setOnCheckedChangeListener(mStarCheckedChanceChangeListener);
                holder.btn.setVisibility(View.VISIBLE);
            }
            return convertView;

	}
		
		}
	
	private void showMessage(String message) {
        Toast.makeText(TripManifestViewActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private OnClickListener mNavigateButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = getListView().getPositionForView(v);
            
            
            if (position != ListView.INVALID_POSITION) {    
            	
            	ArrayList<Shipment> stateList = m_adapter.stateList;
            	Shipment o = stateList.get(position);
            	String address ;
            	if(o.getAgent_num().isEmpty()){
            		address =o.getCsg_add1()+" "+o.getCsg_city()+", "+o.getCsg_state()+" "+o.getCsg_postal();
            	}else{
            		// get Agent address
            		address =o.getAgent_add1()+" "+o.getAgent_city()+", "+o.getAgent_state()+" "+o.getAgent_postal();
            	}
                Intent navIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+address.replaceAll("null", "")));
                startActivity(navIntent);
                
                
            }
        }
    };

    private OnCheckedChangeListener mStarCheckedChanceChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            final int position = getListView().getPositionForView(buttonView);
            if (position != ListView.INVALID_POSITION) {
            	m_adapter.stateList.get(position).setSelected(isChecked);
            }
        }
    };
    
	private void showAlert(String message) {
		
		Builder builder = new Builder(this);
		builder.setTitle("Alert Dialog")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(message)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								
							}
						});

		builder.show();
	}
	
	public void showAlertDismissprogress(String message) {

		progrDialog.dismiss();
		
		Builder builder = new Builder(this);
		builder.setTitle("Alert Dialog")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(message)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								
							}
						});

		builder.show();
		
		
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
	
	public boolean checkInternetConnection(){
		
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		
		if (netinfo != null && netinfo.isConnectedOrConnecting()){
			return true;
		}
		else
			return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		
		try {

			locationTxt = getLocationAddress(location);
		
		} catch (ServerException e) {
			e.printStackTrace();
			showMessage("No Location found");
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}
    
}
