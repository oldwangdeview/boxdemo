package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginForPhoneActivity extends BaseActivity {
    @BindView(R.id.input_username)
    EditText input_username;
    @BindView(R.id.input_password)
    EditText input_password;
    @BindView(R.id.login_phone_imageseepass)
    ImageView login_phone_imageseepass;
    @BindView(R.id.login_btn)
    TextView login_btn;

    private boolean seepass = false;
    private String phone;

    //是否可以点击登录按钮
    private boolean isLogin;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login_phone);
        phone = getIntent().getStringExtra(Contans.INTENT_DATA);
        if (!TextUtils.isEmpty(phone)) {
            input_username.setText(phone);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        input_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userName = input_username.getText().toString().trim();
                String password = input_password.getText().toString().trim();

                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)){
                    login_btn.setBackground(getResources().getDrawable(R.drawable.login_phone_btnshape_two));
                    isLogin =  true;
                }else {
                    login_btn.setBackground(getResources().getDrawable(R.drawable.login_phone_btnshape));
                    isLogin = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String userName = input_username.getText().toString().trim();
                String password = input_password.getText().toString().trim();

                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)){
                    login_btn.setBackground(getResources().getDrawable(R.drawable.login_phone_btnshape_two));
                    isLogin =  true;
                }else {
                    login_btn.setBackground(getResources().getDrawable(R.drawable.login_phone_btnshape));
                    isLogin = false;
                }
            }
        });
    }

    @OnClick(R.id.login_phone_imageseepass)
    public void settpassword() {

        if (seepass) {
            input_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            login_phone_imageseepass.setImageResource(R.mipmap.mimakejian);
        } else {
            input_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            login_phone_imageseepass.setImageResource(R.mipmap.yincangmima);
        }
        seepass = !seepass;
    }


    @OnClick(R.id.login_btn)
    public void loginBnt() {

        if (!isLogin){
            return;
        }

        String username = input_username.getText().toString().trim();
        String password = input_password.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.makeText("请输入用户名/手机号");
            return;
        }

        if (!UIUtils.isPhoneNumber(username)) {
            ToastUtils.makeText("请输入正确的手机号");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.makeText("请输入密码");
            return;
        }

        if (password.length() < 6) {
            ToastUtils.makeText("密码不能小于6位");
            return;
        }


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pwd", password);
        jsonObject.addProperty("userName", username);
        login(HttpUtil.getRequsetBean(mContext, jsonObject));

    }

    private void login(RequestBean requestBean) {
        Observable observable =
                ApiUtils.getApi().login(requestBean)
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
                PreferencesUtils.getInstance().putString(Contans.userInfo, new Gson().toJson(userInfo));
                AbsSuperApplication.setToken(userInfo.getToken());
                MainActivity.startactivity(mContext);
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                dismissLoadingDialog();
            }
        }, "", lifecycleSubject, false, true);

    }


    @OnClick(R.id.forgetpassword)
    public void forgetpassword() {
        FindPasswordActivity.startactivity(this);
    }

    @OnClick(R.id.registertext)
    public void register() {
        RegisterActivity.startactivity(this);
    }

    public static void startactivity(Context mContext) {
        Intent mIntent = new Intent(mContext, LoginForPhoneActivity.class);
        mContext.startActivity(mIntent);

    }

    /**
     * 启动当前页面
     *
     * @param mContext
     */
    public static void startactivity(Context mContext, String phone) {
        Intent mIntent = new Intent(mContext, AddPathActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA, phone);
        mContext.startActivity(mIntent);
    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }


    /**
     * QQ登陆
     */

    @OnClick(R.id.ll_qq)
    public void logininQQ() {
        UMShareAPI.get(LoginForPhoneActivity.this).getPlatformInfo(LoginForPhoneActivity.this, SHARE_MEDIA.QQ, authListener);
    }

    /**
     * 微信登陆
     */
    @OnClick(R.id.ll_weixin)
    public void loginwechart() {
        UMShareAPI.get(LoginForPhoneActivity.this).getPlatformInfo(LoginForPhoneActivity.this, SHARE_MEDIA.WEIXIN, authListener);
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
