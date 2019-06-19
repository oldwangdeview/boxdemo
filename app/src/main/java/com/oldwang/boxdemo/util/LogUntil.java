package com.oldwang.boxdemo.util;

import android.content.Context;
import android.util.Log;

import com.oldwang.boxdemo.application.BaseApplication;

/**
 * Created by oldwang on 2019/1/2 0002.
 */

public class LogUntil {
    public LogUntil(Context mContext, String title, String msg){
        if(BaseApplication.isShowLog){
            Log.e(mContext.getPackageName()+"______"+title+":",msg);
        }

    }
    public static void show(Context mContext, String title, String msg){
        if(BaseApplication.isShowLog){
            Log.e(mContext.getPackageName()+"______"+title+":",msg);
        }
    }
}
