package com.freightgate.android.itrekmobile.server;

public class ServerException extends Exception {
	private static final long serialVersionUID = 1L;

	public ServerException(String errorCode){
		super(errorCode);
	}
}
