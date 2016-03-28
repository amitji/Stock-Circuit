package com.abile2.stockcircuit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackFragment extends AbstractFragment {
	View rootView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_feedback, container,false);
		final TextView name = (TextView) rootView.findViewById(R.id.name);
		final TextView mobile = (TextView) rootView.findViewById(R.id.mobile);
		final TextView city = (TextView) rootView.findViewById(R.id.city);
		final TextView message = (TextView) rootView.findViewById(R.id.message);
		final Button sendFeedback = (Button) rootView.findViewById(R.id.sendFeedback);
		sendFeedback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(name.getText().toString().trim().equals("")){
					 Toast.makeText(getActivity(), "Name can't be blank.",Toast.LENGTH_SHORT).show();
					 return;
				 }  
				if(mobile.getText().toString().trim().equals("")){
					 Toast.makeText(getActivity(), "Mobile number can't be blank.",Toast.LENGTH_SHORT).show();
					 return;
				  }
				  if(message.getText().toString().trim().equals("")){
						 Toast.makeText(getActivity(), "Message can't be blank.",Toast.LENGTH_SHORT).show();
						 return;
			      }
                 String msg="Name : " + name.getText()+" \n \n Mobile Number :"+mobile.getText()
                		 +"\n \n City :"+city.getText() + " \n \n Message  : "+ message.getText(); 
				Intent send = new Intent(Intent.ACTION_SEND);
				send.setType("message/rfc822");
				send.putExtra(Intent.EXTRA_EMAIL,new String[] {"sahil.mahe@gmail.com",});
				send.putExtra(Intent.EXTRA_SUBJECT, "Feedback to Stock Circuit");
				send.putExtra(Intent.EXTRA_TEXT, msg);
				startActivity(Intent.createChooser(send, "Choose an Email Provider."));
			    //((MainActivity)getActivity()).displayView(0, false);
//              MainMapFragment fragment=new MainMapFragment();
//				FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
//				FragmentTransaction ft = fragmentManager.beginTransaction();
//				ft.replace(R.id.frame_container,fragment, fragment.getClass().getName());
//				ft.commit();
			 }
		});
		return rootView;
	}
	@Override
	public void onResume(){
		super.onResume();
	}
}
