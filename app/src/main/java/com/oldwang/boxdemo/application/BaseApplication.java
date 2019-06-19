package com.oldwang.boxdemo.application;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.util.EMLog;
import com.oldwang.boxdemo.activity.MainActivity;
import com.oldwang.boxdemo.db.Constant;
import com.oldwang.boxdemo.help.HxEaseuiHelper;
import com.oldwang.boxdemo.util.LogUntil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.jpush.android.api.JPushInterface;


/**
 * Author wangshifu
 * Time  2018/12/25 14:21
 * Dest  ${TODO}
 */
public class BaseApplication extends AbsSuperApplication {

    public static Context mContext;
    public static Handler mHandler;
    public static long		mMainThreadId;
    public static Thread	mMainThread;
    public static  boolean isLoginSuccess=false;//登录是否成功
    public static String sLocationCity="";  //城市定位
    public static BaseApplication mapp;
    public static boolean isShowLog = true ;//是否显示log

    public static Typeface TypeFaceYaHei;



    @Override
    public void onCreate() {
        super.onCreate();
        mapp = this;

        closeAndroidPDialog();
        mContext = getApplicationContext();
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this," 5b860801f29d98114f000096","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
        PlatformConfig.setWeixin("wx11144bc8fbf8e1e4", "b9230bd18880b3744cbb59fe06d2b66e");
        PlatformConfig.setQQZone("101585344", "8e5dd57f4917cb9fe22e4ec0c2d7cd84");
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);

        // 2.创建一个handler
        mHandler = new Handler();
        // 3.得到一个主线程id
        mMainThreadId = android.os.Process.myTid();
        // 4.得到主线程
        mMainThread = Thread.currentThread();
//        ListenClipboardService.start(this );
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

//        PushAgent

        //初始化环信
        initeaseui();

        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

//        EaseUI.getInstance().init(this, options);
        HxEaseuiHelper.getInstance().init(this.getApplicationContext());
        //设置全局监听
        setGlobalListeners();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        String registrationId = JPushInterface.getRegistrationID(this);

    }


    private void initeaseui(){
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
//初始化
        mContext = this;
        int pid = android.os.Process.myPid();
        String processAppName = getAppName();
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            Log.e("huanxin", "enter the service process--");

            return;
        }
        EMClient.getInstance().init(mContext, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);



    }

    public static BaseApplication getInstance(){
        return mapp;
    }


    @Override
    protected String getAppNameFromSub() {
        return null;
    }

    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置一个全局的监听
     */
    EMConnectionListener connectionListener;
    protected void setGlobalListeners(){


        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                EMLog.d("global listener", "onDisconnect" + error);
                if (error == EMError.USER_REMOVED) {// 显示帐号已经被移除
                    onUserException(Constant.ACCOUNT_REMOVED);
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {// 显示帐号在其他设备登录
                    onUserException(Constant.ACCOUNT_CONFLICT);
                    EMClient.getInstance().logout(true);//退出登录
                    Toast.makeText(getApplicationContext(),"退出成功",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {//
                    onUserException(Constant.ACCOUNT_FORBIDDEN);
                }
            }

            @Override
            public void onConnected() {
                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events

            }
        };

        //register connection listener
//        EMClient.getInstance().addConnectionListener(connectionListener);
    }
    protected void onUserException(String exception){
        EMLog.e("环信", "onUserException: " + exception);
//        Intent intent = new Intent(getBaseContext(), UserQrCodeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(exception, true);
//        this.startActivity(intent);
    }

}
