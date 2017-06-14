package com.abile2.stockcircuit.util;

import android.content.Context;
import android.os.AsyncTask;

import com.abile2.stockcircuit.Constants;

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
import java.io.InputStreamReader;

public class GetStockForecastAsynTask extends AsyncTask<Object, Void, String>{
	Context ctx;
	public GetStockForecastAsynTask(Context ctx) {
		this.ctx = ctx;
	}
	
	@Override
	protected String doInBackground(Object... params) {
		HttpContext localContext = new BasicHttpContext();
		try {
			String fullid= (String) params[0];
			String mobile = (String) params[1];
			String deviceID=(String)params[2];
			String regID =(String)params[3];


			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/getStockForecastData");
			
			MultipartEntityBuilder entity = MultipartEntityBuilder.create();
			entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			entity.addTextBody("fullid", fullid);
			entity.addTextBody("mobile", mobile);
			entity.addTextBody("deviceID", deviceID);
			entity.addTextBody("regID", regID);


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
