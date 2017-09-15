package com.abile2.stockcircuit.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.abile2.stockcircuit.UtilityActivity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;

public class GetLiveQuoteAsyncTask_OLD extends AsyncTask<Object, Void, HashMap<String, String> >{
	Context ctx;
	public GetLiveQuoteAsyncTask_OLD() {
		//this.ctx = ctx;
	}
	
	@Override
	protected HashMap<String, String>  doInBackground(Object... params) {
		HttpContext localContext = new BasicHttpContext();
		HashMap<String, String> quoteParams = new HashMap<String, String>();
		HttpResponse response = null;
		try {
			//String emailID = "amitji@gmail.com";
			String fullid = (String) params[0];
			if(!fullid.contains(":")){
				fullid +="NSE:"+fullid+","; 
			}
			//bugfix - encode for & in the symbol
			fullid = URLEncoder.encode(fullid, "utf-8");

			HttpClient httpClient = new DefaultHttpClient();
			//HttpGet httpGet = new HttpGet("http://finance.google.com/finance/info?client=ig&q="+fullid);
			HttpGet httpGet = new HttpGet("https://finance.google.com/finance?output=json&q="+fullid);


			Log.d("GET AppConfig ", "Geeting App Config From Server");
			
			//MultipartEntityBuilder entity = MultipartEntityBuilder.create();
			//entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			
			//entity.addTextBody("emailID", emailID);
            //HttpEntity httpentity = entity.build();
            //httpPost.setEntity(httpentity);


			response = httpClient.execute(httpGet, localContext);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				Log.d("GetLiveQuoteAsyncTask ","200 Staus code Ok ");
				String url = null;
				String sResponse = "";
				while (((url = reader.readLine()) != null)) {
					sResponse += url;
				}

				
				quoteParams = UtilityActivity.getMapforJsonStringAfterStrip(sResponse);
				System.out.println("BufferedReader response " + sResponse);
				return quoteParams;
				

			} else {

				response.getEntity().getContent().close();
				System.err.println("TEMP ERROR");
				return quoteParams;
			}
		} catch (Exception e) {
			System.out.println("Error in GetLiveQuoteAsyncTask - "
					+ e.getMessage());
			e.printStackTrace();
			return quoteParams;
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
