package com.abile2.stockcircuit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;









import com.abile2.stockcircuit.UtilityActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class GetNewsFeedAsyncTask extends AsyncTask<Object, Void, String >{
	Context ctx;
	public GetNewsFeedAsyncTask() {
		//this.ctx = ctx;
	}
	
	@Override
	protected String  doInBackground(Object... params) {
		HttpContext localContext = new BasicHttpContext();
		HashMap<String, String> quoteParams = new HashMap<String, String>();
		String feed = "";
		String sResponse = "";
		HttpResponse response = null;

			String url = (String) params[0];
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
		try{
			response = httpClient.execute(httpGet, localContext);
			
			
			//InputStream is = new InputStream(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				Log.d("App Config Request Status ","200 Staus code Ok ");
				while (((feed = reader.readLine()) != null)) {
					sResponse += feed;
				}

				
				//quoteParams = UtilityActivity.getMapforJsonStringAfterStrip(sResponse);
				System.out.println("BufferedReader response " + sResponse);
				return sResponse;
				

			} else {
				Log.d("App Config Request Error",
						"App Config Request Status : " + statusLine.getStatusCode());
				response.getEntity().getContent().close();
				System.err.println("TEMP ERROR");
				return sResponse;
			}
		} catch (Exception e) {
			System.out.println("Error in getting App Config - "
					+ e.getMessage());
			e.printStackTrace();
			return sResponse;
		}
		finally{
			if (response != null && response.getEntity() != null) {
				try {
					response.getEntity().consumeContent();
				} catch (IOException e) {
					Log.d( "",e.getMessage());
				}
			}
		}

	}

}
