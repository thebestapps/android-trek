package com.freightgate.android.itrekmobile.data.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PenfingRequestsTable {

	// Database table
		public static final String TABLE_PENDING_REQUESTS = "pending_requests";
		
		public static final String COLUMN_SHIPMENT_INFO_ID 			= "db_id";
		public static final String COLUMN_AGENT_REF 				= "agent_ref";
		public static final String COLUMN_BILLNO 					= "billno";
		public static final String COLUMN_SHIPMENT_REQUEST 			= "to_do_request";

		// Database creation User Table statement
		  private static final String DATABASE_CREATE_PENDING_REQUESTS  = "create table "
		      + TABLE_PENDING_REQUESTS + "(" + COLUMN_SHIPMENT_INFO_ID+" integer primary key autoincrement, " 
							        + COLUMN_AGENT_REF + " text null,"
							        + COLUMN_BILLNO + " text null,"
									+ COLUMN_SHIPMENT_REQUEST + " blob null"
									+ ");";	

		  public static void onCreate(SQLiteDatabase database) {
		    database.execSQL(DATABASE_CREATE_PENDING_REQUESTS );
		    
		  }
		
		  public static void onUpgrade(SQLiteDatabase database, int oldVersion,int newVersion) {
		    Log.w(UserTable.class.getName(), "Upgrading database from version "+ oldVersion + " to " + newVersion + ", which will destroy all old data");
		    database.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_PENDING_REQUESTS );
		    onCreate(database);
		  }
	
	
}
