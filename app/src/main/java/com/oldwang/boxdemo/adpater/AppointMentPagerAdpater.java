package com.oldwang.boxdemo.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oldwang.boxdemo.factory.AppoinmentFragmentFactory;

public class AppointMentPagerAdpater extends FragmentPagerAdapter {
    public AppointMentPagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return AppoinmentFragmentFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 5;
    }
}

