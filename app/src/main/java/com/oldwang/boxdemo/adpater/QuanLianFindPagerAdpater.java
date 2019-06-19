package com.oldwang.boxdemo.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oldwang.boxdemo.factory.QuanLianFragmentFractory;

public class QuanLianFindPagerAdpater  extends FragmentPagerAdapter {
    public QuanLianFindPagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return QuanLianFragmentFractory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}

