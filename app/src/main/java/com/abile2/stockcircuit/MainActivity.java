package com.abile2.stockcircuit;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

//import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;


import com.abile2.stockcircuit.model.Stock;
import com.abile2.stockcircuit.model.StockAlerts;
import com.abile2.stockcircuit.util.GetAlertsForAUserAsyncTask;
import com.abile2.stockcircuit.util.GetUserFavoriteAsyncTask;

import android.support.design.widget.NavigationView;


public class MainActivity extends AppCompatActivity {

	Context context;
	protected MyApp mMyApp;
	ArrayList<StockAlerts> allAlerts;
	String deviceID;
	String regID;
	String city;
	String mobile;
	SharedPreferences mPrefs;

	//NavDrawer variables
	private DrawerLayout mDrawerLayout;
	NavigationView mNavigationView;
	FragmentManager mFragmentManager;
	FragmentTransaction mFragmentTransaction;
	Toolbar mToolbar;
	// used to store app title
	private CharSequence mTitle;

	int previous_position;
	Fragment fragment = null;
	android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

	private FragmentManager.OnBackStackChangedListener
			mOnBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
		@Override
		public void onBackStackChanged() {
			syncActionBarArrowState();
		}
	};

    @Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			final Toolbar ttoolbar = (Toolbar) findViewById(R.id.toolbar);
			mToolbar = ttoolbar;

			context = this;
			mMyApp = (MyApp)this.getApplication();
			
			mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
			deviceID = mPrefs.getString("deviceID","");
			regID = mPrefs.getString("regID", "");
			city = mPrefs.getString("city", "");
		 	mobile = mPrefs.getString("mobile", "");

			mTitle = getTitle();
			SharedPreferences.Editor editor= mPrefs.edit();
			editor.putBoolean("isFavListDirty", true);
			editor.putBoolean("refresh", true);
			editor.commit();
			//Get all alerts for this user
			//Amit
		//allAlerts = getUserAlertsFromDB();



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
		mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
		mFragmentManager = getSupportFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();

		if (mobile.equals("")) {
			//mDrawerToggle.setDrawerIndicatorEnabled(false);
			mFragmentTransaction.replace(R.id.containerView, new FirstTimeRegister()).commit();
		} else {
			//mDrawerToggle.setDrawerIndicatorEnabled(true);
			mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
		}
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name) {

			public void onDrawerClosed(View view) {
				syncActionBarArrowState();
			}

			public void onDrawerOpened(View drawerView) {
				mDrawerToggle.setDrawerIndicatorEnabled(true);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();
		getSupportFragmentManager().addOnBackStackChangedListener(mOnBackStackChangedListener);

		/**
		 * Setup click events on the Navigation View Items.
		 */

		mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				mDrawerLayout.closeDrawers();

				if (menuItem.getItemId() == R.id.dashboard) {
					FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
					//Show Flaoting menu
					FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
					menu.setVisibility(View.VISIBLE);

				}

				if (menuItem.getItemId() == R.id.broker_details) {
					FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.containerView, new BrokerDetailsActivity()).addToBackStack(null).commit();

					//hide Flaoting menu
					FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
					menu.setVisibility(View.INVISIBLE);
					//mDrawerToggle.setDrawerIndicatorEnabled(false);
					//mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
					mToolbar.setTitle("    Broker Details");
					//mToolbar.setLogo(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_people));
				}

				if (menuItem.getItemId() == R.id.profile) {
					FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
					xfragmentTransaction.replace(R.id.containerView, new FirstTimeRegister()).addToBackStack(null).commit();

					FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
					menu.setVisibility(View.INVISIBLE);
					mToolbar.setTitle("    Profile");
					//mToolbar.setLogo(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_whats_hot));
					//Intent i = new Intent(MainActivity.this, FirstTimeRegister.class);
					//startActivity(i);
					//mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
				}
				return true;
			}

		});

	}

	@Override
	protected void onDestroy() {
		getSupportFragmentManager().removeOnBackStackChangedListener(mOnBackStackChangedListener);
		super.onDestroy();
	}

	private void syncActionBarArrowState() {

		if (mobile.equals("")) {
			return;
		}

		int backStackEntryCount =
				getSupportFragmentManager().getBackStackEntryCount();
		mDrawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);
		//mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
		FloatingActionsMenu menu  = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
		if(backStackEntryCount ==0) {
			menu.setVisibility(View.VISIBLE);
			mToolbar.setTitle("Alert Dashboard");
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.isDrawerIndicatorEnabled() &&
				mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		} else if (item.getItemId() == android.R.id.home &&
				getSupportFragmentManager().popBackStackImmediate()) {
//			if (getSupportFragmentManager().getBackStackEntryCount() < 1){
//				mDrawerToggle.setDrawerIndicatorEnabled(true);
//			}
			//onBackPressed();
			FloatingActionsMenu menu  = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
			menu.setVisibility(View.VISIBLE);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	private void setupFloatingMenu() {
	// TODO Auto-generated method stub
    FloatingActionsMenu menu  = (FloatingActionsMenu) findViewById(R.id.multiple_actions);    
    menu.setParentActivity(this);

	menu.setVisibility(View.VISIBLE);
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
		   	 Intent i = new Intent(MyApp.Context(), BrokerDetailsActivity.class);
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

*/

}