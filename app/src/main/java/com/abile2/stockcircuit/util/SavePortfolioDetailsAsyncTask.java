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

public class SavePortfolioDetailsAsyncTask extends AsyncTask<Object, Void, String > {
    //ImageView bmImage;
    //Bitmap  bitImageArray;


    public SavePortfolioDetailsAsyncTask() {

    }
    
    @Override
    protected String doInBackground(Object... params) {
    	  	
        String favID = (String) params[0];
        String qty = (String)params[1];
        String buy_price = (String)params[2];


         HttpClient httpClient = new DefaultHttpClient();  
         HttpContext localContext = new BasicHttpContext();  
         HttpPost httpPost = new HttpPost(Constants.SERVER_BASE_URL+"stockcircuit/SavePortfolioDetails");
         MultipartEntityBuilder entity = MultipartEntityBuilder.create();   
         entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
         entity.addTextBody("favID", favID);
            entity.addTextBody("qty", qty);
            entity.addTextBody("buy_price", buy_price);
         HttpEntity httpentity = entity.build();
         httpPost.setEntity(httpentity);  
    try{
         HttpResponse response = httpClient.execute(httpPost, localContext);  
         BufferedReader reader = new BufferedReader(new InputStreamReader( response.getEntity().getContent(), "UTF-8"));  
         String sResponse = reader.readLine();  
        
        //System.out.println("Some error came up" +sResponse);
        
        return sResponse;
  
        } catch (Exception e) {
            System.out.println("\n\n **** SavePortfolioDetailsAsyncTask  - Error in Saving Portfolio Details" +e.getMessage());
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