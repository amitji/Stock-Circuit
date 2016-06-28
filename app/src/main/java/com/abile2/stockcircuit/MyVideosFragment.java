package com.abile2.stockcircuit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.abile2.stockcircuit.model.StockVideo;
import com.abile2.stockcircuit.util.EmailAndLoggingAsyncTask;
import com.abile2.stockcircuit.util.GetMyVideosAsyncTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyVideosFragment extends AbstractFragment {//implements AsyncTaskCompleteListener<String>{

    //Listener for main activity to pass on data to another fragments
    OnVideoSelectedListener mCallback;
    public interface OnVideoSelectedListener {
        void onItemSelected(String url);
    }

    Context context;
    //protected MyApp mMyApp;
    ListView listview;
    ArrayList selectedItems;
    View rootView;
    SharedPreferences mPrefs;
    ListAdapterVideos  mAdapter;
    FloatingActionButton delete_btn;
    ArrayList<StockVideo> allVideos;
    TextView noAlerts;
    FloatingActionButton video_refresh_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_videos_fragment, container, false);

        MainActivity activity = (MainActivity) getActivity();
        context = activity;
        //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        deviceID = mPrefs.getString("deviceID","");
        regID = mPrefs.getString("regID", "");
        mobile = mPrefs.getString("mobile", "");
        boolean my_video_refresh_flag = mPrefs.getBoolean("my_video_refresh_flag", true);

        listview = (ListView) rootView.findViewById(R.id.video_list);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        if(my_video_refresh_flag){//if flag is true means needs to get new list from server
            allVideos = getAllRecommendedVideos(deviceID, regID);
        }else
        {
            String my_video_str = mPrefs.getString("my_video_list", "");
            allVideos = convertJsonToStockVideoList(my_video_str);
        }

        noAlerts = (TextView) rootView.findViewById(R.id.noAlerts);
        if(allVideos.size()==  0){
            noAlerts.setVisibility(View.VISIBLE);
        }else{
            noAlerts.setVisibility(View.INVISIBLE);
            mAdapter = new ListAdapterVideos(activity, allVideos);
            listview.setAdapter(mAdapter);
            listview.setTextFilterEnabled(true);
            selectedItems = new ArrayList(allVideos.size());

        }

        setupFloatingMenu();
        addListItemListner();
        return rootView;
    }

    protected ArrayList<StockVideo> getAllRecommendedVideos(	String deviceID,String regID) {
        // TODO Auto-generated method stub
        //ArrayList<Stock> list = new ArrayList<Stock>();
        ArrayList<StockVideo> list = new ArrayList<StockVideo>();
        String str = "";
        Object[]  inParams = new Object[2];

        inParams[0] = deviceID;
        inParams[1] = regID;
        try {
            //str = new GetAllRecommendedVideoAsyncTask().execute(inParams).get();
            str = new GetMyVideosAsyncTask().execute(inParams).get();
            if(str != null && !str.equals("")) {
                //save the my video list so that unless there is a change you dont have to call server to provide list...
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString("my_video_list", str);
                editor.putBoolean("my_video_refresh_flag", false);
                editor.commit();

                list = convertJsonToStockVideoList(str);
            }
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    private void setupFloatingMenu() {

        video_refresh_btn  = (FloatingActionButton) rootView.findViewById(R.id.video_refresh);
        video_refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshVideos();
            }
        });

    }
    private void refreshVideos() {

        SharedPreferences.Editor editor= mPrefs.edit();
        editor.putBoolean("my_video_refresh_flag", true);
        editor.commit();

        //Fragment frag = getActiveFragment();

        FragmentTransaction fragTransaction =   getActivity().getSupportFragmentManager().beginTransaction();
        fragTransaction.detach(this);
        fragTransaction.attach(this);
        fragTransaction.commit();

    }

    private void addListItemListner() {
        // TODO Auto-generated method stub
        //ListView lv = getListView();
        ListView lv =  (ListView ) rootView.findViewById(R.id.video_list);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                StockVideo stk = mAdapter.getItem(position);
                String  url = stk.getVideo_url();
                //email & log on server for tracking
                Object object[] = new Object[4];
                object[0] = mobile;
                object[1] = deviceID;
                object[2] = regID;
                object[3] = stk.getFullid();

                new EmailAndLoggingAsyncTask().execute(object);

                mCallback.onItemSelected(url);




            }
        });


    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnVideoSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnVideoSelectedListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		/*
        inflater.inflate(R.menu.activity_main_actions, menu);
        //super.onCreateOptionsMenu(menu, inflater);
        MenuItem action_menu = menu.findItem(R.id.action_menu);
        MenuItem  action_discard = menu.findItem(R.id.action_discard);
        MenuItem action_refresh = menu.findItem(R.id.action_refresh);
        MenuItem action_help = menu.findItem(R.id.action_help);

	    action_menu.setVisible(false);
	    action_discard.setVisible(true);
	    action_refresh.setVisible(true);
	    action_help.setVisible(false);
        */
    }
    public void onResume() {
        super.onResume();

        boolean my_video_refresh_flag = mPrefs.getBoolean("my_video_refresh_flag", true);


        if(my_video_refresh_flag){//if flag is true means needs to get new list from server
            allVideos = getAllRecommendedVideos(deviceID, regID);
            mAdapter = new ListAdapterVideos(getActivity(), allVideos);
            listview.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }else
        {
            String my_video_str = mPrefs.getString("my_video_list", "");
            allVideos = convertJsonToStockVideoList(my_video_str);
        }
        if(allVideos.size()==  0){
            noAlerts.setVisibility(View.VISIBLE);
        }else{
            noAlerts.setVisibility(View.INVISIBLE);
        }
    }
    public void onPause() {
        super.onPause();
    }
    public void onDestroy() {
        super.onDestroy();
    }

}