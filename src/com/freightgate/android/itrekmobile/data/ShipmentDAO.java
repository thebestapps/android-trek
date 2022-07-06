package com.freightgate.android.itrekmobile.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.freightgate.android.itrekmobile.data.tables.ShipmentInfoTable;
import com.freightgate.android.itrekmobile.model.Shipment;

public class ShipmentDAO {

	// Database fields
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = {
			ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_REF,
			ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_NUM,
			ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_NAME,
			ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_ADD1,
			ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_ADD2,
			ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_CITY,
			ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_POSTAL,
			ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_STATE,
			ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_PHONE,
			
			ShipmentInfoTable.COLUMN_SHIPMENT_BILLNO,
			ShipmentInfoTable.COLUMN_SHIPMENT_TTL_PIECES,
			ShipmentInfoTable.COLUMN_SHIPMENT_TTL_WEIGHT,
			ShipmentInfoTable.COLUMN_SHIPMENT_CARRIER,
			ShipmentInfoTable.COLUMN_SHIPMENT_SIGNATURE,
			ShipmentInfoTable.COLUMN_SHIPMENT_PAY_STATUS,
			// consignee
			ShipmentInfoTable.COLUMN_SHIPMENT_CSG_REF,
			ShipmentInfoTable.COLUMN_SHIPMENT_CSG_NAME,
			ShipmentInfoTable.COLUMN_SHIPMENT_CSG_ADD1,
			ShipmentInfoTable.COLUMN_SHIPMENT_CSG_ADD2,
			ShipmentInfoTable.COLUMN_SHIPMENT_CSG_CITY,
			ShipmentInfoTable.COLUMN_SHIPMENT_CSG_POSTAL,
			ShipmentInfoTable.COLUMN_SHIPMENT_CSG_STATE ,
			ShipmentInfoTable.COLUMN_SHIPMENT_CSG_PHONE,
			// shipper
			ShipmentInfoTable.COLUMN_SHIPMENT_SHP_REF,
			ShipmentInfoTable.COLUMN_SHIPMENT_SHP_NAME,
			ShipmentInfoTable.COLUMN_SHIPMENT_SHP_ADD1,
			ShipmentInfoTable.COLUMN_SHIPMENT_SHP_ADD2,
			ShipmentInfoTable.COLUMN_SHIPMENT_SHP_CITY,
			ShipmentInfoTable.COLUMN_SHIPMENT_SHP_POSTAL,
			ShipmentInfoTable.COLUMN_SHIPMENT_SHP_STATE,
			ShipmentInfoTable.COLUMN_SHIPMENT_SHP_PHONE,
			//
			ShipmentInfoTable.COLUMN_SHIPMENT_DELIVERYAGENT_REF,
			ShipmentInfoTable.COLUMN_SHIPMENT_POSITION

	};

	public ShipmentDAO(Context context) {
		dbHelper = DatabaseHelper.getInstance(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void createShipmentInfo(Shipment shipment) {
		ContentValues values = new ContentValues();
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_REF,		shipment.getAgent_ref());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_NUM,		shipment.getAgent_num());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_ADD1,	shipment.getAgent_add1());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_ADD2,	shipment.getAgent_add2());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_CITY,	shipment.getAgent_city());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_NAME,	shipment.getAgent_name());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_POSTAL,	shipment.getAgent_postal());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_STATE,	shipment.getAgent_state());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_PHONE,	shipment.getAgent_phone());
		
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_BILLNO,		shipment.getBillno());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_SIGNATURE,		shipment.getSignature());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_TTL_PIECES,	shipment.getTtl_pieces());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_TTL_WEIGHT,	shipment.getTtl_weight());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_CARRIER,		shipment.getCarrier());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_PAY_STATUS,	shipment.getPay_status());
		
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_CSG_REF,	shipment.getCsg_ref());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_CSG_NAME,	shipment.getCsg_name());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_CSG_ADD1,	shipment.getCsg_add1());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_CSG_ADD2,	shipment.getCsg_add2());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_CSG_CITY,	shipment.getCsg_city());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_CSG_POSTAL,shipment.getCsg_postal());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_CSG_STATE,	shipment.getCsg_state());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_CSG_PHONE,	shipment.getCsg_phone());
		
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_SHP_REF,	shipment.getShp_ref());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_SHP_NAME,	shipment.getShp_name());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_SHP_ADD1,	shipment.getShp_add1());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_SHP_ADD2,	shipment.getShp_add2());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_SHP_CITY,	shipment.getShp_city());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_SHP_POSTAL,shipment.getShp_postal());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_SHP_STATE,	shipment.getShp_state());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_SHP_PHONE,	shipment.getShp_phone());
		
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_DELIVERYAGENT_REF,	shipment.getDeliveryagent_ref());
		values.put(ShipmentInfoTable.COLUMN_SHIPMENT_POSITION,	shipment.getPosition());

		long insertId = database.insert(ShipmentInfoTable.TABLE_SHIPMENT_INFO, null, values);
		Cursor cursor = database.query(ShipmentInfoTable.TABLE_SHIPMENT_INFO,allColumns, ShipmentInfoTable.COLUMN_SHIPMENT_INFO_ID + " = "+ insertId, null, null, null, null);
		cursor.moveToFirst();

		cursor.close();
	}

	
	public void deleteAllShipment() {

		Log.d("SQL list ddebug", "Shipment deleted all ");
		database.delete(ShipmentInfoTable.TABLE_SHIPMENT_INFO, null, null);
	}
	
	public void deleteShipment(Shipment shipment) {
		String id = shipment.getAgent_ref();
		Log.d("SQL list ddebug", "User deleted with id: " + id);
		database.delete(ShipmentInfoTable.TABLE_SHIPMENT_INFO, ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_REF + " = " + id, null);
	}
	
	public void deleteShipment(String agent_ref, String billno) {
		Log.d("SQL list ddebug", "User deleted with agent_ref: " + agent_ref +"billno"+ billno);
		database.delete(ShipmentInfoTable.TABLE_SHIPMENT_INFO, ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_REF + " = '" + agent_ref +"' AND "+ ShipmentInfoTable.COLUMN_SHIPMENT_BILLNO+" = '"+billno+"'", null);
//		database.delete(ShipmentInfoTable.TABLE_SHIPMENT_INFO, ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_REF + " = " + agent_ref , null);

	}
	

	public Shipment getShipmentById(String id) {

		List<String> params = (List<String>) new ArrayList<String>();
		params.add(id);

		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ ShipmentInfoTable.TABLE_SHIPMENT_INFO + " WHERE "
				+ ShipmentInfoTable.COLUMN_SHIPMENT_INFO_ID + " =?",
				(String[]) params.toArray(new String[0]));

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		} else {
			cursor.moveToFirst();

			Shipment user = cursorToShipment(cursor);
			cursor.close();
			return user;
		}

	}

	public List<Shipment> getAllShipents() {
		
		List<Shipment> users = new ArrayList<Shipment>();

		Cursor cursor = database.query(ShipmentInfoTable.TABLE_SHIPMENT_INFO,
				allColumns, null, null, null, null, ShipmentInfoTable.COLUMN_SHIPMENT_POSITION);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Shipment user = cursorToShipment(cursor);
			users.add(user);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return users;
	}
	
	public List<String> getTripManifests() {
		
		Cursor cursor = database.rawQuery("SELECT * "+
				" FROM " + ShipmentInfoTable.TABLE_SHIPMENT_INFO +
				" GROUP BY " + ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_REF +
				" ORDER BY "+ ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_REF, null);

		cursor.moveToFirst();
		List<String> manifests = new ArrayList<String>();
		while (!cursor.isAfterLast()) {
			
			String agent_ref = cursor.getString(1);
			manifests.add(agent_ref);

			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		
		return manifests;
	}
	
	
public ArrayList<Shipment> getShipmentsByManifest(String manifest) {
		
	  List<String> params = (List<String>)new ArrayList<String>();
	  params.add(manifest);
	
	  Cursor cursor = database.rawQuery("SELECT * "+
				 " FROM " + ShipmentInfoTable.TABLE_SHIPMENT_INFO + 
				 " WHERE " + ShipmentInfoTable.COLUMN_SHIPMENT_AGENT_REF +" =?"+
				 " ORDER BY "
				+ ShipmentInfoTable.COLUMN_SHIPMENT_POSITION +" ASC", (String[]) params.toArray(new String[0]));

		cursor.moveToFirst();
		ArrayList<Shipment> manifests = new ArrayList<Shipment>();
		while (!cursor.isAfterLast()) {
			
			String position = cursor.getString(33);
			manifests.add(cursorToShipment(cursor));

			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		
		return manifests;
	}

	private Shipment cursorToShipment(Cursor cursor) {
		Shipment shipment = new Shipment();
		
		shipment.setAgent_ref(cursor.getString(1));
		shipment.setAgent_num(cursor.getString(2));
		shipment.setAgent_name(cursor.getString(3));
		shipment.setAgent_add1(cursor.getString(4));
		shipment.setAgent_add2(cursor.getString(5));
		shipment.setAgent_city(cursor.getString(6));
		shipment.setAgent_postal(cursor.getString(7));
		shipment.setAgent_state(cursor.getString(8));
		shipment.setAgent_phone(cursor.getString(9));
		
		shipment.setBillno(cursor.getString(10));
		shipment.setTtl_pieces(cursor.getString(11));
		shipment.setTtl_weight(cursor.getString(12));
		shipment.setCarrier(cursor.getString(13));
		shipment.setSignature(cursor.getString(14));
		shipment.setPay_status(cursor.getString(15));
		
		// consignee
		shipment.setCsg_ref(cursor.getString(16));
		shipment.setCsg_name(cursor.getString(17));
		shipment.setCsg_add1(cursor.getString(18));
		shipment.setCsg_add2(cursor.getString(19));
		shipment.setCsg_city(cursor.getString(20));
		shipment.setCsg_postal(cursor.getString(21));
		shipment.setCsg_state(cursor.getString(22));
		shipment.setCsg_phone(cursor.getString(23));
		
		// shipper
		shipment.setShp_ref(cursor.getString(24));
		shipment.setShp_name(cursor.getString(25));
		shipment.setShp_add1(cursor.getString(26));
		shipment.setShp_add2(cursor.getString(27));
		shipment.setShp_city(cursor.getString(28));
		shipment.setShp_postal(cursor.getString(29));
		shipment.setShp_state(cursor.getString(30));
		shipment.setShp_phone(cursor.getString(31));
		
		shipment.setDeliveryagent_ref(cursor.getString(32));
		shipment.setPosition(Integer.valueOf(cursor.getString(33)));
		
		return shipment;
	}
}
