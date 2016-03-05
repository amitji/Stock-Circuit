package com.abile2.stockcircuit;

import java.io.IOException;

import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMHelper {
	static GoogleCloudMessaging gcm = null;
	static Context context = null;

	public GCMHelper(Context context) {
		this.context = context;
	}
	public String GCMRegister(String SENDER_ID)  {
		String regid = "";
		try{
		gcm = GoogleCloudMessaging.getInstance(context);
		regid = gcm.register(SENDER_ID);
		}catch(IOException e){
			e.printStackTrace();
		}
		return regid;
	}
}
