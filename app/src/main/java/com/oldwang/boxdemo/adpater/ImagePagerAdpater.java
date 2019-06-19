package com.oldwang.boxdemo.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oldwang.boxdemo.factory.CouponFragmentFactory;
import com.oldwang.boxdemo.fragment.AppoinmentFragment;
import com.oldwang.boxdemo.fragment.ImageFragment;

import java.util.List;

public class ImagePagerAdpater extends FragmentPagerAdapter {


    private List<String> listdata1;
    private List<String> listdataText1;

    public ImagePagerAdpater(FragmentManager fm, List<String> listdata1, List<String> listdataText1) {
        super(fm);
        this.listdata1 = listdata1;
        this.listdataText1 = listdataText1;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(listdata1.get(position),listdataText1.get(position));
    }

    @Override
    public int getCount() {
        return listdata1.size();
    }
}


