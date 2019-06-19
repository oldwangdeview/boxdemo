package com.oldwang.boxdemo.factory;

import android.support.v4.app.Fragment;

import com.oldwang.boxdemo.fragment.AssembleFragment;
import com.oldwang.boxdemo.fragment.BrandFragment;
import com.oldwang.boxdemo.fragment.ClassifcationFragment;


import java.util.HashMap;

public class QuanLianFragmentFractory  {
        public static String TAG="QuanLianFragmentFractory";
        private static final int CLASSIFCATIONFRAGMENT = 0;
        private static final int BRANDFRAGMENT = 1;
        private static final int ASSEMBLEFRAGMENT = 2;



        private static HashMap<Integer,Fragment> mMap=new HashMap();

        public static Fragment getFragment(int index) {
            Fragment fragment=null;
            if (mMap.containsKey(index)){
                fragment=   mMap.get(index);
            }else {
                switch (index) {
                    case CLASSIFCATIONFRAGMENT:
                        fragment = new ClassifcationFragment();
                        break;
                    case BRANDFRAGMENT:
                        fragment = new AssembleFragment();
                        break;
                    case ASSEMBLEFRAGMENT:
                        fragment = new BrandFragment();
                        break;


                }

                mMap.put(index,fragment);

            }
            return fragment;

        }
}
