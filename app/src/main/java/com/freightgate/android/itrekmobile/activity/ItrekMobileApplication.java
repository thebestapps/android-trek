package com.freightgate.android.itrekmobile.activity;
import android.app.Application;
import android.net.Uri;


public class ItrekMobileApplication extends Application {

	
	private Uri picUri;

    public Uri getCamPicUri() {
        return picUri;
    }

    public void setCamPicUri(Uri picUri) {
        this.picUri = picUri;
    }
	
}
