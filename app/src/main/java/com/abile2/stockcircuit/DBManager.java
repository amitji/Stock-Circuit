package com.abile2.stockcircuit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {


  private static final String DATABASE_NAME = "stocks.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String TABLE_STOCK_NAMES_CREATE = "create table stock_names ( _id integer primary key autoincrement, name text, nseid text, bseid text, isin text, industry_vertical text, last_modified datetime, created_on datetime );";


  public DBManager(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    
	  createDBfromScratch(database);
  
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	  Log.w(DBManager.class.getName(),"Upgrading database from version " + oldVersion + " to "+ newVersion);

  }

private void createDBfromScratch(SQLiteDatabase db) {
	// TODO Auto-generated method stub
	
	db.execSQL("DROP TABLE IF EXISTS stock_names; " );
 
	db.execSQL(TABLE_STOCK_NAMES_CREATE);
	
}

} 