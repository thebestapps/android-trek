package com.freightgate.android.itrekmobile.server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.freightgate.android.itrekmobile.activity.LoginActivity;
import com.freightgate.android.itrekmobile.activity.MyTripManifestViewActivity;
import com.freightgate.android.itrekmobile.activity.SubmitShipmentActivity;
import com.freightgate.android.itrekmobile.activity.TripManifestViewActivity;
import com.freightgate.android.itrekmobile.apphelper.ItrekMobileSingelton;
import com.freightgate.android.itrekmobile.apphelper.SHA1;
import com.freightgate.android.itrekmobile.data.DatabaseManager;
import com.freightgate.android.itrekmobile.data.PendingRequestsDAO;
import com.freightgate.android.itrekmobile.model.PendingRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ServerManager {
		
	public ProgressDialog progrDialog;
	private Context context;
	private Activity activity;
	private DatabaseManager dbManager;
	
	public ServerManager(Context con, DatabaseManager dbM) {
		
		context = con;
		activity = (Activity) con;
		dbManager = dbM;
	}
	
	
	public boolean checkInternetConnection(){
		
	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo netinfo = cm.getActiveNetworkInfo();
	
	if (netinfo != null && netinfo.isConnectedOrConnecting()){
		return true;
	}
	else
		return false;
	}
	
	public void webLogin(String company, String userID, String pw_hash) throws ServerException {
		
		if (!checkInternetConnection())
			throw new ServerException("No internet connection available");
		
		try {
			ItrekMobileSingelton.getInstance().setpHash(SHA1.convertToSHA1(pw_hash));
		} catch (Exception e) {

		}
		ItrekMobileSingelton.getInstance().setCompany(company);
		ItrekMobileSingelton.getInstance().setUserID(userID);
		
		
		String parameters = "action="+"get_session_id_json"+
							"&auth_user_name="+userID+
							"&auth_password="+pw_hash+
							"&database="+ItrekMobileSingelton.getInstance().getDATABSE()+
							"&auth_logon_op=0";
		// check user credentials
		ServerConnector servcon = new ServerConnector();		
		servcon.execute(new String[]{"get_session_id_json",parameters});
	}
	
	public void getMyTipManifests() throws ServerException {
		
		if (!checkInternetConnection())
			throw new ServerException("No internet connection available");
		if(ItrekMobileSingelton.getInstance().getSessionID().isEmpty())
			throw new ServerException("You are logged in offline ! Please log in again and test the connectivity status.");
		
		String parameters = "session_id="+ItrekMobileSingelton.getInstance().getSessionID()+
							"&action="+"display_report_json"+
							"&database="+ItrekMobileSingelton.getInstance().getDATABSE()+
							"&search_mode=begins"+
							"&metadata=1"+
							"&start=0&limit=100"+
							"&report_id=a_my_trip_manifest";
		
		// check user credentials
		ServerConnector servcon = new ServerConnector();		
		servcon.execute(new String[]{"getMyTipManifests",parameters});
	}
	
	public void syncOpenRequests() throws ServerException {
		
		if (!checkInternetConnection())
			throw new ServerException("No internet connection available");
		if(ItrekMobileSingelton.getInstance().getSessionID().isEmpty())
			throw new ServerException("You are logged in offline ! Please log in again and test the connectivity status.");

		PendingRequestsDAO requestsDao = new PendingRequestsDAO(context);
		requestsDao.open();
		List<PendingRequest> requests = requestsDao.getAllRequests();
		requestsDao.close();

		Log.d("TODO REQUESTS", "Count of to send requests "+requests.size());
		
		if(requests.size() > 0){
			String parameters = requests.get(0).getRequest();
			parameters += "&session_id="+ItrekMobileSingelton.getInstance().getSessionID();
			parameters += "&database="+ItrekMobileSingelton.getInstance().getDATABSE();
			parameters += "&action=submit_addition_ajax";
			parameters += "&report_id=m_add_status";
			parameters += "&debug=0";

			ServerConnector servcon = new ServerConnector();		
			servcon.execute(new String[]{"syncOpenRequests",parameters, String.valueOf(requests.get(0).getId())});
		}else{
			getMyTipManifests();
		}
	}
		
	public void updateLocation(String agent_ref, String location) throws ServerException {
		
		if (!checkInternetConnection())
			throw new ServerException("No internet connection available");
		if(ItrekMobileSingelton.getInstance().getSessionID().isEmpty())
			throw new ServerException("You are logged in offline ! Please log in again and test the connectivity status.");
		
		// get Date and Time
		
		Date now = new Date();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String entry_status_date = format1.format(now.getTime());
		SimpleDateFormat format2 = new SimpleDateFormat("HH:mm", Locale.US);
		String entry_status_time = format2.format(now.getTime());
		
		String parameters = "session_id="+ItrekMobileSingelton.getInstance().getSessionID()+
							"&report_id=add_status"+
							"&action=submit_addition_ajax"+
							"&database="+ItrekMobileSingelton.getInstance().getDATABSE()+
							"&db_id=-1"+
							"&search_agent_ref="+agent_ref+
							"&entry_agent_ref="+agent_ref+
							"&entry_status_code=LO"+
							"&entry_status_date="+entry_status_date+
							"&entry_status_time="+entry_status_time+
							"&entry_status_location="+location;
		
		// check user credentials
		ServerConnector servcon = new ServerConnector();		
		servcon.execute(new String[]{"updateLocation",parameters});
	}

	private class ServerConnector extends AsyncTask<String, Void, HashMap<String,Object>> {
		 
	    @Override
	    protected void onPreExecute(){
			progrDialog = ProgressDialog.show(context, "", "Getting data from server ...", true);
	    }
		
		@Override
		protected HashMap<String,Object> doInBackground(String... urls) {	
			
			String method = urls[0];
			String parameters = urls[1];
			String ids	= (urls.length == 3)? urls[2]: "";
			
			HashMap<String, Object> result = new HashMap<String,Object>();
			try {
				result = callServer(parameters);
			} catch (ServerException e) {
				e.printStackTrace();
				result.put("exception", e.getMessage());
			}
			result.put("method", method);
			result.put("ids", ids);
			
			return result;
		}

		@Override
		protected void onPostExecute(HashMap<String,Object> result) {			
			if (progrDialog != null)
				progrDialog.cancel();
			
			JSONObject dataJSON = (JSONObject)result.get("json");
			String method = (String) result.get("method");
			
			try {
//				String exception = dataJSON.getString("error");
//				String exception = null;
//				if (exception != null){				
//					LoginActivity logA = (LoginActivity) activity;
//					logA.startDialogBox(exception);
//				}
//				else {					
					String success = (dataJSON != null && dataJSON.has("success"))? dataJSON.getString("success") : "false";

					if (success.equals("true") || success.equals("1")){

						if (method.equals("updateLocation")) {

							// fill local db
//							JSONArray resultJSON = (JSONArray)dataJSON.getJSONArray("Rows");
							
							TripManifestViewActivity logA = (TripManifestViewActivity) activity;
							logA.showAlertDismissprogress(dataJSON.getString("message"));
						}
						else if (method.equals("syncOpenRequests")) {

							String ids = (String) result.get("ids");
//							String agent_ref = ids.split(":")[0];
//							String billno = ids.split(":")[1];
							
							
							PendingRequestsDAO requestsDao = new PendingRequestsDAO(context);
							requestsDao.open();
								requestsDao.deletePendingRequest(Integer.valueOf(ids));
								List<PendingRequest> requests = requestsDao.getAllRequests();
							requestsDao.close();
							
							if(requests.size() > 0){
								try {
									syncOpenRequests();
								} catch (ServerException e) {
								}
							}
							else{
								try {
									getMyTipManifests();
								} catch (ServerException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}
						else if (method.equals("getMyTipManifests")) {

							// fill local db
							JSONArray resultJSON = (JSONArray)dataJSON.getJSONArray("Rows");
							dbManager.refreshTripmanifests(resultJSON);
							String classname =  activity.getClass().getSimpleName();
							if(classname.equals("LoginActivity")){
								LoginActivity logA = (LoginActivity) activity;
								logA.startMyTripManifestViewActivity();
							}else if(classname.equals("MyTripManifestViewActivity")){
								MyTripManifestViewActivity logA = (MyTripManifestViewActivity) activity;
								logA.refreshList();
							}else if(classname.equals("SubmitShipmentActivity")){
								SubmitShipmentActivity logA = (SubmitShipmentActivity) activity;
								logA.triggerBackButton();
							}
						}
						else if (method.equals("get_session_id_json")) {
							
							// save session id
							ItrekMobileSingelton.getInstance().setSessionID(dataJSON.getString("sessionId"));																	
							
							// Save or Update user to local DB
							dbManager.saveOrUpdateUser(ItrekMobileSingelton.getInstance().getCompany(),ItrekMobileSingelton.getInstance().getUserID(),ItrekMobileSingelton.getInstance().getpHash());
							
							LoginActivity logA = (LoginActivity) activity;
							logA.refreshManifest();
							
						}
	
						
					}else {
						// how to find out which activity startet the call (like in DataUpdate)
//						LoginActivity logA = (LoginActivity) activity;
						
						String errorMessagt = "no error message";
						if(dataJSON == null){
							errorMessagt = (String) result.get("exception");
						}else if(dataJSON.has("message")){
							errorMessagt = dataJSON.getString("message");
						}else if(dataJSON.has("error")){
							errorMessagt = dataJSON.getString("error");
						}
					
						Toast.makeText(activity, errorMessagt, Toast.LENGTH_LONG).show();
//						logA.startDialogBox(errorMessagt);
					}
//				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}		
		
		private HashMap<String,Object> callServer(String params) throws ServerException {
			
			HashMap<String,Object> result = new HashMap<String,Object>();
			HttpURLConnection connection;
		    OutputStreamWriter request;
		    URL url; 
			String line = "";
			BufferedReader breader;
	        StringBuilder sb;
			JSONTokener tokener ;
			JSONObject finalJSON = new JSONObject();

	        try {
	        	
	        	url = new URL(ItrekMobileSingelton.getInstance().getSYSTEM_URL());
	            connection = (HttpURLConnection) url.openConnection();
	            connection.setDoOutput(true);
	            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	            connection.setRequestMethod("POST");
	            connection.setConnectTimeout(14*1000);
	            connection.connect();	
	            
	            request = new OutputStreamWriter(connection.getOutputStream());
	            request.write(params);
	            request.flush();
	            request.close();
	                        
	            breader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            sb = new StringBuilder();
	            while ((line = breader.readLine()) != null)
	            {
	                sb.append(line + "\n");
	            }
               
	            tokener = new JSONTokener(sb.toString());
	            finalJSON = new JSONObject(tokener);
	            result.put("json", finalJSON);
	            
//	            System.out.println ("Final Result " + finalJSON);
	        }
	        catch(Exception e){
	        	System.out.println(e.getMessage());
	        	e.printStackTrace();
	        	throw new ServerException("Server not reachable.");
	        }
	        
	        return result;			
		}
		
		
	}

}
