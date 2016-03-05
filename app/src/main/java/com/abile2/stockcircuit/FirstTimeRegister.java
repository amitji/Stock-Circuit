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

public class FirstTimeRegister extends Activity implements AsyncTaskCompleteListener<String>{

    SharedPreferences mPrefs;	
    String user_id;
    //View rootView;
    Context context;
    String deviceID;
    String regID;
    String city;
    LinearLayout loadingView;
    LinearLayout resourceView;
    
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.first_time_register);
	    
	    resourceView=(LinearLayout)findViewById(R.id.resourceView);
	    loadingView=(LinearLayout)findViewById(R.id.loadingView);
	    
	    context = this;
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		deviceID = mPrefs.getString("deviceID","");
		regID = mPrefs.getString("regID", "");
		//city = mPrefs.getString("city", "");
		
		addSaveButtonListner();

    }
	private void addSaveButtonListner() {
		// TODO Auto-generated method stub

		Button button = (Button) findViewById(R.id.savebutton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

		           String userMobile = ((EditText) findViewById(R.id.userMobile)).getText().toString();
		           String userEmail = ((EditText) findViewById(R.id.userEmail)).getText().toString();
		           String userName = ((EditText) findViewById(R.id.userName)).getText().toString();
		           city = ((EditText) findViewById(R.id.city)).getText().toString();
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
			//String[] result = sResponse.split(",");
           String userMobile = ((EditText) findViewById(R.id.userMobile)).getText().toString();
           String userEmail = ((EditText) findViewById(R.id.userEmail)).getText().toString();
           String userName = ((EditText) findViewById(R.id.userName)).getText().toString();
			SharedPreferences.Editor editor = mPrefs.edit();
			
			editor.putString("mobile", userMobile);
			editor.putString("email",userEmail);
			editor.putString("name", userName);
			editor.commit();
			
    		Toast.makeText(context.getApplicationContext(), "User  added successful", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(FirstTimeRegister.this, MainActivity.class);
			startActivity(i);
			finish();
		}
    	else
    	{
        	resourceView.setVisibility(View.VISIBLE);
    		loadingView.setVisibility(View.GONE);

    		Toast.makeText(context.getApplicationContext(), "Could not save preferences", Toast.LENGTH_SHORT).show();
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
