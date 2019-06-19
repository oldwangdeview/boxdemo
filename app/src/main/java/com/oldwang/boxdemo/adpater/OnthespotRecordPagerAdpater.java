package com.oldwang.boxdemo.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oldwang.boxdemo.factory.MyAddMessageFactory;
import com.oldwang.boxdemo.factory.OnthespotRecordFactory;

public class OnthespotRecordPagerAdpater  extends FragmentPagerAdapter {
    public OnthespotRecordPagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return OnthespotRecordFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
