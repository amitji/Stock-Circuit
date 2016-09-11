package com.abile2.stockcircuit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abile2.stockcircuit.model.StockAlerts;
import com.abile2.stockcircuit.model.StockVideo;
import com.abile2.stockcircuit.util.GetAlertsForAUserAsyncTask;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AbstractFragment extends Fragment {

	String deviceID;
	String regID;
	String city;
	String mobile;

	//ArrayList<StockAlerts> allAlerts;
//	public void onCreate (Bundle savedInstanceState){
//
//		super.onCreate(savedInstanceState);
//
//	}
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//							 Bundle savedInstanceState) {
//		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//		return super.onCreateView(inflater,container,savedInstanceState);
//
//	}
public void restartParentActivity()
{
	
	 Intent intent = getActivity().getIntent();
	 getActivity().finish();
	 startActivity(intent);
}
	
	@Override
	public void onResume() {
	    //datasource.open();
	    super.onResume();
	    //restartParentActivity();
	  }

	  @Override
public void onPause() {
    //datasource.close();
    super.onPause();
  }	
    public void onDestroy() {        
        
        super.onDestroy();
        
    }
	protected ArrayList<StockAlerts> getUserAlertsFromDB(	String deviceID,String regID) {
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

	public ArrayList<StockVideo> convertJsonToStockVideoList(String str) {

		Object obj = JSONValue.parse(str);
		JSONArray array=(JSONArray)obj;
		ArrayList<StockVideo> alertList = new ArrayList<StockVideo>();

		for (int i = 0; i < array.size(); i++) {
			StockVideo stkVideo = new StockVideo();
			JSONObject object = (JSONObject) array.get(i);
			stkVideo.setId((String) object.get("id"));
			stkVideo.setNseid((String) object.get("nse_id"));
			stkVideo.setFullid((String) object.get("fullid"));
			stkVideo.setName((String) object.get("name"));

			stkVideo.setDescription((String) object.get("description"));
			stkVideo.setVideo_url((String) object.get("video_url"));
			stkVideo.setThumbnail_url((String) object.get("thumbnail_url"));
			stkVideo.setDisplay_seq((String) object.get("display_seq"));
			stkVideo.setShared_by((String) object.get("shared_by"));

			alertList.add(stkVideo);

		}
		return alertList;
	}

	public ArrayList<StockAlerts> getAlerts(String deviceID,String regID,boolean refresh){

			return getUserAlertsFromDB(deviceID,regID);

	}

}
