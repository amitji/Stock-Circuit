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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
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
		
		Intent secondInt = getIntent();
		String is_world_indices = secondInt.getStringExtra("is_world_indices");
		is_video_list = secondInt.getStringExtra("is_video_list" );
		if(is_video_list == null)
			is_video_list = "n";

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String stocksStr = mPrefs.getString("nseStocksList","");

		
		//String response;
		//try {
			//response = new GetAllStockNames(this).execute().get();
			//response = "";
		ArrayList<Stock> list=null;
		if(is_video_list.equals("y"))
			list = getVideoAvailableStockNames();
		else if(is_world_indices.equals("n"))
				list = getStocksObjects(stocksStr);
			else
				list = getWorldIndicesObjects(stocksStr );
			
			Stock[] stockArray = new Stock[list.size()];
			stockArray = list.toArray(stockArray);
			adapter = new StockListAdapter(this, R.layout.nse_stock_list_item,
					stockArray);

			lv.setAdapter(adapter);

//		} catch (InterruptedException | ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

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
				if (is_video_list.equals("y")) {
					invokeUserRequestedVideo(stk);
				} else {
					invokeSetAlertActivity(stk);

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

	private ArrayList<Stock> getStocksObjects(String response) {

		//response = "[{'nse_id':'INFY','stockname':'Infosys Limited'},{'nse_id':'ABB','stockname':'ABB India Limited'},{'nse_id':'ABBOTINDIA','stockname':'Abbott India Limited'},{'nse_id':'ABGSHIP','stockname':'ABG Shipyard Limited'},{'nse_id':'ACC','stockname':'ACC Limited'}]";	
		ArrayList<Stock> list = new ArrayList<Stock>();
		try {
			JSONArray getArray = new JSONArray(response);
			for (int i = 0; i < getArray.length(); i++) {
				JSONObject objects = getArray.getJSONObject(i);
				Iterator key = objects.keys();
				Stock stk;
				//while (key.hasNext()) {
					//String k1 = key.next().toString();
					// System.out.println("Key : " + k1 + ", value : " +
					// objects.getString(k1));
					//String k2 = key.next().toString();
					// System.out.println("Key : " + k2 + ", value : " +
					// objects.getString(k2));
					//stk = new Stock(objects.getString(k1),objects.getString(k2), null);
					//String is_world_indices2 = objects.getString("is_world_indices");
							stk = new Stock(objects.getString("stockname"),objects.getString("nseid"), objects.getString("fullid"));
							list.add(stk);
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
				//while (key.hasNext()) {
					//String k1 = key.next().toString();
					// System.out.println("Key : " + k1 + ", value : " +
					// objects.getString(k1));
					//String k2 = key.next().toString();
					// System.out.println("Key : " + k2 + ", value : " +
					// objects.getString(k2));
					//stk = new Stock(objects.getString(k1),objects.getString(k2), null);
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
	private ArrayList<Stock> getVideoAvailableStockNames() {
			String stocksStr="";
		try {
			String is_video_available = "y";
			Object object[] = new Object[1];
			object[0] = is_video_available;

			stocksStr =  new GetAllStockNames().execute(object).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Stock> list = new ArrayList<Stock>();
		try {
			JSONArray getArray = new JSONArray(stocksStr);
			for (int i = 0; i < getArray.length(); i++) {
				JSONObject object = getArray.getJSONObject(i);
				//Iterator key = object.keys();
				Stock stk;
				if (object.has("is_video_available")) {
					String is_video_available = object.getString("is_video_available");

					if (is_video_available.equals("y")) {
						stk = new Stock(object.getString("stockname"), object.getString("nseid"), object.getString("fullid"));
						list.add(stk);
					}
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
