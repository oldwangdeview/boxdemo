package com.oldwang.boxdemo.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oldwang.boxdemo.factory.MainFragmentFactory;
import com.oldwang.boxdemo.factory.MyAddMessageFactory;

public class MyAddmessagePagerAdpater extends FragmentPagerAdapter {
    public MyAddmessagePagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MyAddMessageFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
