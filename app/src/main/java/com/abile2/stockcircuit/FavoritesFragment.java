package com.abile2.stockcircuit;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.abile2.stockcircuit.model.Stock;
import com.abile2.stockcircuit.util.GetLiveQuoteAsyncTask;
import com.abile2.stockcircuit.util.GetUserFavoriteAsyncTask;


public class FavoritesFragment extends AbstractFragment  {
	
    SharedPreferences mPrefs;	
	Context context;
    //protected MyApp mMyApp;
    ListView listview;
    ArrayList selectedItems;
    View rootView;
    ArrayList<Stock> favorites;
	TextView noAlerts;
	FloatingActionButton refresh_btn;


	public 	FavoritesFragment(){
    	setHasOptionsMenu(true);
	}
  @Override
//  protected void onCreate(Bundle savedInstanceState) {
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
      rootView = inflater.inflate(R.layout.favorites_fragment, container, false);
      MainActivity activity = (MainActivity)getActivity();
	  context = activity;
      setHasOptionsMenu(true);
	//mMyApp = (MyApp)this.getActivity().getApplication();
	mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
	deviceID = mPrefs.getString("deviceID","");
	regID = mPrefs.getString("regID", "");
	city = mPrefs.getString("city", "");
	
	favorites =  new ArrayList<Stock>();
	boolean isFavListDirty = mPrefs.getBoolean("isFavListDirty", true);
	if(isFavListDirty){
		favorites = getUserFavorites();
	}else{
		String favStr = mPrefs.getString("FavList", "");
		if(favStr != null && !favStr.equals(""))
			favorites = convertJsonToStockList(favStr);
	}

	  listview = (ListView ) rootView.findViewById(R.id.favoriteList);
	noAlerts = (TextView) rootView.findViewById(R.id.noAlerts);
	if(favorites.size()==  0){
		noAlerts.setVisibility(View.VISIBLE);
	}else{
	    //setup passive list

		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    ListAdapterStockFavorite  adapter2 = new ListAdapterStockFavorite(activity, favorites);
		listview.setAdapter(adapter2);
		listview.setTextFilterEnabled(true);
	    addListItemListner();
	}

	  setupFloatingMenu();
    return rootView;  	
  }
	private void setupFloatingMenu() {

		refresh_btn  = (FloatingActionButton) rootView.findViewById(R.id.refresh);
		//menu.setParentActivity(this);

		//refresh_btn.setVisibility(View.INVISIBLE);
		//menu.setBackgroundColor(Color.WHITE);;
		//final FloatingActionButton nseBtn = (FloatingActionButton) findViewById(R.id.nseBtn);
		refresh_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				refreshFavorites();
			}
		});


	}

	private void refreshFavorites() {

		SharedPreferences.Editor editor= mPrefs.edit();
		editor.putBoolean("isFavListDirty", true);
		editor.commit();

		//Fragment frag = getActiveFragment();

		FragmentTransaction fragTransaction =   getActivity().getSupportFragmentManager().beginTransaction();
		fragTransaction.detach(this);
		fragTransaction.attach(this);
		fragTransaction.commit();

	}


	public ArrayList<Stock>  getUserFavorites() {
		// TODO Auto-generated method stub
		//ArrayList<Stock> list = new ArrayList<Stock>();
		ArrayList<Stock> list = new ArrayList<Stock>();

		String str = "";
		Object[]  inParams = new Object[2];

		inParams[0] = deviceID;
		inParams[1] = regID;


		try {
			str = new GetUserFavoriteAsyncTask().execute(inParams).get();

			SharedPreferences.Editor editor= mPrefs.edit();
			editor.putString("FavList", str);
			editor.putBoolean("isFavListDirty", false);
			editor.commit();

			if(str != null && !str.equals(""))
				list = convertJsonToStockList(str);

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}
	public ArrayList<Stock> convertJsonToStockList(String str) {
		Object obj = JSONValue.parse(str);
		JSONArray array=(JSONArray)obj;
		ArrayList<Stock> alertList = new ArrayList<Stock>();
		for (int i = 0; i < array.size(); i++) {
			Stock stk = new Stock();
			JSONObject object = (JSONObject) array.get(i);
			stk.setId((String) object.get("id"));
			stk.setNseid((String) object.get("nseid"));
			stk.setFullid((String) object.get("fullid"));
			stk.setStockname((String) object.get("stockname"));
			stk.setCurrentPrice((String) object.get("currentPrice"));
			stk.setChange((String) object.get("change"));
			stk.setChangeStr((String) object.get("changeStr"));
			alertList.add(stk);
		}
		return alertList;
	}

	private void addListItemListner() {
		// TODO Auto-generated method stub
		//ListView lv = getListView();
		ListView lv =  (ListView ) rootView.findViewById(R.id.favoriteList);



        lv.setOnItemClickListener(new OnItemClickListener() {
        	
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {


            	((ListAdapterStockFavorite) parent.getAdapter()).toggleSelection(position);
 
	    		Stock stk = (Stock) parent.getItemAtPosition(position);
				String favId = stk.getId();
	    		String name  = stk.getStockname();
	    		String nseid = stk.getNseid();
	    		String fullid = stk.getFullid();
				Object object[] = new Object[1];
				object[0] = fullid;

				String quote="";
				String change="";
				try {
					HashMap<String, String> quoteParams = new GetLiveQuoteAsyncTask().execute(object).get();
					quote = quoteParams.get("l_fix");
					change = quoteParams.get("c_fix")+" ( "+quoteParams.get("cp_fix")+ "% ) ";
					if (quote != null && !(quote.isEmpty())) {
						System.out.println("quote - "+quote);
					}

				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MainActivity activity = (MainActivity)getActivity();
	    		Intent i = new Intent(activity, SetAlertActivity.class);
	    		i.putExtra("stockname",name);
	    		i.putExtra("nseid",nseid);
	    		i.putExtra("fullid",fullid);
	    		i.putExtra("price",quote);
	    		i.putExtra("change",change);
				i.putExtra("isFavorite","yes");
				i.putExtra("id",favId); //this is id from user_favorite table and not an stock id
	    		startActivity(i);
            }
          });
        
		
	}
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		/*
        inflater.inflate(R.menu.activity_main_actions, menu);
        //super.onCreateOptionsMenu(menu, inflater);
        MenuItem action_menu = menu.findItem(R.id.action_menu);
        MenuItem  action_discard = menu.findItem(R.id.action_discard);
        MenuItem action_refresh = menu.findItem(R.id.action_refresh);
        MenuItem action_help = menu.findItem(R.id.action_help);
        
	    action_menu.setVisible(false);
	    action_discard.setVisible(true);
	    action_refresh.setVisible(true);
	    action_help.setVisible(false);
        */
    }
    public void onResume() {
        super.onResume();

		boolean isFavListDirty = mPrefs.getBoolean("isFavListDirty", true);
		if(favorites.size()==  0){
			noAlerts.setVisibility(View.VISIBLE);
		}else{
			noAlerts.setVisibility(View.INVISIBLE);
		}
		if(isFavListDirty){
			favorites = getUserFavorites();
			ListAdapterStockFavorite  adapter2 = new ListAdapterStockFavorite(getActivity(), favorites);
			listview.setAdapter(adapter2);

		}else{
			String favStr = mPrefs.getString("FavList", "");
			if(favStr != null && !favStr.equals(""))
				favorites = convertJsonToStockList(favStr);
		}

    }
    public void onPause() {
        clearReferences();
        super.onPause();
    }
    public void onDestroy() {        
        clearReferences();
        super.onDestroy();
        //UtilMobileAdvt.getInstance().showInterstitial();
    }

    private void clearReferences(){
    }

    
}
