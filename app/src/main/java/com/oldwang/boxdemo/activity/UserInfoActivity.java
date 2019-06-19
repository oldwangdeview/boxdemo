package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.MemberInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.event.UpdateUserEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.FileUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.CircleImageView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
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

public class UserInfoActivity extends BaseActivity {
    private static final int REQUEST_CODE_HEAD = 0x123;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.et_nick_name)
    EditText et_nick_name;

    @BindView(R.id.et_state)
    TextView et_state;

    @BindView(R.id.et_level)
    EditText et_level;


    private MemberInfo userInfo;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_userinfo);
    }


    @Override
    protected void initData() {
        super.initData();
        getUserInfo();
        titlename.setText("个人资料");
    }

    @OnClick(R.id.ll_head)
    public void chooseHead() {
        Matisse.from(UserInfoActivity.this)
                .choose(MimeType.allOf())//图片类型
                .countable(false)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(1)//可选的最大数
                .capture(true)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.oldwang.boxdemo"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new GlideEngine())//图片加载引擎
                .forResult(REQUEST_CODE_HEAD);
    }

    @OnClick(R.id.layout_bangdingidcard)
    public void bangdingidcard() {
        //	状态(-1审核打回 0-待审核 1-审核通过)
        if (!TextUtils.isEmpty(userInfo.getIdentStates())) {
            AuthenticationIDCardActivity.startactivity(mContext,userInfo.getIdentStates());
        }else {
            AuthenticationIDCardActivity.startactivity(mContext,"");
        }


    }


    @OnClick(R.id.layout_invitationcode)
    public void gotoInviatationcode() {
        InvitationcodeActivity.startactivity(mContext,userInfo.getInvitateCode());
    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }

    public static void startactivity(Context mContext) {
        Intent mIntent = new Intent(mContext, UserInfoActivity.class);
        mContext.startActivity(mIntent);
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
                    userInfo = data.getMemberInfo();
                    setUserData();
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }



    private void setUserData() {

        if (userInfo != null) {

            if (!TextUtils.isEmpty(userInfo.getMemberHeadLogo())) {
                UIUtils.loadImageView(mContext, userInfo.getMemberHeadLogo(), iv_head);
            }
            if (!TextUtils.isEmpty(userInfo.getMemberNickname())) {
                et_nick_name.setText(userInfo.getMemberNickname());
                et_nick_name.setSelection(et_nick_name.getText().toString().length());
            }


            if (!TextUtils.isEmpty(userInfo.getScoresLevel())) {
                et_level.setText(userInfo.getScoresLevel());
            }
            //	状态(-1审核打回 0-待审核 1-审核通过)
            if (!TextUtils.isEmpty(userInfo.getIdentStates())) {
                et_state.setText(userInfo.getIdentStatesDes());
            }else {
                et_state.setText("未提交审核");
            }

        }

        et_nick_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String nickName = et_nick_name.getText().toString().trim();
                if (TextUtils.isEmpty(nickName)){
                    ToastUtils.makeText("请输入昵称");
                    return false;
                }
                updateNick(nickName);
                return false;
            }
        });
    }

    public void uploadHead(File head) {
        showLoadingDialog();
        MultipartBody.Part body = null;

        if (head.exists()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), head);
            body = MultipartBody.Part.createFormData("file", head.getName(), requestBody);
        }

        Observable observable = ApiUtils.getApi().uploadfile(AbsSuperApplication.getMemberId(), "member_info.member_headurl", body)
                .compose(RxHelper.getObservaleTransformer())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(UserInfoActivity.this) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                dismissLoadingDialog();
                ToastUtils.makeText("头像更新成功");
                EventBus.getDefault().post(new UpdateUserEvent());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                dismissLoadingDialog();
            }
        }, "", lifecycleSubject, false, true);
    }


    /**
     * 更新昵称
     */
    private void updateNick(String memberNickname) {
        showLoadingDialog();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("memberNickname", memberNickname);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().updateNick(requestBean)
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
                ToastUtils.makeText("昵称更新成功");
                EventBus.getDefault().post(new UpdateUserEvent());

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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_HEAD:
                    List<Uri> result2 = Matisse.obtainResult(data);
                    if (result2 != null && result2.size() > 0) {
                        Glide.with(mContext).load(result2.get(0))
                                .into(iv_head);
                        File file = FileUtils.uriToFile(result2.get(0), mContext);
                        uploadHead(file);
                    }

                    break;

            }

        }
    }
    /**
     * 更新身份状态
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserEvent event) {
        getUserInfo();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
