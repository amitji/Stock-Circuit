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

public class SaveStockVideoShareAsyncTask extends AsyncTask<Object, Void, String > {
    //ImageView bmImage;
    //Bitmap  bitImageArray;


    public SaveStockVideoShareAsyncTask() {

    }
    
    @Override
    protected String doInBackground(Object... params) {

        String mobile=(String)params[0];
        String deviceID=(String)params[1];
        String regID =(String)params[2];
        String shared_by =(String)params[3];
        String recom_ids =(String)params[4];

         HttpClient httpClient = new DefaultHttpClient();  
         HttpContext localContext = new BasicHttpContext();  
         HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/saveStockVideoShare");
         MultipartEntityBuilder entity = MultipartEntityBuilder.create();   
         entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

         entity.addTextBody("mobile", mobile);
            entity.addTextBody("deviceID", deviceID);
            entity.addTextBody("regID", regID);
            entity.addTextBody("shared_by", shared_by);
            entity.addTextBody("recom_ids", recom_ids);
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
            System.out.println("\n\n **** SaveStockVideoShareAsyncTask  - Error in Getting Stock Alert" +e.getMessage());
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