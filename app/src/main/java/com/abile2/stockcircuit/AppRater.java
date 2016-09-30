package com.abile2.stockcircuit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by amahe6 on 9/12/2016.
 */
public class AppRater {
    private final static String APP_TITLE = "Stock Circuit App";// App Name
    private final static String APP_PNAME = "com.abile2.stockcircuit";// Package Name

    //private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 6;//Min number of launches (before you start shwoing it again)


    public static void app_launched(Context mContext) {
        //SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String app_rater_show = prefs.getString("app_rater_show","n");
        int app_rater_days_until_prompt =  Integer.parseInt(prefs.getString("app_rater_days_until_prompt","3"));
        if (!app_rater_show.equals("y") || prefs.getBoolean("dontshowagain", false))
        {
            return ;
        }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (app_rater_days_until_prompt * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }


    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.app_rater_dialog_box);
        //dialog.setTitle("Rate " + APP_TITLE);
        dialog.setTitle( Html.fromHtml("<font color='#FFFFFF'>Rate Stock Circuit App</font>"));
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);

        TextView tv = (TextView) dialog.findViewById(R.id.text);
        //TextView tv = new TextView(mContext);
        tv.setText("Your feedback & encouragement is very important for us to excel.We appreciate your few seconds to rate us. Thanks !");


        Button dialogButtonOK = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //if user has clicked on rate button then 'assume' he is going to rate. No other way to find out !!
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }

                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });

        Button dialogButtonLater = (Button) dialog.findViewById(R.id.dialogButtonLater);

        dialogButtonLater.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (editor != null) {
                    editor.putLong("launch_count", 0);
                    editor.commit();
                }

                dialog.dismiss();
            }
        });
        dialog.show();



//        int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
//        if (textViewId != 0) {
//            TextView tv2 = (TextView) dialog.findViewById(textViewId);
//            tv2.setTextColor(Color.WHITE);
//        }
    }


/*

    public static void showRateDialog222(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + APP_TITLE);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);

        Button b1 = new Button(mContext);
        b1.setText("Rate " + APP_TITLE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(15, 15, 15, 15);
        b1.setLayoutParams(params);
        b1.setBackgroundColor(b1.getContext().getResources().getColor(R.color.buttonColor1));
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText("Remind me later");
        b2.setLayoutParams(params);
        b2.setBackgroundColor(b2.getContext().getResources().getColor(R.color.buttonColor1));

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText("No, thanks");
        b3.setLayoutParams(params);
        b3.setBackgroundColor(b3.getContext().getResources().getColor(R.color.buttonColor1));

        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);
        dialog.show();
    }
    */
}