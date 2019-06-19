package com.oldwang.boxdemo.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.oldwang.boxdemo.bean.UserInfo;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.util.PreferencesUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Author wangshifu
 * Time  2018/12 16:02
 * Dest  ${TODO}
 */

public abstract class AbsSuperApplication extends Application {

    protected static Context context;
    protected static String appName;

    /**
     *获取当前启动Activityd个数
     * @return
     */
    public static List<Activity> getmActivitys() {
        return mActivitys;
    }

    /**
     * 维护Activity 的list
     */
    private static List<Activity> mActivitys = Collections
            .synchronizedList(new LinkedList<Activity>());

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        ToastUtils.init(this);
        context = this.getApplicationContext();
        appName =  getAppNameFromSub();
        registerActivityListener();
    }
    private static String token;
    private static String memberId;


    public static String getToken() {

        if (TextUtils.isEmpty(token)){
            token = PreferencesUtils.getInstance().getString(Contans.token,"");
        }
        return token;
    }
    public static String getMemberId() {

        if (TextUtils.isEmpty(memberId)){
            String temp = PreferencesUtils.getInstance().getString(Contans.userInfo,"");

            if (!TextUtils.isEmpty(temp)){
                UserInfo userInfo = new Gson().fromJson(temp,UserInfo.class);
                memberId = userInfo.getMemberId();
            }
        }

        return  memberId;
    }

    public static void clearMemberId() {
        memberId = "";
    }
    public static void setToken(String nowToken) {
        token = nowToken;
        PreferencesUtils.getInstance().putString(Contans.token,token);
    }


    public static String getAppName() {
        return appName;
    }

    public static Context getContext() {
        return context;
    }

    protected abstract String  getAppNameFromSub();

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public void pushActivity(Activity activity) {
        mActivitys.add(activity);
//        LogUtils.d("activityList:size:"+mActivitys.size());
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public void popActivity(Activity activity) {
        mActivitys.remove(activity);
//        LogUtils.d("activityList:size:"+mActivitys.size());
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return null;
        }
        Activity activity = mActivitys.get(mActivitys.size()-1);
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public static void finishCurrentActivity() {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        Activity activity = mActivitys.get(mActivitys.size()-1);
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        if (activity != null) {
            mActivitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 按照指定类名找到activity
     *
     * @param cls
     * @return
     */
    public static Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (mActivitys != null) {
            for (Activity activity : mActivitys) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }

    /**
     * @return 作用说明 ：获取当前最顶部activity的实例
     */
    public Activity getTopActivity() {
        Activity mBaseActivity = null;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity;

    }

    /**
     * @return 作用说明 ：获取当前最顶部的acitivity 名字
     */
    public String getTopActivityName() {
        Activity mBaseActivity = null;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity.getClass().getName();
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        mActivitys.clear();
    }

    /**
     * 保留指定界面
     * @param cls
     */
    public static void finishAllActivityReserver(Class<?> cls){

        if (mActivitys == null) {
            return;
        }

        Iterator<Activity> iterator = mActivitys.iterator();
        while(iterator.hasNext()){
            Activity activity = iterator.next();
            if (!activity.getClass().equals(cls)) {
                activity.finish();
                mActivitys.remove(activity);
            }
        }

    }


    /**
     * 退出应用程序
     */
    public  static void appExit() {
        try {
           // LogUtils.e("app exit");
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    private void registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    /**
                     *  监听到 Activity创建事件 将该 Activity 加入list
                     */
                    pushActivity(activity);

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (null==mActivitys||mActivitys.isEmpty()){
                        return;
                    }
                    if (mActivitys.contains(activity)){
                        /**
                         *  监听到 Activity销毁事件 将该Activity 从list中移除
                         */
                        popActivity(activity);
                    }
                }
            });
        }
    }

}

