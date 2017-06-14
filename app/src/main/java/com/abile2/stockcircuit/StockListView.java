package com.abile2.stockcircuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.abile2.stockcircuit.model.Stock;
import com.abile2.stockcircuit.util.GetLiveQuoteAsyncTask;
import com.abile2.stockcircuit.util.GetStocksListForForecastAsynTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class StockListView extends Activity {

	private ListView lv;
	EditText inputSearch;
	StockListAdapter adapter;
	Context context;
	 SharedPreferences mPrefs;
	String is_video_list;
	String comingFrom;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ListView listview = getListView();
		setContentView(R.layout.nse_stock_list);
		context =  this;
		// final ListView stkList = (ListView)
		// findViewById(R.id.nse_stock_list);
		lv = (ListView) findViewById(R.id.stockListView);
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		LinearLayout layout= (LinearLayout) findViewById(R.id.linearLayout1);
		
		Intent secondInt = getIntent();
		//String is_world_indices = secondInt.getStringExtra("is_world_indices");
		is_video_list = secondInt.getStringExtra("is_video_list" );
		String exchange = secondInt.getStringExtra("exchange");
		comingFrom = secondInt.getStringExtra("parent");


		if(is_video_list == null)
			is_video_list = "n";

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String stocksStr = mPrefs.getString("nseStocksList","");

		
		ArrayList<Stock> list=null;
		if(comingFrom.equals("dashboard")){
			if(is_video_list.equals("y")){
				list = getVideoEnabledStockList(stocksStr, exchange);
			}else if(exchange != null && ! exchange.equals("")) {
				if(exchange.equals("NSE") || exchange.equals("NASDAQ")){
					list = getExchangeStocks(stocksStr, exchange);
				}else if(exchange.equals("BOM") ){
					String bomStocksStr = mPrefs.getString("bomStocksList","");
					list = getExchangeStocks(bomStocksStr, exchange);
				}
			}else{
				list = getWorldIndicesObjects(stocksStr );
			}

		}else if(comingFrom.equals("stock_forecast")){

			layout.setVisibility(View.VISIBLE);
			list = getStocksListForForecast();
		}else{
			System.out.println("No Parent , Its a problem...");
		}

			
			Stock[] stockArray = new Stock[list.size()];
			stockArray = list.toArray(stockArray);
			adapter = new StockListAdapter(this, R.layout.nse_stock_list_item,
					stockArray);

			lv.setAdapter(adapter);


		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				adapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		setListViewListner();
		

	}

	private void setListViewListner() {
		// TODO Auto-generated method stub
		
		lv = (ListView) findViewById(R.id.stockListView);
	    lv.setOnItemClickListener(new OnItemClickListener() {
	    	
	    	public void onItemClick(AdapterView<?> parent, View view,
	    			int position, long id) {

				Stock stk = (Stock) parent.getItemAtPosition(position);
				if(comingFrom.equals("dashboard")){
					if (is_video_list.equals("y")) {
						invokeUserRequestedVideo(stk);

						//reset the my_video_refresh_flag so that new request can be sent to server to get this new video..
						SharedPreferences.Editor editor = mPrefs.edit();
						//editor.putString("my_video_list", str);
						editor.putBoolean("my_video_refresh_flag", true);
						editor.commit();

					} else {
						invokeSetAlertActivity(stk);

					}
				}else if(comingFrom.equals("stock_forecast")){
					Intent i = new Intent(context, StockForecastActivity.class);
					i.putExtra("fullid", stk.getFullid());
					i.putExtra("name", stk.getStockname());
					startActivity(i);
				}
			}
	    	
	    });

	}
 	private void invokeUserRequestedVideo(Stock stk){


		Intent i = new Intent(StockListView.this, GetUserRequestedVideoActivity.class);

		String name = stk.getStockname();
		String nseid = stk.getNseid();
		String fullid = stk.getFullid();
		i.putExtra("stockname", name);
		i.putExtra("nseid", nseid);
		i.putExtra("fullid", fullid);
		//UtilityActivity.showMessage(context, "Starting SetAlertActivity", Gravity.CENTER);
		startActivity(i);

	}

	private ArrayList<Stock> getStocksListForForecast(){

		Object object[] = new Object[1];
		//object[0] = mobile;
		object[0] = "";
//		object[1] = deviceID;
//		object[2] = regID;
		String sResponse="";
		ArrayList<Stock> list = new ArrayList<Stock>();
		try {
			sResponse = new GetStocksListForForecastAsynTask(getApplicationContext()).execute(object).get();
			System.out.println("\n\n*** sResponse - " + sResponse);

			list = UtilityActivity.convertJsonIntoStockList(sResponse);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}
	private void invokeSetAlertActivity(Stock stk){

		String name = stk.getStockname();
		String nseid = stk.getNseid();
		String fullid = stk.getFullid();
		Object object[] = new Object[1];
		object[0] = fullid;

		String quote = "";
		String change = "";
		try {
			HashMap<String, String> quoteParams = new GetLiveQuoteAsyncTask().execute(object).get();
			quote = quoteParams.get("l_fix");
			change = quoteParams.get("c_fix") + " ( " + quoteParams.get("cp_fix") + "% ) ";
			if (quote != null && !(quote.isEmpty())) {
				System.out.println("quote - " + quote);
			}

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		Intent i = new Intent(StockListView.this, SetAlertActivity.class);
		//UtilityActivity.showMessage(context, "After Calling GetNewsFeedAsyncTask"+change, Gravity.CENTER);
		i.putExtra("stockname", name);
		i.putExtra("nseid", nseid);
		i.putExtra("fullid", fullid);
		i.putExtra("price", quote);
		i.putExtra("change", change);
		//UtilityActivity.showMessage(context, "Starting SetAlertActivity", Gravity.CENTER);
		startActivity(i);

	}

	private ArrayList<Stock> getExchangeStocks(String response, String exchange) {

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
					list.add(stk);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	private ArrayList<Stock> getWorldIndicesObjects(String response) {

		//response = "[{'nse_id':'INFY','stockname':'Infosys Limited'},{'nse_id':'ABB','stockname':'ABB India Limited'},{'nse_id':'ABBOTINDIA','stockname':'Abbott India Limited'},{'nse_id':'ABGSHIP','stockname':'ABG Shipyard Limited'},{'nse_id':'ACC','stockname':'ACC Limited'}]";	
		ArrayList<Stock> list = new ArrayList<Stock>();
		try {
			JSONArray getArray = new JSONArray(response);
			for (int i = 0; i < getArray.length(); i++) {
				JSONObject objects = getArray.getJSONObject(i);
				Iterator key = objects.keys();
				Stock stk;
					String is_world_indices2 = objects.getString("is_world_indices");

						if(is_world_indices2.equals("y")){
							stk = new Stock(objects.getString("stockname"),objects.getString("nseid"), objects.getString("fullid"));
							list.add(stk);
						}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}


	private ArrayList<Stock> getVideoEnabledStockList(String response, String exchange) {

		//response = "[{'nse_id':'INFY','stockname':'Infosys Limited'},{'nse_id':'ABB','stockname':'ABB India Limited'},{'nse_id':'ABBOTINDIA','stockname':'Abbott India Limited'},{'nse_id':'ABGSHIP','stockname':'ABG Shipyard Limited'},{'nse_id':'ACC','stockname':'ACC Limited'}]";
		ArrayList<Stock> list = new ArrayList<Stock>();
		try {
			JSONArray getArray = new JSONArray(response);
			for (int i = 0; i < getArray.length(); i++) {
				JSONObject objects = getArray.getJSONObject(i);
				Iterator key = objects.keys();
				Stock stk;
				String is_video_available = objects.getString("is_video_available");
				String exchange2 = objects.getString("exchange");

				if(is_video_available.equals("y")&& exchange2.equals(exchange)){
					stk = new Stock(objects.getString("stockname"),objects.getString("nseid"), objects.getString("fullid"));
					list.add(stk);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}


    protected void onResume() {
        super.onResume();	
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        
    }

}
