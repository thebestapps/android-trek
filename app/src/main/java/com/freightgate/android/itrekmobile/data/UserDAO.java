package com.freightgate.android.itrekmobile.data;

import java.util.ArrayList;
import java.util.List;

import com.freightgate.android.itrekmobile.data.tables.UserTable;
import com.freightgate.android.itrekmobile.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDAO {

	// Database fields
	  private SQLiteDatabase database;
	  private DatabaseHelper dbHelper;
	  private String[] allColumns = { UserTable.COLUMN_USERS_ID, 
									  UserTable.COLUMN_USERS_USERNAME,
									  UserTable.COLUMN_USERS_PASSWORD};
	
	  public UserDAO(Context context) {
		    dbHelper = DatabaseHelper.getInstance(context);
	  }
	  
	  public void open() throws SQLException {
		    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
		  	dbHelper.close();
	  }

	  public User createUser(String username, String password) {
	    ContentValues values = new ContentValues();
	    values.put(UserTable.COLUMN_USERS_USERNAME, username);
	    values.put(UserTable.COLUMN_USERS_PASSWORD, password);
	    
	    long insertId = database.insert(UserTable.TABLE_USERS, null, values);
	    Cursor cursor = database.query(UserTable.TABLE_USERS, allColumns, UserTable.COLUMN_USERS_ID + " = " + insertId, null,null, null, null);
	    cursor.moveToFirst();
	    
	    User newUser = cursorToUser(cursor);
	    cursor.close();
	    
	    return newUser;
	  }

	  public void deleteUser(User user) {
	    int id = user.getId();
	    Log.d("SQL list ddebug","User deleted with id: " + id);
	    database.delete(UserTable.TABLE_USERS, UserTable.COLUMN_USERS_ID+ " = " + id, null);
	  }
	  
	  public User getUser(String username){
		  
		  List<String> params = (List<String>)new ArrayList<String>();
		  params.add(username);
			
		  Cursor cursor = database.rawQuery("SELECT * FROM "+UserTable.TABLE_USERS+" WHERE "+UserTable.COLUMN_USERS_USERNAME+" =?", (String[]) params.toArray(new String[0]));	
		  
		  if (cursor.getCount() == 0){
			  	cursor.close();
				return null;
		  }else{
			  cursor.moveToFirst();
			    
			  User user = cursorToUser(cursor);
			  cursor.close();
			  return user;
		  }
		  
	  }

	  public List<User> getAllUsers() {
	    List<User> users = new ArrayList<User>();

	    Cursor cursor = database.query(UserTable.TABLE_USERS,allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      User user = cursorToUser(cursor);
	      users.add(user);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return users;
	  }

	  private User cursorToUser(Cursor cursor) {
	    User user = new User();
	    user.setId(cursor.getInt(0));
	    user.setUsername(cursor.getString(1));
	    user.setPassword(cursor.getString(2));
//		user.setLastlogin(cursor.getString(3));
	    return user;
	  }
	  
}
