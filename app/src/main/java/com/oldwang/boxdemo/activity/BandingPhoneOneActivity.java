package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
 * 绑定手机号
 */
public class BandingPhoneOneActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.input_phone)
    EditText input_phone;
    @BindView(R.id.input_code)
    EditText input_code;
    @BindView(R.id.getcode)
    Button getcode;

    @BindView(R.id.ll_code)
    LinearLayout ll_code;



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
        phone = getIntent().getStringExtra(Contans.INTENT_DATA);
        input_phone.setHint("输入原密码");
        titlename.setText("绑定手机");
        ll_code.setVisibility(View.GONE);
    }



    @OnClick(R.id.login_btn)
    public void ojbkbtn(){
        oldPwd = input_phone.getText().toString().trim();
        if(TextUtils.isEmpty(oldPwd)){
            ToastUtils.makeText("请输入原密码");
            return;
        }
        if (oldPwd.length() < 6){
            ToastUtils.makeText("密码长度不能小于6位");
            return;
        }

        bindCheck();
    }



    private void bindCheck(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("oldPwd",oldPwd);
//        jsonObject.addProperty("password",oldPwd);
        jsonObject.addProperty("phone",phone);

        RequestBean requsetBean = HttpUtil.getRequsetBean(mContext,jsonObject);

        Observable observable =
                ApiUtils.getApi().bindCheck(requsetBean)
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
                BandingPhoneActivity.startactivity(mContext,oldPwd);
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

    public static void startactivity(Context mContext,String phone){
        Intent mintent = new Intent(mContext,BandingPhoneOneActivity.class);
        mintent.putExtra(Contans.INTENT_DATA,phone);
        mContext.startActivity(mintent);
    }
}
