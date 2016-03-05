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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abile2.stockcircuit.model.StockAlerts;

public class ActiveAlertsFragment extends AbstractFragment  {
	
	Context context;
    protected MyApp mMyApp;	
    ListView listview;
    ArrayList selectedItems;
    View rootView;
    SharedPreferences mPrefs;	
    ListAdapterStockAlerts  mAdapter;

    public ActiveAlertsFragment(){
    	setHasOptionsMenu(true);
    }
  @Override
//  protected void onCreate(Bundle savedInstanceState) {
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {

    rootView = inflater.inflate(R.layout.active_alert_fragment, container, false);
    MainActivity activity = (MainActivity)getActivity();
	mMyApp = (MyApp)this.getActivity().getApplication();
	setHasOptionsMenu(true);
	mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
	boolean refresh = mPrefs.getBoolean("refresh", false);


	final TextView noAlerts = (TextView) rootView.findViewById(R.id.noAlerts);
    //listview = getListView();
	listview = (ListView ) rootView.findViewById(R.id.activeList);
	listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	
    //ArrayList<StockAlerts> alerts = activity.getAlerts(refresh);
    ArrayList<StockAlerts> activeAlerts = activity.getActiveAlerts(refresh);

    //reset teh refresh flag
//	SharedPreferences.Editor editor= mPrefs.edit();
//	editor.putBoolean("refresh", false);
//	editor.commit();

    
	if(activeAlerts.size()==  0){
		noAlerts.setVisibility(View.VISIBLE);
		listview.setVisibility(View.GONE);
	}else{
		
		//setup Active alerts
		mAdapter = new ListAdapterStockAlerts (activity, activeAlerts);
	    listview.setAdapter(mAdapter);
	    listview.setTextFilterEnabled(true);
	    selectedItems = new ArrayList(activeAlerts.size());
	    addListItemListner();
	}
	

    return rootView;  
  }	
 

	private void addListItemListner() {
		// TODO Auto-generated method stub
		//ListView lv = getListView();
		ListView lv =  (ListView ) rootView.findViewById(R.id.activeList);

	       lv.setOnItemLongClickListener(new OnItemLongClickListener(){ 
	            @Override 
	            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
	           { 
	                //Toast.makeText(view.getContext(), "LongClick", Toast.LENGTH_LONG).show();
	            	int _id = ((Integer)view.getTag(R.id.TAG_PC_ID)).intValue();
	            	((ListAdapterStockAlerts) listview.getAdapter()).toggleSelection(position);              

	            	return false;
	           } 
	      }); 
	       
        lv.setOnItemClickListener(new OnItemClickListener() {
        	
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                
            	int _id = ((Integer)view.getTag(R.id.TAG_PC_ID)).intValue();
            	((ListAdapterStockAlerts) listview.getAdapter()).toggleSelection(position);              
            }
            
          });
        
 
		
	}
	
//	public void deleteAlerts(){
//	
//		boolean noneSelected = true;
//
//		ListView listview = (ListView ) rootView.findViewById(R.id.list);
//    	boolean[] selItems = ((ListAdapterStockAlerts) listview.getAdapter()).getSelectedItems(); //mAdapter.getSelectedItems();
//    	StringBuilder commaSepAlertIds = new StringBuilder();
//    	
//    	for(int j=0;j < selItems.length; j++ )
//    	{
//    		
//        	boolean isSelected =  selItems[j];
//        	if(isSelected)
//        	{
//        		//((ListAdapterStockAlerts) listview.getAdapter()).deleteItemAt(j);
//        		noneSelected = false;
//            	View vi = (View)listview.getChildAt(j);
//            	int id = ((Integer)vi.getTag(R.id.TAG_PC_ID)).intValue();
//            	commaSepAlertIds.append(id);
//				commaSepAlertIds.append(",");	            	
//        	}
//        }
//		if(noneSelected){
//			UtilityActivity.showMessage(context, "You didn't Select any alerts to delete.",Gravity.CENTER);
//			//return false;
//		}
//    	
//    	//strip ','
//    	String alertIds = commaSepAlertIds.substring(0, commaSepAlertIds.length()-1);
//		Object params[] = new Object[1];
//		params[0] = alertIds;
//		try {
//			 String sResponse = new DeleteUserAlertsAsyncTask().execute(params).get();
//			 //Iterator<String>  promoPositionItrator= selecetedPromos.keySet().iterator();
//			 if(sResponse!=null && !sResponse.trim().equals("")){
//
//				 UtilityActivity.showMessage(context, sResponse, Gravity.CENTER);
//			
//			 }
//		 }catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				e.printStackTrace();
//			}
//
//		mAdapter.notifyDataSetChanged();
//	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//return super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_main_actions, menu);
	    
	    MenuItem item2 = menu.findItem(R.id.action_discard);
	    MenuItem item3 = menu.findItem(R.id.action_menu);
	    item2.setVisible(true);
	    item3.setVisible(true);
	    return true;

	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
    	
        switch (item.getItemId()) {
        case R.id.action_discard:
        	boolean noneSelected = true;
        	boolean[] selItems = ((ListAdapterStockAlerts) listview.getAdapter()).getSelectedItems();
        	StringBuilder commaSepAlertIds = new StringBuilder();
        	
        	for(int j=0;j < selItems.length; j++ )
        	{
            	boolean isSelected =  selItems[j];
            	if(isSelected)
            	{
            		noneSelected = false;
	            	View vi = (View)listview.getChildAt(j);
	            	int id = ((Integer)vi.getTag(R.id.TAG_PC_ID)).intValue();
	            	commaSepAlertIds.append(id);
					commaSepAlertIds.append(",");	            	
            	}
            }
			if(noneSelected){
				UtilityActivity.showMessage(context, "You didn't Select any alerts to delete.",Gravity.CENTER);
				return false;
			}
        	
        	//strip ','
        	String alertIds = commaSepAlertIds.substring(0, commaSepAlertIds.length()-1);
			Object params[] = new Object[1];
			params[0] = alertIds;
			try {
				 String sResponse = new DeleteUserAlertsAsyncTask().execute(params).get();
				 //Iterator<String>  promoPositionItrator= selecetedPromos.keySet().iterator();
				 if(sResponse!=null && !sResponse.trim().equals("")){

					 UtilityActivity.showMessage(context, sResponse, Gravity.CENTER);
				
				 }
			 }catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				
				
        	this.onCreate(null);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
 */	   
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_actions, menu);
        //super.onCreateOptionsMenu(menu, inflater);
        MenuItem action_menu = menu.findItem(R.id.action_menu);
        MenuItem  action_discard = menu.findItem(R.id.action_discard);
        MenuItem action_refresh = menu.findItem(R.id.action_refresh);
        MenuItem action_help = menu.findItem(R.id.action_help);
        
	    action_menu.setVisible(true);
	    action_discard.setVisible(true);
	    action_refresh.setVisible(false);
	    action_help.setVisible(true);
        
    }
	
    public void onResume() {
        super.onResume();
        //following line refreshes the activity when resumed. 
        final MainActivity activity = (MainActivity)getActivity();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(activity.context);
    	boolean refresh = mPrefs.getBoolean("refresh", false);

        
    	//activity.viewPager.setCurrentItem(0);
    	

		ListAdapterStockAlerts adapter = new ListAdapterStockAlerts (activity, activity.getActiveAlerts(false));
		listview.setAdapter(adapter);

		SharedPreferences.Editor editor= mPrefs.edit();
		editor.putBoolean("refresh", false);
		editor.commit();

       
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
