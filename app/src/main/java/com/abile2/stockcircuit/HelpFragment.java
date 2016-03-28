package com.abile2.stockcircuit;


import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HelpFragment extends ListFragment {

	//@Override
	//public void onViewCreated(View view, Bundle savedInstanceState) {
		//super.onViewCreated(view, savedInstanceState);
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	    String[] instructions = getResources().getStringArray(R.array.instructions);
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.instructions_list,R.id.inst, Arrays.asList(instructions));
	    setListAdapter(adapter);
	    return super.onCreateView(inflater, container, savedInstanceState);
	}

	
	public void restartParentActivity()
	{
		
		 Intent intent = getActivity().getIntent();
		 getActivity().finish();
		 startActivity(intent);
	}
	
	  public void onDestroy() {        
	        
        super.onDestroy();
        //restartParentActivity();
        
    }
	
}
