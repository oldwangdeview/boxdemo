package com.oldwang.boxdemo.factory;

import android.support.v4.app.Fragment;

import com.oldwang.boxdemo.fragment.Goods_CarsFragmnet;
import com.oldwang.boxdemo.fragment.HomeFragment;
import com.oldwang.boxdemo.fragment.MessageFragment;
import com.oldwang.boxdemo.fragment.QuanLianFragmnet;
import com.oldwang.boxdemo.fragment.UserFragment;

import java.util.HashMap;

public class MainFragmentFactory {

    public static String TAG="MainFragmentFactory";
    private static final int Home_FRAGEMT = 0;
    private static final int QUANLIAN_ZHUANBEI = 1;
    private static final int GOODS_CARS = 2;
    private static final int QUNALIAN_JISHI = 3;
    private static final int MUSER_FRAGMNET = 4;


    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment=null;
//        if (mMap.containsKey(index)){
//            fragment=   mMap.get(index);
//        }else {
        switch (index) {
            case Home_FRAGEMT:
                fragment = new HomeFragment();
                break;
            case QUANLIAN_ZHUANBEI:
                fragment = new QuanLianFragmnet();
                break;
            case GOODS_CARS:
                fragment = new Goods_CarsFragmnet();
                break;
            case QUNALIAN_JISHI:
                fragment = new MessageFragment();
                break;
            case MUSER_FRAGMNET:
                fragment = new UserFragment();
                break;

        }

//            mMap.put(index,fragment);
//
//        }
        return fragment;

    }

}
