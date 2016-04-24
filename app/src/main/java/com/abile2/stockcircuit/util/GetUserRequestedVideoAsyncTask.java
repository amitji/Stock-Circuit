package com.abile2.stockcircuit.util;

import android.content.Context;
import android.os.AsyncTask;
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
import java.io.InputStreamReader;
import java.util.HashMap;

public class GetUserRequestedVideoAsyncTask extends AsyncTask<Object, Void, String >{
	Context ctx;

	LinearLayout resourceView;
	LinearLayout loadingView;
	private AsyncTaskCompleteListener<String> callback;

	public GetUserRequestedVideoAsyncTask(Context ctx,AsyncTaskCompleteListener<String> cb) {
		//this.ctx = ctx;
		this.ctx = ctx;
		this.callback = cb;
	}
	
	@Override
	protected String  doInBackground(Object... params) {
		HttpContext localContext = new BasicHttpContext();
		HashMap<String, String> quoteParams = new HashMap<String, String>();
		
		try {
			//String emailID = "amitji@gmail.com";
			String fullid = (String) params[0];
			String deviceID = (String) params[1];
			String regID = (String) params[2];
			String mobile = (String) params[3];

			
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/createStockVideo");

			MultipartEntityBuilder entity = MultipartEntityBuilder.create();
			entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			entity.addTextBody("fullid", fullid);
			entity.addTextBody("deviceID", deviceID);
			entity.addTextBody("regID", regID);
			entity.addTextBody("mobile", mobile);

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

				
				quoteParams = UtilityActivity.getMapforJsonString(sResponse);
				return quoteParams.get("url");
			} else {
				response.getEntity().getContent().close();
				System.err.println("TEMP ERROR");
				return null;
			}
		} catch (Exception e) {
			System.out.println("Error in getting App Config - "	+ e.getMessage());
			e.printStackTrace();
			return null;
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
