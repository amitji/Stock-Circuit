package com.abile2.stockcircuit.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;



import com.abile2.stockcircuit.Constants;

import android.os.AsyncTask;

public class DeleteUserAlertsAsyncTask extends AsyncTask<Object, Void, String> {

	@Override
	protected String doInBackground(Object... params) {
		 String alertIDs=params[0].toString();
		try{
	        HttpClient httpClient = new DefaultHttpClient();  
	        HttpContext localContext = new BasicHttpContext();  
	  
	        HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/deleteUserAlerts");

	        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
	        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	 
	        entity.addTextBody("alertIDs", alertIDs);
	        
	         System.out.println("alertIDs to be Deleted are  :" + alertIDs);   
	        HttpEntity httpentity = entity.build();
	        httpPost.setEntity(httpentity);  
	  
	        HttpResponse response = httpClient.execute(httpPost, localContext);  
	        BufferedReader reader = new BufferedReader(new InputStreamReader( response.getEntity().getContent(), "UTF-8"));  
	        String sResponse = reader.readLine();
	        System.out.println("Alert Deleted Request Send Success");
	      return sResponse;
	    } catch (Exception e){    
	        System.out.println("\n\n ****DeleteUserAlertsAsyncTask Task  - Error in Deleting Aletrs" +e.getMessage());  
	    	e.printStackTrace();
	    	return "";
	    }  

	    	

	}

}
