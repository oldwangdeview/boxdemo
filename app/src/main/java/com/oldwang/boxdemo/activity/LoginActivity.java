package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.LoginBean;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.UserInfo;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LogUntil;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.showFullScreen(LoginActivity.this,false);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);
        updateactionbar();
    }


    @Override
    protected void initData() {
        super.initData();

        setGoneTitle();
    }

    private void setGoneTitle(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    Thread.sleep(200);
                   Message msg = new Message();
                   msg.arg1 = 101;
                   mhandel.sendMessage(msg);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler mhandel = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UIUtils.showFullScreen(LoginActivity.this,false);
            updateactionbar();

        }
    };

    /**
     * 注册
     */
    @OnClick(R.id.layout_register)
    public void gotoREgitser(){
        RegisterActivity.startactivity(mContext);
        finish();
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,LoginActivity.class);
        mContext.startActivity(mIntent);
    }



    @OnClick(R.id.login_btn_loginforphone)
    public void GotoLoginPhoneActivity(){
       LoginForPhoneActivity.startactivity(this);
       finish();
    }

    /**
     * QQ登陆
     */

    @OnClick(R.id.layout_qq)
    public void logininQQ() {
        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
    }

    /**
     * 微信登陆
     */
    @OnClick(R.id.layout_wechart)
    public void loginwechart() {
        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            //   Toast.makeText(mContext, "成功了", Toast.LENGTH_LONG).show();
            String uid = data.get("uid");
            String name = data.get("name");
            String gender = data.get("gender");
            String iconurl = data.get("iconurl");
            LoginBean loginuser = new LoginBean();
            loginuser.nickName = name;
            loginuser.headBigImage = iconurl;
            loginuser.headSmallImage = iconurl;
            loginuser.sex = TextUtils.isEmpty(gender) ? "0" : gender.equals("男") ? "1" : "2";

            if (platform == SHARE_MEDIA.QQ) {
                loginuser.qqOpenid = uid;
                loginQQ(loginuser.headBigImage, loginuser.nickName, uid);
            }
            if (platform == SHARE_MEDIA.WEIXIN) {
                loginuser.weixinOpenid = uid;
                loginWeiXin(loginuser.headBigImage, loginuser.nickName, uid);
            }

        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(mContext, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            Toast.makeText(mContext, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    private void loginQQ(String figureUrl, String nickName, String openId) {


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("figureUrl", figureUrl);
        jsonObject.addProperty("nickName", nickName);
        jsonObject.addProperty("openId", openId);

        RequestBean requsetBean = HttpUtil.getRequsetBean(mContext, jsonObject);

        Observable observable =
                ApiUtils.getApi().loginQQ(requsetBean)
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
                dismissLoadingDialog();
                new LogUntil(mContext, TAG, new Gson().toJson(stringStatusCode));
                UserInfo userInfo = stringStatusCode.getData().getUserInfo();

                String phone = "";
                if (userInfo != null) {
                    if (!TextUtils.isEmpty(userInfo.getMemberAccount())) {
                        phone = userInfo.getMemberAccount();
                    }
                    AbsSuperApplication.setToken(userInfo.getToken());
                }

                if (!TextUtils.isEmpty(phone)) {
                    PreferencesUtils.getInstance().putString(Contans.userInfo, new Gson().toJson(userInfo));
                    MainActivity.startactivity(mContext);
                }else {
                    //未绑定手机号码
                    CompleteDataActivity.startactivity(mContext,userInfo.getMemberId());
                }

                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                dismissLoadingDialog();
            }
        }, "", lifecycleSubject, false, true);
    }

    private void loginWeiXin(String figureUrl, String nickName, String openId) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("figureUrl", figureUrl);
        jsonObject.addProperty("nickName", nickName);
        jsonObject.addProperty("openId", openId);

        RequestBean requsetBean = HttpUtil.getRequsetBean(mContext, jsonObject);

        Observable observable =
                ApiUtils.getApi().loginWeiXin(requsetBean)
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
                dismissLoadingDialog();
                new LogUntil(mContext, TAG, new Gson().toJson(stringStatusCode));
                UserInfo userInfo = stringStatusCode.getData().getUserInfo();

                String phone = "";
                if (userInfo != null) {
                    if (!TextUtils.isEmpty(userInfo.getMemberAccount())) {
                        phone = userInfo.getMemberAccount();
                    }
                    AbsSuperApplication.setToken(userInfo.getToken());
                }

                if (!TextUtils.isEmpty(phone)) {
                    PreferencesUtils.getInstance().putString(Contans.userInfo, new Gson().toJson(userInfo));
                    MainActivity.startactivity(mContext);
                }else {
                    //未绑定手机号码
                    CompleteDataActivity.startactivity(mContext,userInfo.getMemberId());
                }

                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                dismissLoadingDialog();
            }
        }, "", lifecycleSubject, false, true);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
