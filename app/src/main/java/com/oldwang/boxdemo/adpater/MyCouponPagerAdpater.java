package com.oldwang.boxdemo.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oldwang.boxdemo.factory.CouponFragmentFactory;

public class MyCouponPagerAdpater extends FragmentPagerAdapter {
    public MyCouponPagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CouponFragmentFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}


