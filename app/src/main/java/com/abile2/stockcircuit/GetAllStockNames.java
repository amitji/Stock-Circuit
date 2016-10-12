package com.abile2.stockcircuit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
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

public class GetAllStockNames extends AsyncTask<Object, Void, String>{
	//Context ctx;
	public GetAllStockNames() {
		//this.ctx = ctx;
	}
	
	@Override
	protected String doInBackground(Object... params) {
		HttpContext localContext = new BasicHttpContext();
		try {
			//String emailID = "amitji@gmail.com";
			//String is_video_available = (String) params[0];
			//String exchange_flag = (String) params[1];
			String exchange = (String) params[0];

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/getAllStockNames");
			Log.d("GET AppConfig ", "Geeting App Config From Server");
			
			MultipartEntityBuilder entity = MultipartEntityBuilder.create();
			entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			//entity.addTextBody("is_video_available", is_video_available);
			//entity.addTextBody("exchange_flag", exchange_flag);
			entity.addTextBody("exchange", exchange);

            HttpEntity httpentity = entity.build();
            httpPost.setEntity(httpentity);
			
			HttpResponse response = httpClient.execute(httpPost, localContext);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

				String url = null;
				String sResponse = "";
				while (((url = reader.readLine()) != null)) {
					sResponse += url;
				}
				//System.out.println("BufferedReader response " + sResponse);

//				HashMap<String, String> appConfig=UtilityActivity.getMapforJsonString(sResponse);
//				if(appConfig!=null){
//						SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//						SharedPreferences.Editor editor = mPrefs.edit();
//						editor.remove("referral_scheme_active");
//						editor.commit();
//						System.out.println("Old referral_scheme_active Value Removed from mPrefs.");
//						editor = mPrefs.edit();
//						editor.putString("referral_scheme_active", appConfig.get("referral_scheme_active"));
//				editor.commit();
//				System.out.println("Successfully Stored New referral_scheme_active Value in mPrefs.");
//				System.out.println("referral_scheme_active : "+ mPrefs.getString("referral_scheme_active", ""));
//				}
				
				return sResponse;
			} else {

				response.getEntity().getContent().close();
				System.err.println("TEMP ERROR");
				return "";
			}
		} catch (Exception e) {
			System.out.println("Error in getting App Config - "
					+ e.getMessage());
			e.printStackTrace();
			return "";
		}

	}

}
