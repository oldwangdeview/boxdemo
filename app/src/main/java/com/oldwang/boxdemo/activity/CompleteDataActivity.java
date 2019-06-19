package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.MemberInfo;
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

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 完善资料
 */

public class CompleteDataActivity extends BaseActivity {
    @BindView(R.id.input_username)
    EditText input_username;

    @BindView(R.id.getcode)
    Button getcode;
    @BindView(R.id.input_code)
    EditText input_code;
    @BindView(R.id.input_yqm)
    EditText input_yqm;



    private String memberId;
    private String beinvitateCode = "";
    private String identifyingCode = "";
    private String memberAccount = "";

    private boolean seepass = false;
    private final static int MAX_TIME = 60;
    private boolean startthread = true;
    private boolean btn_getcode = true;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_completedata);
    }


    @Override
    protected void initData() {
        super.initData();
        memberId = getIntent().getStringExtra(Contans.INTENT_DATA);

    }

    @OnClick(R.id.next_btn)
    public void okbtton(){

        memberAccount = input_username.getText().toString().trim();
        identifyingCode =  input_code.getText().toString().trim();
        beinvitateCode =  input_yqm.getText().toString().trim();

        if(TextUtils.isEmpty(memberAccount)){
            ToastUtils.makeText("请输入用户名/手机号");
            return;
        }

        if(!UIUtils.isPhoneNumber(memberAccount)){
            ToastUtils.makeText("请输入正确的手机号");
            return;
        }

        if(TextUtils.isEmpty(memberAccount)){
            ToastUtils.makeText("请输入用户名/手机号");
            return;
        }

        if(TextUtils.isEmpty(identifyingCode)){
            ToastUtils.makeText("请输入验证码");
            return;
        }

        if(TextUtils.isEmpty(beinvitateCode)){
            beinvitateCode = "";
        }
        updateMemberInfo();
    }



    public static void startactivity(Context mContext,String memberId){
        Intent mIntent = new Intent(mContext,CompleteDataActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,memberId);
        mContext.startActivity(mIntent);

    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }

    @OnClick(R.id.getcode)
    public void getcode(){

        if (!btn_getcode){
            return;
        }
        String username = input_username.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            ToastUtils.makeText("请输入手机号");
            return;
        }

        if(!UIUtils.isPhoneNumber(username)){
            ToastUtils.makeText("请输入正确的手机号");
            return;
        }

        JsonObject jsonObject = new JsonObject();
        //	0-注册验证码 1-忘记密码验证码 2-资料完善 3-修改手机号）
        jsonObject.addProperty("type",2);
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<MemberInfo>(CompleteDataActivity.this) {
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

    private void updateMemberInfo() {


        JsonObject jsonObject = new JsonObject();
        if (!TextUtils.isEmpty(beinvitateCode)){
            jsonObject.addProperty("beinvitateCode", beinvitateCode);
        }
        jsonObject.addProperty("identifyingCode", identifyingCode);
        jsonObject.addProperty("memberAccount", memberAccount);
        jsonObject.addProperty("memberId", memberId);

        RequestBean requsetBean = HttpUtil.getRequsetBean(mContext, jsonObject);

        Observable observable =
                ApiUtils.getApi().updateMemberInfo(requsetBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
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
}
