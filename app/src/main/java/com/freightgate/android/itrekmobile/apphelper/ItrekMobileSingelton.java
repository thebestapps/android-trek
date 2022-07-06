package  com.freightgate.android.itrekmobile.apphelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class ItrekMobileSingelton {
    private static ItrekMobileSingelton instance = new ItrekMobileSingelton();
    private String deviceID;
    private String sessionID = "";
    private String userID;
    private String pHash;
    private String Company;
	//    private String SYSTEM_URL = "http://awest.freightgate.com/cgi-bin/tarifftrek5.1/itrek.cgi";
	private String SYSTEM_URL = "http://fgdemo.freightgate.com/cgi-bin/tarifftrek5.1/itrek.cgi";
	//    private String DATABSE = "awest";
	private String DATABSE = "fgdemo";
	private ItrekMobileSingelton() {
    }
	
	public static synchronized ItrekMobileSingelton getInstance() {
        return instance;
    }	
 
    public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	 
    public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getpHash() {
		return pHash;
	}

	public void setpHash(String pHash) {
		this.pHash = pHash;
	}

	public String getCompany() {
		return Company;
	}

	public void setCompany(String company) {
		Company = company;
	}

	public String getSYSTEM_URL() {
		return SYSTEM_URL;
	}

	public void setSYSTEM_URL(String sYSTEM_URL) {
		SYSTEM_URL = sYSTEM_URL;
	}

	public String getDATABSE() {
		return DATABSE;
	}

	public void setDATABSE(String dATABSE) {
		DATABSE = dATABSE;
	}

	@Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
    
    public boolean checkInternetConnection(Context context){

    	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo netinfo = cm.getActiveNetworkInfo();

    	if (netinfo != null && netinfo.isConnectedOrConnecting()){
    	return true;
    	}
    	else
		Toast.makeText(context, new Errormessage("5003" ,context.getApplicationContext()).get_errorText(), Toast.LENGTH_SHORT).show(); 
    	return false;
	}
    
    public void startDialogBox(Context context,String msg){
//        new AlertDialog.Builder(context)
//        .setIcon(android.R.drawable.ic_dialog_alert)
//        .setTitle(context.getResources().getString(R.string.db_title))
//        .setMessage(msg)
//        .setPositiveButton(context.getResources().getString(R.string.db_okay), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        })
//        .show(); 	    	
    }
     
}
	
	
