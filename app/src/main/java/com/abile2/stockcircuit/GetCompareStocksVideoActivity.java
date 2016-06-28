package com.abile2.stockcircuit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.abile2.stockcircuit.util.GetCompareStocksVideoAsyncTask;

import java.util.HashMap;


public class GetCompareStocksVideoActivity extends Activity implements AsyncTaskCompleteListener<String>{
    //View rootView = null;

    //String url = "https://www.youtube.com/watch?v=LhW8lida80A";
    VideoView myVideoView;
    Context context;
    String fullid1;
    String fullid2;
    String fullid3;
    LinearLayout resourceView;
    LinearLayout loadingView;
    SharedPreferences mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_compare_stocks_video_activity);

        resourceView=(LinearLayout)findViewById(R.id.resourceView);
        loadingView=(LinearLayout)findViewById(R.id.loadingView);

        UtilityActivity.hideSoftKeyboard(this);
        context = this;
        Intent secondInt = getIntent();
        fullid1 = secondInt.getStringExtra("fullid1");
        fullid2 = secondInt.getStringExtra("fullid2");
        fullid3 = secondInt.getStringExtra("fullid3");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        requestForCompareVideo(fullid1,fullid2,fullid3);


    }

    private void  requestForCompareVideo(String fullid1, String fullid2, String fullid3){
        //String url = "";

        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String deviceID = mPrefs.getString("deviceID","");
        String regID = mPrefs.getString("regID", "");
        String mobile = mPrefs.getString("mobile", "");
        Object[]  formList = new Object[6];

        formList[0] =  fullid1;
        formList[1] =  fullid2;
        formList[2] =  fullid3;
        formList[3] =  deviceID;
        formList[4] =  regID;
        formList[5] =  mobile;

        //formList[1] = "999";
        resourceView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        try {
            launchTask(formList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchTask(Object[] formList) {
        GetCompareStocksVideoAsyncTask asynTask = new GetCompareStocksVideoAsyncTask(this, this);
        asynTask.execute(formList);
    }

    public void onTaskComplete(String sResponse) {

        HashMap<String, String> params = new HashMap<String, String>();
        params = UtilityActivity.getMapforJsonString(sResponse);
        String url = params.get("url");
        String video_under_process_msg= params.get("video_under_process_msg");


        if(url != null && !url.equals(""))
        {
            resourceView.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);

            try {
                myVideoView = (VideoView) findViewById(R.id.video);
                MediaController controller = new MediaController(this);
                myVideoView.setMediaController(controller);
                myVideoView.setVideoURI(Uri.parse(url));

                myVideoView.requestFocus();
                myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                      @Override
                                                      public void onPrepared(MediaPlayer mp) {
                                                          myVideoView.start();
                                                      }
                                                  }
                );

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else  //url is blank , that means video is getting created on sevrer in background and server has returned
        //for app to be responsive rather stuck
        {
            if(!video_under_process_msg.equals("")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Video Request Progress");
                alertDialog.setMessage(video_under_process_msg);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        }

    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//    }
}