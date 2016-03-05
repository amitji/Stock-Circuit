package com.abile2.stockcircuit;

import java.util.ArrayList;
import java.util.HashMap;






import com.abile2.stockcircuit.model.Stock;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StockListAdapter extends ArrayAdapter implements Filterable {
	ArrayList<Stock> stkList;
	
	Activity activity;
	private static LayoutInflater inflater = null;

    Context context; 
    int layoutResourceId;    
    //Stock data[] = null;
    private Stock originalData[];
    private Stock filteredData[];    
    
    public StockListAdapter(Context context, int layoutResourceId, Stock[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;	
        this.originalData = data;
        this.filteredData = data;
        
        inflater = (LayoutInflater)context.getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);        
    }

//	public StockListAdapter(Activity activity,
//			ArrayList<Stock> stkList) {
//		this.stkList = stkList;
//		
//		this.activity = activity;
//		inflater = (LayoutInflater) activity
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	}
	@Override
	public int getCount() {
		return filteredData.length;
	}
	@Override
	public Object getItem(int position) {
		return filteredData[position];
	}
	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null){
			vi = inflater.inflate(R.layout.nse_stock_list_item, parent,false);
		}else{
			vi=convertView;
		}
		
		final TextView stockID = (TextView) vi.findViewById(R.id.stockID);
		final TextView stockName = (TextView) vi.findViewById(R.id.stockName);
		Stock stk = filteredData[position];
		stockID.setText(stk.getNseid());
		stockName.setText(stk.getStockname());

		return vi;
	}

	@Override
    public Filter getFilter()
    {
       return new Filter()
       {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults results = new FilterResults();

                //If there's nothing to filter on, return the original data for your list
                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = originalData;
                    results.count = originalData.length;
                }
                else
                {
                    //Stock filterResultsData[] = new Stock();
                	ArrayList<Stock> stkList = new ArrayList<Stock>();
                    String stk_name;
                    String stk_nseid; 

                    for(Stock stk : originalData)
                    {
                    	stk_name = stk.getStockname();
                    	stk_nseid = stk.getNseid();
                    	if(stk_name.toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                    			stk_nseid.toLowerCase().contains(charSequence.toString().toLowerCase()))
                    	{
                    		stkList.add(stk);
                    	}
                    	
                    }            
                    
                    Stock[] stkArray = new Stock[stkList.size()];
                    stkArray = stkList.toArray(stkArray);
                    results.values = stkArray;
                    results.count = stkArray.length;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                filteredData = (Stock[]) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
	
}
