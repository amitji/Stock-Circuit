package com.abile2.stockcircuit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;












import com.abile2.stockcircuit.model.Stock;
import com.abile2.stockcircuit.util.GPSTracker;
import com.abile2.stockcircuit.util.GetAppConfigParamsAsyncTask;
import com.abile2.stockcircuit.util.NetworkUtil;
import com.abile2.stockcircuit.util.ProcessSharedVideoAsyncTask;
import com.abile2.stockcircuit.util.SaveUserAsyncTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.model.LatLng;

public class SplashScreen extends Activity {
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 1200;//1200;
	SharedPreferences mPrefs;
	//AtomicInteger msgId = new AtomicInteger();
	Context context;
	GoogleCloudMessaging gcm;
	String regID;
	String deviceID;
	//double latitude, longitude;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_activity);
		context = this;
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);



		new Handler().postDelayed(new Runnable() {
			/*
            * Showing splash screen with a timer. This will be useful when you
            * want to show case your app logo / company
            */
			@Override
			public void run() {
				checkConfig();
			}
		}, SPLASH_TIME_OUT);

	}

	private void processSharedVideoIntent(){
		StringBuilder text = new StringBuilder();

		Uri data = getIntent().getData();
		if(data != null){
			text.append("Path:\n");
			text.append(data.getPath());

			text.append("\n\nScheme:\n");
			text.append(data.getScheme());

			text.append("\n\nHost:\n");
			text.append(data.getHost());

			text.append("\n\nPath segments:\n");
			text.append(Arrays.toString(data.getPathSegments().toArray()));

			System.out.println("Data from Video Intent - "+text);
			String mobile = mPrefs.getString("mobile", "");
			String deviceID= mPrefs.getString("deviceID","");
			String regID = mPrefs.getString("regID", "");



			Object object[] = new Object[5];
			object[0] = mobile;
			object[1] = deviceID;
			object[2] = regID;
			object[3] = data.getPathSegments().get(0);

			try {
				new ProcessSharedVideoAsyncTask().execute(object).get();
				//reset video refersh flag so that updated list is fetched from server
				SharedPreferences.Editor editor= mPrefs.edit();
				editor.putBoolean("my_video_refresh_flag", true);
				editor.commit();

			} catch (InterruptedException | ExecutionException e) {

				e.printStackTrace();
			}

		} else {
			text.append("Uri is null");
		}

	}
	@SuppressLint("NewApi")
	private void checkConfig() {
		// Checks Internet Connection
		if (NetworkUtil.getConnectivityStatus(getApplicationContext())) {
			// Checks Play Service is Installed or Not
			if (checkPlayServices()) {
				String devId= mPrefs.getString("deviceID","");
				if(devId.equals("") || devId==null){
					TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					deviceID = telephonyManager.getDeviceId();
					SharedPreferences.Editor editor= mPrefs.edit();
					editor.putString("deviceID",deviceID);
					editor.commit();
				}

				// Getting RegId from Shared Prefs.
				regID = getRegistrationId(context);
				if (TextUtils.isEmpty(regID) && TextUtils.equals(regID, "")) {
					String result = RegisterInBackground();
					if (result != null) {
						openActivityTask();
					} else {
						Log.d("GCM REGISTER","Error in Register or in Sending Request to Server.");
					}
				} else {
					Log.d("REG ID", regID);
					openActivityTask();
				}
			} else {
				AlertDialog.Builder build = new AlertDialog.Builder(this);
				build.setMessage(
						"Your Device is Not Support For This App Please Install Google Play Services to Use This App.")
						.setTitle("Play Service Not Installed")
						.setPositiveButton(R.string.action_ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int id) {
										dialog.dismiss();
										finish();
									}
								}).setCancelable(false);
				AlertDialog dialog = build.create();
				dialog.show();
			}
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.no_internet_msg)
					.setTitle(R.string.no_internet_title)
					.setPositiveButton(R.string.action_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									dialog.dismiss();
									finish();
								}
							}).setCancelable(false);
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}
	private void openActivityTask() {

		String mobile = mPrefs.getString("mobile", "");
		long nseStocksListLastFetch = mPrefs.getLong("nseStocksListLastFetch", 0);
		String stocksStr = mPrefs.getString("nseStocksList", "");

		processSharedVideoIntent();

		try {
			String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			int vCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			String versionCode = Integer.toString(vCode);
			Object[] formList = new Object[2];
			formList[0] = versionName;
			formList[1] = versionCode;
			HashMap<String, String> app_config = new GetAppConfigParamsAsyncTask().execute(formList).get();

			if(app_config != null && app_config.get("app_rater_show") != null)
			{
				SharedPreferences.Editor mpref = mPrefs.edit();
				mpref.putString("app_rater_show", app_config.get("app_rater_show"));
				mpref.putString("app_rater_days_until_prompt", app_config.get("app_rater_days_until_prompt"));

				mpref.commit();
			}



			if(nseStocksListLastFetch !=0 && !stocksStr.equals(""))
			{
				Date lastUpdateDate = new Date(nseStocksListLastFetch);

				Date currDate = new Date(System.currentTimeMillis());

				long diff = Math.abs(currDate.getTime() - lastUpdateDate.getTime());
				long diffDays = diff / (24 * 60 * 60 * 1000);
				int app_stock_list_update_days = Constants.STOCK_LIST_FETCH_TIME;


				if(app_config != null && app_config.get("app_stock_list_update_days") != null)
				{
					app_stock_list_update_days = Integer.parseInt(app_config.get("app_stock_list_update_days"));
				}

				if(((int) diffDays ) > app_stock_list_update_days){
					stocksStr = getNseStocksList();
					SharedPreferences.Editor mpref = mPrefs.edit();
					mpref.putString("nseStocksList", stocksStr);
					mpref.putLong("nseStocksListLastFetch", currDate.getTime());
					mpref.commit();
				}
			}else{
				stocksStr = getNseStocksList();
				SharedPreferences.Editor mpref = mPrefs.edit();
				mpref.putString("nseStocksList", stocksStr);
				mpref.putLong("nseStocksListLastFetch", System.currentTimeMillis());
				mpref.commit();
			}

			if(mobile.equals("")){
				//Intent i = new Intent(SplashScreen.this, MainActivity.class);
				Intent i = new Intent(SplashScreen.this, ProfileActivity.class);
				startActivity(i);
				finish();
			}else{
				Intent intent = getIntent();
				String notificationIntent = intent.getStringExtra("notification");
				if(notificationIntent==null || notificationIntent.equals("")){
					if(app_config != null) {
						String app_upgrade_force = app_config.get("app_upgrade_force");
						if (app_upgrade_force != null && app_upgrade_force.equals("y")) //force user to upgrade to new version
						{
							goToGooglePlayStoreForUpgrade();
						}
						else{
							Intent i = new Intent(SplashScreen.this, MainActivity.class);
							startActivity(i);
							finish();
						}
					}
					else{
						Intent i = new Intent(SplashScreen.this, MainActivity.class);
						startActivity(i);
						finish();
					}
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	// when upgrade is required take user to google play for upgrade...
	private void goToGooglePlayStoreForUpgrade() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle("Please upgrade the App");
		alertDialog.setMessage("You need to upgrade to an important new version to use this app.");

		alertDialog.setPositiveButton(android.R.string.ok, null);

		alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.abile2.stockcircuit")));
				//dialog.dismiss();
			}
		});

		alertDialog.setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}
		});
		AlertDialog dialog = alertDialog.create();
		dialog.show();
	}

	private String  getNseStocksList() {
		// TODO Auto-generated method stub
		//ArrayList<Stock> list = new ArrayList<Stock>();
		String str = "";
		try {

			String is_video_available = "n";
			String exchange_flag = "y";
			Object object[] = new Object[2];
			object[0] = is_video_available;
			object[1] = exchange_flag;
			return new GetAllStockNames().execute(object).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;

	}
	/*
	private void showUserLocationSettingsDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setMessage("This App would be able to provide Promotion & Discounts nearby you with help of GPS. GPS is currently not enabled on your Phone. Would you like to turn it on ?");
		dialog.setPositiveButton("Enable GPS",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						Intent myIntent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(myIntent);
						finish();
					}
				});
		dialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						finish();
					}
				});
		dialog.show();
	}
	*/
	// Check if Play Service is Installed On Mobile or Not.
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i("Device Support", "This device is not supported.");
			}
			return false;
		}
		return true;
	}
	// Get Registration ID from Shared Prefs.
	@SuppressLint("NewApi")
	private String getRegistrationId(Context context) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String registrationId = mPrefs.getString("regID", "");
		if (registrationId.isEmpty()) {
			Log.i("GCM REGISTER", "Registration not found In Shared Prefs.");
			return "";
		}
		return registrationId;
	}
	/*
	private void storeRegistrationId(Context context, String regId ,String deviceID) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putString("regID", regId);
		editor.putString("deviceID", deviceID);
		System.out.println("Device id to Be Stored is :" + deviceID);
		editor.commit();
	}
	*/
	/*
	private LatLng getUserCurrentLocation() {
		// TODO Auto-generated method stub
		GPSTracker gps = new GPSTracker(context);
		// check if GPS enabled
		if (gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			// Toast.makeText(getApplicationContext(),
			// "Your Location is - \nLat: " + latitude + "\nLong: " + longitude,
			// Toast.LENGTH_LONG).show();
			return new LatLng(latitude, longitude);
		} else {

			return null;
		}
	}

	private String getCityName(double latitude, double longitude) {
		Geocoder gcd = new Geocoder(context, Locale.getDefault());
		String cityName = "";
		try {
			List<Address> address = gcd.getFromLocation(latitude, longitude, 1);
			if (address != null && address.size() > 0) {
				cityName = address.get(0).getLocality();
				Log.d("Location Info", "City Name is " + cityName);
			} else {
				address = gcd.getFromLocation(latitude, longitude, 1);
				if (address != null && address.size() > 0) {
					cityName = address.get(0).getLocality();
					Log.d("Location info Retry ", "City Name is " + cityName);
				}
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return cityName;
	}
	*/
	private String RegisterInBackground() {
		gcm = GoogleCloudMessaging.getInstance(this);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				deviceID = telephonyManager.getDeviceId();
				System.out.println("Device Id is :" + deviceID);
				//String cityName = getCityName(latitude, longitude);
				//System.out.println("City Name is :" + cityName);
				regID = new GCMHelper(context)
						.GCMRegister(Constants.SENDER_ID);
				System.out.println("regID Id is :" + regID);

				//save in preference

				SharedPreferences.Editor editor = mPrefs.edit();

				editor.putString("regID", String.valueOf(regID));
				editor.putString("deviceID", String.valueOf(deviceID));
				//editor.putString("city", String.valueOf(cityName));
				editor.commit();
			}
		});
		thread.start();
		return "Success";
	}


}