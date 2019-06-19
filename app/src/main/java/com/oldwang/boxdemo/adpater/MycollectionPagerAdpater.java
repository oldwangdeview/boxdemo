package com.oldwang.boxdemo.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oldwang.boxdemo.factory.CollectionFragmentFactory;

public class MycollectionPagerAdpater extends FragmentPagerAdapter {
    public MycollectionPagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CollectionFragmentFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
