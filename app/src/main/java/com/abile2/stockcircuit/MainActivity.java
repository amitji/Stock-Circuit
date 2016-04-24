package com.abile2.stockcircuit;


import java.util.ArrayList;

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


import com.abile2.stockcircuit.model.StockAlerts;

import android.support.design.widget.NavigationView;


public class MainActivity extends AppCompatActivity
			implements RecommendedVideosFragment.OnVideoSelectedListener{
	//public class MainActivity extends AppCompatActivity {
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
			//setViewPagrListner();
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
			mFragmentTransaction.replace(R.id.containerView, new FirstTimeRegister(), "first_time_register").commit();
		} else {
			//mDrawerToggle.setDrawerIndicatorEnabled(true);
			mFragmentTransaction.replace(R.id.containerView, new TabFragment(), "tab_fragment").commit();
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
					fragmentTransaction.replace(R.id.containerView, new TabFragment(), "dashboard").commit();
					//Show Flaoting menu
					FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
					menu.setVisibility(View.VISIBLE);

				}

				if (menuItem.getItemId() == R.id.broker_details) {
					FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.containerView, new BrokerDetailsFragment(), "broker_details").addToBackStack(null).commit();

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
					xfragmentTransaction.replace(R.id.containerView, new FirstTimeRegister(),"profile").addToBackStack(null).commit();

					FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
					menu.setVisibility(View.INVISIBLE);
					mToolbar.setTitle("    Profile");
					//mToolbar.setLogo(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_whats_hot));
					//Intent i = new Intent(MainActivity.this, FirstTimeRegister.class);
					//startActivity(i);
					//mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
				}
				if (menuItem.getItemId() == R.id.feeback) {
					FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
					xfragmentTransaction.replace(R.id.containerView, new FeedbackFragment(),"feedback").addToBackStack(null).commit();

					FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
					menu.setVisibility(View.INVISIBLE);
					mToolbar.setTitle("    Feedback");
				}
				if (menuItem.getItemId() == R.id.help) {
					FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
					xfragmentTransaction.replace(R.id.containerView, new HelpFragment(),"help").addToBackStack(null).commit();

					FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
					menu.setVisibility(View.INVISIBLE);
					mToolbar.setTitle("    Help is Here !");
				}

				if (menuItem.getItemId() == R.id.share) {
					Intent i= new Intent(Intent.ACTION_SEND);
					i.setType("text/plain");
					i.putExtra(Intent.EXTRA_SUBJECT, Constants.shareSubject);
					i.putExtra(Intent.EXTRA_TEXT, Constants.shareMessage);
					startActivity(Intent.createChooser(i, "Share Stock Circuit App via"));

				}

				if (menuItem.getItemId() == R.id.recommended_videos) {
					FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
					xfragmentTransaction.replace(R.id.containerView, new RecommendedVideosFragment(),"videos").addToBackStack(null).commit();

					FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
					menu.setVisibility(View.INVISIBLE);
					mToolbar.setTitle("    Recommended Videos");
				}

				if (menuItem.getItemId() == R.id.user_requested_video) {
					Intent stockVideoList= new Intent(context, StockListView.class);
					stockVideoList.putExtra("is_video_list", "y");
					stockVideoList.putExtra("is_world_indices", "n");

					startActivity(stockVideoList);
				}

				return true;
			}

		});

	}
	//Amit - function for OnVideoSelectedListener Fragment Listner

	public void onItemSelected(String url) {
/*
		    GetSelectedVideoFragment getSelectedVideoFragment = new GetSelectedVideoFragment();
			Bundle args = new Bundle();
			args.putString("url", url);
			getSelectedVideoFragment.setArguments(args);
			FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
			xfragmentTransaction.replace(R.id.containerView,getSelectedVideoFragment, "get_selected_video").addToBackStack(null).commit();

			FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
			menu.setVisibility(View.INVISIBLE);
			mToolbar.setTitle("    Selected Video");

		*/
                Intent resultIntent = new Intent(this,GetRecommendedSelectedVideoActivity.class);
                resultIntent.putExtra("url", url);
                startActivity(resultIntent);

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




}