package com.abile2.stockcircuit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
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

import com.abile2.stockcircuit.model.Stock;

public class UtilityActivity {
	static SharedPreferences mPrefs;

	public static String  getStocksListForExchange(String exchange, String deviceID) {
		return getStocksListForExchange(exchange,deviceID,"n");
	}
	public static String  getStocksListForExchange(String exchange,String deviceID, String isNewUser) {
		// TODO Auto-generated method stub
		//ArrayList<Stock> list = new ArrayList<Stock>();
		String str = "";
		try {
			long startTime = System.currentTimeMillis();
			//String is_video_available = "n";
			//String exchange_flag = "y";
			//exchange = "NSE";
			Object object[] = new Object[3];
			//object[0] = is_video_available;
			//object[1] = exchange_flag;
			object[0] = exchange;
			object[1] = deviceID;
			object[2] = isNewUser;

			str  = new GetAllStockNames().execute(object).get();
			long endTime = System.currentTimeMillis();
			System.out.println("\n\n Time taken to get the Stocks List - "+(endTime-startTime));
			return str;
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;

	}




	public static void hideSoftKeyboard(Activity activity) {
	    //InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    //inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	public static void hideSoftKeyboard(View v) {
	InputMethodManager inputManager = (InputMethodManager)
            v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 

		inputManager.hideSoftInputFromWindow(v.getWindowToken(),0);
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
			int endInd = sResponse.lastIndexOf("]")  ;
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

	public static ArrayList<Stock> getExchangeStocks(String response, String exchange) {

		//response = "[{'nse_id':'INFY','stockname':'Infosys Limited'},{'nse_id':'ABB','stockname':'ABB India Limited'},{'nse_id':'ABBOTINDIA','stockname':'Abbott India Limited'},{'nse_id':'ABGSHIP','stockname':'ABG Shipyard Limited'},{'nse_id':'ACC','stockname':'ACC Limited'}]";
		ArrayList<Stock> list = new ArrayList<Stock>();
		try {
			JSONArray getArray = new JSONArray(response);
			for (int i = 0; i < getArray.length(); i++) {
				JSONObject objects = getArray.getJSONObject(i);
				Iterator key = objects.keys();
				Stock stk;
				String exchange2 = objects.getString("exchange");

				if(exchange2.equals(exchange)){
					stk = new Stock(objects.getString("stockname"),objects.getString("nseid"), objects.getString("fullid"));
					stk.setIndustry_vertical(objects.getString("industry_vertical"));
					stk.setIndustry_sub_vertical(objects.getString("industry_sub_vertical"));
					list.add(stk);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ArrayList<Stock> convertJsonIntoStockList(String response) {

		ArrayList<Stock> list = new ArrayList<Stock>();
		try {
			JSONArray getArray = new JSONArray(response);
			for (int i = 0; i < getArray.length(); i++) {
				JSONObject objects = getArray.getJSONObject(i);
				Iterator key = objects.keys();

				Stock stk = new Stock(objects.getString("stockname"),objects.getString("nseid"), objects.getString("fullid"));
				//stk.setIndustry_vertical(objects.getString("industry_vertical"));
				//stk.setIndustry_sub_vertical(objects.getString("industry_sub_vertical"));
				list.add(stk);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}


}
