package com.oldwang.boxdemo.factory;

import android.support.v4.app.Fragment;

import com.oldwang.boxdemo.fragment.OnthespotRecordFragmnet1;
import com.oldwang.boxdemo.fragment.OnthespotRecordFragmnet2;
import com.oldwang.boxdemo.fragment.OnthespotRecordFragmnet3;
import com.oldwang.boxdemo.fragment.OnthespotRecordFragmnet4;

import java.util.HashMap;

public class OnthespotRecordFactory {
    public static String TAG="OnthespotRecordFactory";
    private static final int OnthespotRecord1 = 0;
    private static final int OnthespotRecord2 = 1;
    private static final int OnthespotRecord3 = 2;
    private static final int OnthespotRecord4 = 3;



    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment=null;
        if (mMap.containsKey(index)){
            fragment=   mMap.get(index);
        }else {
            switch (index) {
                case OnthespotRecord1:
                    fragment = new OnthespotRecordFragmnet1();
                    break;
                case OnthespotRecord2:
                    fragment = new OnthespotRecordFragmnet2();
                    break;
                case OnthespotRecord3:
                    fragment = new OnthespotRecordFragmnet3();
                    break;
                case OnthespotRecord4:
                    fragment = new OnthespotRecordFragmnet4();
                    break;
            }

            mMap.put(index,fragment);

        }
        return fragment;

    }
}
