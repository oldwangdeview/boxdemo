package com.oldwang.boxdemo.factory;

import android.support.v4.app.Fragment;

import com.oldwang.boxdemo.fragment.CouponFragmnet;


import java.util.HashMap;

public class CouponFragmentFactory {
    public static String TAG="CouponFragmentFactory";
    private static final int GET_CASH_FRAGMENT1 = 0;
    private static final int GET_CASH_FRAGMENT2 = 1;
    private static final int GET_CASH_FRAGMENT3 = 2;




    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment=null;
        if (mMap.containsKey(index)){
            fragment=   mMap.get(index);
        }else {
            switch (index) {
                //优惠券状态（0未使用1以使用2未领取3已过期）
                case GET_CASH_FRAGMENT1:
                    fragment = CouponFragmnet.newInstance(0);
                    break;
                case GET_CASH_FRAGMENT2:
                    fragment = CouponFragmnet.newInstance(1);
                    break;
                case GET_CASH_FRAGMENT3:
                    fragment = CouponFragmnet.newInstance(3);
                    break;

            }

            mMap.put(index,fragment);

        }
        return fragment;

    }
}
