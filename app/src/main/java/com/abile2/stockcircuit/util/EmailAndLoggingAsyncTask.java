package com.abile2.stockcircuit.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.abile2.stockcircuit.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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

public class EmailAndLoggingAsyncTask extends AsyncTask<Object, Void, String > {
    //ImageView bmImage;
    //Bitmap  bitImageArray;


    public EmailAndLoggingAsyncTask() {

    }
    
    @Override
    protected String doInBackground(Object... params) {
    	  	
        String mobile = (String) params[0];
        String deviceId=(String)params[1];
        String regID =(String)params[2];
        String fullid=(String)params[3];
        

         HttpClient httpClient = new DefaultHttpClient();  
         HttpContext localContext = new BasicHttpContext();  
         HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/emailAndLogging");
         MultipartEntityBuilder entity = MultipartEntityBuilder.create();   
         entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
         entity.addTextBody("mobile", mobile);
         entity.addTextBody("deviceID", deviceId);
         entity.addTextBody("regID", regID);
         entity.addTextBody("fullid", fullid);
         HttpEntity httpentity = entity.build();
         httpPost.setEntity(httpentity);  
    try{
         HttpResponse response = httpClient.execute(httpPost, localContext);  
         BufferedReader reader = new BufferedReader(new InputStreamReader( response.getEntity().getContent(), "UTF-8"));  
         //String sResponse = reader.readLine();
        


  
    } catch (Exception e) {  
        System.out.println("\n\n **** EmailAndLoggingAsyncTask  - Error in Email" +e.getMessage());
    	e.printStackTrace();
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

        return "";
    }


}