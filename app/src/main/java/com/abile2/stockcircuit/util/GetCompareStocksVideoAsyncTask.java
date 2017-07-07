package com.abile2.stockcircuit.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;

import com.abile2.stockcircuit.AsyncTaskCompleteListener;
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

public class GetCompareStocksVideoAsyncTask extends AsyncTask<Object, Void, String >{

	Context ctx;
	private AsyncTaskCompleteListener<String> callback;

	public GetCompareStocksVideoAsyncTask(Context ctx, AsyncTaskCompleteListener<String> cb) {
		this.ctx = ctx;
		this.callback = cb;
	}
	
	@Override
	protected String  doInBackground(Object... params) {
		HttpContext localContext = new BasicHttpContext();
		HashMap<String, String> quoteParams = new HashMap<String, String>();
		
			//String emailID = "amitji@gmail.com";
			String fullid1 = (String) params[0];
			String fullid2 = (String) params[1];
			String fullid3 = (String) params[2];
			String deviceID = (String) params[3];
			String regID = (String) params[4];
			String mobile = (String) params[5];

			
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/compareStocksVideo");

			MultipartEntityBuilder entity = MultipartEntityBuilder.create();
			entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			entity.addTextBody("fullid1", fullid1);
			entity.addTextBody("fullid2", fullid2);
			entity.addTextBody("fullid3", fullid3);
			entity.addTextBody("deviceID", deviceID);
			entity.addTextBody("regID", regID);
			entity.addTextBody("mobile", mobile);

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

				
				//quoteParams = UtilityActivity.getMapforJsonString(sResponse);
				//return quoteParams.get("url");
				return sResponse;
			} else {
				response.getEntity().getContent().close();
				System.err.println("TEMP ERROR");
				return null;
			}
		} catch (Exception e) {
			System.out.println("Error running GetCompareStocksVideoAsyncTask - "	+ e.getMessage());
			e.printStackTrace();
			return null;
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
	protected void onPreExecute() {
		super.onPreExecute();

	}
	protected void onPostExecute(String result) {
		//bmImage.setImageBitmap(result);
		super.onPostExecute(result);
		callback.onTaskComplete(result);
	}
}
