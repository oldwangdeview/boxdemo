package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateAppoinmentEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * 举报页面
 */
public class ReportActivity extends BaseActivity {


    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.et_content)
    EditText et_content;


    //0场馆举报
    private int type = 0;

    private String  venueId;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_report);
    }


    @Override
    protected void initData() {
        super.initData();
        titlename.setText("举报");
        venueId = getIntent().getStringExtra(Contans.INTENT_DATA);
        type = getIntent().getIntExtra(Contans.INTENT_TYPE,0);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }

    @OnClick(R.id.tv_submit)
    public void tv_submit(){
        String trim = et_content.getText().toString().trim();

        if (TextUtils.isEmpty(trim)){
            ToastUtils.makeText("请输入举报内容");
            return;
        }
        if (type == 0){
            delateBespeak(trim);
        }else{
            videorReportSave(trim);
        }

    }

    private void delateBespeak(String complaintReason) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("complaintReason", complaintReason);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("venueId", venueId);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().delateBespeak(requestBean)
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
                if (stringStatusCode != null) {
                    ToastUtils.makeText("举报成功，感谢您的反馈");
                    finish();
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    private void videorReportSave(String boxingVideoReportDetail) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("boxingVideoReportDetail", boxingVideoReportDetail);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("boxingVideoId", venueId);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().videorReportSave(requestBean)
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
                if (stringStatusCode != null) {
                    ToastUtils.makeText("举报成功，感谢您的反馈");
                    EventBus.getDefault().post(new UpdateAppoinmentEvent());
                    finish();
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    public static void startactivity(Context mContext,String venueId,int type){
        Intent mIntent = new Intent(mContext,ReportActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,venueId);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mContext.startActivity(mIntent);
    }



}
