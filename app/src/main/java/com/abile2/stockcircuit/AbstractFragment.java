package com.abile2.stockcircuit;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class AbstractFragment extends Fragment {
	
	
	
public void restartParentActivity()
{
	
	 Intent intent = getActivity().getIntent();
	 getActivity().finish();
	 startActivity(intent);
}
	
	@Override
	public void onResume() {
	    //datasource.open();
	    super.onResume();
	    //restartParentActivity();
	  }

	  @Override
public void onPause() {
    //datasource.close();
    super.onPause();
  }	
    public void onDestroy() {        
        
        super.onDestroy();
        
    }

}
