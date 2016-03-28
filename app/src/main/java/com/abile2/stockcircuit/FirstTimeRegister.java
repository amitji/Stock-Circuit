package com.abile2.stockcircuit;

import com.abile2.stockcircuit.util.SaveUserAsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FirstTimeRegister extends AbstractFragment implements AsyncTaskCompleteListener<String>{

    SharedPreferences mPrefs;	
    String user_id;
    //View rootView;
    Context context;
    String deviceID;
    String regID;
    String city;
    LinearLayout loadingView;
    LinearLayout resourceView;

	View x;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		x = inflater.inflate(R.layout.first_time_register, container, false);


		context = getActivity();

	    resourceView=(LinearLayout)x.findViewById(R.id.resourceView);
	    loadingView=(LinearLayout)x.findViewById(R.id.loadingView);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		deviceID = mPrefs.getString("deviceID","");
		regID = mPrefs.getString("regID", "");
		//city = mPrefs.getString("city", "");


		setUpInitialValues();
		addSaveButtonListner();
	return x;
    }
	private void setUpInitialValues() {
		// TODO Auto-generated method stub

		String mobile = mPrefs.getString("mobile","");
		String emailid = mPrefs.getString("email", "");
		String name = mPrefs.getString("name", "");
		String city = mPrefs.getString("city", "");
		//String brokerWebsite = mPrefs.getString("brokerWebsite", "");

		((EditText) x.findViewById(R.id.userMobile)).setText(mobile);
		((EditText) x.findViewById(R.id.userEmail)).setText(emailid);
		((EditText) x.findViewById(R.id.userName)).setText(name);
		((EditText) x.findViewById(R.id.city)).setText(city);



	}


	private void addSaveButtonListner() {
		// TODO Auto-generated method stub

		Button button = (Button) x.findViewById(R.id.savebutton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

		           String userMobile = ((EditText) x.findViewById(R.id.userMobile)).getText().toString();
		           String userEmail = ((EditText) x.findViewById(R.id.userEmail)).getText().toString();
		           String userName = ((EditText) x.findViewById(R.id.userName)).getText().toString();
		           city = ((EditText) x.findViewById(R.id.city)).getText().toString();
		           if(userMobile.equals("") || !isValidMobile(userMobile)){    
						Toast.makeText(context.getApplicationContext(), "Please enter a valid Mobile number", Toast.LENGTH_SHORT).show();
					    return;
					 }else if(userEmail.equals("") || !isValidMail(userEmail)){
							Toast.makeText(context.getApplicationContext(), "Please enter a valid Email", Toast.LENGTH_SHORT).show();
						    return;
						 
					 }else if(userName.equals("")){
							Toast.makeText(context.getApplicationContext(), "Name cannot be blank", Toast.LENGTH_SHORT).show();
						    return;
					 }else if(city.equals("")){
							Toast.makeText(context.getApplicationContext(), "City cannot be blank", Toast.LENGTH_SHORT).show();
						    return;
						 
					 }
		   		deviceID = mPrefs.getString("deviceID","");
				regID = mPrefs.getString("regID", "");
				//city = mPrefs.getString("city", "");
				
		           
				       Object[]  formList = new Object[6];
			           
			           formList[0] = regID;
			           formList[1] = city;
			           formList[2] = deviceID;
			           formList[3] = userName;
			           formList[4] = userEmail;
			           formList[5] = userMobile;
			           resourceView.setVisibility(View.GONE);
			           loadingView.setVisibility(View.VISIBLE);

						try {
							launchTask(formList);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
				}
				
		});

		
	}
	
	public void onTaskComplete(String sResponse) {
        // do whatever you need

		if(sResponse != null && !(sResponse.isEmpty()))
		{
			loadingView.setVisibility(View.GONE);
			//String[] result = sResponse.split(",");
           String userMobile = ((EditText) x.findViewById(R.id.userMobile)).getText().toString();
           String userEmail = ((EditText) x.findViewById(R.id.userEmail)).getText().toString();
           String userName = ((EditText) x.findViewById(R.id.userName)).getText().toString();
			String city = ((EditText) x.findViewById(R.id.city)).getText().toString();
			SharedPreferences.Editor editor = mPrefs.edit();
			
			editor.putString("mobile", userMobile);
			editor.putString("email",userEmail);
			editor.putString("name", userName);
			editor.putString("city", city);
			editor.commit();
			
    		Toast.makeText(context.getApplicationContext(), "User  saved successfully", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(context, MainActivity.class);
			startActivity(i);
		}
    	else
    	{
        	resourceView.setVisibility(View.VISIBLE);
    		loadingView.setVisibility(View.GONE);

    		Toast.makeText(context.getApplicationContext(), "Could not save User Details", Toast.LENGTH_SHORT).show();
    	}
    }

    public void launchTask(Object[] formList) {
    	//UploadPromotionsAsyncTask asynTask = new UploadPromotionsAsyncTask(getActivity(), this);
    	SaveUserAsyncTask  asynTask = new SaveUserAsyncTask(this);
    	asynTask.execute(formList);
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
