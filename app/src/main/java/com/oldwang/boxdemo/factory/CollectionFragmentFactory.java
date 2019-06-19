package com.oldwang.boxdemo.factory;

import android.support.v4.app.Fragment;

import com.oldwang.boxdemo.fragment.CollectionGoodsFragment;
import com.oldwang.boxdemo.fragment.CollectionVideoFragment;


import java.util.HashMap;

public class CollectionFragmentFactory {
    public static String TAG="CollectionFragmentFactory";
    private static final int COLLECTION_GOODS = 0;
    private static final int COLLECTION_VIDEO = 1;



    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment=null;
        if (mMap.containsKey(index)){
            fragment=   mMap.get(index);
        }else {
            switch (index) {
                case COLLECTION_GOODS:
                    fragment = new CollectionGoodsFragment();
                    break;
                case COLLECTION_VIDEO:
                    fragment = new CollectionVideoFragment();
                    break;
            }

            mMap.put(index,fragment);

        }
        return fragment;

    }
}
