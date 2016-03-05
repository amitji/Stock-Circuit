package com.abile2.stockcircuit;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.abile2.stockcircuit.model.Stock;
import com.abile2.stockcircuit.util.GetUserFavoriteAsyncTask;


public class FavoritesFragment extends AbstractFragment  {
	
    SharedPreferences mPrefs;	
	Context context;
    String deviceID;
    String regID;
    String city;
    protected MyApp mMyApp;	
    ListView listview;
    ArrayList selectedItems;
    View rootView;
    ArrayList<Stock> favorites;

    
    public 	FavoritesFragment(){
    	setHasOptionsMenu(true);
	}
  @Override
//  protected void onCreate(Bundle savedInstanceState) {
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
      rootView = inflater.inflate(R.layout.favorites_fragment, container, false);
      MainActivity activity = (MainActivity)getActivity();
      setHasOptionsMenu(true);
	mMyApp = (MyApp)this.getActivity().getApplication();
	mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
	deviceID = mPrefs.getString("deviceID","");
	regID = mPrefs.getString("regID", "");
	city = mPrefs.getString("city", "");
	
	favorites =  new ArrayList<Stock>();
	boolean isFavListDirty = mPrefs.getBoolean("isFavListDirty", true);
	if(isFavListDirty){
		favorites = activity.getUserFavorites();
	}else{
		String favStr = mPrefs.getString("FavList", "");
		if(favStr != null && !favStr.equals(""))
			favorites = activity.convertJsonToStockList(favStr);
	}

	final TextView noAlerts = (TextView) rootView.findViewById(R.id.noAlerts);
	if(favorites.size()==  0){
		noAlerts.setVisibility(View.VISIBLE);
	}else{
	    //setup passive list
	    ListView passiveList = (ListView ) rootView.findViewById(R.id.favoriteList);
	    passiveList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    ListAdapterStockFavorite  adapter2 = new ListAdapterStockFavorite(activity, favorites);
	    passiveList.setAdapter(adapter2);
	    passiveList.setTextFilterEnabled(true);
	    addListItemListner();
	}
    return rootView;  	
  }
  





	private void addListItemListner() {
		// TODO Auto-generated method stub
		//ListView lv = getListView();
		ListView lv =  (ListView ) rootView.findViewById(R.id.favoriteList);
		
        lv.setOnItemClickListener(new OnItemClickListener() {
        	
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                
            	//View vi = (View)parent.getChildAt(position);
            	//Stock stock = (Stock)parent.getAdapter().getItem(position);
            	//int _id = ((Integer)view.getTag(R.id.TAG_PC_ID)).intValue();
            	((ListAdapterStockFavorite) parent.getAdapter()).toggleSelection(position);
 
	    		Stock stk = (Stock) parent.getItemAtPosition(position);
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
	    		startActivity(i);
            }
          });
        
		
	}
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
        
    }
    public void onResume() {
        super.onResume();

//        MainActivity activity = (MainActivity)getActivity();
//        mPrefs = PreferenceManager.getDefaultSharedPreferences(activity.context);
//    	boolean refresh = mPrefs.getBoolean("refresh", false);
//		ListAdapterStockAlerts adapter = new ListAdapterStockAlerts (activity, activity.getActiveAlerts(false));
//		listview.setAdapter(adapter);
//		SharedPreferences.Editor editor= mPrefs.edit();
//		editor.putBoolean("refresh", false);
//		editor.commit();

       
        //this.onCreate(null);
        //restartParentActivity();
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
        Activity currActivity = mMyApp.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
            mMyApp.setCurrentActivity(null);
    }

    
}
