package com.freightgate.android.itrekmobile.data;

import java.util.ArrayList;
import java.util.List;

import com.freightgate.android.itrekmobile.data.tables.PenfingRequestsTable;
import com.freightgate.android.itrekmobile.model.PendingRequest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PendingRequestsDAO {

	// Database fields
	  private SQLiteDatabase database;
	  private DatabaseHelper dbHelper;
	  private String[] allColumns = { PenfingRequestsTable.COLUMN_SHIPMENT_INFO_ID,	
			  							PenfingRequestsTable.COLUMN_AGENT_REF,
			  							PenfingRequestsTable.COLUMN_BILLNO,
			  							PenfingRequestsTable.COLUMN_SHIPMENT_REQUEST};
	
	  public PendingRequestsDAO(Context context) {
		    dbHelper = DatabaseHelper.getInstance(context);
	  }
	  
	  public void open() throws SQLException {
		    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
		  	dbHelper.close();
	  }

	  public void createPendingRequest(String agent_ref, String billno, String request) {
	    
		ContentValues values = new ContentValues();
	    values.put(PenfingRequestsTable.COLUMN_AGENT_REF, agent_ref);
	    values.put(PenfingRequestsTable.COLUMN_BILLNO, billno);
	    values.put(PenfingRequestsTable.COLUMN_SHIPMENT_REQUEST, request);
	    
	    long insertId = database.insert(PenfingRequestsTable.TABLE_PENDING_REQUESTS, null, values);
	    Log.d("inserted id",String.valueOf(insertId));
	    Cursor cursor = database.query(PenfingRequestsTable.TABLE_PENDING_REQUESTS, allColumns, PenfingRequestsTable.COLUMN_SHIPMENT_INFO_ID + " = " + insertId, null,null, null, null);
	    cursor.moveToFirst();

	  }

	  public void deletePendingRequest(int id) {
	
	    Log.d("SQL list ddebug","Request deleted with id: " + id);
	    database.delete(PenfingRequestsTable.TABLE_PENDING_REQUESTS, PenfingRequestsTable.COLUMN_SHIPMENT_INFO_ID+ " = " + id, null);
	  }
	  
	  public void deletePendingRequest(String agent_ref, String billno) {
			
		    Log.d("SQL list ddebug","Request deleted with agent_ref: " + agent_ref +" and billno"+billno);
		    database.delete(PenfingRequestsTable.TABLE_PENDING_REQUESTS, PenfingRequestsTable.COLUMN_AGENT_REF+ " = \"" + agent_ref+"\" AND "+PenfingRequestsTable.COLUMN_BILLNO+ " = \"" + billno+"\"", null);
	  }
	  
	  public void deleteAllPendingRequest() {
			
		  database.delete(PenfingRequestsTable.TABLE_PENDING_REQUESTS, null, null);
	  }
	  
	  public List<PendingRequest> getAllRequests() {
	    List<PendingRequest> list = new ArrayList<PendingRequest>();

	    Cursor cursor = database.query(PenfingRequestsTable.TABLE_PENDING_REQUESTS,allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	list.add(cursorToUser(cursor));
	      cursor.moveToNext();
	    }

	    cursor.close();
	    return list;
	  }
	  
	  public boolean areOfflineDataAvailable() {

		    Cursor c = database.rawQuery("SELECT COUNT(db_id) as db_id FROM " + PenfingRequestsTable.TABLE_PENDING_REQUESTS, null);
		    c.moveToFirst();

		    int count = c.getInt(0);

		    c.close();
		    
		    
		    return (count > 0)? true : false;
	  }

	  private PendingRequest cursorToUser(Cursor cursor) {
	   PendingRequest request = new PendingRequest();
	   request.setId(cursor.getInt(0));
	   request.setAgent_ref(cursor.getString(1));
	   request.setBillno(cursor.getString(2));
	   request.setRequest(cursor.getString(3));
	    return request;
	  }
	  
}
