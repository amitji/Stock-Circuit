package com.abile2.stockcircuit;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.abile2.stockcircuit.model.NewsFeedItem;
import com.abile2.stockcircuit.util.GetLiveQuoteAsyncTask;
import com.abile2.stockcircuit.util.GetNewsFeedAsyncTask;
import com.abile2.stockcircuit.util.RssHandler;

public class StockAlertNewsListView extends Activity {
	
	WebView webView;
	String url="";
	SharedPreferences mPrefs;
	Context context;
	private ListView listView;
	TextView name;
	TextView change;
	TextView stock_curr_price;
	TextView tv_alert_price;

	String stockName;
	String nseid;
	String fullid;
	String changeStr;
	String priceStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_alert_news_list_view);
		listView = (ListView) findViewById(R.id.NewsFeed);

		View header = getLayoutInflater().inflate(R.layout.header, null);
		 name = (TextView)header.findViewById(R.id.stockname);	
		 change = (TextView)header.findViewById(R.id.change);
		 stock_curr_price = (TextView)header.findViewById(R.id.stock_curr_price);
		 tv_alert_price = (TextView)header.findViewById(R.id.alert_price);

		
		//View footer = getLayoutInflater().inflate(R.layout.footer, null);
		listView.addHeaderView(header);
		//listView.addFooterView(footer);

		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Intent secondInt = getIntent();
		context = this;
		//nseid = secondInt.getStringExtra("nseid");
		fullid = secondInt.getStringExtra("fullid");
		nseid = fullid.substring(fullid.lastIndexOf(":") + 1);
		String alert_price = secondInt.getStringExtra("alert_price");
		//String url = secondInt.getStringExtra("url");
		setupStockParams(nseid,alert_price,fullid);
		String url = "https://www.google.com/finance/company_news?q="+fullid+"&output=rss&num=20";
		Object object[] = new Object[1];
		object[0] = url;

		String rssFeed="";
		try {
			rssFeed = new GetNewsFeedAsyncTask().execute(object).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<NewsFeedItem> list=null;
		//list = processNewsFeed(rssFeed);
		list = processNewsFeed(rssFeed);
		
		NewsFeedItem[] newsFeedArray = new NewsFeedItem[list.size()];
		newsFeedArray = list.toArray(newsFeedArray);
		
		ListAdapterNewsFeed  adapter = new ListAdapterNewsFeed(context, R.layout.news_feed_list_item,newsFeedArray);
		listView.setAdapter(adapter);

		addResetAlertButtonListener();
		addCallBrokerButtonListner();
		addGoToTradingWebsiteButtonListner();

		setListViewListner();
		
	}

	private ArrayList<NewsFeedItem> processNewsFeed(String rssFeed) {
		// TODO Auto-generated method stub
		
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            RssHandler handler = new RssHandler();
            
            InputSource inStream = new InputSource();
            inStream.setCharacterStream(new StringReader(rssFeed));
            
            //rssFeed = "<rss><channel><item><title>Test 11</title><description>This is just a test</description></item></channel></rss>";
            
            
            parser.parse(inStream, handler);
            return handler.getMessages();
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException(e);
        } 
	}

	private void setupStockParams(String nseid, String alert_price, String fullid) {
		// TODO Auto-generated method stub

		Object object[] = new Object[1];
		//object[0] = nseid;
		object[0] = fullid;
		String quote="";
		try {

			HashMap<String, String> quoteParams = new GetLiveQuoteAsyncTask().execute(object).get();

			if (quoteParams != null ) {
				//View header = getLayoutInflater().inflate(R.layout.header, null);
				
//				TextView name = (TextView)header.findViewById(R.id.stockname);	
//				TextView change = (TextView)header.findViewById(R.id.change);
//				TextView stock_curr_price = (TextView)header.findViewById(R.id.stock_curr_price);
//				TextView tv_alert_price = (TextView)header.findViewById(R.id.alert_price);

				stockName = quoteParams.get("t");
				changeStr = quoteParams.get("c_fix")+" ( "+quoteParams.get("cp_fix")+ "% )" ;
				priceStr = quoteParams.get("l_fix");

				name.setText("Stock Symbol : "+stockName);
				change.setText("Change : "+changeStr);
				stock_curr_price.setText("Last Trade : "+priceStr);
				tv_alert_price.setText("Alert @ : "+alert_price);
				
				//System.out.println("quote - "+quote);
			}

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}

	private void setListViewListner() {
		// TODO Auto-generated method stub
		
		listView = (ListView) findViewById(R.id.NewsFeed);
		listView.setOnItemClickListener(new OnItemClickListener() {
	    	
	    	public void onItemClick(AdapterView<?> parent, View view,
	    			int position, long id) {
	    						
	    		NewsFeedItem news= (NewsFeedItem) parent.getItemAtPosition(position);
	    		String link  = news.getLink();

	    		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
	    		startActivity(browserIntent);	    		
	    	}
	    	
	    	
	    });
		
	}
	private void addResetAlertButtonListener() {

		//View header = getLayoutInflater().inflate(R.layout.header, null);

		Button button = (Button) findViewById(R.id.resetAlertBtn);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//MainActivity activity = (MainActivity)getActivity();
				Intent i = new Intent(context, SetAlertActivity.class);
				i.putExtra("stockname",stockName);
				i.putExtra("nseid",nseid);
				i.putExtra("fullid",fullid);
				i.putExtra("price",priceStr);
				i.putExtra("change",changeStr);
				i.putExtra("isFavorite","no");
				//i.putExtra("id",favId); //this is id from user_favorite table and not an stock id
				startActivity(i);

			}
		});
	}
	
	private void addCallBrokerButtonListner() {
		
		//View header = getLayoutInflater().inflate(R.layout.header, null);
		
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
		
		//View header = getLayoutInflater().inflate(R.layout.header, null);
		
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
	                Intent intent = new Intent(context, BrokerDetailsActivity.class);
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
	
 

   
}
