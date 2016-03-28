package com.abile2.stockcircuit;

public class Constants {
	
	//PROD shopbindaas.co.in
//	public static final String SERVER_BASE_URL = "http://shopbindaas.co.in/StockCircuitServer/spring/";
//    public static final String FA_VIDEO_URL = "http://shopbindaas.co.in/stockcircuitserver/fund_analysis/videos/";

	//DEV At// home
    public static final String SERVER_BASE_URL = "http://192.168.1.3:8080/StockCircuitServer/spring/";
    public static final String FA_VIDEO_URL = "http://192.168.1.3/stockcircuitserver/fund_analysis/videos/";

	//public static final String SERVER_BASE_URL = "http://10.202.204.187:8080/StockCircuitServer/spring/";

	

	//Stock List renew time in days
	public static final int STOCK_LIST_FETCH_TIME = 7;
	//GCM Server Related constants
	public static final String EXTRA_MESSAGE = "message";
    //public static final String PROPERTY_REG_ID = "registration_id";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //public static final String SENDER_ID = "556217428282";
    //public static final String SENDER_ID = "134058574952";
    //This is the Project ID in Google Developer Console. This is the way Server key connects with a specific app.
    public static final String SENDER_ID = "618780929868";
    //public static final String SENDER_ID = "170066342993";
    
    
    public static final String loadingText1="Loading... a bit more patience";
    public static final String loadingText2="BTW, you can flip through quickly all the promotions.";
    public static final String loadingText4="Almost Done..";
    public static final String loadingText3="We first show promotions closer to your current location and then all others..";
    //sapient office 
	//public static final String SERVER_BASE_URL = "http://10.202.98.185:8080/ShopBindaasServer/spring/";
    public static String shareMessage="Stock Circuit App - Get Alerts for your Stocks real time when they hit High/Low circuit set by you. And Latest News on it. "
            +" Downlaod here - https://play.google.com/store/apps/details?id=com.abile2.stockcircuit";
    
    public static String shareSubject="Download Stock Circuit App";
	public static final int promo_image_width = 1600;
	/*
	public static String TermConditionText=" * This Referral Program is for limited period and can be withdraw any time." +
			"\n * For every successful refrral you will earn 10 reward points. which you can redeem for" +
			" Mobile Recharge or with our selected partner Merchants." +
			"\n * The maximum reward point you can earn in this scheme is 100 Points. You need minimum 30 points to redeem";      	
	public static String TermConditionTitle="Terms & Conditions ";
	*/			

	//Singapore - PROD  Amazon EC2 Server PROD
	//public static final String SERVER_BASE_URL = "http://ec2-52-74-57-166.ap-southeast-1.compute.amazonaws.com:8080/ShopBindaasServer/spring/";
	//OLD -      PROD Amazon EC2 Server PROD OLD
	//public static final String SERVER_BASE_URL = "http://ec2-54-187-37-104.us-west-2.compute.amazonaws.com:8080/ShopBindaasServer/spring/";
	

}
