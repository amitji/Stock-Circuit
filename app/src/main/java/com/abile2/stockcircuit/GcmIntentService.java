package com.abile2.stockcircuit;

/*
 	* Copyright (C) 2013 The Android Open Source Project
 	*
 	* Licensed under the Apache License, Version 2.0 (the "License");
 	* you may not use this file except in compliance with the License.
 	* You may obtain a copy of the License at
 	*
 	*      http://www.apache.org/licenses/LICENSE-2.0
 	*
 	* Unless required by applicable law or agreed to in writing, software
 	* distributed under the License is distributed on an "AS IS" BASIS,
 	* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 	* See the License for the specific language governing permissions and
 	* limitations under the License.
 */

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;


/**
  * This {@code IntentService} does the actual handling of the GCM message.
  * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
  * partial wake lock for this service while the service does its work. When the
  * service is finished, it calls {@code completeWakefulIntent()} to release the
  * wake lock.
*/
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint({ "InlinedApi", "NewApi", "WorldWriteableFiles" })
public class GcmIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	int count = 0 ;
	Context context;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}
	public static final String TAG = "Stock Circuit GCM";
	public static int numMsg = 0;

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		context = this;
		String messageType = gcm.getMessageType(intent);
		if (!extras.isEmpty()) {
			// has effect of unparcelling Bundle
			/*
			 * Filter notificationList based on message type. Since it is likely
			 * that GCM will be extended in the future with new message types,
			 * just ignore any message types you're not interested in, or that
			 * you don't recognize.
			 */
			/*
			 * if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
			 * .equals(messageType)) { // sendNotification("Send error: " +
			 * extras.toString()); } else if
			 * (GoogleCloudMessaging.MESSAGE_TYPE_DELETED .equals(messageType))
			 * { // sendNotification("Deleted notificationList on server: " + //
			 * extras.toString()); // If it's a regular GCM message, do some
			 * work. } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
			 * .equals(messageType)) { String message =
			 * extras.getString("message"); String
			 * type=extras.getString("message_type");
			 * sendNotification(message,type); // Post notification of received
			 * message. Log.i(TAG, "Received: " + extras.toString()); }
			 */
		}
		String message = extras.getString("message");
		String type = extras.getString("message_type");
		String imageURL = extras.getString("image_url");
        String title=extras.getString("title");
        String fullid =extras.getString("fullid");
        String alert_price =extras.getString("alert_price");
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		 boolean notificationState= prefs.getBoolean("notificationsStatus",true);  
        if(notificationState){
        	count = count+5;
        	//sendNotification(title,message, type, imageURL,fullid, alert_price, count);
			sendNotification(title,message, type, imageURL,fullid, alert_price, count);
        }
		// Post notification of received message.
		Log.i(TAG, "Received: " + extras.toString());
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
	@SuppressLint("NewApi")
	private void sendNotification(String title, String msg, String type, String imageURl, String fullid, String alert_price, int count) {
		WakeLocker.acquire(this);
		//msg = msg+"Amit this is testing for long text";
		long when = System.currentTimeMillis();
		//count = count+5;
		System.out.println(msg);
		Intent resultIntent = null;
		if(type.equals("post")) {
			resultIntent = new Intent(getApplicationContext(),
					MainActivity.class);
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			resultIntent.putExtra("notification", "yes");
			resultIntent.putExtra("fullid", fullid);
			resultIntent.putExtra("alert_price", alert_price);
			resultIntent.putExtra("isNotification", true);

		}else{
			resultIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id=com.abile2.stockcircuit"));
			resultIntent.putExtra("fullid", fullid);
		}
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, count,
				resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.logo_small)
						.setAutoCancel(true)
						.setPriority(Notification.PRIORITY_HIGH);

		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);
		//RemoteViews contentView = new RemoteViews(getPackageName(),com.android.internal.R.layout.status_bar_latest_event_content);

		contentView.setImageViewResource(R.id.image, R.drawable.logo_small);
		contentView.setTextViewText(R.id.title, title);
		contentView.setTextViewText(R.id.text, msg);
		mBuilder.setContent(contentView);
		mBuilder.setContentIntent(resultPendingIntent);

		Notification notification = mBuilder.build();

		Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.sound);
		mNotificationManager.notify((int) when, notification);
		//RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION,     uri);
		Ringtone ring = RingtoneManager.getRingtone(getApplicationContext(), uri);
		ring.play();
		((Vibrator) getApplicationContext().getSystemService( Context.VIBRATOR_SERVICE)).vibrate(1600);

		WakeLocker.release();
	}

}
