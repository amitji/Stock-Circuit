package com.abile2.stockcircuit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
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

import com.abile2.stockcircuit.Constants;
import com.abile2.stockcircuit.UtilityActivity;

public class GetLiveQuoteAsyncTask extends AsyncTask<Object, Void, HashMap<String, String> > {
	Context ctx;

	public GetLiveQuoteAsyncTask() {
		//this.ctx = ctx;
	}

	@Override
	protected HashMap<String, String> doInBackground(Object... params) {
		HttpContext localContext = new BasicHttpContext();
		HashMap<String, String> quoteParams = new HashMap<String, String>();

		//String emailID = "amitji@gmail.com";
		String fullid = (String) params[0];
		if (!fullid.contains(":")) {
			fullid += "NSE:" + fullid + ",";
		}
		//bugfix - encode for & in the symbol
		//fullid = URLEncoder.encode(fullid, "utf-8");

		HttpClient httpClient = new DefaultHttpClient();
		//HttpGet httpGet = new HttpGet("http://finance.google.com/finance/info?client=ig&q="+fullid);
		//HttpGet httpGet = new HttpGet("https://finance.google.com/finance?output=json&q="+fullid);
		HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL + "stockcircuit/getLiveQuote");


		MultipartEntityBuilder entity = MultipartEntityBuilder.create();
		entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		entity.addTextBody("fullid", fullid);


		HttpEntity httpentity = entity.build();
		httpPost.setEntity(httpentity);
		try {
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


				quoteParams = UtilityActivity.getMapforJsonStringAfterStrip(sResponse);
				return quoteParams;
				//return quoteParams.get("url");

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
		} finally {
			if (httpentity != null) {
				try {
					httpentity.consumeContent();
				} catch (IOException e) {
					Log.d("", e.getMessage());
				}
			}
		}

	}
}