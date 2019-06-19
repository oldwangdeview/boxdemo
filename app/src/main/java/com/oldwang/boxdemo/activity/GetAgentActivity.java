package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.oldwang.boxdemo.event.UpdateUserEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.FileUtils;
import com.oldwang.boxdemo.util.PhotoUtils;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.BottomMenuDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GetAgentActivity extends BaseActivity {

    private static final int REQUEST_CODE_ONE = 0x0001;
    private static final int REQUEST_CODE_TWO = 0x0002;



    //上传身份证试图
    @BindView(R.id.layout_view1)
    LinearLayout layout_view1;
    //正在审核试图
    @BindView(R.id.layout_view2)
    LinearLayout layout_view2;
    //审核通过视图
    @BindView(R.id.layout_view3)
    LinearLayout layout_view3;

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    //身份证正面照
    @BindView(R.id.image_idcardz)
    ImageView image_idcardz;
    //身份证反面照
    @BindView(R.id.image_idcardf)
    ImageView image_idcardf;


    @BindView(R.id.user_name)
    TextView user_name;

    @BindView(R.id.user_basename)
    TextView user_phone;

    private  File fileOne;
    private  File fileTwo;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_getagent);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("申请代理");
        String temp = PreferencesUtils.getInstance().getString(Contans.userInfo,"");

        if (!TextUtils.isEmpty(temp)){
            UserInfo myInfo = new Gson().fromJson(temp,UserInfo.class);

            if (myInfo != null && !TextUtils.isEmpty(myInfo.getMemberAccount())){
                user_phone.setText(myInfo.getMemberAccount());
            }

        }
        getUserInfo();

    }
    /**
     * 获取用户信息
     */
    private void getUserInfo() {

        showLoadingDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().getUerInfo(requestBean)
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
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    MemberInfo userInfo = data.getMemberInfo();

                    if (userInfo != null){
                        String agentType = userInfo.getAgentType();
                        //为空为未提交审核
                        if (TextUtils.isEmpty(agentType) || agentType.equals(-1)) {
                            layout_view1.setVisibility(View.VISIBLE);

                        } else {
                            //	状态(-1审核打回 0-待审核 1-审核通过)
                            layout_view1.setVisibility(View.GONE);
                            if (agentType.equals("0")) {
                                layout_view2.setVisibility(View.VISIBLE);
                            } else if (agentType.equals("1")) {
                                layout_view3.setVisibility(View.VISIBLE);
                            }

                        }
                        if (!TextUtils.isEmpty(userInfo.getRealName())){
                            user_name.setText(userInfo.getRealName());
                        }


                    }

                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }



    //身份证正面图
    public void uploadPositiveImg(File head) {
        showLoadingDialog();
        MultipartBody.Part body = null;

        if (head.exists()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), head);
            body = MultipartBody.Part.createFormData("file", head.getName(), requestBody);
        }

        Observable observable = ApiUtils.getApi().uploadfile(AbsSuperApplication.getMemberId(), "member_agent_apply.card_positive", body)
                .compose(RxHelper.getObservaleTransformer())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(mContext) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                uploadNegativeImg(fileTwo);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                dismissLoadingDialog();
            }
        }, "", lifecycleSubject, false, true);
    }

    //身份证反面图
    public void uploadNegativeImg(File head) {
        MultipartBody.Part body = null;

        if (head.exists()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), head);
            body = MultipartBody.Part.createFormData("file", head.getName(), requestBody);
        }

        Observable observable = ApiUtils.getApi().uploadfile(AbsSuperApplication.getMemberId(), "member_agent_apply.card_negative", body)
                .compose(RxHelper.getObservaleTransformer())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(mContext) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                upload();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                dismissLoadingDialog();
            }
        }, "", lifecycleSubject, false, true);
    }


    /**
     * 提交身份认证信息
     */
    private void upload() {

        String applyName = user_name.getText().toString();
        String applyPhone = user_phone.getText().toString();



        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("applyName", applyName);
        jsonObject.addProperty("applyPhone", applyPhone);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().agentApply(requestBean)
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
                EventBus.getDefault().post(new UpdateUserEvent());
                dismissLoadingDialog();
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode) {
                case REQUEST_CODE_ONE:
                    List<Uri> result = Matisse.obtainResult(data);
                    if (result != null && result.size() > 0) {
                        Glide.with(mContext).load(result.get(0))
                                .into(image_idcardz);
                        fileOne = FileUtils.uriToFile(result.get(0), mContext);
                    }
                    break;

                case REQUEST_CODE_TWO:
                    List<Uri> result2 = Matisse.obtainResult(data);
                    if (result2 != null && result2.size() > 0) {
                        Glide.with(mContext).load(result2.get(0))
                                .into(image_idcardf);
                        fileTwo = FileUtils.uriToFile(result2.get(0), mContext);
                    }

                    break;


            }
        }
    }



    /**
     * 获取身份证正面照
     */
    @OnClick(R.id.image_idcardz)
    public void getIDcardZ() {
        Matisse.from(GetAgentActivity.this)
                .choose(MimeType.allOf())//图片类型
                .countable(false)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(1)//可选的最大数
                .capture(true)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.oldwang.boxdemo"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new GlideEngine())//图片加载引擎
                .forResult(REQUEST_CODE_ONE);
    }

    /**
     * 获取身份证反面照
     */
    @OnClick(R.id.image_idcardf)
    public void getIDcardF() {
        Matisse.from(GetAgentActivity.this)
                .choose(MimeType.allOf())//图片类型
                .countable(false)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(1)//可选的最大数
                .capture(true)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.oldwang.boxdemo"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new GlideEngine())//图片加载引擎
                .forResult(REQUEST_CODE_TWO);
    }


    @OnClick(R.id.btn_sumbit)
    public void sumbit() {
        String applyName = user_name.getText().toString();
        String applyPhone = user_phone.getText().toString();

        if (TextUtils.isEmpty(applyName)){
            ToastUtils.makeText("请输入姓名");
            return;
        }

        if (TextUtils.isEmpty(applyPhone)){
            ToastUtils.makeText("请输入手机号码");
            return;
        }

        if (!UIUtils.isPhoneNumber(applyPhone)){
            ToastUtils.makeText("手机号不合法");
            return;
        }

        if (fileOne == null){
            ToastUtils.makeText("请上传身份证正面照片");
            return;
        }
        if (fileTwo == null){
            ToastUtils.makeText("请上传身份证反面照片");
            return;
        }

        uploadPositiveImg(fileOne);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }





    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,GetAgentActivity.class);
        mContext.startActivity(mIntent);
    }

}
