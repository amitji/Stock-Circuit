package com.abile2.stockcircuit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
    	final Fragment result;
        switch (index) {
        case 0:
            // Top Rated fragment activity
        	result =  new ActiveAlertsFragment();
        	break;
        case 1:
            // Games fragment activity
        	//result =  new PassiveAlertsFragment();
            result =  new MyVideosFragment();
        	break;
        case 2:
            // Games fragment activity
        	result =  new FavoritesFragment();
        	break;
        default: 
        	result = null;
        	break;
        }
 
        return result;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}