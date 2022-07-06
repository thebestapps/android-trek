package  com.freightgate.android.itrekmobile.apphelper;


import android.content.Context;

public class Errormessage {
		
private String description;

	public Errormessage(String code, Context Ctxt) {
  
		this.description = Ctxt.getResources().getString(Ctxt.getResources().getIdentifier( "error_"+code , "string" ,	Ctxt.getPackageName() ));
	}

	public String get_errorText() {
		return description;
	}
}



