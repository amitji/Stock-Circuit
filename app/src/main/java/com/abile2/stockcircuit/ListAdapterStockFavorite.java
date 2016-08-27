package com.abile2.stockcircuit;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.abile2.stockcircuit.model.Stock;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapterStockFavorite extends BaseAdapter {
    private Activity activity;
    private List <Stock> data;
    private static LayoutInflater inflater=null;
    boolean is_element_selected[];

    public ListAdapterStockFavorite(Activity a, List<Stock> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        is_element_selected = new boolean[data.size()];
    }
    public int getCount() {
        return data.size();
    }
    public Stock getItem(int position) {
        return data.get(position);
    }
    public long getItemId(int position) {
        return Long.valueOf(data.get(position).getId());
    }
//Amit
    public void deleteItemAt(int position) {
        data.remove(position);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.favorite_list_row_item, null);
        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView desc = (TextView)vi.findViewById(R.id.description); // artist name
        TextView weight = (TextView)vi.findViewById(R.id.weightage); // duration
        TextView change = (TextView)vi.findViewById(R.id.change); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        TextView qty = (TextView)vi.findViewById(R.id.qty); // duration
        TextView gainTv = (TextView)vi.findViewById(R.id.gain); // duration

        final int listenerPos = position;
        Stock pc = data.get(position);
//        if (is_element_selected[position]){
//            vi.setBackgroundColor(Color.LTGRAY);
//        	thumb_image.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_action_done));
//        }
        //else{
            vi.setBackgroundColor(Color.TRANSPARENT);
        //}
//        thumb_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            	//this.setItemChecked(position, !this.isItemChecked(position));
//            	//toggleSelection(listenerPos);
//            }
//        });
        vi.setTag(R.id.TAG_PC_ID, new Integer(pc.getId()));
        // Setting all values in listview
        desc.setText(pc.getNseid());
        String stockName = pc.getStockname();
        if(stockName.length() > 20)
        	stockName = stockName.substring(0, 20)+"...";
        title.setText(stockName);
        weight.setText(pc.getCurrentPrice());
        change.setText("["+pc.getChangeStr()+ "]");
        if(pc.getChangeStr().contains("-")){
        	change.setTextColor(Color.parseColor("#ff0000"));
        }else{
        	change.setTextColor(Color.parseColor("#29BA1B"));
        }
        DecimalFormat df = new DecimalFormat("####0.00");
        qty.setText("Qty : "+pc.getQty());
        Double buy_value =  Double.parseDouble(pc.getQty()) * Double.parseDouble(pc.getBuy_price());
        Double gain_loss = Double.parseDouble(pc.getQty()) * (Double.parseDouble(pc.getCurrentPrice()) - Double.parseDouble(pc.getBuy_price()));
        String gain_loss_str = df.format(gain_loss);
        String gl_percent_str = "0.0";
        if(buy_value != 0.0) {
            gl_percent_str = df.format(gain_loss * 100 / buy_value);

        }

        gainTv.setText("Gain : [ "+gain_loss_str+" , "+gl_percent_str+" % ]");
        if(gain_loss_str.contains("-")){
            gainTv.setTextColor(Color.parseColor("#ff0000"));
        }else{
            gainTv.setTextColor(Color.parseColor("#29BA1B"));
        }
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