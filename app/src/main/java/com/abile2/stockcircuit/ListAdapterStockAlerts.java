package com.abile2.stockcircuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.abile2.stockcircuit.model.StockAlerts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;





public class ListAdapterStockAlerts extends BaseAdapter {
    
    private Activity activity;
    private List <StockAlerts> data;
    private static LayoutInflater inflater=null;
    boolean is_element_selected[];

    
    public ListAdapterStockAlerts(Activity a, List<StockAlerts> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        is_element_selected = new boolean[data.size()];
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
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
            vi = inflater.inflate(R.layout.user_alerts_list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView desc = (TextView)vi.findViewById(R.id.description); // artist name
        TextView weight = (TextView)vi.findViewById(R.id.weightage); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        final int listenerPos = position;
        
        StockAlerts pc =  (StockAlerts)data.get(position);
        
        if (is_element_selected[position]){
            vi.setBackgroundColor(Color.LTGRAY);
        	thumb_image.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_action_done));
        }
        else{
            vi.setBackgroundColor(Color.TRANSPARENT);
            if(pc.getLow_high().equals("low"))
            	thumb_image.setImageDrawable(parent.getResources().getDrawable(R.drawable.red));
            else
            	thumb_image.setImageDrawable(parent.getResources().getDrawable(R.drawable.green));
        }
        thumb_image.setOnClickListener(new View.OnClickListener() {
        	
            @Override
            public void onClick(View v) {
            	//this.setItemChecked(position, !this.isItemChecked(position));
            	toggleSelection(listenerPos);
            }
        });
        
        
        
        vi.setTag(R.id.TAG_PC_ID, new Integer(pc.getId()));
        
        // Setting all values in listview
        title.setText(pc.getNseid());
        if(pc.getAlert_price() != null)
        	desc.setText(pc.getName());
        
        if(pc.getIs_active() != null)
        	weight.setText(pc.getAlert_price());
        //thumb_image.setImageDrawable(parent.getResources().getDrawable(R.drawable.one_person));
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
}