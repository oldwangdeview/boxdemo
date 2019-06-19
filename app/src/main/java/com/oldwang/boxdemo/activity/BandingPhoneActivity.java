package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.MemberInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.UserInfo;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdatePhoneEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LogUntil;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 绑定手机号
 */
public class BandingPhoneActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.input_phone)
    EditText input_phone;
    @BindView(R.id.input_code)
    EditText input_code;
    @BindView(R.id.getcode)
    Button getcode;

    private final static int MAX_TIME = 60;
    private boolean startthread = true;
    private boolean btn_getcode = true;

    private String code;
    private String oldPwd;
    private String phone;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_bandingphone);
    }


    @Override
    protected void initData() {
        super.initData();
        titlename.setText("绑定手机");
        oldPwd = getIntent().getStringExtra(Contans.INTENT_DATA);
    }

    @OnClick(R.id.getcode)
    public void getcode(){

        String username = input_phone.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            ToastUtils.makeText("请输入用户名/手机号");
            return;
        }

        if(!UIUtils.isPhoneNumber(username)){
            ToastUtils.makeText("请输入正确的手机号");
            return;
        }

        JsonObject jsonObject = new JsonObject();
        //	0-注册验证码 1-忘记密码验证码 2-资料完善 3-修改手机号）
        jsonObject.addProperty("type",3);
        jsonObject.addProperty("userName",username);
        getCode(HttpUtil.getRequsetBean(mContext,jsonObject));
    }

    private void getCode(RequestBean requestBean){
        Observable observable =
                ApiUtils.getApi().getCode(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<MemberInfo>(BandingPhoneActivity.this) {
            @Override
            protected void _onNext(StatusCode<MemberInfo> stringStatusCode) {
                starttimecode();
            }
            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                dismissLoadingDialog();
            }
        }, "", lifecycleSubject, false, true);

    }


    private void starttimecode(){
        if(btn_getcode) {
            btn_getcode = false;
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        if (startthread) {

                            for (int i = MAX_TIME; i >= 0; i--) {
                                Thread.sleep(1000);
                                Message msg = new Message();
                                msg.arg1 = i;
                                mHandler.sendMessage(msg);
                            }

                        }
                    } catch (Exception e) {

                    }
                }
            }.start();
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==0){
                btn_getcode = true;
                getcode.setText("获取验证码");
            }else{
                getcode.setText(msg.arg1+"S 后获取");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startthread = false;
    }


    @OnClick(R.id.login_btn)
    public void ojbkbtn(){
        phone = input_phone.getText().toString().trim();
         code = input_code.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.makeText("请输入用户名/手机号");
            return;
        }

        if(!UIUtils.isPhoneNumber(phone)){
            ToastUtils.makeText("请输入正确的手机号");
            return;
        }

        if(TextUtils.isEmpty(code)){
            ToastUtils.makeText("请输入验证码");
            return;
        }
        bindingPhone();
    }



    private void bindingPhone(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code",code);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("oldPwd",oldPwd);
        jsonObject.addProperty("phone",phone);

        RequestBean requsetBean = HttpUtil.getRequsetBean(mContext,jsonObject);

        Observable observable =
                ApiUtils.getApi().bindingPhone(requsetBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<MemberInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<MemberInfo> stringStatusCode) {
                new LogUntil(mContext,TAG,new Gson().toJson(stringStatusCode));

                String user = PreferencesUtils.getInstance().getString(Contans.userInfo, "");
                if (!TextUtils.isEmpty(user)){
                    UserInfo userInfo = new Gson().fromJson(user,UserInfo.class);
                    userInfo.setMemberAccount(phone);
                    PreferencesUtils.getInstance().putString(Contans.userInfo,new Gson().toJson(user));
                }
                EventBus.getDefault().post(new UpdatePhoneEvent());
                ToastUtils.makeText("更改成功");
                dismissLoadingDialog();
                finish();
            }
            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                dismissLoadingDialog();
            }
        }, "", lifecycleSubject, false, true);
    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }

    public static void startactivity(Context mContext,String oldPwd){
        Intent mintent = new Intent(mContext,BandingPhoneActivity.class);
        mintent.putExtra(Contans.INTENT_DATA,oldPwd);
        mContext.startActivity(mintent);
    }
}
