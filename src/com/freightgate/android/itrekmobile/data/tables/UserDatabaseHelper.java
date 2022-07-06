package com.freightgate.android.itrekmobile.data.tables;

import com.freightgate.android.itrekmobile.data.DatabaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class UserDatabaseHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	private Context context;

	  public UserDatabaseHelper(Context context) {
	    super(context, DatabaseHelper.DB_NAME, null, DATABASE_VERSION);
	    this.context = context;
	  }

	  // Method is called during creation of the database
	  @Override
	  public void onCreate(SQLiteDatabase database) {
		  UserTable.onCreate(database);
//		  Toast.makeText(this.context , "CREATE TABLE USERS",Toast.LENGTH_LONG).show();
	  }

	  // Method is called during an upgrade of the database,
	  // e.g. if you increase the database version
	  @Override
	  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
	    UserTable.onUpgrade(database, oldVersion, newVersion);
	  }

	
	
}
