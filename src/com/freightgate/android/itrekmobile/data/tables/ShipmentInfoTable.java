package com.freightgate.android.itrekmobile.data.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ShipmentInfoTable {

	// Database table
		public static final String TABLE_SHIPMENT_INFO = "shipment_info";
		
		public static final String COLUMN_SHIPMENT_INFO_ID 			= "db_id";
		
		public static final String COLUMN_SHIPMENT_AGENT_REF 		= "agent_ref";
		public static final String COLUMN_SHIPMENT_AGENT_NUM		= "agent_num";
		public static final String COLUMN_SHIPMENT_AGENT_NAME 		= "agent_name";
		public static final String COLUMN_SHIPMENT_AGENT_ADD1 		= "agent_add1";
		public static final String COLUMN_SHIPMENT_AGENT_ADD2 		= "agent_add2";
		public static final String COLUMN_SHIPMENT_AGENT_CITY 		= "agent_city";
		public static final String COLUMN_SHIPMENT_AGENT_POSTAL 	= "agent_postal";
		public static final String COLUMN_SHIPMENT_AGENT_STATE 		= "agent_state";
		public static final String COLUMN_SHIPMENT_AGENT_PHONE		= "agent_phone";
		
		public static final String COLUMN_SHIPMENT_BILLNO			= "billno";
		public static final String COLUMN_SHIPMENT_TTL_PIECES 		= "ttl_pieces";
		public static final String COLUMN_SHIPMENT_TTL_WEIGHT 		= "ttl_weight";
		public static final String COLUMN_SHIPMENT_CARRIER 			= "carrier";
		public static final String COLUMN_SHIPMENT_SIGNATURE 		= "signature";
		public static final String COLUMN_SHIPMENT_PAY_STATUS		= "pay_status";
		
		// consignee
		public static final String COLUMN_SHIPMENT_CSG_REF 			= "csg_ref";
		public static final String COLUMN_SHIPMENT_CSG_NAME 		= "csg_name";
		public static final String COLUMN_SHIPMENT_CSG_ADD1 		= "csg_add1";
		public static final String COLUMN_SHIPMENT_CSG_ADD2 		= "csg_add2";
		public static final String COLUMN_SHIPMENT_CSG_CITY 		= "csg_city";
		public static final String COLUMN_SHIPMENT_CSG_POSTAL 		= "csg_postal";
		public static final String COLUMN_SHIPMENT_CSG_STATE 		= "csg_state";
		public static final String COLUMN_SHIPMENT_CSG_PHONE 		= "csg_phone";
		
		// shipper
		public static final String COLUMN_SHIPMENT_SHP_REF 			= "shp_ref";
		public static final String COLUMN_SHIPMENT_SHP_NAME 		= "shp_name";
		public static final String COLUMN_SHIPMENT_SHP_ADD1 		= "shp_add1";
		public static final String COLUMN_SHIPMENT_SHP_ADD2 		= "shp_add2";
		public static final String COLUMN_SHIPMENT_SHP_CITY 		= "shp_city";
		public static final String COLUMN_SHIPMENT_SHP_POSTAL 		= "shp_postal";
		public static final String COLUMN_SHIPMENT_SHP_STATE 		= "shp_state";
		public static final String COLUMN_SHIPMENT_SHP_PHONE		= "shp_phone";
		
		//
		public static final String COLUMN_SHIPMENT_DELIVERYAGENT_REF= "deliveryagent_ref";
		public static final String COLUMN_SHIPMENT_POSITION			= "position";

		// Database creation User Table statement
		  private static final String DATABASE_CREATE_SHIPMENT_INFO = "create table "
		      + TABLE_SHIPMENT_INFO + "(" + COLUMN_SHIPMENT_INFO_ID+" integer primary key autoincrement, " 
		      					+ COLUMN_SHIPMENT_AGENT_REF + " text null,"
		      					+ COLUMN_SHIPMENT_AGENT_NUM + " text null,"
		      					+ COLUMN_SHIPMENT_AGENT_NAME + " text null,"
		      					+ COLUMN_SHIPMENT_AGENT_ADD1 + " text null,"
		      					+ COLUMN_SHIPMENT_AGENT_ADD2 + " text null,"
		      					+ COLUMN_SHIPMENT_AGENT_CITY + " text null,"
		      					+ COLUMN_SHIPMENT_AGENT_POSTAL + " text null,"
		      					+ COLUMN_SHIPMENT_AGENT_STATE + " text null,"
		      					+ COLUMN_SHIPMENT_AGENT_PHONE + " text null,"
		      					
		      					+ COLUMN_SHIPMENT_BILLNO + " text null,"
		      					+ COLUMN_SHIPMENT_TTL_PIECES + " text null DEFAULT \"0\","
		      					+ COLUMN_SHIPMENT_TTL_WEIGHT + " text null DEFAULT \"0\","
		      					+ COLUMN_SHIPMENT_CARRIER + " text null,"
		      					+ COLUMN_SHIPMENT_SIGNATURE + " text null,"
		      					+ COLUMN_SHIPMENT_PAY_STATUS + " text null,"
	      						// consignee
		      					+ COLUMN_SHIPMENT_CSG_REF + " text null,"
		      					+ COLUMN_SHIPMENT_CSG_NAME + " text null,"
		      					+ COLUMN_SHIPMENT_CSG_ADD1 + " text null,"
		      					+ COLUMN_SHIPMENT_CSG_ADD2 + " text null,"
		      					+ COLUMN_SHIPMENT_CSG_CITY + " text null,"
		      					+ COLUMN_SHIPMENT_CSG_POSTAL + " text null,"
		      					+ COLUMN_SHIPMENT_CSG_STATE + " text null,"
		      					+ COLUMN_SHIPMENT_CSG_PHONE + " text null,"
		      					// shipper
		      					+ COLUMN_SHIPMENT_SHP_REF + " text null,"
		      					+ COLUMN_SHIPMENT_SHP_NAME + " text null,"
		      					+ COLUMN_SHIPMENT_SHP_ADD1 + " text null,"
		      					+ COLUMN_SHIPMENT_SHP_ADD2 + " text null,"
		      					+ COLUMN_SHIPMENT_SHP_CITY + " text null,"
		      					+ COLUMN_SHIPMENT_SHP_POSTAL + " text null,"
		      					+ COLUMN_SHIPMENT_SHP_STATE + " text null,"
		      					+ COLUMN_SHIPMENT_SHP_PHONE + " text null,"
		      					//
		      					+ COLUMN_SHIPMENT_DELIVERYAGENT_REF + " text null,"
		      					+ COLUMN_SHIPMENT_POSITION + " integer null"
	  							+ ");";	

		  public static void onCreate(SQLiteDatabase database) {
		    database.execSQL(DATABASE_CREATE_SHIPMENT_INFO);
		    
		  }
		
		  public static void onUpgrade(SQLiteDatabase database, int oldVersion,int newVersion) {
		    Log.w(UserTable.class.getName(), "Upgrading database from version "+ oldVersion + " to " + newVersion + ", which will destroy all old data");
		    database.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_SHIPMENT_INFO);
		    onCreate(database);
		  }
	
	
}
