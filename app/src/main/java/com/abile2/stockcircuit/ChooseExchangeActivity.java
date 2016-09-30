package com.abile2.stockcircuit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class ChooseExchangeActivity extends AppCompatActivity {

    Context context;
    String type;
    String is_video_list;
    String is_world_indices;
    String exchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_exchange);
        context = this;
        Intent secondInt = getIntent();
        type = secondInt.getStringExtra("type");
        //is_video_list = secondInt.getStringExtra("is_video_list" );
        //is_world_indices = secondInt.getStringExtra("is_world_indices" );
        //exchange = secondInt.getStringExtra("exchange");

        if(type.equals("compare")){
            setupFloatingMenuForCompare();
        }else
        {
            setupFloatingMenu();
        }

        //setupFloatingMenu();
        //final FloatingActionButton us_option = (FloatingActionButton) findViewById(R.id.us_option);
        //us_option.setTitle("NASDAQ Stocks");
    }


    private void setupFloatingMenuForCompare() {

        final FloatingActionButton nse_option = (FloatingActionButton) findViewById(R.id.nse_option);
        nse_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appInfo = new Intent(view.getContext(), CompareStocksVideoActivity.class);
                appInfo.putExtra("is_video_list", "y");
                appInfo.putExtra("exchange", "NSE");
                startActivity(appInfo);
            }
        });

        //Need to chage this...
        final FloatingActionButton us_option = (FloatingActionButton) findViewById(R.id.us_option);
        us_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appInfo = new Intent(view.getContext(), CompareStocksVideoActivity.class);
                appInfo.putExtra("is_video_list", "y");
                appInfo.putExtra("exchange", "NASDAQ");
                startActivity(appInfo);

            }
        });
    }

    private void setupFloatingMenu() {

        final FloatingActionButton nse_option = (FloatingActionButton) findViewById(R.id.nse_option);
        nse_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appInfo = new Intent(view.getContext(), StockListView.class);
                appInfo.putExtra("is_video_list", "y");
                appInfo.putExtra("exchange", "NSE");
                startActivity(appInfo);
            }
        });

        //Need to chage this...
        final FloatingActionButton us_option = (FloatingActionButton) findViewById(R.id.us_option);
        us_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appInfo = new Intent(view.getContext(), StockListView.class);
                //appInfo.putExtra("is_world_indices", is_world_indices);
                appInfo.putExtra("is_video_list", "y");
                appInfo.putExtra("exchange", "NASDAQ");
                startActivity(appInfo);

            }
        });
    }


}
