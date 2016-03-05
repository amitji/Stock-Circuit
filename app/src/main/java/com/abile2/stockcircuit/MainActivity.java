package com.abile2.stockcircuit;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.app.Activity;
//import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;


import com.abile2.stockcircuit.model.Stock;
import com.abile2.stockcircuit.model.StockAlerts;
import com.abile2.stockcircuit.util.DeleteStockFavoriteAsyncTask;
import com.abile2.stockcircuit.util.DeleteUserAlertsAsyncTask;
import com.abile2.stockcircuit.util.GetAlertsForAUserAsyncTask;
import com.abile2.stockcircuit.util.GetUserFavoriteAsyncTask;
import android.support.design.widget.NavigationView;

public class MainActivity extends AppCompatActivity  {

    Context context;
protected MyApp mMyApp;	
ArrayList<StockAlerts> allAlerts;
String deviceID;
String regID;
    String city;
    SharedPreferences mPrefs;

    //NavDrawer variables
private DrawerLayout mDrawerLayout;
	NavigationView mNavigationView;
	FragmentManager mFragmentManager;
	FragmentTransaction mFragmentTransaction;

    // used to store app title
private CharSequence mTitle;

    int previous_position;
Fragment fragment = null;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			context = this;
			mMyApp = (MyApp)this.getApplication();
			
			mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
			deviceID = mPrefs.getString("deviceID","");
			regID = mPrefs.getString("regID", "");
			city = mPrefs.getString("city", "");

			SharedPreferences.Editor editor= mPrefs.edit();
			editor.putBoolean("isFavListDirty", true);
			editor.putBoolean("refresh", true);
			editor.commit();
			//Get all alerts for this user
		    allAlerts = getUserAlertsFromDB();
/*
			// Initilization
			viewPager = (ViewPager) findViewById(R.id.pager);
			actionBar = getActionBar();
			mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
			
			viewPager.setAdapter(mAdapter);
			actionBar.setHomeButtonEnabled(false);
			//Amit
			viewPager.setOffscreenPageLimit(2);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
			
			// Adding Tabs
			for (String tab_name : tabs) {
			    actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
			}
			
	*/

			setupNavigationDrawer222(savedInstanceState);
			setViewPagrListner();
			setupFloatingMenu();

			Intent secondInt = getIntent();
			Boolean isNotification = secondInt.getBooleanExtra("isNotification", false);
			String fullid = secondInt.getStringExtra("fullid");
			String alert_price = secondInt.getStringExtra("alert_price");
			if(isNotification){
				//Intent resultIntent = new Intent(getApplicationContext(),StockAlertNewsWebView.class);
				Intent resultIntent = new Intent(getApplicationContext(),StockAlertNewsListView.class);
				resultIntent.putExtra("notification", "yes");
				resultIntent.putExtra("fullid", fullid);
				
				resultIntent.putExtra("alert_price", alert_price);
				startActivity(resultIntent);
				//resultIntent.putExtra("isNotification", true);			
			}
			
}
	private void setupNavigationDrawer222(Bundle savedInstanceState) {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;

		/**
		 * Lets inflate the very first fragment
		 * Here , we are inflating the TabFragment as the first Fragment
		 */

		mFragmentManager = getSupportFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
		/**
		 * Setup click events on the Navigation View Items.
		 */

		mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				mDrawerLayout.closeDrawers();



				if (menuItem.getItemId() == R.id.nav_item_sent) {
					FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.containerView,new ActiveAlertsFragment()).commit();

				}

				if (menuItem.getItemId() == R.id.nav_item_inbox) {
					FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
					xfragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
				}

				return false;
			}

		});

		/**
		 * Setup Drawer Toggle of the Toolbar
		 */

		android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
		android.support.v7.app.ActionBarDrawerToggle mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
				R.string.app_name);

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mDrawerToggle.syncState();


	}
/*
private void setupNavigationDrawer(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	mTitle = mDrawerTitle = getTitle();

	// load slide menu items
	navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

	// nav drawer icons from resources
	navMenuIcons = getResources()
			.obtainTypedArray(R.array.nav_drawer_icons);

	mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;

	mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

	
	navDrawerItems = new ArrayList<NavDrawerItem>();

	// Home
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));


	// Recycle the typed array
	navMenuIcons.recycle();

	mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

	// setting the nav drawer list adapter
	adapter = new NavDrawerListAdapter(getApplicationContext(),
			navDrawerItems);
	mDrawerList.setAdapter(adapter);
	
	// enabling action bar app icon and behaving it as toggle button
	getActionBar().setDisplayHomeAsUpEnabled(true);
	getActionBar().setHomeButtonEnabled(true);

	mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
			R.drawable.ic_drawer_icon, //nav menu toggle icon
			R.string.app_name, // nav drawer open - description for accessibility
			R.string.app_name // nav drawer close - description for accessibility
			){
		public void onDrawerClosed(View view) {
			getActionBar().setTitle(mTitle);
			// calling onPrepareOptionsMenu() to show action bar icons
			invalidateOptionsMenu();
		}
		public void onDrawerOpened(View drawerView){
			getActionBar().setTitle(mDrawerTitle);
			// calling onPrepareOptionsMenu() to hide action bar icons
			invalidateOptionsMenu();
		}
	};
	mDrawerLayout.setDrawerListener(mDrawerToggle);
//	Intent secondInt = getIntent();
//	String openMerchant = secondInt.getStringExtra("openMerchant");	
	if (savedInstanceState == null) {
		// on first time display view for first nav item
//			displayView(0, true);
//			previous_position  = 0;
	}

	
}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position, false);
		}
	}

*/
private void setupFloatingMenu() {
	// TODO Auto-generated method stub
    FloatingActionsMenu menu  = (FloatingActionsMenu) findViewById(R.id.multiple_actions);    
    menu.setParentActivity(this);
    
    //menu.setBackgroundColor(Color.WHITE);;
    final FloatingActionButton nseBtn = (FloatingActionButton) findViewById(R.id.nseBtn);
    nseBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
    	  Intent appInfo = new Intent(view.getContext(), StockListView.class);
    	  appInfo.putExtra("is_world_indices", "n");
          startActivity(appInfo);
  		FloatingActionsMenu menu  = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
  		menu.toggle();
      }
    });

    //Need to chage this...
    final FloatingActionButton bseBtn = (FloatingActionButton) findViewById(R.id.bseBtn);
    bseBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
    	  UtilityActivity.showMessage(context, "Coming Soon...Use Nse Stocks for now", Gravity.CENTER);
      }
     });
    
    
    final FloatingActionButton indexesBtn = (FloatingActionButton) findViewById(R.id.indexesBtn);
    indexesBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
    	  //UtilityActivity.showMessage(context, "Coming Soon...Use Nse Stocks for now", Gravity.CENTER);
    	  Intent appInfo = new Intent(view.getContext(), StockListView.class);
    	  appInfo.putExtra("is_world_indices", "y");
          startActivity(appInfo);
  		FloatingActionsMenu menu  = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
  		menu.toggle();

      }
     });    

    final FloatingActionButton commdtBtn = (FloatingActionButton) findViewById(R.id.commdtBtn);
    commdtBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
    	  UtilityActivity.showMessage(context, "Coming Soon...Use Nse Stocks for now", Gravity.CENTER);
    	  
    	  
    	//this is for testing
    	  /*
			Intent resultIntent = new Intent(getApplicationContext(),StockAlertNewsListView.class);
			resultIntent.putExtra("notification", "yes");
			resultIntent.putExtra("fullid", "NSE:NIFTY");
			
			resultIntent.putExtra("alert_price", "7200");
			startActivity(resultIntent);
    	  */
    	  
    	  
      }
     });   			
	
}

private ArrayList<StockAlerts>  getUserAlertsFromDB() {
	// TODO Auto-generated method stub
	//ArrayList<Stock> list = new ArrayList<Stock>();
	ArrayList<StockAlerts> list = new ArrayList<StockAlerts>();
	String str = "";
	Object[]  inParams = new Object[2];

	inParams[0] = deviceID;
	inParams[1] = regID;
	try {
		str = new GetAlertsForAUserAsyncTask().execute(inParams).get();
		if(str != null && !str.equals(""))
			list = convertJsonToStockAlertList(str);

	} catch (InterruptedException | ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return list;
}

public ArrayList<StockAlerts> convertJsonToStockAlertList(String str) {	

	Object obj = JSONValue.parse(str);
	JSONArray array=(JSONArray)obj;
	ArrayList<StockAlerts> alertList = new ArrayList<StockAlerts>();

	for (int i = 0; i < array.size(); i++) {
		StockAlerts stkAlert = new StockAlerts();
		JSONObject object = (JSONObject) array.get(i);
		stkAlert.setId((String) object.get("id"));
		stkAlert.setIs_active((String) object.get("is_active"));
		stkAlert.setLow_high((String) object.get("low_high"));
		stkAlert.setHas_hit((String) object.get("has_hit"));
		stkAlert.setNseid((String) object.get("nse_id"));
		stkAlert.setFullid((String) object.get("fullid"));
		stkAlert.setName((String) object.get("name"));
		stkAlert.setAlert_price((String) object.get("alert_price"));
		alertList.add(stkAlert);

	}
	return alertList;
}



public ArrayList<StockAlerts> getAlerts(boolean refresh){
	
	if(refresh)
	{
		allAlerts = getUserAlertsFromDB(); 
		return allAlerts;
		
	}else{
		return allAlerts;
		//return getUserAlertsFromDB();
	}
	
}

public ArrayList<StockAlerts> getActiveAlerts(boolean refresh) {
	// TODO Auto-generated method stub
		ArrayList<StockAlerts> allAlerts = getAlerts(refresh);
		ArrayList<StockAlerts> actives =  new ArrayList<StockAlerts>();
		for(StockAlerts sa: allAlerts)
		{
			if(sa.getHas_hit().equals("n"))
			{
				
				actives.add(sa);
			}
			
		}
	return actives;
}

public ArrayList<StockAlerts> getPassiveAlerts(boolean refresh) {
// TODO Auto-generated method stub
	ArrayList<StockAlerts> allAlerts = getAlerts(refresh);
	ArrayList<StockAlerts> passives =  new ArrayList<StockAlerts>();
	for(StockAlerts sa: allAlerts)
	{
		if(sa.getHas_hit().equals("y"))
		{
			
			passives.add(sa);
		}
		
	}
return passives;

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
		alertList.add(stk);
	}
	return alertList;
}

private void setViewPagrListner() {
	// TODO Auto-generated method stub
	/**
	 * on swiping the viewpager make respective tab selected
	 * */
    /*
	viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	 
	    @Override
	    public void onPageSelected(int position) {
	        // on changing the page
	        // make respected tab selected
	        actionBar.setSelectedNavigationItem(position);
	    }
	 
	    @Override
	    public void onPageScrolled(int arg0, float arg1, int arg2) {
	    }
	 
	    @Override
	    public void onPageScrollStateChanged(int arg0) {
	    }
	});

	*/
}

@Override
public boolean onCreateOptionsMenu(Menu menu ) {
	//return super.onCreateOptionsMenu(menu);
	//MenuInflater inflater = getMenuInflater();
    //inflater.inflate(R.menu.activity_main_actions, menu);
    //return super.onCreateOptionsMenu(menu);
    
	
	//return false;

	getMenuInflater().inflate(R.menu.main, menu);
	return true; 
    
    
    
    
//    action_menu = menu.findItem(R.id.action_menu);
//    action_discard = menu.findItem(R.id.action_discard);
//    action_refresh = menu.findItem(R.id.action_refresh);
//    action_help = menu.findItem(R.id.action_help);
//    
//    action_menu.setVisible(false);
//    action_discard.setVisible(false);
//    action_refresh.setVisible(false);
//    action_help.setVisible(false);
//    
//    return true;

}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
   /*
	if (mDrawerToggle.onOptionsItemSelected(item)) {
		return true;
	}

	
	ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
	int pos = viewPager.getCurrentItem();
	if(pos == 0){
		return updateActiveFragmentList(item);
	}else if(pos == 1){
		return updatePassiveFragmentList(item);
		//return false;
	}else if(pos == 2){
		return updateFavoriteFragmentList(item);
		//return false;
	}else 
		return true;
	*/
	
    return true;
	//return super.onOptionsItemSelected(item);
}   

  private boolean updatePassiveFragmentList(MenuItem item) {
	// TODO Auto-generated method stub
//	    switch (item.getItemId()) {
//	    case R.id.action_discard:
//
//	    	UtilityActivity.showMessage(context, "You cannot delete old alerts.",Gravity.CENTER);
//	    	return true;
//	    default:
//	        return super.onOptionsItemSelected(item);	
//	    }
	  
	    return true;

	    
}
/*
private boolean updateFavoriteFragmentList(MenuItem item) {
	// TODO Auto-generated method stub
	  	boolean noneSelected = true;
		ListView listview = (ListView) viewPager.findViewById(R.id.favoriteList);
		
	    switch (item.getItemId()) {
	    case R.id.action_discard:
//	    	ActiveAlertsFragment currentFragment = (ActiveAlertsFragment)mAdapter.getItem(0);
//	    	currentFragment.deleteAlerts();
	    	int pos = viewPager.getCurrentItem();
	    	boolean[] selItems = ((ListAdapterStockFavorite) listview.getAdapter()).getSelectedItems();
	    	StringBuilder commaSepAlertIds = new StringBuilder();
	    	for(int j=0;j < selItems.length; j++ )
	    	{
	        	boolean isSelected =  selItems[j];
	        	if(isSelected)
	        	{
	        		noneSelected = false;
	            	View vi = listview.getChildAt(j);
	            	int id = ((Integer)vi.getTag(R.id.TAG_PC_ID)).intValue();
	            	commaSepAlertIds.append(id);
					commaSepAlertIds.append(",");	            	
	        	}
	        }
			if(noneSelected){
				UtilityActivity.showMessage(context, "You didn't Select any favorites to delete.",Gravity.CENTER);
				return false;
			}
	    	//strip ','
	    	String favIds = commaSepAlertIds.substring(0, commaSepAlertIds.length()-1);
			Object params[] = new Object[1];
			params[0] = favIds;
			try {
				 //String sResponse = new DeleteStockFavoriteAsyncTask().execute(params).get();
				new DeleteStockFavoriteAsyncTask().execute(params).get();
				UtilityActivity.showMessage(context, "Favorite Deleted", Gravity.CENTER);
			 }catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			ListAdapterStockFavorite adapter = new ListAdapterStockFavorite(this, getUserFavorites());
			listview.setAdapter(adapter);
	        return true;
		    case R.id.action_menu:
		   	 Intent i = new Intent(MyApp.Context(), UserSettingsActivity.class);
		   	 //i.putExtra("showmessage", "false");
		   	 startActivity(i);
		   	 return true;
		    case R.id.action_refresh:
				SharedPreferences.Editor editor= mPrefs.edit();
				editor.putBoolean("isFavListDirty", true);
				editor.commit();
				
				//Fragment frag = getActiveFragment();
				
		    	List<Fragment> allFragments = getSupportFragmentManager().getFragments();
		    	for(Fragment frag: allFragments )
		    	{
		    		if(frag instanceof FavoritesFragment)
		    		{
				    	 FragmentTransaction fragTransaction =   getSupportFragmentManager().beginTransaction();
				    	 fragTransaction.detach(frag);
				    	 fragTransaction.attach(frag);
				    	 fragTransaction.commit();
		    		}
		    	}

		    	
			   	return true;

	    default:
	        return super.onOptionsItemSelected(item);
	    }
	
}

private boolean updateActiveFragmentList(MenuItem item) {
	// TODO Auto-generated method stub
	  	boolean noneSelected = true;
		ListView listview = (ListView) viewPager.findViewById(R.id.activeList);
		
	    switch (item.getItemId()) {
	    case R.id.action_discard:
	    	
//	    	ActiveAlertsFragment currentFragment = (ActiveAlertsFragment)mAdapter.getItem(0);
//	    	currentFragment.deleteAlerts();
	    	int pos = viewPager.getCurrentItem();
	    	
	    	boolean[] selItems = ((ListAdapterStockAlerts) listview.getAdapter()).getSelectedItems();
	    	StringBuilder commaSepAlertIds = new StringBuilder();
	    	
	    	for(int j=0;j < selItems.length; j++ )
	    	{
	    		
	        	boolean isSelected =  selItems[j];
	        	if(isSelected)
	        	{
	        		//((ListAdapterStockAlerts) listview.getAdapter()).deleteItemAt(j);
	        		noneSelected = false;
	            	View vi = listview.getChildAt(j);
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

//			SharedPreferences.Editor editor= mPrefs.edit();
//			editor.putBoolean("refresh", true);
//			editor.commit();
//			refreshFragment();
			ListAdapterStockAlerts adapter = new ListAdapterStockAlerts (this, getActiveAlerts(true));
			listview.setAdapter(adapter);
	        return true;
		    case R.id.action_menu:
		   	 Intent i = new Intent(MyApp.Context(), UserSettingsActivity.class);
		   	 //i.putExtra("showmessage", "false");
		   	 startActivity(i);
		   	 return true;

	    default:
	        return super.onOptionsItemSelected(item);
	    }
	
}
*/
    protected void onResume() {
        super.onResume();
        //following line refreshes the activity when resumed. 
        //this.onCreate(null);
   /*
        viewPager.setAdapter(mAdapter);
        actionBar.setSelectedNavigationItem(0);
        final int pos = 0;
        viewPager.postDelayed(new Runnable() {

            @Override
            public void run() {
            	viewPager.setCurrentItem(pos);
            }
        }, 100);

      */
        //viewPager.setCurrentItem(0);
        mMyApp.setCurrentActivity(this);
    }
    protected void onPause() {
        clearReferences();
        super.onPause();
    }
    protected void onDestroy() {        
        clearReferences();
        super.onDestroy();
        //UtilMobileAdvt.getInstance().showInterstitial();
    }
    private void clearReferences(){
        Activity currActivity = mMyApp.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
            mMyApp.setCurrentActivity(null);
    }

    @Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

}