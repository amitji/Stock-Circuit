package com.abile2.stockcircuit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abile2.stockcircuit.model.StockAlerts;
import com.abile2.stockcircuit.model.StockFinalRating;

import java.util.List;


public class ListAdapterStockFinalRating extends BaseAdapter {

    private Activity activity;
    private List <StockFinalRating> data;
    private static LayoutInflater inflater=null;
    boolean is_element_selected[];


    public ListAdapterStockFinalRating(Activity a, List<StockFinalRating> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        is_element_selected = new boolean[data.size()];
    }

    public int getCount() {
        return data.size();
    }

    public StockFinalRating getItem(int position)
    {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
//Amit
    public void deleteItemAt(int position) {
        data.remove(position);
    }
   
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.stock_final_rating_list_row, null);

        TextView name = (TextView)vi.findViewById(R.id.name); // title
        TextView nseid = (TextView)vi.findViewById(R.id.nseid); // artist name
        TextView rating = (TextView)vi.findViewById(R.id.rating); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        final int listenerPos = position;
        
        StockFinalRating pc = data.get(position);
        
        if (is_element_selected[position]){
            vi.setBackgroundColor(Color.LTGRAY);
        	//thumb_image.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_action_done));
        }
        else {
            vi.setBackgroundColor(Color.TRANSPARENT);
        }

        float perc_rat = Float.valueOf(pc.getPercentage_rating());
        if(perc_rat > 4)
            thumb_image.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_like));
        else
            thumb_image.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_unlike));


        thumb_image.setOnClickListener(new View.OnClickListener() {
        	
            @Override
            public void onClick(View v) {

                //DO NOTHING
            	//this.setItemChecked(position, !this.isItemChecked(position));
            	//toggleSelection(listenerPos);
            }
        });

        
        
        //vi.setTag(R.id.TAG_PC_ID, new Integer(pc.getId()));
        
        // Setting all values in listview
        nseid.setText(pc.getNseid());

        String stockName = pc.getName();
        if(stockName.length() > 20)
            stockName = stockName.substring(0, 20)+"...";

        name.setText(stockName);
        rating.setText(pc.getPercentage_rating());
        return vi;
    }

    public void toggleSelection(int index) {
        is_element_selected[index] = !is_element_selected[index];
        notifyDataSetChanged();
    }
    
    public boolean[] getSelectedItems()
    {
    	return is_element_selected;
    }

    public int getSelectedItemCount()
    {
        int count = 0;
        for(boolean flag: is_element_selected){
            if(flag){
                ++count;
            }
        }

        return count;
    }

}