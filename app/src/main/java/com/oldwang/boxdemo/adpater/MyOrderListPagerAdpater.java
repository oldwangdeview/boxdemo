package com.oldwang.boxdemo.adpater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oldwang.boxdemo.fragment.OrderListFragmnet;

public class MyOrderListPagerAdpater  extends FragmentPagerAdapter {
    public MyOrderListPagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment mfragmnet = new OrderListFragmnet();
        Bundle args = new Bundle();
        if (position == 1){
            args.putString("position", "-1");
        }else if (position > 1){
            args.putString("position", (position-1)+"");
        }else {
            args.putString("position", position+"");
        }
        mfragmnet.setArguments(args);
        return  mfragmnet;
    }

    @Override
    public int getCount() {
        return 7;
    }
}
