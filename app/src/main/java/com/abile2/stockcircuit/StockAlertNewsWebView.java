package com.abile2.stockcircuit;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class StockAlertNewsWebView extends Activity {
	
	WebView webView;
	String url="";
	SharedPreferences mPrefs;
	Context context;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_alert_news_web_view);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Intent secondInt = getIntent();
		context = this;
		String nseid = secondInt.getStringExtra("nseid");
		String fullid = secondInt.getStringExtra("fullid");
		String alert_price = secondInt.getStringExtra("alert_price");
		//String url = secondInt.getStringExtra("url");
		setupStockParams(nseid,alert_price,fullid);
		webView = (WebView)findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(false);
        
		webView.setWebViewClient(new MyWebViewClient());
		//webView.setWebChromeClient(new WebChromeClient());
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		
		//String url = "https://www.google.com/finance/company_news?q=NSE:"+nseid;

		String url = "https://www.google.com/finance/company_news?q="+fullid;
		
		webView.clearView();
		webView.loadUrl(url);
		
		
		/*
		String url = "https://www.google.com/finance/company_news?q="+fullid+"&output=rss";
		
		webView.clearView();
		String html = "<html><body onload=\"location.href='"+url+"'\"  style=\"margin:0px;padding:0px; \"></body></html>";
		webView.loadData(html, "text/html", null);	
		*/
		
		addCallBrokerButtonListner();
		addGoToTradingWebsiteButtonListner();
		
		//String html = "<html><body onload=\"location.href='"+url+"'\"  style=\"margin:0px;padding:0px; \"></body></html>";
		//webView.loadData(html, "text/html", null);		
		
	}

	private void setupStockParams(String nseid, String alert_price, String fullid) {
		// TODO Auto-generated method stub

		Object object[] = new Object[1];
		object[0] = nseid;	
		object[0] = fullid;
		String quote="";
		try {

			HashMap<String, String> quoteParams = new GetLiveQuoteAsyncTask().execute(object).get();

			if (quoteParams != null ) {
				
				TextView name = (TextView)findViewById(R.id.stockname);	
				TextView change = (TextView)findViewById(R.id.change);
				TextView stock_curr_price = (TextView)findViewById(R.id.stock_curr_price);
				TextView tv_alert_price = (TextView)findViewById(R.id.alert_price);
				
				name.setText("Stock Symbol : "+quoteParams.get("t"));
				change.setText("Change : "+quoteParams.get("c_fix")+" ( "+quoteParams.get("cp_fix")+ "% ) ");
				stock_curr_price.setText("Last Trade : "+quoteParams.get("l_fix"));
				tv_alert_price.setText("Alert @ : "+alert_price);
				
				//System.out.println("quote - "+quote);
			}

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}

	private void addCallBrokerButtonListner() {
		
		Button button = (Button) findViewById(R.id.callBrokerBtn);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String mobile = mPrefs.getString("brokerMobile1","");
				//String mobile2 = mPrefs.getString("brokerMobile2","");
				
				if (mobile != null && !mobile.isEmpty()) {
					//String uri = "tel:" + mobile+";"+mobile2;
					String uri = "tel:" + mobile;
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse(uri));
					startActivity(intent);
				} else {
					//UtilityActivity.showMessage("You need to setup your broker details",v.getContext());
					showSettingsAlert();
				}

				// UtilityActivity.hideSoftKeyboard(v);

			}
		});
	}
	private void addGoToTradingWebsiteButtonListner() {
		
		Button button = (Button) findViewById(R.id.websiteBtn);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String website = mPrefs.getString("brokerWebsite","");
				//String mobile2 = mPrefs.getString("brokerMobile2","");
				
				if (website != null && !website.isEmpty()) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(website));
					startActivity(i);				
				} 
				else {
					//UtilityActivity.showMessage("You need to setup your broker details",v.getContext());
					showSettingsAlert();
				}

				// UtilityActivity.hideSoftKeyboard(v);

			}
		});
	}	
	
	   public void showSettingsAlert(){
	        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
	      
	        // Setting Dialog Title
	        alertDialog.setTitle("Broker Setup Missing");
	  
	        // Setting Dialog Message
	        alertDialog.setMessage("Your Broker details need to be setup. Do you want to setup now ?");
	  
	        // Setting Icon to Dialog
	        //alertDialog.setIcon(R.drawable.delete);
	  
	        // On pressing Settings button
	        alertDialog.setPositiveButton("Setup Broker", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	                Intent intent = new Intent(context, UserSettingsActivity.class);
	                startActivity(intent);
	                
	            }
	        });
	  
	        // on pressing cancel button
	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	            }
	        });
	  
	        // Showing Alert Message
	        alertDialog.show();
	    }
	 
	   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock_alert_news_web_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.clearCache(true);
        } 
   }
//    @Override
//    public void onResume(){
//    // ...
//    super.onResume();
//    webView.clearCache(true);
//	Intent secondInt = getIntent();
//	String nseid = secondInt.getStringExtra("nseid");
//	String url = "https://www.google.com/finance/company_news?q=NSE:"+nseid;
//    
//    if (!url.equals(webView.getUrl())) {
//        webView.loadUrl(url);
//    }
//}
   
}
