package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.MemberInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ReplecePasswordActivity extends BaseActivity {

    @BindView(R.id.input_newpassword)
    EditText input_newpassword;
    @BindView(R.id.input_aginpassword)
    EditText input_aginpassword;
    @BindView(R.id.repele_imageseepass)
    ImageView repele_imageseepass;
    @BindView(R.id.imageseepass)
    ImageView imageseepass;
    private boolean seepass = false;
    private boolean seepass1 = false;

private String phone;
private String code;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_repelcepassword);
        phone = getIntent().getStringExtra(Contans.INTENT_PHONE);
        code = getIntent().getStringExtra(Contans.INTENT_CODE);
    }


    @OnClick(R.id.repele_imageseepass)
    public void settpassword(){

        if(seepass){
            input_newpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            repele_imageseepass.setImageResource(R.mipmap.mimakejian);
        }else{
            input_newpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            repele_imageseepass.setImageResource(R.mipmap.yincangmima);
        }
        seepass = !seepass;

    }


    @OnClick(R.id.imageseepass)
    public void settpassword2(){

        if(seepass1){
            input_aginpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageseepass.setImageResource(R.mipmap.mimakejian);
        }else{
            input_aginpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageseepass.setImageResource(R.mipmap.yincangmima);
        }
        seepass = !seepass;

    }

    @OnClick({R.id.ojbk_btn})
    public void okbtn(){
        String pass1 = input_newpassword.getText().toString().trim();
        String pass2 = input_aginpassword.getText().toString().trim();


        if(TextUtils.isEmpty(pass1)){
            ToastUtils.makeText("请输入新密码");
            return;
        }
        if(TextUtils.isEmpty(pass2)){
            ToastUtils.makeText("请二次输入新密码");
            return;
        }
        if(!pass1.equals(pass2)){
            ToastUtils.makeText("两次输入密码不一致");
            return;
        }


        showLoadingDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("identifyingCode", code);
        jsonObject.addProperty("memberAccount", phone);
        jsonObject.addProperty("newPwd", pass1);
        jsonObject.addProperty("rePwd", pass2);
        resetpwd(HttpUtil.getRequsetBean(mContext, jsonObject));

    }

    private void resetpwd(RequestBean requestBean) {
        Observable observable =
                ApiUtils.getApi().resetpwd(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<MemberInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<MemberInfo> stringStatusCode) {
                dismissLoadingDialog();
                ToastUtils.makeText(stringStatusCode.getMessage());
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

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,ReplecePasswordActivity.class);
        mContext.startActivity(mIntent);
    }
    public static void startactivity(Context mContext,String phone ,String code){
        Intent mIntent = new Intent(mContext,ReplecePasswordActivity.class);
        mIntent.putExtra(Contans.INTENT_PHONE,phone);
        mIntent.putExtra(Contans.INTENT_CODE,code);
        mContext.startActivity(mIntent);
    }
}
