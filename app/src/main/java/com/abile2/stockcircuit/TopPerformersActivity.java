package com.abile2.stockcircuit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.abile2.stockcircuit.util.GetStocksForIndustryVerticalsAsynTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;


public class TopPerformersActivity extends Activity {

	SharedPreferences mPrefs;
	Geocoder geoCoder;
	AutoCompleteTextView sector;
	ListView listViewSubSector;
	ArrayList<String> sectorList;
	ArrayList<String> subSectorList;
	String industryVerticalsStr;
	String[] subSectorArray;

	String deviceID;
	String regID;
	String mobile;

	String  selected1;


	Context context = null;
	Activity activity = null;
	Button luckyBtn = null;
	List<Address> list = null;
	//ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.top_performers);
		activity = this;
		context = this;

		Intent secondInt = getIntent();
		//String exchange = secondInt.getStringExtra("exchange");


		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		deviceID = mPrefs.getString("deviceID","");
		regID = mPrefs.getString("regID", "");
		mobile = mPrefs.getString("mobile", "");

		geoCoder = new Geocoder(this);
		sector = (AutoCompleteTextView) findViewById(R.id.sector);


		//String stocksStr = mPrefs.getString("nseStocksList","");
		industryVerticalsStr = mPrefs.getString("industry_verticals","");

		sectorList = getSectorList(industryVerticalsStr);
		//ArrayAdapter<Stock> adapter = new ArrayAdapter<Stock>(this,android.R.layout.simple_list_item_1, list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item_layout, R.id.listItemtext, sectorList);
		sector.setThreshold(0);
		sector.setAdapter(adapter);

		subSectorList = new ArrayList<String>();//getSubSectorList(sectorStr);

		listViewSubSector = (ListView) findViewById(R.id.listViewSubSector);
		listViewSubSector.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		subSectorArray = subSectorList.toArray(new String[subSectorList.size()]);

		ArrayAdapter<String> subSectorAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, subSectorArray);

		listViewSubSector.setAdapter(subSectorAdapter);


		luckyBtn = (Button) findViewById(R.id.luckyBtn);


		 //dialog = new ProgressDialog(context,AlertDialog.THEME_HOLO_LIGHT);
			//dialog.setMessage("loading please wait...");
		addTextViewListners();
		addMultiSelectsBtnListner();

	}

	private void addTextViewListners() {

		sector.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {

				selected1 = (String) arg0.getAdapter().getItem(arg2);
				UtilityActivity.hideSoftKeyboard(activity);

				subSectorList = getSubSectorList(industryVerticalsStr, selected1);
				subSectorArray = subSectorList.toArray(new String[subSectorList.size()]);
				ArrayAdapter<String> subSectorAdapter = new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_multiple_choice, subSectorArray);

				listViewSubSector.setAdapter(subSectorAdapter);

			}
		});

		sector.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				sector.showDropDown();
				return false;
			}
		});
	}



	private void addMultiSelectsBtnListner() {
		// TODO Auto-generated method stub
		luckyBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				int len = listViewSubSector.getCount();
				SparseBooleanArray selItems = listViewSubSector.getCheckedItemPositions();
//				for (int i = 0; i < len; i++)
//					if (selItems.get(i)) {
//						String item = subSectorArray[i];
//					}


				boolean noneSelected = true;
//				listViewSubSector = (ListView) findViewById(R.id.listViewSubSector);
//				boolean[] selItems = ((ListAdapterVideos) listViewSubSector.getAdapter()).getSelectedItems(); //mAdapter.getSelectedItems();
				StringBuilder subSecNames = new StringBuilder();

				for(int j=0;j < len; j++ )
				{
					boolean isSelected =  selItems.get(j);
					if(isSelected)
					{
						noneSelected = false;
						String subSecName = (String)listViewSubSector.getAdapter().getItem(j);
						subSecNames.append(subSecName);
						subSecNames.append(",");
					}
				}
				if(noneSelected){
					UtilityActivity.showMessage(context, "Are you Crazy ! Select a Sector on top", Gravity.CENTER);
					return ;
				}

				//strip ','
				String subSecNamesStr = subSecNames.substring(0, subSecNames.length()-1);

				Object object[] = new Object[4];
				object[0] = subSecNamesStr;
				object[1] = mobile;
				object[2] = deviceID;
				object[3] = regID;

				try {
					String sResponse = new GetStocksForIndustryVerticalsAsynTask(getApplicationContext()).execute(object).get();
					System.out.println("\n\n*** sResponse - "+sResponse);
					SharedPreferences.Editor editor= mPrefs.edit();
					editor.putString("stocks_ratings", sResponse);
					editor.commit();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

				Intent tpIntent = new Intent(context, StocksRatingActivity.class);
				startActivity(tpIntent);









			}
		});
	}


	private ArrayList<String> getSectorList(String response) {

		//response = "[{'nse_id':'INFY','stockname':'Infosys Limited'},{'nse_id':'ABB','stockname':'ABB India Limited'},{'nse_id':'ABBOTINDIA','stockname':'Abbott India Limited'},{'nse_id':'ABGSHIP','stockname':'ABG Shipyard Limited'},{'nse_id':'ACC','stockname':'ACC Limited'}]";

		TreeSet<String> hashSet = new TreeSet<String>();
		try {
			JSONArray getArray = new JSONArray(response);
			for (int i = 0; i < getArray.length(); i++) {
				JSONObject objects = getArray.getJSONObject(i);
				//Iterator key = objects.keys();
				//Stock stk;
				String industry_vertical = objects.getString("industry_vertical");
				hashSet.add(industry_vertical);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ArrayList<String> list = new ArrayList<String>(hashSet);
		return list;
	}

	private ArrayList<String> getSubSectorList(String industryVerticalsStr, String selected1) {

		TreeSet<String> hashSet = new TreeSet<String>();
		try {
			JSONArray getArray = new JSONArray(industryVerticalsStr);
			for (int i = 0; i < getArray.length(); i++) {
				JSONObject objects = getArray.getJSONObject(i);
				//Iterator key = objects.keys();
				//Stock stk;
				String industry_sub_vertical = objects.getString("industry_sub_vertical");
				String industry_vertical = objects.getString("industry_vertical");
				if(industry_vertical.equals(selected1))
					hashSet.add(industry_sub_vertical);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ArrayList<String> list = new ArrayList<String>(hashSet);
		return list;

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
}
