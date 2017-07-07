package com.abile2.stockcircuit.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.abile2.stockcircuit.Constants;
import com.abile2.stockcircuit.UtilityActivity;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GetIndustryVerticalsAsynTask extends AsyncTask<Object, Void, String>{
	Context ctx;
	public GetIndustryVerticalsAsynTask(Context ctx) {
		this.ctx = ctx;
	}
	
	@Override
	protected String doInBackground(Object... params) {
		HttpContext localContext = new BasicHttpContext();

			//String emailID = "amitji@gmail.com";
			//String is_video_available = (String) params[0];
			//String exchange_flag = (String) params[1];

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/getIndustryVerticals");
			
			MultipartEntityBuilder entity = MultipartEntityBuilder.create();
			entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			//entity.addTextBody("is_video_available", is_video_available);
			//entity.addTextBody("exchange_flag", exchange_flag);


            HttpEntity httpentity = entity.build();
            httpPost.setEntity(httpentity);
		try{
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

				//HashMap<String, String> sectorMap= UtilityActivity.getMapforJsonString(sResponse);
				//if(sectorMap!=null){
						SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
						SharedPreferences.Editor editor = mPrefs.edit();
						editor.putString("industry_verticals", sResponse);
						editor.commit();
//				System.out.println("Successfully Stored New referral_scheme_active Value in mPrefs.");
//				System.out.println("referral_scheme_active : "+ mPrefs.getString("referral_scheme_active", ""));
				//}
				
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
