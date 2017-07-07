package com.abile2.stockcircuit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.abile2.stockcircuit.AsyncTaskCompleteListener;
import com.abile2.stockcircuit.Constants;

public class SaveUserAsyncTask extends AsyncTask<Object, Void, String > {
    //ImageView bmImage;
    //Bitmap  bitImageArray;

	private AsyncTaskCompleteListener<String> callback;
    public SaveUserAsyncTask(AsyncTaskCompleteListener<String> cb) {
    	callback = cb;
    }
    
    @Override
    protected String doInBackground(Object... params) {
    	  	
        String regID = (String) params[0];
        String city=(String)params[1];
        String deviceId=(String)params[2];
        String name = (String) params[3];
        String email=(String)params[4];
        String mobile=(String)params[5];

         HttpClient httpClient = new DefaultHttpClient();  
         HttpContext localContext = new BasicHttpContext();  
         Log.d("Register Id to Send :", regID);
         HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/saveUserInfo");
         MultipartEntityBuilder entity = MultipartEntityBuilder.create();   
         entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
         entity.addTextBody("regID", regID);
         entity.addTextBody("city", city);
         entity.addTextBody("deviceID", deviceId);     
         entity.addTextBody("name", name);
         entity.addTextBody("email", email);
         entity.addTextBody("mobile", mobile);  
         HttpEntity httpentity = entity.build();
         httpPost.setEntity(httpentity);  
    try{
         HttpResponse response = httpClient.execute(httpPost, localContext);  
         BufferedReader reader = new BufferedReader(new InputStreamReader( response.getEntity().getContent(), "UTF-8"));  
         String sResponse = reader.readLine();  
        
        //System.out.println("Some error came up" +sResponse);
        
        return sResponse;
  
        } catch (Exception e) {
            System.out.println("\n\n **** SaveUserAsyncTask  - Error in Saving  User Info" +e.getMessage());
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

    protected void onPreExecute() {
    	super.onPreExecute();
        
    }       
     
    protected void onPostExecute(String result) {
        //bmImage.setImageBitmap(result);
       	super.onPostExecute(result);
        callback.onTaskComplete(result);          
     	
    }

}