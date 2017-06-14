package com.abile2.stockcircuit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.abile2.stockcircuit.util.GetStockForecastAsynTask;
import com.abile2.stockcircuit.util.GetStocksStatisticsAsynTask;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeSet;


public class StockForecastActivity extends AppCompatActivity {

    SharedPreferences mPrefs;
    String deviceID;
    String regID;
    String mobile;
    String fullid;
    String name;

    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_forecast);

        UtilityActivity.hideSoftKeyboard(this);
        Intent secondInt = getIntent();
        fullid = secondInt.getStringExtra("fullid");
        name= secondInt.getStringExtra("name");
        TextView title = (TextView)findViewById(R.id.stock_name);
        title.setText(name+" price projection for FY 2017 & FY 2018");

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        deviceID = mPrefs.getString("deviceID","");
        regID = mPrefs.getString("regID", "");
        mobile = mPrefs.getString("mobile", "");
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        TableLayout table1 = (TableLayout)findViewById(R.id.fintable);
        //TableLayout table2 = (TableLayout)findViewById(R.id.forcastTable2);

        String sResponse = getFutureStocksPriceData();
        populateForecastData(table1, sResponse);
        //populateForecastData(table2);


        createChart(sResponse);

    }
    private String getFutureStocksPriceData(){

        Object object[] = new Object[4];
        object[0] = fullid;
        //object[0] = "NSE:INFY";
        object[1] = mobile;
        object[2] = deviceID;
        object[3] = regID;
        String sResponse="";
        try {
            sResponse = new GetStockForecastAsynTask(getApplicationContext()).execute(object).get();
            System.out.println("\n\n*** sResponse - " + sResponse);
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("stock_forcast", sResponse);
            editor.commit();

            //HashMap<String, String> statsDataMap= UtilityActivity.getMapforJsonString(sResponse);

            //JSONArray quatData = (JSONArray)((JSONObject)(main.get(0))).get("quatData");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sResponse;
    }
    private void populateForecastData(TableLayout table, String sResponse) {


            //JSONArray fcData = new JSONArray(sResponse);
            //JSONObject fcData = new JSONObject(sResponse);
            //table.removeAllViews();




            /*
            int totalRows = 9;
            TableRow[] tableRow = new TableRow[totalRows];
            for(int i=0; i<totalRows; i++){
                tableRow[i] = (TableRow)inflater.inflate(R.layout.stock_forecast_table_row, null);
            }

            TextView row_title = (TextView)tableRow[0].findViewById(R.id.row_title);
            row_title.setText("Forecast->");
            row_title = (TextView)tableRow[1].findViewById(R.id.row_title);
            row_title.setText("Profit");
            row_title = (TextView)tableRow[2].findViewById(R.id.row_title);
            row_title.setText("Profit Growth %");
             row_title = (TextView)tableRow[3].findViewById(R.id.row_title);
            row_title.setText("Best Case EPS");
             row_title = (TextView)tableRow[4].findViewById(R.id.row_title);
            row_title.setText("Worst Case EPS");
             row_title = (TextView)tableRow[5].findViewById(R.id.row_title);
            row_title.setText("Best Case P/E");
             row_title = (TextView)tableRow[6].findViewById(R.id.row_title);
            row_title.setText("Worst Case P/E");
            row_title = (TextView)tableRow[7].findViewById(R.id.row_title);
            row_title.setText("Best Case Stock Price");
            row_title = (TextView)tableRow[8].findViewById(R.id.row_title);
            row_title.setText("Worst Case Stock Price");

            for (int rowNo=0; rowNo<totalRows; rowNo++) {
                for(int i = 4; i > 0; i-- ) {
                    JSONObject oneColData = (JSONObject) (fcData.get(4-i));
                    //JSONObject oneColData = (JSONObject)jo.get(i + "");
                    String colName = "col_q"+i;
                    int resID = getResources().getIdentifier(colName, "id", getPackageName());
                    if(rowNo==0)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("months"));
                    else if(rowNo==1)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("profit"));
                    else if(rowNo==2)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("profit_growth_rate"));
                    else if(rowNo==3)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("eps_best"));
                    else if(rowNo==4)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("eps_worst"));
                    else if(rowNo==5)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("pe_best"));
                    else if(rowNo==6)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("pe_worst"));
                    else if(rowNo==7)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("price_best"));
                    else if(rowNo==8)
                        ((TextView)tableRow[rowNo].findViewById(resID)).setText((String)oneColData.get("price_worst"));


                }
            }


            for(int i=0; i<totalRows; i++){
                table.addView(tableRow[i]);
            }
            */

            //2017 table
            TableLayout fintable = (TableLayout)findViewById(R.id.fintable);
            fintable.removeAllViews();
            createTable( fintable,  sResponse, "cy");
            //2018 table
            TableLayout fintable2 = (TableLayout)findViewById(R.id.fintable2);
            fintable2.removeAllViews();
            createTable( fintable2,  sResponse, "ny");




    }
    private void createTable (TableLayout table, String sResponse, String year_prefix){
        try{
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                JSONObject oneFinData = new JSONObject(sResponse);
                //JSONObject oneFinData = (JSONObject)finRatioData.get(0);
                int finTotalRows = 6;
                TableRow[] finTableRows = new TableRow[finTotalRows];
                for(int i=0; i<finTotalRows; i++){
                    finTableRows[i] = (TableRow)inflater.inflate(R.layout.stock_ratio_numbers_row, null);
                }

                TextView fin_row_title = (TextView)finTableRows[0].findViewById(R.id.fin_row_title);
                fin_row_title.setText("Current Stock Price");
                TextView fin_value = (TextView)finTableRows[0].findViewById(R.id.ratio_value);
                fin_value.setText(oneFinData.getString("current_price"));


                fin_row_title = (TextView)finTableRows[1].findViewById(R.id.fin_row_title);
                fin_row_title.setText("Best Case Price");
                fin_value = (TextView)finTableRows[1].findViewById(R.id.ratio_value);
                fin_value.setText(oneFinData.getString(year_prefix+"_best_price")+" ["+oneFinData.getString(year_prefix+"bp_change_perct")+" %]");

                fin_row_title = (TextView)finTableRows[2].findViewById(R.id.fin_row_title);
                fin_row_title.setText("Worst Case Price");
                fin_value = (TextView)finTableRows[2].findViewById(R.id.ratio_value);
                fin_value.setText(oneFinData.getString(year_prefix+"_worst_price")+" ["+oneFinData.getString(year_prefix+"wp_change_perct")+" %]");

                fin_row_title = (TextView)finTableRows[3].findViewById(R.id.fin_row_title);
                fin_row_title.setText("Ttm EPS");
                fin_value = (TextView)finTableRows[3].findViewById(R.id.ratio_value);
                fin_value.setText(oneFinData.getString("ttm_eps"));

                fin_row_title = (TextView)finTableRows[4].findViewById(R.id.fin_row_title);
                fin_row_title.setText("Best EPS");
                fin_value = (TextView)finTableRows[4].findViewById(R.id.ratio_value);
                fin_value.setText(oneFinData.getString(year_prefix+"_best_eps"));

                fin_row_title = (TextView)finTableRows[5].findViewById(R.id.fin_row_title);
                fin_row_title.setText("Worst EPS");
                fin_value = (TextView)finTableRows[5].findViewById(R.id.ratio_value);
                fin_value.setText(oneFinData.getString(year_prefix+"_worst_eps"));


                for(int i=0; i<finTotalRows; i++){

                    table.addView(finTableRows[i]);
                }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void createChart ( String sResponse){

        HashMap<String, String> params = UtilityActivity.getMapforJsonString(sResponse);

        plot = (XYPlot) findViewById(R.id.plot);

        //final String[] domainLabels = {"Q416","Q117","Q217","Q317","Q417" };
        final Number[] domainLabels = {0,1,2};
        Number[] series1Numbers = {Double.valueOf(params.get("current_price")),Double.valueOf(params.get("cy_best_price")),Double.valueOf(params.get("ny_best_price")) };
        Number[] series2Numbers = {Double.valueOf(params.get("current_price")),Double.valueOf(params.get("cy_worst_price")),Double.valueOf(params.get("ny_worst_price")) };


        plot.setDomainBoundaries(0,2.4, BoundaryMode.FIXED );
        plot.setDomainStep(StepMode.INCREMENT_BY_VAL,1);

        //plot.getGraph().getLineLabelInsets().setBottom(PixelUtils.dpToPix(-5));  //done in xml

        float rangeStart = getMinMax(series1Numbers,series2Numbers,0 );
        float rangePreEnd = getMinMax(series1Numbers,series2Numbers,1 );
        float rangeStep = (rangePreEnd - rangeStart)/5;
        rangeStart = rangeStart-rangeStep;
        float rangeEnd = rangePreEnd+ rangeStep;
        plot.setRangeBoundaries(rangeStart, rangeEnd, BoundaryMode.FIXED );
        plot.setRangeStep(StepMode.INCREMENT_BY_VAL, rangeStep);
        //plot.setLinesPerRangeLabel(1);

        setXYBoundries(plot,domainLabels );
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Best Price");
        XYSeries series2 = new SimpleXYSeries(
                Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Worst Price");

        LineAndPointFormatter series1Format = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);
        LineAndPointFormatter series2Format = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_2);


        float vOffset = -20;
        float hOffset = 40;
        PointLabelFormatter plf = new PointLabelFormatter(this.getResources().getColor(R.color.white) );
        plf.getTextPaint().setTextSize(PixelUtils.spToPix(12));
        plf.vOffset=vOffset;
        plf.hOffset=hOffset;

        //plf.getTextPaint().setColor(this.getResources().getColor(R.color.yellow));
        series1Format.setPointLabelFormatter(plf);

        PointLabelFormatter plf2 = new PointLabelFormatter(this.getResources().getColor(R.color.white) );
        plf2.getTextPaint().setTextSize(PixelUtils.spToPix(12));
        plf2.vOffset=vOffset;
        plf2.hOffset=hOffset;
        //plf2.getTextPaint().setColor(this.getResources().getColor(R.color.blue_1));
        series2Format.setPointLabelFormatter(plf2);

        // add an "dash" effect to the series2 line:
        //Amit
        /*
        series2Format.getLinePaint().setPathEffect(new DashPathEffect(new float[] {

                // always use DP when specifying pixel sizes, to keep things consistent across devices:
                PixelUtils.dpToPix(20),
                PixelUtils.dpToPix(15)}, 0));
*/
        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/

        /* Amit
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        series2Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
*/

        plot.addSeries(series1, series1Format);
        plot.addSeries(series2, series2Format);


        //This gets rid of the gray grid
        plot.getGraph().setGridBackgroundPaint(new Paint(Color.TRANSPARENT));

        //This gets rid of the black border (up to the graph) there is no black border around the labels
        //plot.getBackgroundPaint().setColor(Color.TRANSPARENT);

        //This gets rid of the black behind the graph
        //plot.getGraph().getBackgroundPaint().setColor(Color.TRANSPARENT);

        //With a new release of AndroidPlot you have to also set the border paint
        plot.getGraph().getDomainGridLinePaint().setColor(Color.TRANSPARENT);
        plot.getGraph().getRangeGridLinePaint().setColor(Color.TRANSPARENT);

        /*
        //change fill for sort of animation
        boolean change_fill = true;
        while(change_fill){
            try {
                Thread.sleep(5000);
                Paint lineFill = new Paint();
                //lineFill.setAlpha(200);
                lineFill.setColor(this.getResources().getColor(R.color.pink));
                //lineFill.setShader(new LinearGradient(0, 0, 0, 250, Color.WHITE, Color.GREEN, Shader.TileMode.MIRROR));

                series1Format.setFillPaint(lineFill);
                change_fill  = false;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */
    }

    public float getMinMax(Number[] series1, Number[] series2, int type){

        //float[] convertedValues = new int[10];
        float max=series1[0].floatValue();
        if(type==1) { //get Max


            for (int i = 0; i < series1.length; i++) {
                if (series1[i].floatValue() > max) {
                    max = series1[i].floatValue();
                }
            }

            for (int i = 0; i < series2.length; i++) {
                if (series2[i].floatValue() > max) {
                    max = series2[i].floatValue();
                }
            }
        }

        else if(type==0) { //get Min


            for (int i = 0; i < series1.length; i++) {
                if (series1[i].floatValue() < max) {
                    max = series1[i].floatValue();
                }
            }

            for (int i = 0; i < series2.length; i++) {
                if (series2[i].floatValue() < max) {
                    max = series2[i].floatValue();
                }
            }

        }
    return max;
    }
    //public void setXYBoundries(XYPlot plot,Number[] domainLabels,XYSeries series )
    public void setXYBoundries(XYPlot plot,final Number[] domainLabels)
    {

        //final String[] domainLabelsStrings = {"Q416","Q117","Q217","Q317","Q417" };
        final String[] domainLabelsStrings = {"Now","2017 FY","2018 FY"," "};
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {

            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                Object tempObj = obj;
                float temp = ((Number) obj).floatValue();
                int i = Math.round(temp);
                return toAppendTo.append(domainLabelsStrings[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

    }
}
