package com.oldwang.boxdemo.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.oldwang.boxdemo.factory.MessageFragmentFactory;

public class MyMessagePagerAdpater extends FragmentPagerAdapter {
    public MyMessagePagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MessageFragmentFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}

