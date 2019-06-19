package com.oldwang.boxdemo.adpater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oldwang.boxdemo.fragment.GoodsCommentFragmnet;
import com.oldwang.boxdemo.fragment.OrderListFragmnet;

public class GoodsCommentpagerAdpater  extends FragmentPagerAdapter {
    public GoodsCommentpagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment mfragmnet = new GoodsCommentFragmnet();
        Bundle args = new Bundle();
        args.putString("position", position+"");
        mfragmnet.setArguments(args);
        return  mfragmnet;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
