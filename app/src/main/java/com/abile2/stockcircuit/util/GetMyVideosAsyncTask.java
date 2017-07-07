package com.abile2.stockcircuit.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

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
import java.io.IOException;
import java.io.InputStreamReader;

public class GetMyVideosAsyncTask extends AsyncTask<Object, Void, String > {
    //ImageView bmImage;
    //Bitmap  bitImageArray;


    public GetMyVideosAsyncTask() {

    }
    
    @Override
    protected String doInBackground(Object... params) {
    	  	
        String deviceID=(String)params[0];
        String regID =(String)params[1];


         HttpClient httpClient = new DefaultHttpClient();  
         HttpContext localContext = new BasicHttpContext();  
         HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/getMyVideos");
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
        System.out.println("\n\n **** GetAllRecommendedVideoAsyncTask  - Error in Getting Stock Alert" +e.getMessage());
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