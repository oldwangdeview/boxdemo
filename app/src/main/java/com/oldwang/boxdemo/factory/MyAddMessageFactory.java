package com.oldwang.boxdemo.factory;

import android.support.v4.app.Fragment;

import com.oldwang.boxdemo.fragment.Goods_CarsFragmnet;
import com.oldwang.boxdemo.fragment.HomeFragment;
import com.oldwang.boxdemo.fragment.MessageFragment;
import com.oldwang.boxdemo.fragment.MyCommentFragment;
import com.oldwang.boxdemo.fragment.MyVideoFragment;
import com.oldwang.boxdemo.fragment.QuanLianFragmnet;
import com.oldwang.boxdemo.fragment.UserFragment;

import java.util.HashMap;

public class MyAddMessageFactory {
    public static String TAG="MyAddMessageFactory";
    private static final int Home_FRAGEMT = 0;
    private static final int QUANLIAN_ZHUANBEI = 1;



    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment=null;

            switch (index) {
                case Home_FRAGEMT:
                    fragment = new MyVideoFragment();
                    break;
                case QUANLIAN_ZHUANBEI:
                    fragment = new MyCommentFragment();
                    break;
            }


        return fragment;

    }
}
