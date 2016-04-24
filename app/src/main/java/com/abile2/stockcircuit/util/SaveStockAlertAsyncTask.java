package com.abile2.stockcircuit.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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

public class SaveStockAlertAsyncTask extends AsyncTask<Object, Void, String > {
    //ImageView bmImage;
    //Bitmap  bitImageArray;

 
    public SaveStockAlertAsyncTask() {

    }
    
    @Override
    protected String doInBackground(Object... params) {
    	  	
        String mobile = (String) params[0];
        String deviceId=(String)params[1];
        String regID =(String)params[2];
        String nseid=(String)params[3];
        String alert_price = (String) params[4];
        String low_high=(String)params[5];
        String fullid=(String)params[6];
        
    	try{
         HttpClient httpClient = new DefaultHttpClient();  
         HttpContext localContext = new BasicHttpContext();  
         HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/saveStockAlert");
         MultipartEntityBuilder entity = MultipartEntityBuilder.create();   
         entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
         entity.addTextBody("mobile", mobile);
         entity.addTextBody("deviceID", deviceId);
         entity.addTextBody("regID", regID);
         entity.addTextBody("nseid", nseid);
         entity.addTextBody("alert_price", alert_price);
         entity.addTextBody("low_high", low_high);  
         entity.addTextBody("fullid", fullid);
         HttpEntity httpentity = entity.build();
         httpPost.setEntity(httpentity);  
 
         HttpResponse response = httpClient.execute(httpPost, localContext);  
         BufferedReader reader = new BufferedReader(new InputStreamReader( response.getEntity().getContent(), "UTF-8"));  
         String sResponse = reader.readLine();  
        
        //System.out.println("Some error came up" +sResponse);
        
        return sResponse;
  
    } catch (Exception e) {  
        System.out.println("\n\n **** GetAlertsForAUserAsyncTask  - Error in Saving Stock Alert" +e.getMessage());  
    	e.printStackTrace();
    	return "";
    }  

  	
    }

    protected void onPostExecute(Bitmap result) {
        //bmImage.setImageBitmap(result);
    }

}