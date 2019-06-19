package com.oldwang.boxdemo.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oldwang.boxdemo.factory.AppoinmentFragmentFactory;
import com.oldwang.boxdemo.factory.HomeSearchFragmentFactory;
import com.oldwang.boxdemo.fragment.HomeSearchFragment;

public class HosmeSearchPagerAdpater extends FragmentPagerAdapter {
    public HosmeSearchPagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return HomeSearchFragmentFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}

