package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.Addimageadpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.ImageBean;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateAppoinmentEvent;
import com.oldwang.boxdemo.event.UpdateOrderListEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.MyGridView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
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

/**
 * 场馆评价
 */
public class VenusPriseActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;


    @BindView(R.id.image_1)
    ImageView image_1;

    @BindView(R.id.image_2)
    ImageView image_2;

    @BindView(R.id.image_3)
    ImageView image_3;

    @BindView(R.id.image_4)
    ImageView image_4;

    @BindView(R.id.image_5)
    ImageView image_5;


    @BindView(R.id.et_content)
    EditText et_content;



    private String bookingId;
    private String venueId;


    private String commentGrade;
    private String commentContent;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_venue_pirsel);
    }

    @Override
    protected void initData() {
        super.initData();
        commentGrade  =  "4";
        titlename.setText("场馆评价");
        bookingId = getIntent().getStringExtra(Contans.INTENT_DATA);
        venueId = getIntent().getStringExtra(Contans.INTENT_TYPE);
    }



    public static void startactivity(Context mContext,String bookingId,String venueId){
        Intent mIntent = new Intent(mContext,VenusPriseActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,bookingId);
        mIntent.putExtra(Contans.INTENT_TYPE,venueId);
        mContext.startActivity(mIntent);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }

    @OnClick({R.id.image_1,R.id.image_2,R.id.image_3,R.id.image_4,R.id.image_5,R.id.btn_submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_1:
                commentGrade = "1";
                image_1.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_2.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
                image_3.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
                image_4.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
                image_5.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));

                break;
            case R.id.image_2:
                commentGrade = "2";
                image_1.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_2.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_3.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
                image_4.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
                image_5.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
                break;
            case R.id.image_3:
                commentGrade = "3";
                image_1.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_2.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_3.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_4.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
                image_5.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
                break;
            case R.id.image_4:
                commentGrade = "4";
                image_1.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_2.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_3.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_4.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_5.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
                break;
            case R.id.image_5:
                commentGrade = "5";
                image_1.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_2.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_3.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_4.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                image_5.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
                break;

            case R.id.btn_submit:
                commentContent = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(commentContent)){
                    ToastUtils.makeText("请输入评论信息");
                    return;
                }
                customerServiceSave();
                break;
        }

    }



    /**
     * 评价
     */
    private void customerServiceSave() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("bookingId",bookingId);
        jsonObject.addProperty("commentContent",commentContent);
        jsonObject.addProperty("commentGrade",commentGrade);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("venueId",venueId);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().bespeakEvaluate(requestBean)
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

                if (stringStatusCode != null) {
                    ToastUtils.makeText("评论成功");
                    EventBus.getDefault().post(new UpdateAppoinmentEvent());
                    finish();
                    dismissLoadingDialog();
                }
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }

    private int count ;
    public void uploadFileList(List<File> files,String id) {
        count = 0;
        MultipartBody.Part body = null;

        for (File file : files) {
            if (file.exists()) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                Observable observable = ApiUtils.getApi().uploadfile(id, "shop_order_recommend_pics", body)
                        .compose(RxHelper.getObservaleTransformer())
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

                HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(VenusPriseActivity.this) {
                    @Override
                    protected void _onNext(StatusCode<Object> stringStatusCode) {

                        count++;
                        if (count == files.size()){
                            ToastUtils.makeText("评论成功");
                            EventBus.getDefault().post(new UpdateOrderListEvent());
                            finish();
                        }
                        dismissLoadingDialog();
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.makeText(message);
                        dismissLoadingDialog();
                    }
                }, "", lifecycleSubject, false, true);            }
        }



    }
}
