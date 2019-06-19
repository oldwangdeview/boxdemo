package com.oldwang.boxdemo.factory;

import android.support.v4.app.Fragment;

import com.oldwang.boxdemo.fragment.AppoinmentFragment;
import com.oldwang.boxdemo.fragment.HomeSearchFragment;

import java.util.HashMap;

public class HomeSearchFragmentFactory {

    private static final int APPOINMENT_1 = 0;
    private static final int APPOINMENT_2 = 1;
    private static final int APPOINMENT_3 = 2;
    private static final int APPOINMENT_4 = 3;


    private static HashMap<Integer, Fragment> mMap = new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment = null;
        if (mMap.containsKey(index)) {
            fragment = mMap.get(index);
        } else {
            switch (index) {
                case APPOINMENT_1:
                    fragment = HomeSearchFragment.newInstance(0);
                    break;
                case APPOINMENT_2:
                    fragment = HomeSearchFragment.newInstance(1);
                    break;
                case APPOINMENT_3:
                    fragment = HomeSearchFragment.newInstance(2);
                    break;
                case APPOINMENT_4:
                    fragment = HomeSearchFragment.newInstance(3);
                    break;
            }

            mMap.put(index, fragment);

        }
        return fragment;

    }
}
