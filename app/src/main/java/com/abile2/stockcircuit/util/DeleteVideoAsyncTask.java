package com.abile2.stockcircuit.util;

import android.os.AsyncTask;
import android.util.Log;

import com.abile2.stockcircuit.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeleteVideoAsyncTask extends AsyncTask<Object, Void, String> {

	@Override
	protected String doInBackground(Object... params) {
		String recomID= (String) params[0];
		String mobile = (String) params[1];
		String deviceID=(String)params[2];
		String regID =(String)params[3];


	        HttpClient httpClient = new DefaultHttpClient();  
	        HttpContext localContext = new BasicHttpContext();  
	  
	        HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/DeleteVideo");

	        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
	        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			entity.addTextBody("recomID", recomID);
			entity.addTextBody("mobile", mobile);
			entity.addTextBody("deviceID", deviceID);
			entity.addTextBody("regID", regID);
	        HttpEntity httpentity = entity.build();
	        httpPost.setEntity(httpentity);
		try{
	        HttpResponse response = httpClient.execute(httpPost, localContext);
	        BufferedReader reader = new BufferedReader(new InputStreamReader( response.getEntity().getContent(), "UTF-8"));  
	        String sResponse = reader.readLine();

	      return sResponse;
	    } catch (Exception e){    
	        System.out.println("\n\n ****DeleteVideoAsyncTask Task  - Error in Deleting Video" +e.getMessage());
	    	e.printStackTrace();
	    	return "";
	    }
		finally{
			if (httpentity != null) {
				try {
					httpentity.consumeContent();
				} catch (IOException e) {
					Log.d( "",e.getMessage());
				}
			}
		}

	    	

	}

}
