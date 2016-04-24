package com.abile2.stockcircuit.util;

import java.io.BufferedReader;
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







import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.abile2.stockcircuit.UtilityActivity;

public class GetLiveQuoteAsyncTask extends AsyncTask<Object, Void, HashMap<String, String> >{
	Context ctx;
	public GetLiveQuoteAsyncTask() {
		//this.ctx = ctx;
	}
	
	@Override
	protected HashMap<String, String>  doInBackground(Object... params) {
		HttpContext localContext = new BasicHttpContext();
		HashMap<String, String> quoteParams = new HashMap<String, String>();
		
		try {
			//String emailID = "amitji@gmail.com";
			String fullid = (String) params[0];
			if(!fullid.contains(":")){
				fullid +="NSE:"+fullid+","; 
			}

			
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("http://finance.google.com/finance/info?client=ig&q="+fullid);
			//HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"shopbindaas/getAllPromotions");
			Log.d("GET AppConfig ", "Geeting App Config From Server");
			
			//MultipartEntityBuilder entity = MultipartEntityBuilder.create();
			//entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			
			//entity.addTextBody("emailID", emailID);
            //HttpEntity httpentity = entity.build();
            //httpPost.setEntity(httpentity);  
			
			HttpResponse response = httpClient.execute(httpGet, localContext);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				Log.d("App Config Request Status ","200 Staus code Ok ");
				String url = null;
				String sResponse = "";
				while (((url = reader.readLine()) != null)) {
					sResponse += url;
				}

				
				quoteParams = UtilityActivity.getMapforJsonStringAfterStrip(sResponse);
				System.out.println("BufferedReader response " + sResponse);
				return quoteParams;
				

			} else {
				Log.d("App Config Request Error",
						"App Config Request Status : " + statusLine.getStatusCode());
				response.getEntity().getContent().close();
				System.err.println("TEMP ERROR");
				return quoteParams;
			}
		} catch (Exception e) {
			System.out.println("Error in getting App Config - "
					+ e.getMessage());
			e.printStackTrace();
			return quoteParams;
		}

	}

}
