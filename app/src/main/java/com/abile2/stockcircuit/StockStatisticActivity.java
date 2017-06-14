package com.abile2.stockcircuit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.abile2.stockcircuit.util.GetStocksStatisticsAsynTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeSet;


public class StockStatisticActivity extends AppCompatActivity {

    SharedPreferences mPrefs;
    String deviceID;
    String regID;
    String mobile;
    String fullid;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_statistic);

        Intent secondInt = getIntent();
        fullid = secondInt.getStringExtra("fullid");
        name= secondInt.getStringExtra("name");
        TextView title = (TextView)findViewById(R.id.asklabel);
        title.setText(name+" Statistics");

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        deviceID = mPrefs.getString("deviceID","");
        regID = mPrefs.getString("regID", "");
        mobile = mPrefs.getString("mobile", "");
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        populateStatsData();


    }

    private void populateStatsData() {

        Object object[] = new Object[4];
        object[0] = fullid;
        //object[0] = "NSE:INFY";
        object[1] = mobile;
        object[2] = deviceID;
        object[3] = regID;
        String sResponse="";
        try {
            sResponse = new GetStocksStatisticsAsynTask(getApplicationContext()).execute(object).get();
            System.out.println("\n\n*** sResponse - "+sResponse);
            SharedPreferences.Editor editor= mPrefs.edit();
            editor.putString("stocks_ratings", sResponse);
            editor.commit();

            //HashMap<String, String> statsDataMap= UtilityActivity.getMapforJsonString(sResponse);
            JSONArray main = new JSONArray(sResponse);
            JSONArray quatData = (JSONArray)((JSONObject)(main.get(0))).get("quatData");

            TableLayout table = (TableLayout)findViewById(R.id.table);
            table.removeAllViews();


            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            int totalRows = 7;
            TableRow[] tableRow = new TableRow[totalRows];
            for(int i=0; i<totalRows; i++){
                tableRow[i] = (TableRow)inflater.inflate(R.layout.stock_quarterly_number_row, null);
            }

            TextView row_title = (TextView)tableRow[0].findViewById(R.id.row_title);
            row_title.setText("In Cr.");
            row_title = (TextView)tableRow[1].findViewById(R.id.row_title);
            row_title.setText("Revenue");
            row_title = (TextView)tableRow[2].findViewById(R.id.row_title);
            row_title.setText("Profit");
             row_title = (TextView)tableRow[3].findViewById(R.id.row_title);
            row_title.setText("Revenue Growth %");
             row_title = (TextView)tableRow[4].findViewById(R.id.row_title);
            row_title.setText("Profit Growth %");
             row_title = (TextView)tableRow[5].findViewById(R.id.row_title);
            row_title.setText("Profit Margin %");
             row_title = (TextView)tableRow[6].findViewById(R.id.row_title);
            row_title.setText("Op Profit Margin %");
            //TableRow row = (TableRow)inflater.inflate(R.layout.stock_quarterly_number_row, null);

            //get the row divider
            //LinearLayout ll = (LinearLayout)tableRow[0].findViewById(R.id.row_divider);

            for (int rowNo=0; rowNo<totalRows; rowNo++) {
                for(int i = 5; i > 0; i-- ) {
                    JSONObject jo = (JSONObject) (quatData.get(5-i));
                    JSONObject oneColData = (JSONObject)jo.get(i + "");
                    String colName = "col_q"+i;
                    int resID = getResources().getIdentifier(colName, "id", getPackageName());
                    if(rowNo==0)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("quater_name"));
                    else if(rowNo==1)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("revenueC"));
                    else if(rowNo==2)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("profitC"));
                    else if(rowNo==3)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("revenue_growth_rate"));
                    else if(rowNo==4)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("profit_growth_rate"));
                    else if(rowNo==5)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("profit_margin"));
                    else if(rowNo==6)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("operating_profit_margin"));


                }
            }

            for(int i=0; i<totalRows; i++){
                table.addView(tableRow[i]);
            }


            TableLayout fintable = (TableLayout)findViewById(R.id.fintable);
            fintable.removeAllViews();


            JSONObject oneFinData = (JSONObject)((JSONObject)(main.get(1))).get("finRatio");
            //JSONObject oneFinData = (JSONObject)finRatioData.get(0);
            int finTotalRows = 7;
            TableRow[] finTableRows = new TableRow[finTotalRows];
            for(int i=0; i<finTotalRows; i++){
                finTableRows[i] = (TableRow)inflater.inflate(R.layout.stock_ratio_numbers_row, null);
            }

            TextView fin_row_title = (TextView)finTableRows[0].findViewById(R.id.fin_row_title);
            fin_row_title.setText("ROE %");
            TextView fin_value = (TextView)finTableRows[0].findViewById(R.id.ratio_value);
            fin_value.setText(oneFinData.getString("roe"));


            fin_row_title = (TextView)finTableRows[1].findViewById(R.id.fin_row_title);
            fin_row_title.setText("P/B");
            fin_value = (TextView)finTableRows[1].findViewById(R.id.ratio_value);
            fin_value.setText(oneFinData.getString("pb"));

            fin_row_title = (TextView)finTableRows[2].findViewById(R.id.fin_row_title);
            fin_row_title.setText("P/E");
            fin_value = (TextView)finTableRows[2].findViewById(R.id.ratio_value);
            fin_value.setText(oneFinData.getString("pe"));

            fin_row_title = (TextView)finTableRows[3].findViewById(R.id.fin_row_title);
            fin_row_title.setText("EPS (Rs)");
            fin_value = (TextView)finTableRows[3].findViewById(R.id.ratio_value);
            fin_value.setText(oneFinData.getString("eps_ttm"));

            fin_row_title = (TextView)finTableRows[4].findViewById(R.id.fin_row_title);
            fin_row_title.setText("Debt (Cr)");
            fin_value = (TextView)finTableRows[4].findViewById(R.id.ratio_value);
            fin_value.setText(oneFinData.getString("debt"));

            fin_row_title = (TextView)finTableRows[5].findViewById(R.id.fin_row_title);
            fin_row_title.setText("Interest (Cr)");
            fin_value = (TextView)finTableRows[5].findViewById(R.id.ratio_value);
            fin_value.setText(oneFinData.getString("interest"));

            fin_row_title = (TextView)finTableRows[6].findViewById(R.id.fin_row_title);
            fin_row_title.setText("Interest Coverage");
            fin_value = (TextView)finTableRows[6].findViewById(R.id.ratio_value);
            fin_value.setText(oneFinData.getString("interest_cover"));

            for(int i=0; i<finTotalRows; i++){

                fintable.addView(finTableRows[i]);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


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
//                if(industry_vertical.equals(selected1))
//                    hashSet.add(industry_sub_vertical);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ArrayList<String> list = new ArrayList<String>(hashSet);
//        return list;
//
//    }
}
