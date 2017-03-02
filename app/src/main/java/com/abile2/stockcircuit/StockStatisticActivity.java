package com.abile2.stockcircuit;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StockStatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_statistic);
        TableLayout table = (TableLayout)findViewById(R.id.table);

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TableRow rowView1 = (TableRow)inflater.inflate(R.layout.stock_statistic, null);
        TableRow rowView2 = (TableRow)inflater.inflate(R.layout.stock_statistic, null);
        TableRow rowView3 = (TableRow)inflater.inflate(R.layout.stock_statistic, null);
        TableRow rowView4 = (TableRow)inflater.inflate(R.layout.stock_statistic, null);
        TableRow rowView5 = (TableRow)inflater.inflate(R.layout.stock_statistic, null);
        TableRow rowView6 = (TableRow)inflater.inflate(R.layout.stock_statistic, null);

        table.addView(rowView1);
        table.addView(rowView2);
        table.addView(rowView3);
        table.addView(rowView4);
        table.addView(rowView5);
        table.addView(rowView6);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
