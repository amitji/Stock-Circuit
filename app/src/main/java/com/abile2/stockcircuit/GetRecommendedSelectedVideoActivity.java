package com.abile2.stockcircuit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.HashMap;

public class GetRecommendedSelectedVideoActivity extends Activity {//implements AsyncTaskCompleteListener<String>{
    //View rootView = null;

    //String url = "https://www.youtube.com/watch?v=LhW8lida80A";
    VideoView myVideoView;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_recommended_selected_video_activity);
       context = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        Intent secondInt = getIntent();
        String url = secondInt.getStringExtra("url");

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

}