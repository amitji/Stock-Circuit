package com.abile2.stockcircuit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.abile2.stockcircuit.Constants;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class GetUserFavoriteAsyncTask extends AsyncTask<Object, Void, String > {
    //ImageView bmImage;
    //Bitmap  bitImageArray;

 
    public GetUserFavoriteAsyncTask() {

    }
    
    @Override
    protected String doInBackground(Object... params) {
    	  	
        String deviceID=(String)params[0];
        String regID =(String)params[1];


         HttpClient httpClient = new DefaultHttpClient();  
         HttpContext localContext = new BasicHttpContext();  
         HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/GetUserfavorite");
         MultipartEntityBuilder entity = MultipartEntityBuilder.create();   
         entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

         entity.addTextBody("deviceID", deviceID);
         entity.addTextBody("regID", regID);
         HttpEntity httpentity = entity.build();
         httpPost.setEntity(httpentity);

		try{
			HttpResponse response = httpClient.execute(httpPost, localContext);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				Log.d("User Favorite Request Status ","200 Staus code Ok ");
				String url = "";
				String sResponse = "";
				while (((url = reader.readLine()) != null)) {
					sResponse += url;
				}
				return sResponse;	
			} else {
				Log.d("User Favorite Request Error",
						"User Favorite Request Status : " + statusLine.getStatusCode());
				response.getEntity().getContent().close();
				System.err.println("TEMP ERROR");
				return "";
			}        
  
    } catch (Exception e) {  
        System.out.println("\n\n **** GetUserFavoriteAsyncTask  - Error in Getting Stock Favorite" +e.getMessage());  
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

    protected void onPostExecute(Bitmap result) {
        //bmImage.setImageBitmap(result);
    }

}