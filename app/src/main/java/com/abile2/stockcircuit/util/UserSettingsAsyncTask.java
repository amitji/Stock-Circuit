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

public class UserSettingsAsyncTask extends AsyncTask<Object, Void, String > {
    //ImageView bmImage;
    //Bitmap  bitImageArray;

 
    public UserSettingsAsyncTask() {

    }
    
    @Override
    protected String doInBackground(Object... params) {

        
        String regID = (String) params[0];
        String city=(String)params[1];
        String deviceId=(String)params[2];
        String mobile=(String)params[3];
        String brokerMobile1 = (String) params[4];
        String brokerName1=(String)params[5];
        String brokerMobile2=(String)params[6];
        String brokerName2=(String)params[7];
        String brokerWebsite=(String)params[8];
        
        try{
         HttpClient httpClient = new DefaultHttpClient();  
         HttpContext localContext = new BasicHttpContext();  
         Log.d("Register Id to Send :", regID);
         HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/saveUserSettings");
         MultipartEntityBuilder entity = MultipartEntityBuilder.create();   
         entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
         entity.addTextBody("regID", regID);
         entity.addTextBody("city", city);
         entity.addTextBody("deviceID", deviceId);     
         entity.addTextBody("mobile", mobile);  
         entity.addTextBody("brokerMobile1", brokerMobile1);
         entity.addTextBody("brokerName1", brokerName1);
         entity.addTextBody("brokerMobile2", brokerMobile2);     
         entity.addTextBody("brokerName2", brokerName2);  
         entity.addTextBody("brokerWebsite", brokerWebsite);  
         
         HttpEntity httpentity = entity.build();
         httpPost.setEntity(httpentity);  
 
         HttpResponse response = httpClient.execute(httpPost, localContext);  
         BufferedReader reader = new BufferedReader(new InputStreamReader( response.getEntity().getContent(), "UTF-8"));  
         String sResponse = reader.readLine();  
        
        //System.out.println("Some error came up" +sResponse);
        
        return sResponse;
  
    } catch (Exception e) {  
        System.out.println("\n\n **** UserSettingsAsyncTask  - Error in Saving  User Settings" +e.getMessage());  
    	e.printStackTrace();
    	return "";
    }  

  	
    }

    protected void onPostExecute(Bitmap result) {
        //bmImage.setImageBitmap(result);
    }

}