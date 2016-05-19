package com.abile2.stockcircuit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.abile2.stockcircuit.model.Stock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CompareStocksVideoActivity extends Activity {

	SharedPreferences mPrefs;
	Geocoder geoCoder;
	AutoCompleteTextView stock1;
	AutoCompleteTextView stock2;
	AutoCompleteTextView stock3;

	Stock selected1;
	Stock selected2;
	Stock selected3;

	Context context = null;
	Activity activity = null;
	Button compareBtn = null;
	List<Address> list = null;
	//ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.compare_stocks_video);
		activity = this;
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		geoCoder = new Geocoder(this);
		stock1 = (AutoCompleteTextView) findViewById(R.id.stock1);
		stock2 = (AutoCompleteTextView) findViewById(R.id.stock2);
		stock3 = (AutoCompleteTextView) findViewById(R.id.stock3);


		String stocksStr = mPrefs.getString("nseStocksList","");

		ArrayList<Stock> list= getVideoEnabledStockList(stocksStr);

		ArrayAdapter<Stock> adapter = new ArrayAdapter<Stock>(this,android.R.layout.simple_list_item_1, list);
		stock1.setThreshold(1);
		stock2.setThreshold(1);
		stock3.setThreshold(1);

		stock1.setAdapter(adapter);
		stock2.setAdapter(adapter);
		stock3.setAdapter(adapter);

		compareBtn = (Button) findViewById(R.id.compareBtn);


		 //dialog = new ProgressDialog(context,AlertDialog.THEME_HOLO_LIGHT);
			//dialog.setMessage("loading please wait...");
		addTextViewListners();
		addCompareStocksBtnListner();

	}

	private void addTextViewListners() {

		stock1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				selected1 = (Stock) arg0.getAdapter().getItem(arg2);
				UtilityActivity.hideSoftKeyboard(activity);

			}
		});

		stock2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				 selected2 = (Stock) arg0.getAdapter().getItem(arg2);
				UtilityActivity.hideSoftKeyboard(activity);
			}
		});


		stock3.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				 selected3 = (Stock) arg0.getAdapter().getItem(arg2);
				UtilityActivity.hideSoftKeyboard(activity);
			}
		});

	}

	private void addCompareStocksBtnListner() {
		// TODO Auto-generated method stub
		compareBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UtilityActivity.hideSoftKeyboard(activity);
				String fullid1="";
				String fullid2="";
				String fullid3="";
				if(selected1 != null && selected2 != null ){
					fullid1 = selected1.getFullid();
					fullid2 = selected2.getFullid();
				}else{
					UtilityActivity.showMessage(getApplicationContext(),"Stock Names cannot be blank. Enter atleast two stocks.", Gravity.TOP);
					return;
				}


				if(selected3 != null){
					fullid3 = selected3.getFullid();
				}



				if(fullid1.equals(fullid2) || fullid2.equals(fullid3) ||
						fullid1.equals(fullid3)){

					UtilityActivity.showMessage(getApplicationContext(),"Please Enter different Stock Names to Compare", Gravity.TOP);
				}else {
					Intent i = new Intent(CompareStocksVideoActivity.this, GetCompareStocksVideoActivity.class);
					i.putExtra("fullid1", fullid1);
					i.putExtra("fullid2", fullid2);
					i.putExtra("fullid3", fullid3);
					startActivity(i);

				}
			}
		});
	}


	private ArrayList<Stock> getVideoEnabledStockList(String response) {

		//response = "[{'nse_id':'INFY','stockname':'Infosys Limited'},{'nse_id':'ABB','stockname':'ABB India Limited'},{'nse_id':'ABBOTINDIA','stockname':'Abbott India Limited'},{'nse_id':'ABGSHIP','stockname':'ABG Shipyard Limited'},{'nse_id':'ACC','stockname':'ACC Limited'}]";
		ArrayList<Stock> list = new ArrayList<Stock>();
		try {
			JSONArray getArray = new JSONArray(response);
			for (int i = 0; i < getArray.length(); i++) {
				JSONObject objects = getArray.getJSONObject(i);
				Iterator key = objects.keys();
				Stock stk;
				String is_video_available = objects.getString("is_video_available");

				if(is_video_available.equals("y")){
					stk = new Stock(objects.getString("stockname"),objects.getString("nseid"), objects.getString("fullid"));
					list.add(stk);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
}
