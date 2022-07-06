package com.freightgate.android.itrekmobile.data;

import com.freightgate.android.itrekmobile.data.tables.PendingRequestsDatabseHelper;
import com.freightgate.android.itrekmobile.data.tables.ShipmentInfoDatabseHelper;
import com.freightgate.android.itrekmobile.data.tables.UserDatabaseHelper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static DatabaseHelper mInstance = null;
	public static String DB_NAME = "itrek_mobile_db.db";	
	private static int dbVersion = 1;	
	private SQLiteDatabase db;	
	private final Context con;
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, dbVersion);
		this.con = context;		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// List all Tables
		Toast.makeText(this.con , "CREATE DATABASE",Toast.LENGTH_LONG).show();
		
		UserDatabaseHelper userTable = new UserDatabaseHelper(this.con);
		userTable.onCreate(db);
		ShipmentInfoDatabseHelper shipmentInfoTable = new ShipmentInfoDatabseHelper(this.con);
		shipmentInfoTable.onCreate(db);
		PendingRequestsDatabseHelper pendingReuestsTable = new PendingRequestsDatabseHelper(this.con);
		pendingReuestsTable.onCreate(db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public SQLiteDatabase getDatabase() throws SQLException {
		
		db = SQLiteDatabase.openDatabase(DB_NAME,null, SQLiteDatabase.OPEN_READWRITE);
		return db;
	}
	
	public static DatabaseHelper getInstance(Context ctx) {

	    // Use the application context, which will ensure that you 
	    // don't accidentally leak an Activity's context.
	    // See this article for more information: http://bit.ly/6LRzfx
	    if (mInstance == null) {
	      mInstance = new DatabaseHelper(ctx.getApplicationContext());
	    }
	    return mInstance;
	  }

	
    @Override
	public synchronized void close() {
    	if(db != null)
    		db.close();
 
    	super.close();
	}
}
