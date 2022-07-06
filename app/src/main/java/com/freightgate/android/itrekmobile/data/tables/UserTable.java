package com.freightgate.android.itrekmobile.data.tables;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.freightgate.android.itrekmobile.apphelper.SHA1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserTable {

	// Database table
	public static final String TABLE_USERS = "users";
	public static final String COLUMN_USERS_ID = "id";
	public static final String COLUMN_USERS_USERNAME = "username";
	public static final String COLUMN_USERS_PASSWORD = "password";
	public static final String COLUMN_USERS_LASTLOGIN = "lastlogin";

	// Database creation User Table statement
	  private static final String DATABASE_CREATE_USERS = "create table "
	      + TABLE_USERS + "(" + COLUMN_USERS_ID+" integer primary key autoincrement, " 
	      					+ COLUMN_USERS_USERNAME + " text not null,"
	      					+ COLUMN_USERS_PASSWORD + " text not null,"
	      					+ COLUMN_USERS_LASTLOGIN + " text"
  							+ ");";	

	  public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE_USERS);
	    
	    // insert default user 
	    String[] allColumns = { UserTable.COLUMN_USERS_ID, 
				  UserTable.COLUMN_USERS_USERNAME,
				  UserTable.COLUMN_USERS_PASSWORD};
	    
	    ContentValues values = new ContentValues();
	    values.put(UserTable.COLUMN_USERS_USERNAME, "UPTIMECT");
	    try {
			values.put(UserTable.COLUMN_USERS_PASSWORD, SHA1.convertToSHA1("8002*flog"));
			Log.d("PASSWORD HASH",SHA1.convertToSHA1("8002*flog"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    long insertId = database.insert(UserTable.TABLE_USERS, null, values);
	    Cursor cursor = database.query(UserTable.TABLE_USERS, allColumns, UserTable.COLUMN_USERS_ID + " = " + insertId, null,null, null, null);
	    cursor.moveToFirst();
	    
	    cursor.close();
	    
	  }
	
	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,int newVersion) {
	    Log.w(UserTable.class.getName(), "Upgrading database from version "+ oldVersion + " to " + newVersion + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_USERS);
	    onCreate(database);
	  }
	
	
}
