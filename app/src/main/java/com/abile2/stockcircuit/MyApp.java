package com.abile2.stockcircuit;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

public class MyApp extends Application {
	private static MyApp myApp;
	
	public MyApp()
	{
		myApp = this;
	}
	
    public void onCreate() {
          super.onCreate();
    }

    private Activity mCurrentActivity = null;
    public Activity getCurrentActivity(){
          return mCurrentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
          this.mCurrentActivity = mCurrentActivity;
    }
    public static Context Context() {
        return myApp;
    }

    public static ContentResolver ContentResolver() {
        return myApp.getContentResolver();
    }    

}