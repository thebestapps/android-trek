package com.freightgate.android.itrekmobile.data.tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.freightgate.android.itrekmobile.data.DatabaseHelper;

public class ShipmentInfoDatabseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;
	private Context context;

	  public ShipmentInfoDatabseHelper(Context context) {
	    super(context, DatabaseHelper.DB_NAME, null, DATABASE_VERSION);
	    this.context = context;
	  }

	  // Method is called during creation of the database
	  @Override
	  public void onCreate(SQLiteDatabase database) {
		  ShipmentInfoTable.onCreate(database);
//		  Toast.makeText(this.context , "CREATE TABLE Shipment Info",Toast.LENGTH_LONG).show();
	  }

	  // Method is called during an upgrade of the database,
	  // e.g. if you increase the database version
	  @Override
	  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		  ShipmentInfoTable.onUpgrade(database, oldVersion, newVersion);
	  }
	
}
