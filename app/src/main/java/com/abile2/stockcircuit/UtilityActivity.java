package com.abile2.stockcircuit;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

public class UtilityActivity {
	static SharedPreferences mPrefs;

	/*
	public static String  refreshNseStocksList() {
		// TODO Auto-generated method stub
		//ArrayList<Stock> list = new ArrayList<Stock>();
		String stocksStr = "";
		Date currDate = new Date(System.currentTimeMillis());
		try {
			String is_video_available = "n";
			Object object[] = new Object[1];
			object[0] = is_video_available;

			stocksStr =  new GetAllStockNames().execute(object).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SharedPreferences.Editor mpref = mPrefs.edit();
		mpref.putString("nseStocksList", stocksStr);
		mpref.putLong("nseStocksListLastFetch", currDate.getTime());
		mpref.commit();

		return stocksStr;

	}
	*/
	public static void hideSoftKeyboard(Activity activity) {
	    //InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    //inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	public static void hideSoftKeyboard(View v) {
	InputMethodManager inputManager = (InputMethodManager)
            v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 

		inputManager.hideSoftInputFromWindow(v.getWindowToken(),
               InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void showMessage(String msg, Context ctx) {
		// TODO Auto-generated method stub
		
	   	Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
	    //toast.getView().setBackgroundColor(Color.parseColor("#FFD800"));
	    toast.getView().setBackgroundColor(Color.parseColor("#404040"));
	    toast.setGravity(Gravity.CENTER, 20, 120);
	    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
	    v.setTypeface(null, Typeface.NORMAL);
	    v.setTextColor(Color.WHITE);
	    //v.setTextColor(Color.BLACK);
	    //toast.setDuration(100000);
	    toast.show();

		
	}


	public static void showMessage(Context ctx, String msg, int position) {
		// TODO Auto-generated method stub
		
	   	Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
	    //toast.getView().setBackgroundColor(Color.parseColor("#FFD800"));
	    toast.getView().setBackgroundColor(Color.parseColor("#404040"));
	    toast.setGravity(position, 20, 120);
	    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
	    v.setTypeface(null, Typeface.NORMAL);
	    v.setTextColor(Color.WHITE);
	    //v.setTextColor(Color.BLACK);
	    //toast.setDuration(100000);
	    toast.show();
		
	}
	public static void showShortMessage(Context ctx, String msg, int position) {
		// TODO Auto-generated method stub
		
	   	Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
	    //toast.getView().setBackgroundColor(Color.parseColor("#FFD800"));
	    toast.getView().setBackgroundColor(Color.parseColor("#404040"));
	    toast.setGravity(position, 20, 120);
	    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
	    v.setTypeface(null, Typeface.NORMAL);
	    v.setTextColor(Color.WHITE);
	    //v.setTextColor(Color.BLACK);
	    //toast.setDuration(100000);
	    toast.show();
		
	}
	public static void showMessageYellow(Context ctx, String msg, int position) {
		// TODO Auto-generated method stub
		
	   	Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
	    //toast.getView().setBackgroundColor(Color.parseColor("#FFD800"));
	    toast.getView().setBackgroundColor(Color.parseColor("#FFD800"));
	    toast.setGravity(position, 20, 120);
	    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
	    v.setTypeface(Typeface.SANS_SERIF);
	    v.setTextColor(Color.parseColor("#404040"));
	    //v.setTextColor(Color.BLACK);
	    //toast.setDuration(100000);
	    toast.show();
		
	}
	
	public static HashMap<String, String> getMapforJsonString(String sResponse) {
		// TODO Auto-generated method stub
		
		//JSONParser parser = new JSONParser();
		
		//LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		HashMap<String, String> map = new HashMap<String, String> () ;
		try {
			JSONObject jsonObject = new JSONObject(sResponse);
			Iterator keys =  jsonObject.keys();

			while( keys.hasNext() ) {
			    String key = (String)keys.next();
			    map.put(key, jsonObject.get(key).toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		  map.put("Error", "Somthing went wrong at server.");
		}
		 return map; 		

	}
	
	public static HashMap<String, String> getMapforJsonStringAfterStrip(String sResponse) {
		// TODO Auto-generated method stub
		
		//JSONParser parser = new JSONParser();
		
		//LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		HashMap<String, String> map = new HashMap<String, String> () ;
		try {
			int beginInd = sResponse.indexOf("[")+1;
			int endInd = sResponse.indexOf("]")  ;
			sResponse = sResponse.substring(beginInd, endInd);
			
			JSONObject jsonObject = new JSONObject(sResponse);
			Iterator keys =  jsonObject.keys();

			while( keys.hasNext() ) {
			    String key = (String)keys.next();
			    map.put(key, jsonObject.get(key).toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		  map.put("Error", "Somthing went wrong at server.");
		}
		 return map; 		

	}
	
}
