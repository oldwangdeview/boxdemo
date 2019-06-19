package com.oldwang.boxdemo.factory;

import android.support.v4.app.Fragment;

import com.oldwang.boxdemo.fragment.OrderMessageFragmnet;
import com.oldwang.boxdemo.fragment.SystemMessageFragmnet;

import java.util.HashMap;

public class MessageFragmentFactory {
    public static String TAG="MessageFragmentFactory";
    private static final int ORDERMESSAGE = 0;
    private static final int SYSTENMESSAGE = 1;


    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {

        Fragment fragment = null;
            switch (index) {
                case ORDERMESSAGE:
                    fragment = OrderMessageFragmnet.newInstance(0);
                    break;
                case SYSTENMESSAGE:
                    fragment = OrderMessageFragmnet.newInstance(1);
                    break;
            }


        return fragment;

    }
}
