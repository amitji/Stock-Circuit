package com.abile2.stockcircuit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.abile2.stockcircuit.model.StockVideo;
import com.abile2.stockcircuit.util.EmailAndLoggingAsyncTask;
import com.abile2.stockcircuit.util.GetAllRecommendedVideoAsyncTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RecommendedVideosFragment extends AbstractFragment {//implements AsyncTaskCompleteListener<String>{

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recommended_videos, container, false);

        MainActivity activity = (MainActivity) getActivity();
        context = activity;
        //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        deviceID = mPrefs.getString("deviceID","");
        regID = mPrefs.getString("regID", "");
        mobile = mPrefs.getString("mobile", "");

        listview = (ListView) rootView.findViewById(R.id.video_list);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        allVideos = getAllRecommendedVideos(deviceID, regID);

        mAdapter = new ListAdapterVideos(activity, allVideos);
        listview.setAdapter(mAdapter);
        listview.setTextFilterEnabled(true);
        selectedItems = new ArrayList(allVideos.size());
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
            str = new GetAllRecommendedVideoAsyncTask().execute(inParams).get();
            if(str != null && !str.equals(""))
                list = convertJsonToStockVideoList(str);

        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
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
}