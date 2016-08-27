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

import com.abile2.stockcircuit.model.StockVideo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class ListAdapterVideos extends BaseAdapter {

    private Activity activity;
    private List <StockVideo> data;
    private static LayoutInflater inflater=null;
    boolean is_element_selected[];
    private HashMap<String, Boolean> listOfSelection;
    private HashMap<String, View> listOfViews ;

    public ListAdapterVideos(Activity a, List<StockVideo> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        is_element_selected = new boolean[data.size()];
        listOfViews = new HashMap<String, View>();
        listOfSelection = new HashMap<String, Boolean>();
    }

    public int getCount() {
        return data.size();
    }

    public StockVideo getItem(int position)
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
            vi = inflater.inflate(R.layout.my_videos__list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title);
        TextView desc = (TextView)vi.findViewById(R.id.description);
        TextView imageTitle = (TextView)vi.findViewById(R.id.imageTitle);

//        TextView weight = (TextView)vi.findViewById(R.id.weightage);
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.video_thumbnail); // thumb image
        final int listenerPos = position;
        
        StockVideo pc = data.get(position);




        //boolean isSelected = listOfSelection.get((pc.getId())).booleanValue();
        if (is_element_selected[position]){
            vi.setBackgroundColor(Color.LTGRAY);
        	//thumb_image.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_action_done));
        }
        else{
            vi.setBackgroundColor(Color.TRANSPARENT);
            	//thumb_image.setImageDrawable(parent.getResources().getDrawable(R.drawable.logo));
        }

        imageTitle.setText(pc.getName());
        Random r = new Random();
        int suffix = r.nextInt(5-1) + 1;
        String tmbImgName = "th"+suffix;
        int resID = activity.getResources().getIdentifier(tmbImgName , "drawable", activity.getPackageName());

        thumb_image.setImageResource(resID);
//        thumb_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {   }      });

        vi.setTag(R.id.TAG_PC_ID, new Integer(pc.getId()));
        
        // Setting all values in listview
       desc.setText(pc.getDescription());

            String stockName = pc.getName();
            if(stockName.length() > 20)
                stockName = stockName.substring(0, 20)+"...";
            title.setText(stockName);

        listOfViews.put(pc.getId(), vi);
        return vi;
    }

    public HashMap<String, View> getListOfViews(){
        return listOfViews;
    }
    public void toggleSelection(int index) {
        is_element_selected[index] = !is_element_selected[index];
        //boolean flag = listOfSelection.get(id).booleanValue();
        //listOfSelection.put(id, new Boolean(!flag));

        notifyDataSetChanged();
    }
    
    public boolean[] getSelectedItems()
    {
    	return is_element_selected;
    }

    public boolean isSelected(int position){
        return is_element_selected[position];
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