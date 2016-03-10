package com.abile2.stockcircuit;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AbstractListActivity extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_main_actions, menu);
	    MenuItem item2 = menu.findItem(R.id.action_discard);
	    item2.setVisible(false);
	    return true;
	}
	   @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Take appropriate action for each action item click
		   Intent i;
	        switch (item.getItemId()) {
//	        case R.id.action_help:
//	        	i = new Intent(getApplicationContext(), InstructionsView.class);
//	        	startActivity(i);
//	            return true;
	        case R.id.action_menu:
	        	 i = new Intent(MyApp.Context(), BrokerDetailsActivity.class);
	        	 //i.putExtra("showmessage", "false");
	        	 startActivity(i);
	        	 return true;

	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }	

}
