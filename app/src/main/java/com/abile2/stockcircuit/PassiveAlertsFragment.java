package com.abile2.stockcircuit;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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

import com.abile2.stockcircuit.model.StockAlerts;

public class PassiveAlertsFragment extends AbstractFragment  {
	
	Context context;
    //protected MyApp mMyApp;
    ListView listview;
    ArrayList selectedItems;
    View rootView;
	SharedPreferences mPrefs;
	ArrayList<StockAlerts> passiveAlerts;

	public PassiveAlertsFragment(){
    	setHasOptionsMenu(true);
    }
    
  @Override
//  protected void onCreate(Bundle savedInstanceState) {
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {

      rootView = inflater.inflate(R.layout.passive_alert_fragment, container, false);
	  MainActivity activity = (MainActivity) getActivity();
	  context = activity;
	  mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	  deviceID = mPrefs.getString("deviceID","");
	  regID = mPrefs.getString("regID", "");

      //super.onCreate(savedInstanceState);
    //setContentView(R.recommended_videos.active_alert_fragment);
      setHasOptionsMenu(true);

	//mMyApp = (MyApp)this.getActivity().getApplication();
    
	final TextView noAlerts = (TextView) rootView.findViewById(R.id.noAlerts);
    //listview = getListView();
//	listview = (ListView ) rootView.findViewById(R.id.list);
//	listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
	
    //ArrayList<StockAlerts> alerts = activity.getAlerts(false);
    passiveAlerts = getPassiveAlerts(true);
    ListView passiveList = (ListView ) rootView.findViewById(R.id.passiveList);
    passiveList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    
    //passiveList.setBackgroundColor(0x55ffffff);
    
	if(passiveAlerts.size()==  0){
		noAlerts.setVisibility(View.VISIBLE);
		passiveList.setVisibility(View.GONE);
	}else{
	    ListAdapterStockAlerts  adapter2 = new ListAdapterStockAlerts (activity, passiveAlerts);
	    passiveList.setAdapter(adapter2);
	    passiveList.setTextFilterEnabled(true);
	}
    return rootView;  	
  }
  

	private void addListItemListner() {
		// TODO Auto-generated method stub
		// ListView lv = getListView();
		ListView lv = (ListView) rootView.findViewById(R.id.passiveList);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// View vi = (View)parent.getChildAt(position);
				int _id = ((Integer) view.getTag(R.id.TAG_PC_ID)).intValue();
				((ListAdapterStockAlerts) parent.getAdapter())
				.toggleSelection(position);

			}
		});

	}

	public ArrayList<StockAlerts> getPassiveAlerts(boolean refresh) {
// TODO Auto-generated method stub
		ArrayList<StockAlerts> allAlerts =  new ArrayList<StockAlerts>();
		ArrayList<StockAlerts> passives =  new ArrayList<StockAlerts>();
		if(refresh){
			allAlerts = getAlerts(deviceID, regID,refresh);
			for(StockAlerts sa: allAlerts)
			{
				if(sa.getHas_hit().equals("y"))
				{

					passives.add(sa);
				}

			}
			passiveAlerts = passives;
		}



		//allAlerts = getAlerts(deviceID, regID,refresh);

		return passiveAlerts;

	}
	/*
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_actions, menu);
        //super.onCreateOptionsMenu(menu, inflater);
        MenuItem action_menu = menu.findItem(R.id.action_menu);
        MenuItem  action_discard = menu.findItem(R.id.action_discard);
        MenuItem action_refresh = menu.findItem(R.id.action_refresh);
        MenuItem action_help = menu.findItem(R.id.action_help);
        
	    action_menu.setVisible(false);
	    action_discard.setVisible(false);
	    action_refresh.setVisible(false);
	    action_help.setVisible(false);
        
    }
	*/
	   public void onResume() {
	        super.onResume();
	    }
	    public void onPause() {
	        clearReferences();
	        super.onPause();
	    }
	    public void onDestroy() {        
	        clearReferences();
	        super.onDestroy();
	    }

	    private void clearReferences(){
	    }
}
