package com.oldwang.boxdemo.factory;

import android.support.v4.app.Fragment;

import com.oldwang.boxdemo.fragment.AppoinmentFragment;

import java.util.HashMap;

public class AppoinmentFragmentFactory {
    public static String TAG="AppoinmentFragmentFactory";
    private static final int APPOINMENT_1 = 0;
    private static final int APPOINMENT_2 = 1;
    private static final int APPOINMENT_3 = 2;
    private static final int APPOINMENT_4 = 3;
    private static final int APPOINMENT_5 = 4;


    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment=null;
        if (mMap.containsKey(index)){
            fragment=   mMap.get(index);
        }else {
            switch (index) {
                //预约状态(0:预约中,1:预约成功,-1预约失败,3完成)
                case APPOINMENT_1:
                    fragment = AppoinmentFragment.newInstance(0);
                    break;
                case APPOINMENT_2:
                    fragment = AppoinmentFragment.newInstance(1);
                    break;
                case APPOINMENT_3:
                    fragment = AppoinmentFragment.newInstance(-1);
                    break;
                case APPOINMENT_4:
                    fragment = AppoinmentFragment.newInstance(3);
                    break;
                case APPOINMENT_5:
                    fragment = AppoinmentFragment.newInstance(-3);
                    break;
            }

            mMap.put(index,fragment);

        }
        return fragment;

    }
}
