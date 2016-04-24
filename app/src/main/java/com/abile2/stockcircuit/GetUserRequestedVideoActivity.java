package com.abile2.stockcircuit;

import android.app.Activity;
import android.content.Context;
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

import com.abile2.stockcircuit.util.GetUserRequestedVideoAsyncTask;

public class GetUserRequestedVideoActivity extends Activity implements AsyncTaskCompleteListener<String>{
    //View rootView = null;

    //String url = "https://www.youtube.com/watch?v=LhW8lida80A";
    VideoView myVideoView;
    Context context;
    String fullid;
    LinearLayout resourceView;
    LinearLayout loadingView;
    SharedPreferences mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_user_requested_video_activity);

        resourceView=(LinearLayout)findViewById(R.id.resourceView);
        loadingView=(LinearLayout)findViewById(R.id.loadingView);

        UtilityActivity.hideSoftKeyboard(this);
        context = this;
        Intent secondInt = getIntent();
         fullid = secondInt.getStringExtra("fullid");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        requestForVideo(fullid);


    }

    private void  requestForVideo( String fullid){
        //String url = "";

        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String deviceID = mPrefs.getString("deviceID","");
        String regID = mPrefs.getString("regID", "");
        String mobile = mPrefs.getString("mobile", "");
        Object[]  formList = new Object[4];

        formList[0] =  fullid;
        formList[1] =  deviceID;
        formList[2] =  regID;
        formList[3] =  mobile;

        //formList[1] = "999";
        resourceView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        try {
            launchTask(formList);
//             HashMap responseMap = new GetUserRequestedVideoAsyncTask().execute(formList).get();
//             if(responseMap != null && !responseMap.isEmpty()){
//                 url = (String) responseMap.get("url");
//             }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void launchTask(Object[] formList) {
        GetUserRequestedVideoAsyncTask asynTask = new GetUserRequestedVideoAsyncTask(this, this);
        asynTask.execute(formList);
    }

    public void onTaskComplete(String url) {
        // do whatever you need
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
        else
        {

        }

    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//    }
}