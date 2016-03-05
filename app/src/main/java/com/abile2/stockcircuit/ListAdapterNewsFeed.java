package com.abile2.stockcircuit;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.abile2.stockcircuit.model.NewsFeedItem;

public class ListAdapterNewsFeed extends ArrayAdapter{
	ArrayList<NewsFeedItem> newsFeedList;
	
	Activity activity;
	private static LayoutInflater inflater = null;

    Context context; 
    int layoutResourceId;    
    NewsFeedItem data[] = null;
    //private Stock originalData[];
    //private Stock filteredData[];    
    
    public ListAdapterNewsFeed(Context context, int layoutResourceId, NewsFeedItem[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;	
        this.data = data;
       
        inflater = (LayoutInflater)context.getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);        
    }
	@Override
	public int getCount() {
		return data.length;
	}
	@Override
	public Object getItem(int position) {
		return data[position];
	}
	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null){
			vi = inflater.inflate(R.layout.news_feed_list_item, parent,false);
		}else{
			vi=convertView;
		}
		
		final TextView headline = (TextView) vi.findViewById(R.id.headline);
		final TextView description = (TextView) vi.findViewById(R.id.description);
		//final TextView link = (TextView) vi.findViewById(R.id.link);
		NewsFeedItem item = data[position];
		headline.setText(item.getTitle());
		description.setText(Html.fromHtml("<html><body>"+item.getDescription()+"</body></html>"));
		

		return vi;
	}

}
