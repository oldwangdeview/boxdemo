package com.oldwang.boxdemo.factory;

import android.support.v4.app.Fragment;


import com.oldwang.boxdemo.fragment.GetCash_other_Fragment1;
import com.oldwang.boxdemo.fragment.GetCash_other_Fragment2;

import java.util.HashMap;

public class GetCashFragmentFactory {
    public static String TAG="GetCashFragmentFactory";
    private static final int GET_CASH_FRAGMENT1 = 0;
    private static final int GET_CASH_FRAGMENT2 = 1;
    private static final int GET_CASH_FRAGMENT3 = 2;
    private static final int GET_CASH_FRAGMENT4 = 3;



    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment=null;
        if (mMap.containsKey(index)){
            fragment=   mMap.get(index);
        }else {
            switch (index) {
                case GET_CASH_FRAGMENT1:
                    fragment = new GetCash_other_Fragment1();
                    break;
                case GET_CASH_FRAGMENT2:
                    fragment = GetCash_other_Fragment2.newInstance(0);
                    break;
                case GET_CASH_FRAGMENT3:
                    fragment = GetCash_other_Fragment2.newInstance(1);
                    break;
                case GET_CASH_FRAGMENT4:
                    fragment = GetCash_other_Fragment2.newInstance(2);
                    break;


            }

            mMap.put(index,fragment);

        }
        return fragment;

    }

}
