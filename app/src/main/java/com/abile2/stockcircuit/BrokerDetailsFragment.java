package com.abile2.stockcircuit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abile2.stockcircuit.util.UserSettingsAsyncTask;

public class BrokerDetailsFragment extends AbstractFragment {

    SharedPreferences mPrefs;	
    String user_id;
    //View rootView;
    Context context;
    String deviceID;
    String regID;
    String mobile;
    String city;
    
    View rootView;
    
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.broker_details, container, false);
	    
 
	    context = getActivity();
		mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		deviceID = mPrefs.getString("deviceID","");
		regID = mPrefs.getString("regID", "");
		mobile = mPrefs.getString("mobile", "");
		city = mPrefs.getString("city", "");
		setUpInitialValues();
		
		addSaveButtonListner();
		return rootView;

    }
	private void setUpInitialValues() {
		// TODO Auto-generated method stub

		String brokerMobile1 = mPrefs.getString("brokerMobile1","");
		String brokerName1 = mPrefs.getString("brokerName1", "");
		String brokerMobile2 = mPrefs.getString("brokerMobile2", "");
		String brokerName2 = mPrefs.getString("brokerName2", "");
		String brokerWebsite = mPrefs.getString("brokerWebsite", "");
		
		((EditText) rootView.findViewById(R.id.brokerMobile1)).setText(brokerMobile1);
        ((EditText) rootView.findViewById(R.id.brokerName1)).setText(brokerName1);
        //((EditText) findViewById(R.id.brokerMobile2)).setText(brokerMobile2);
        //((EditText) findViewById(R.id.brokerName2)).setText(brokerName2);
        ((EditText) rootView.findViewById(R.id.brokerWebsite)).setText(brokerWebsite);

			
			
	}
	private void addSaveButtonListner() {
		// TODO Auto-generated method stub

		Button button = (Button) rootView.findViewById(R.id.savebutton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

		           String brokerMobile1 = ((EditText) rootView.findViewById(R.id.brokerMobile1)).getText().toString();
		           String brokerName1 = ((EditText) rootView.findViewById(R.id.brokerName1)).getText().toString();
//		           String brokerMobile2 = ((EditText) findViewById(R.id.brokerMobile2)).getText().toString();
//		           String brokerName2 = ((EditText) findViewById(R.id.brokerName2)).getText().toString();
		           String brokerMobile2 = "";
		           String brokerName2 = "";

		           String brokerWebsite = ((EditText) rootView.findViewById(R.id.brokerWebsite)).getText().toString();
		           boolean matches = brokerWebsite.startsWith("http://") || brokerWebsite.startsWith("https://") ;
		           if(!matches)
		           {
		        	   brokerWebsite = "http://"+brokerWebsite;
		        	   
		           }
		           
//		           if(userMobile.equals("") || !isValidMobile(userMobile)){    
//						Toast.makeText(context.getApplicationContext(), "Please enter a valid Mobile number", Toast.LENGTH_SHORT).show();
//					    return;
//					 }else if(userEmail.equals("") || !isValidMail(userEmail)){
//							Toast.makeText(context.getApplicationContext(), "Please enter a valid Email", Toast.LENGTH_SHORT).show();
//						    return;
//						 
//					 }else if(userName.equals("")){
//							Toast.makeText(context.getApplicationContext(), "Name cannot be blank", Toast.LENGTH_SHORT).show();
//						    return;
//						 
//					 }
		   		deviceID = mPrefs.getString("deviceID","");
				regID = mPrefs.getString("regID", "");
				mobile = mPrefs.getString("mobile", "");
				city = mPrefs.getString("city", "");
				
					SharedPreferences.Editor editor = mPrefs.edit();
					
					editor.putString("brokerMobile1", brokerMobile1);
					editor.putString("brokerName1",brokerName1);
					editor.putString("brokerMobile2", brokerMobile2);
					editor.putString("brokerName2", brokerName2);
					editor.putString("brokerWebsite",brokerWebsite);
					editor.commit();
		           
				       Object[]  formList = new Object[9];
			           
			           formList[0] = regID;
			           formList[1] = city;
			           formList[2] = deviceID;
			           formList[3] = mobile;
			           formList[4] = brokerMobile1;
			           formList[5] = brokerName1;
			           formList[6] = brokerMobile2;
			           formList[7] = brokerName2;
			           formList[8] = brokerWebsite;

			           try {
			            	//ImageView bitImage = 
			            	String sResponse = new UserSettingsAsyncTask().execute(formList).get();
			    			if(sResponse != null && !(sResponse.isEmpty()))
			    			{
			    				String[] result = sResponse.split(",");
			            		Toast.makeText(context.getApplicationContext(), "Broker Details saved successfully", Toast.LENGTH_SHORT).show();
			        				Intent i = new Intent(context, MainActivity.class);
			        				startActivity(i);
			        				//finish();
			    			}
			            	else
			            	{
			            		Toast.makeText(context.getApplicationContext(), "Could not save Broker Details", Toast.LENGTH_SHORT).show();
			            	}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					
				}
				
		});

		
	}
	private boolean isValidMobile(String phone) 
	{
	    return android.util.Patterns.PHONE.matcher(phone).matches();   
	}
	private boolean isValidMail(String email)
	{
	   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}	
	private void save(final boolean isChecked) {

	}

	private boolean load() { 
		return mPrefs.getBoolean("notificationsStatus", true);
	}
	@Override
    public void onStop() {
   	// TODO Auto-generated method stub
    	super.onStop();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    
}
