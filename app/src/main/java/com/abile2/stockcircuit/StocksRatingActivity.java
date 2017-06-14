package com.abile2.stockcircuit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.abile2.stockcircuit.model.StockFinalRating;
import com.abile2.stockcircuit.util.SaveStockFavoriteAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.TreeSet;


public class StocksRatingActivity extends Activity {

    SharedPreferences mPrefs;
    Geocoder geoCoder;
    AutoCompleteTextView sector;
    ListView listview;
    ListAdapterStockFinalRating mAdapter;
    ArrayList<StockFinalRating> stocksRatingList;
    FloatingActionButton showVideoBtn;
    FloatingActionButton compareVideoBtn;
    FloatingActionButton statsBtn;
    FloatingActionButton addToPortfolioBtn;


    String deviceID;
    String regID;
    String mobile;


    Context context = null;
    Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stocks_rating);
        activity = this;
        context = this;

        context = activity;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        deviceID = mPrefs.getString("deviceID", "");
        regID = mPrefs.getString("regID", "");
        mobile = mPrefs.getString("mobile", "");

        mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String stocksRatingStr = mPrefs.getString("stocks_ratings", "");
        stocksRatingList = convertJsonToStockRatingList(stocksRatingStr);
        final TextView noAlerts = (TextView) findViewById(R.id.noAlerts);
        listview = (ListView) findViewById(R.id.activeList);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mAdapter = new ListAdapterStockFinalRating(this, stocksRatingList);
        listview.setAdapter(mAdapter);
        listview.setTextFilterEnabled(true);
        setupFloatingMenu();
        addListItemListner();

    }

    private void setupFloatingMenu() {


        showVideoBtn = (FloatingActionButton) findViewById(R.id.showVideo);
        showVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVideo();
            }
        });

        compareVideoBtn = (FloatingActionButton) findViewById(R.id.compareVideo);
        compareVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compareVideo();
            }
        });

        statsBtn = (FloatingActionButton) findViewById(R.id.stats);
        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStats();
            }
        });

        addToPortfolioBtn = (FloatingActionButton) findViewById(R.id.addToPortfolio);
        addToPortfolioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToPortfolio();
            }
        });

    }

    private void addListItemListner() {
        // TODO Auto-generated method stub
        //ListView lv = getListView();
        ListView lv = (ListView) findViewById(R.id.activeList);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ((ListAdapterStockFinalRating) listview.getAdapter()).toggleSelection(position);

                int sel_count = ((ListAdapterStockFinalRating) listview.getAdapter()).getSelectedItemCount();
//                if (sel_count > 0) {
//                    showVideoBtn.setVisibility(View.VISIBLE);
//                    compareVideoBtn.setVisibility(View.VISIBLE);
////                    statsBtn.setVisibility(View.VISIBLE);
////                    addToPortfolioBtn.setVisibility(View.VISIBLE);
//                } else {
//                    showVideoBtn.setVisibility(View.GONE);
//                    compareVideoBtn.setVisibility(View.GONE);
////                    statsBtn.setVisibility(View.GONE);
////                    addToPortfolioBtn.setVisibility(View.GONE);
//                }

            }
        });

    }

    private void showVideo() {

        StockFinalRating stock = checkIfSingleSelected("video");
        if(stock != null){
            Intent i = new Intent(getApplicationContext(), GetUserRequestedVideoActivity.class);
            i.putExtra("fullid", stock.getFullid());
            startActivity(i);
        }
    }
    private StockFinalRating checkIfSingleSelected(String action)
    {

        boolean noneSelected = true;
        listview = (ListView) findViewById(R.id.activeList);
        boolean[] selItems = ((ListAdapterStockFinalRating) listview.getAdapter()).getSelectedItems(); //mAdapter.getSelectedItems();
        StringBuilder commaSepRecommIds = new StringBuilder();
        int selectedCount = 0;
        StockFinalRating stock = null;
        for (int j = 0; j < selItems.length; j++) {
            boolean isSelected = selItems[j];
            if (isSelected) {
                noneSelected = false;
                ++selectedCount;
                stock = (StockFinalRating) listview.getAdapter().getItem(j);
            }
        }
        if (noneSelected) {
            if(action.equals("stats"))
                UtilityActivity.showMessage(context, "Select at least one Stock for Statistics", Gravity.CENTER);
            else if(action.equals("portfolio"))
                UtilityActivity.showMessage(context, "Select at least one Stock to add to portfolio", Gravity.CENTER);
            else
                UtilityActivity.showMessage(context, "Select at least one Stock to watch Video", Gravity.CENTER);
            //return stock;
        }
        if (selectedCount > 1) {
            if(action.equals("stats"))
                UtilityActivity.showMessage(context, "Please select only one Stock for Statistics", Gravity.CENTER);
            else if(action.equals("portfolio"))
                UtilityActivity.showMessage(context, "Please select only one Stock to add to portfolio", Gravity.CENTER);
            else
                UtilityActivity.showMessage(context, "Please select only one Stock to watch Video", Gravity.CENTER);
            //if two selected than nothing should happen so set stock to null
            stock = null;
            //return stock;
        }
        return stock;
    }

    private void compareVideo() {

        boolean noneSelected = true;
        listview = (ListView) findViewById(R.id.activeList);
        boolean[] selItems = ((ListAdapterStockFinalRating) listview.getAdapter()).getSelectedItems(); //mAdapter.getSelectedItems();
        int selectedCount = 0;
        StockFinalRating[] stocks = new StockFinalRating[3];

        for (int j = 0; j < selItems.length; j++) {
            boolean isSelected = selItems[j];
            if (isSelected) {
                noneSelected = false;

                if (selectedCount < 3)
                    stocks[selectedCount] = (StockFinalRating) listview.getAdapter().getItem(j);
                ++selectedCount;
            }
        }
        if (noneSelected || selectedCount > 3 || selectedCount < 2) {
            UtilityActivity.showMessage(context, "Please select up to 3 stocks to compare", Gravity.CENTER);
            return;
            //return false;
        }
//		if(selectedCount > 3 || selectedCount < 2){
//			UtilityActivity.showMessage(context, "You can select maximum 3 stocks to compare", Gravity.CENTER);
//			return;
//		}

        Intent i = new Intent(getApplicationContext(), GetCompareStocksVideoActivity.class);
        i.putExtra("fullid1", stocks[0].getFullid());
        i.putExtra("fullid2", stocks[1].getFullid());
        if (stocks[2] != null)
            i.putExtra("fullid3", stocks[2].getFullid());
        else
            i.putExtra("fullid3", "");
        startActivity(i);


    }

    private void showStats() {

        StockFinalRating stock = checkIfSingleSelected("stats");
        if(stock != null){
            Intent i = new Intent(context, StockStatisticActivity.class);
            i.putExtra("fullid", stock.getFullid());
            i.putExtra("name", stock.getName());
            startActivity(i);
        }
    }

    private void addToPortfolio() {

        StockFinalRating stock = checkIfSingleSelected("portfolio");
        if(stock != null) {
            Object object[] = new Object[8];
            object[0] = mobile;
            object[1] = deviceID;
            object[2] = regID;
            object[3] = stock.getNseid();
            object[4] = stock.getName();
            object[5] = stock.getFullid();
            object[6] = "";
            object[7] = "";

            String msg = "Stock is added to your portfolio";
            String sResponse;
            try {
                new SaveStockFavoriteAsyncTask().execute(object);
                UtilityActivity.showShortMessage(context, msg, Gravity.TOP);

                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putBoolean("isFavListDirty", true);
                editor.commit();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }


    private ArrayList<StockFinalRating> convertJsonToStockRatingList(String stkRatingStr) {

        Object obj = JSONValue.parse(stkRatingStr);
        org.json.simple.JSONArray array = (org.json.simple.JSONArray) obj;
        ArrayList<StockFinalRating> alertList = new ArrayList<StockFinalRating>();

        for (int i = 0; i < array.size(); i++) {
            StockFinalRating stkRating = new StockFinalRating();
            org.json.simple.JSONObject object = (org.json.simple.JSONObject) array.get(i);
            //stkRating.setId((String) object.get("id"));
            stkRating.setFullid((String) object.get("fullid"));
            stkRating.setNseid((String) object.get("nseid"));
            stkRating.setName((String) object.get("name"));
            stkRating.setPercentage_rating((String) object.get("percentage_rating"));
            alertList.add(stkRating);

        }
        return alertList;
    }

//    private ArrayList<String> getSubSectorList(String industryVerticalsStr, String selected1) {
//
//        TreeSet<String> hashSet = new TreeSet<String>();
//        try {
//            JSONArray getArray = new JSONArray(industryVerticalsStr);
//            for (int i = 0; i < getArray.length(); i++) {
//                JSONObject objects = getArray.getJSONObject(i);
//                //Iterator key = objects.keys();
//                //Stock stk;
//                String industry_sub_vertical = objects.getString("industry_sub_vertical");
//                String industry_vertical = objects.getString("industry_vertical");
//                if (industry_vertical.equals(selected1))
//                    hashSet.add(industry_sub_vertical);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ArrayList<String> list = new ArrayList<String>(hashSet);
//        return list;
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
