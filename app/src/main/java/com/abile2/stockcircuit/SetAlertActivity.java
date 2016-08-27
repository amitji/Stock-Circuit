package com.abile2.stockcircuit;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;

import com.abile2.stockcircuit.util.DeleteStockFavoriteAsyncTask;
import com.abile2.stockcircuit.util.SavePortfolioDetailsAsyncTask;
import com.abile2.stockcircuit.util.SaveStockAlertAsyncTask;
import com.abile2.stockcircuit.util.SaveStockFavoriteAsyncTask;

public class SetAlertActivity extends Activity {
	VerticalSeekBar seekBar;
	double curr_stock_price = 0;
	String nseid="";
	String fullid="";
	String stockname ="";
	double step = 1;
	double min = 0;
	double max = 100;
	double middle = 50;
	double sliding_factor = 0;
	double alert_price = 0;
	EditText slider_value;
	TextView stockNameTv;
	TextView priceTv;
	TextView changeTv;
	Context context;
	SharedPreferences mPrefs;		 
	String deviceID;
	String regID;
	String city;
	String mobile;
	String name;
	String email;
	String isFavorite;
	String favID;
	String qty;
	String buy_price;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_alert);
		context = this;
		UtilityActivity.hideSoftKeyboard(this);
		Intent secondInt = getIntent();
		stockname = secondInt.getStringExtra("stockname");
		nseid = secondInt.getStringExtra("nseid");
		fullid = secondInt.getStringExtra("fullid");
		qty = secondInt.getStringExtra("qty");
		buy_price = secondInt.getStringExtra("buy_price");

		String stock_price = secondInt.getStringExtra("price");
		String change = secondInt.getStringExtra("change");
		isFavorite = secondInt.getStringExtra("isFavorite");
		favID = secondInt.getStringExtra("id");


		//if(stock_price != null){
		//UtilityActivity.showMessage(context, "in If loop ", Gravity.CENTER);
		curr_stock_price = Double.valueOf(stock_price);
		//}
		//UtilityActivity.showMessage(context, "After If loop name "+stockname, Gravity.CENTER);
		setupseekBarParams(curr_stock_price);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		deviceID = mPrefs.getString("deviceID","");
		regID = mPrefs.getString("regID", "");
		city = mPrefs.getString("city", "");
		mobile = mPrefs.getString("mobile", "");
		name = mPrefs.getString("name", "");
		email = mPrefs.getString("email", "");
		seekBar = (VerticalSeekBar) findViewById(R.id.seekBar1);
		slider_value = (EditText) findViewById(R.id.slider_value);
		stockNameTv = (TextView) findViewById(R.id.stockname);
		priceTv = (TextView) findViewById(R.id.stock_curr_price);
		changeTv = (TextView) findViewById(R.id.change);
		priceTv.setText("Last Trade : "+stock_price);
		changeTv.setText("Change : "+change);
		stockNameTv.setText(stockname);
        if(stockname.length() > 20)
            stockNameTv.setTextSize(16);

		((TextView) findViewById(R.id.quantity)).setText(qty);
		((TextView) findViewById(R.id.buyPrice)).setText(buy_price);

		slider_value.setText(stock_price);
		setupSeekBar();
		setupAlertButtonListner();
		setupCloseButtonListner();

		setupPlusMinusButtonsListner();
		setupPortfolioSaveButtonsListner();


		if(isFavorite != null && isFavorite.equals("yes")){
			Button button = (Button) findViewById(R.id.setFavBtn);
			button.setText("Remove Favorite");
			setupDeleteFavoriteButtonListner();

		}else{
			setupAddFavoriteButtonListner();
		}

	}
	private void setupPlusMinusButtonsListner() {
		// TODO Auto-generated method stub
		ImageView plus = (ImageView) findViewById(R.id.plus);
		ImageView minus = (ImageView) findViewById(R.id.minus);
		plus.setOnClickListener(new OnClickListener() {
			public void onClick(View v)
			{
				String alertP = slider_value.getText().toString();
				int ind = alertP.indexOf("(");
				if(ind > -1)
					alertP = alertP.substring(0, ind);
				double plusD = Double.valueOf(alertP);
				//double plusD = curr_stock_price;
				double onePercent = curr_stock_price/100;
				DecimalFormat df = new DecimalFormat("#.00");
				double total = Double.valueOf(df.format(plusD+onePercent));
				double totalPercentChange = Double.valueOf(df.format((total - curr_stock_price)*100/curr_stock_price));
				//double total = Math.round((plusD+onePercent)*100)/100;
				slider_value.setText(String.valueOf(total)+" ("+String.valueOf(totalPercentChange)+"%)");
			} 
		});
		minus.setOnClickListener(new OnClickListener() {
			public void onClick(View v)
			{
				String alertP = slider_value.getText().toString();
				int ind = alertP.indexOf("(");
				if(ind > -1)
					alertP = alertP.substring(0, ind);
				double minusD = Double.valueOf(alertP);
				//double minusD = curr_stock_price;
				double onePercent = curr_stock_price/100;
				DecimalFormat df = new DecimalFormat("#.00");
				double total = Double.valueOf(df.format(minusD - onePercent));
				double totalPercentChange = Double.valueOf(df.format((total - curr_stock_price)*100/curr_stock_price));
				slider_value.setText(String.valueOf(total)+" ("+String.valueOf(totalPercentChange)+"%)");
			} 
		});
	}
	private void setupCloseButtonListner() {
		// TODO Auto-generated method stub
		Button button = (Button) findViewById(R.id.closeBtn);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				finish();
			}
		});
	}
	
	private void setupseekBarParams(double curr_stock_price2) {
		// TODO Auto-generated method stub
		min = 0.8*curr_stock_price2;
		max = 1.2*curr_stock_price2;
		sliding_factor = ((max-min)/max);
		DecimalFormat df = new DecimalFormat("#.00");
		sliding_factor = Double.valueOf(df.format(sliding_factor));
		step = .005*curr_stock_price2;
		middle = max/2;
		alert_price = curr_stock_price2;
	}
	private void setupSeekBar() {
		// TODO Auto-generated method stub

		seekBar = (VerticalSeekBar) findViewById(R.id.seekBar1);
		seekBar.setMax( (int)(max ));
		int max2= seekBar.getMax();
		//seekBar.setProgress(0);
		seekBar.setProgress((int)middle);
		int progress3 = seekBar.getProgress();
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress = (int)min;
			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
				progress = progresValue;
				alert_price = min+sliding_factor*progresValue;
				DecimalFormat df = new DecimalFormat("#.00");
				double percentage =  (Math.abs(alert_price - curr_stock_price)/curr_stock_price)*100;
				percentage = Double.valueOf(df.format(percentage));
				if (alert_price < curr_stock_price)
					percentage = 0-percentage;
				alert_price = Double.valueOf(df.format(alert_price));
				//Toast.makeText(getApplicationContext(), progress+"-"+value, Toast.LENGTH_SHORT).show();
				slider_value.setText(alert_price+""+" ("+percentage+"%)");
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});	    
	}
	private void setupAlertButtonListner() {
		// TODO Auto-generated method stub
		Button button = (Button) findViewById(R.id.setAlertBtn);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String alertP = slider_value.getText().toString();
				int ind = alertP.indexOf("(");
				if(ind > -1)
					alertP = alertP.substring(0, ind);
				Object object[] = new Object[7];
				object[0] = mobile;
				object[1] = deviceID;
				object[2] = regID;
				object[3] = nseid;
				object[4] = String.valueOf(alertP);
				String msg = "";
				if(Double.valueOf(alertP) > curr_stock_price){
					object[5] = "high";
					msg = "A HIGH Stock Alert has been set. You can also set the low alert.";
				}
				else{
					object[5] = "low";
					msg = "A LOW Stock Alert has been set. You can also set the high alert.";
				}
				object[6] = fullid;
				String sResponse;
				try {
					sResponse = new SaveStockAlertAsyncTask()
					.execute(object).get();
					if (sResponse != null && !(sResponse.isEmpty())) {
						System.out.println("sResponse - "+sResponse);
						UtilityActivity.showShortMessage(context, msg, Gravity.TOP);
						//UtilityActivity.showMessage(context, sResponse, Gravity.CENTER);
					}
					//Set the Alert refresh flag so that on ActiveAlertFragment it can refresh the view
					mPrefs = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
					SharedPreferences.Editor editor= mPrefs.edit();
					editor.putBoolean("active_alert_refresh", true);
					editor.commit();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	private void setupAddFavoriteButtonListner() {
		// TODO Auto-generated method stub
		Button button = (Button) findViewById(R.id.setFavBtn);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

                SaveFavorite();

			}
		});
	}

    private void SaveFavorite(){

        String _qty = ((TextView) findViewById(R.id.quantity)).getText().toString();
        String _buy_price = ((TextView) findViewById(R.id.buyPrice)).getText().toString();

        Object object[] = new Object[8];
        object[0] = mobile;
        object[1] = deviceID;
        object[2] = regID;
        object[3] = nseid;
        object[4] = stockname;
        object[5] = fullid;
        object[6] = _qty;
        object[7] = _buy_price;

        String msg = "Stock is added to your favorite screen";
        String sResponse;
        try {
            new SaveStockFavoriteAsyncTask().execute(object);
            UtilityActivity.showShortMessage(context, msg, Gravity.TOP);

            SharedPreferences.Editor editor= mPrefs.edit();
            editor.putBoolean("isFavListDirty", true);
            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	private void setupPortfolioSaveButtonsListner() {
		// TODO Auto-generated method stub
		Button button = (Button) findViewById(R.id.setPortfolioBtn);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

                if(favID == null){
                    SaveFavorite();
                    return;
                }

				String _qty = ((TextView) findViewById(R.id.quantity)).getText().toString();
				String _buy_price = ((TextView) findViewById(R.id.buyPrice)).getText().toString();

				Object object[] = new Object[3];
				object[0] = favID;
				object[1] = _qty;
				object[2] = _buy_price;

				String msg = "Portfolio Details are saved";
				String sResponse;
				try {
					new SavePortfolioDetailsAsyncTask().execute(object);
					UtilityActivity.showShortMessage(context, msg, Gravity.TOP);

					SharedPreferences.Editor editor= mPrefs.edit();
					editor.putBoolean("isFavListDirty", true);
					editor.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void setupDeleteFavoriteButtonListner() {
		// TODO Auto-generated method stub
		Button button = (Button) findViewById(R.id.setFavBtn);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Object object[] = new Object[1];
				//int id = ((Integer)v.getTag(R.id.TAG_PC_ID)).intValue();
				object[0] = favID;
				String msg = "Stock is deleted from your favorite list";
				String sResponse;
				try {
					new DeleteStockFavoriteAsyncTask().execute(object);
					UtilityActivity.showShortMessage(context, msg, Gravity.TOP);

					SharedPreferences.Editor editor= mPrefs.edit();
					editor.putBoolean("isFavListDirty", true);
					editor.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

}
