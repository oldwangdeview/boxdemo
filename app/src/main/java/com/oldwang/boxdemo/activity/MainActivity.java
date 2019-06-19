package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;

import android.view.Gravity;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.EvalutionAdapter;
import com.oldwang.boxdemo.adpater.MainPagerAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.CouponData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.EvaluationData;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.UserInfo;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.ExitLoginSuccess;
import com.oldwang.boxdemo.event.MainEvent;
import com.oldwang.boxdemo.event.MainJumpEvent;
import com.oldwang.boxdemo.event.ShowTitleEvent;
import com.oldwang.boxdemo.help.BottomNavigationViewHelper;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.FileUtils;
import com.oldwang.boxdemo.util.LogUntil;
import com.oldwang.boxdemo.util.PinyinEquipmentComparator;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.ForbidScrollViewpager;
import com.oldwang.boxdemo.view.MainCouPonDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.viewpager_activity_main)
    ForbidScrollViewpager viewpager_activity_main;


    private MainPagerAdpater pageradptaer;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    LinearLayout navigationView;

    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    public LinearLayout getNavigationView() {
        return navigationView;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            refreshItemIcon();
            UIUtils.showFullScreen(MainActivity.this,false);
            updateactionbar();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewpager_activity_main.setCurrentItem(0);
                    item.setIcon(R.mipmap.main_image_home_x);
                    return true;
                case R.id.navigation_zhunbei:
                    viewpager_activity_main.setCurrentItem(1);
                    item.setIcon(R.mipmap.main_image_quanlian_x);
                    return  true;
                case R.id.navigation_gwc:
                    viewpager_activity_main.setCurrentItem(2);
                    item.setIcon(R.mipmap.main_image_gwc_x);
                    return  true;
                case R.id.navigation_js:
                    viewpager_activity_main.setCurrentItem(3);
                    item.setIcon(R.mipmap.main_image_js_x);
                    return true;
                case R.id.navigation_muser:
                    viewpager_activity_main.setCurrentItem(4);
                    item.setIcon(R.mipmap.main_image_muser_x);
                    return true;
            }
            return false;
        }
    };


    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,MainActivity.class);
        mContext.startActivity(mIntent);
    }



    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
    }

    /**
     * 未选中时加载默认的图片
     */
    public void refreshItemIcon() {
        MenuItem item1 = navigation.getMenu().findItem(R.id.navigation_home);
        item1.setIcon(R.mipmap.main_image_home);
        MenuItem item2 = navigation.getMenu().findItem(R.id.navigation_zhunbei);
        item2.setIcon(R.mipmap.main_image_quanlian);
        MenuItem item3 = navigation.getMenu().findItem(R.id.navigation_gwc);
        item3.setIcon(R.mipmap.main_image_gwc);
        MenuItem item4 = navigation.getMenu().findItem(R.id.navigation_js);
        item4.setIcon(R.mipmap.main_image_js);
        MenuItem item5 = navigation.getMenu().findItem(R.id.navigation_muser);
        item5.setIcon(R.mipmap.main_image_muser);
    }

    @Override
    protected void initData() {
        super.initData();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);

        EventBus.getDefault().register(this);
        pageradptaer = new MainPagerAdpater(getSupportFragmentManager());
        viewpager_activity_main.setOffscreenPageLimit(5);
        viewpager_activity_main.setAdapter(pageradptaer);
        viewpager_activity_main.setCurrentItem(0);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        MenuItem item1 = navigation.getMenu().findItem(R.id.navigation_home);
        item1.setIcon(R.mipmap.main_image_home_x);
        UIUtils.showFullScreen(MainActivity.this,false);
        updateactionbar();
        creartFilepath();
        initEvIm();


        String temp = PreferencesUtils.getInstance().getString(Contans.userInfo,"");

        if (!TextUtils.isEmpty(temp)){
            UserInfo userInfo = new Gson().fromJson(temp,UserInfo.class);
            setAlias(userInfo.getMemberAccount());
        }


//        getcoupondata();

    }




    @Override
    protected void initEvent() {
        super.initEvent();

    }

    /**
     * 创建缓存目录文件夹
     */

    private void creartFilepath(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileUtils.APP_DIR = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/boxdemo";
        } else {
            FileUtils.APP_DIR = this.getFilesDir().getAbsolutePath() + "/boxdemo";
        }
        FileUtils.APP_LOG = FileUtils.APP_DIR + "/log";
        FileUtils.APP_CRASH = FileUtils.APP_DIR + "/crash";
        FileUtils.APK_DIR = FileUtils.APP_DIR + "/apks";
        FileUtils.IMAGE_DIR = FileUtils.APP_DIR + "/imag";

        File appDir = new File(FileUtils.APP_DIR);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File logDir = new File(FileUtils.APP_LOG);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        File crashDir = new File(FileUtils.APP_CRASH);
        if (!crashDir.exists()) {
            crashDir.mkdirs();
        }
        File apkDir = new File(FileUtils.APK_DIR);
        if (!apkDir.exists()) {
            apkDir.mkdirs();
        }
        File imageDir = new File(FileUtils.IMAGE_DIR);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
    }


    /**
     * 结束主页面
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishnowactivity(MainEvent event){
        JPushInterface.setAliasAndTags(getApplicationContext(),
                "",
                null,
                mAliasCallback);
        LoginForPhoneActivity.startactivity(mContext);
        AbsSuperApplication.setToken("");
        AbsSuperApplication.clearMemberId();
        PreferencesUtils.getInstance().putString(Contans.userInfo,"");
        BaseActivity.setmUserData(null);
        finish();
    }

    private boolean isStart = false;

    /**
     * 重新登录
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exitLoginSuccess(ExitLoginSuccess event){
        if (!isStart){
            isStart = true;
            JPushInterface.setAliasAndTags(getApplicationContext(),
                    "",
                    null,
                    mAliasCallback);
            BaseActivity.setmUserData(null);
            AbsSuperApplication.setToken("");
            AbsSuperApplication.clearMemberId();
            PreferencesUtils.getInstance().putString(Contans.userInfo,"");
            ToastUtils.makeText("登陆已失效，请重新登陆");
            AbsSuperApplication.finishAllActivity();
            LoginForPhoneActivity.startactivity(mContext);
        }

    }

    /**
     * 跳转
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void jump(MainJumpEvent event){
        int postion = event.getPostion();
        viewpager_activity_main.setCurrentItem(postion);
        navigation.setSelectedItemId(navigation.getMenu().getItem(postion).getItemId());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                LogUntil.show(mContext,TAG,"环信__"+"显示帐号已经退出登录");

            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub

            }
        });

    }

    private long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtils.makeText( "再点一次退出"+getString(R.string.app_name));
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTitle(ShowTitleEvent event){
        UIUtils.showFullScreen(MainActivity.this,event.showtitle);
        updateactionbar();
    }



    private void initEvIm(){

        if(BaseActivity.getUserInfo(this)!=null){
            if(!TextUtils.isEmpty(BaseActivity.getUserInfo(this).getMemberAccount())){


                Log.e("huanxin_username:",BaseActivity.getUserInfo(this).getMemberAccount());
                EMClient.getInstance().login(BaseActivity.getUserInfo(this).getMemberAccount(),"123456",new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.e("huanxin", "登录聊天服务器成功！");

                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        Log.e("huanxin", status);
                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.e("huanxin", "登录聊天服务器失败！");
                    }
                });

            }
            EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        }



    }


    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                        LogUntil.show(mContext,TAG,"环信__"+"显示帐号已经被移除");
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        LogUntil.show(mContext,TAG,"环信__"+"显示帐号在其他设备登录");
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this))
                        //连接不到聊天服务器
                            LogUntil.show(mContext,TAG,"环信__"+"连接不到聊天服务器");
                else
                        //当前网络不可用，请检查网络设置
                            LogUntil.show(mContext,TAG,"环信__"+"当前网络不可用，请检查网络设置");
                    }
                }
            });
        }
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias(String alias) {

        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
//            ToastUtils.makeText(logs);
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };





    private void getcoupondata(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", 1);
        jsonObject.addProperty("pageSize", 10000);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().updateComupons(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {

                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    ListData<CouponData> datalis = data.getCouponData();
                    if(datalis.getList()!=null&&datalis.getList().size()>0){


                        new MainCouPonDialog.Builder(mContext).build().show();
                    }
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);
    }
}
