package com.oldwang.boxdemo.util;

import android.widget.Toast;

import com.oldwang.boxdemo.application.BaseApplication;


/**
 * @autor wangshifu
 * @time 2018/12/911:47
 * @des ${TODO}
 */
public class ToastUtils {
    static Toast mToast;

    public static void makeText( final String msg) {

        com.hjq.toast.ToastUtils.show(msg);

//        if (BaseApplication.mMainThreadId==android.os.Process.myTid()){
//            if (mToast==null){
//                mToast = Toast.makeText(UIUtils.getContext(), msg, Toast.LENGTH_SHORT);
//            }else {
//                mToast.setText(msg);
//            }
//            mToast.show();
//        }else {
//            BaseApplication.mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (mToast==null){
//                        mToast = Toast.makeText(UIUtils.getContext(), msg, Toast.LENGTH_SHORT);
//                    }else {
//                        mToast.setText(msg);
//                    }
//
//                    mToast.show();
//                }
//            });
//
//        }






        }

    public static void makeText( final int msg) {
        if (BaseApplication.mMainThreadId==android.os.Process.myTid()){
            if (mToast==null){
                mToast = Toast.makeText(UIUtils.getContext(), msg, Toast.LENGTH_SHORT);
            }else {
                mToast.setText(msg);
            }
            mToast.show();
        }else {
            BaseApplication.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(UIUtils.getContext(), msg, Toast.LENGTH_SHORT);
                    } else {
                        mToast.setText(msg);
                    }

                    mToast.show();
                }
            });
        }

 /*       if (mToast == null) {
            mToast = Toast.makeText(UIUtils.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        if (BaseApplication.mMainThreadId == android.os.Process.myTid()) {
            mToast.show();
        } else {

            BaseApplication.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mToast.show();
                }
            });
        }*/

    }



}
