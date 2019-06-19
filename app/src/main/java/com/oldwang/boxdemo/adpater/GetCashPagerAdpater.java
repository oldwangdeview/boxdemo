package com.oldwang.boxdemo.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oldwang.boxdemo.factory.GetCashFragmentFactory;
import com.oldwang.boxdemo.factory.MainFragmentFactory;

public class GetCashPagerAdpater  extends FragmentPagerAdapter {
    public GetCashPagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return GetCashFragmentFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
