package com.freightgate.android.itrekmobile.data;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.freightgate.android.itrekmobile.apphelper.SHA1;
import com.freightgate.android.itrekmobile.model.Shipment;
import com.freightgate.android.itrekmobile.model.User;

import android.R.bool;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;

// am besten wohl noch als singleton implementieren
public class DatabaseManager {
	
//	private SQLiteDatabase database;
//	private Cursor cursor;
	private Context context;
//	private DatabaseHelper dbHelper;
		
	public DatabaseManager (Context cont){
		
//		dbHelper = new DatabaseHelper(cont);  
//		database = dbHelper.getWritableDatabase();;
		context = cont;
	}	
	
	
	public boolean localLogin(String username, String password){
		
		UserDAO userDao = new UserDAO(this.context);
		
		userDao.open();
		User user = userDao.getUser(username);
		userDao.close();
		
		if(user == null){
			return false;
		}else{
			
			try {
				String passwordHash = SHA1.convertToSHA1(password);
				Log.d("localLogin HASH",passwordHash);
				if(passwordHash.equals(user.getPassword())){
					return true;
				}else{
					return false;
				}
				
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		}

		return false;
	}
	
	public boolean refreshTripmanifests(JSONArray rows){
		
		
		ShipmentDAO shipDao = new ShipmentDAO(this.context);
		shipDao.open();
		shipDao.deleteAllShipment();
		shipDao.close();

		// do for every table
		for (int i=0;i<rows.length();i++){
			
			try {
				JSONObject joTable = (JSONObject) rows.getJSONObject(i);		
				Log.d("agent_ref","AGENT REF"+joTable.get("agent_ref").toString()+"  BILLNO "+joTable.get("billno").toString());
				
				Shipment shipment = new Shipment();
				
					shipment.setAgent_ref(joTable.get("agent_ref").toString());
					shipment.setAgent_num(joTable.get("agent_num").toString());
					shipment.setAgent_name((joTable.has("agent_name"))? joTable.get("agent_name").toString() : "");
					shipment.setAgent_add1((joTable.has("agent_add1"))? joTable.get("agent_add1").toString() : "");
					shipment.setAgent_add2((joTable.has("agent_add2"))? joTable.get("agent_add2").toString() : "");
					shipment.setAgent_city((joTable.has("agent_city"))? joTable.get("agent_city").toString() : "");
					shipment.setAgent_postal((joTable.has("agent_postal"))? joTable.get("agent_postal").toString() : "");
					shipment.setAgent_state((joTable.has("agent_state"))? joTable.get("agent_state").toString() : "");
					shipment.setAgent_phone((joTable.has("agent_phone"))? joTable.get("agent_phone").toString() : "");
					
					shipment.setBillno(joTable.get("billno").toString());
					shipment.setSignature(joTable.get("signature").toString());
					shipment.setTtl_pieces(joTable.get("ttl_pieces").toString());
					shipment.setTtl_weight(joTable.get("ttl_weight").toString());
					shipment.setCarrier(joTable.get("carrier").toString());
					shipment.setPay_status((joTable.has("pay_status"))? joTable.get("pay_status").toString() : "");
					
					// consignee
					shipment.setCsg_ref(joTable.get("csg_ref").toString());
					shipment.setCsg_name(joTable.get("csg_name").toString());
					shipment.setCsg_add1(joTable.get("csg_add1").toString());
					shipment.setCsg_add2(joTable.get("csg_add2").toString());
					shipment.setCsg_city(joTable.get("csg_city").toString());
					shipment.setCsg_postal(joTable.get("csg_postal").toString());
					shipment.setCsg_state(joTable.get("csg_state").toString());
					shipment.setCsg_phone((joTable.has("csg_phone"))? joTable.get("csg_phone").toString() : "");
					
					// shipment
					shipment.setShp_ref(joTable.get("shp_ref").toString());
					shipment.setShp_name(joTable.get("shp_name").toString());
					shipment.setShp_add1(joTable.get("shp_add1").toString());
					shipment.setShp_add2((joTable.has("shp_add2"))? joTable.get("shp_add2").toString() : "");
					shipment.setShp_city(joTable.get("shp_city").toString());
					shipment.setShp_postal(joTable.get("shp_postal").toString());
					shipment.setShp_state(joTable.get("shp_state").toString());
					shipment.setShp_phone((joTable.has("shp_phone"))? joTable.get("shp_phone").toString() : "");
					
					////////
					shipment.setDeliveryagent_ref((joTable.has("deliveryagent_ref"))? joTable.get("deliveryagent_ref").toString() : "");
					String position = (joTable.has("position"))? (joTable.get("position").toString()) : "";
					shipment.setPosition((!position.isEmpty())? Integer.valueOf(position) : 1);
					
				
				shipDao.open();
				shipDao.createShipmentInfo(shipment);
				shipDao.close();
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		
		
		
		return true;
	}
	
	
	public boolean saveOrUpdateUser(String company, String userID, String getpHash) {
		
		UserDAO userDao = new UserDAO(this.context);
		
		try{
			userDao.open();
			User user = userDao.getUser(userID);
			
			if(user == null){
				userDao.createUser(userID,getpHash);
	
				return true;
			}
			return false;
			
		}finally{
			userDao.close();
		}
		
	}	
	
	
	public List<String> getTripManifests() {
		
		ShipmentDAO shipmentDao = new ShipmentDAO(this.context);
		try{		
			shipmentDao.open();
			List<String> tripmanifest = shipmentDao.getTripManifests();
			
			if(tripmanifest != null){
	
				return tripmanifest;
			}
			return null;
			
		}finally{
			shipmentDao.close();
		}
	}
	
	
	public ArrayList<Shipment> getShipmentsByManifest(String manifest) {
		ShipmentDAO shipmentDao = new ShipmentDAO(this.context);
				
		try{
			shipmentDao.open();
			ArrayList<Shipment> tripmanifest = shipmentDao.getShipmentsByManifest(manifest);
			
			if(tripmanifest != null){
	
				return tripmanifest;
			}
	
			return null;
		}finally{
			shipmentDao.close();
		}
	}
	
	
	public boolean areOfflineDataAvailable() {
		
		boolean isAvailable = false;
		
		PendingRequestsDAO pendingDao = new PendingRequestsDAO(this.context);
				
		try{
			pendingDao.open();
			isAvailable = pendingDao.areOfflineDataAvailable();
			
			return isAvailable;
		}finally{
			pendingDao.close();
		}
	}

	
	public class DBException extends Exception {
		private static final long serialVersionUID = 1L;

		public DBException(String msg){
		super(msg);
		}
	}


}
