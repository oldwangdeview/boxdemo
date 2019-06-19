package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.MasterData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.SchoolData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateSchoolEvent;
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

public class ArtsSchoolDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.shareimage)
    ImageView shareimage;

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.iv_zan)
    ImageView iv_zan;
    @BindView(R.id.tv_zan_count)
    TextView tv_zan_count;
    @BindView(R.id.webView)
    WebView mWebView;

    private String newsBoxingSchoolId = "";
    private SchoolData schoolInfo;
    private MasterData masterInfo;

    private boolean isTeacthear;


    @Override
    protected void initView() {
        setContentView(R.layout.activiy_artschool);
    }

    @Override
    protected void initData() {
        super.initData();
        newsBoxingSchoolId = getIntent().getStringExtra(Contans.INTENT_DATA);
        isTeacthear = getIntent().getBooleanExtra(Contans.INTENT_TYPE,false);
        shareimage.setVisibility(View.VISIBLE);
        initWebView();

        if (isTeacthear){
            titlename.setText("名师战将");
            masterDetail();
        }else {
            titlename.setText("武校详情");
            schoolDetail();
        }

    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setTextSize(WebSettings.TextSize.LARGEST);
        settings.setUseWideViewPort(true); // 关键点
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setSupportZoom(false); // 支持缩放
        settings.setLoadWithOverviewMode(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
    }


    /**
     * 分享
     */
    @OnClick(R.id.shareimage)
    public void shared(){
        share(1,"","","");
    }

    /**
     * 赞
     */
    @OnClick(R.id.ll_zan)
    public void zan(){
//        if (isZan){
//            ToastUtils.makeText("你已经赞过啦");
//            return;
//        }
        if (isTeacthear){
            masterPraise();
        }else {
            schoolPraise();
        }
    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }


    public static void startactivity(Context mContext, String newsBoxingSchoolId, boolean isTeacthear){
        Intent mIntent = new Intent(mContext,ArtsSchoolDetailActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,newsBoxingSchoolId);
        mIntent.putExtra(Contans.INTENT_TYPE,isTeacthear);
        mContext.startActivity(mIntent);
    }

    private void masterPraise() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("newsMasterId", newsBoxingSchoolId);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().masterPraise(requestBean)
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
                masterDetail();
            }

            @Override
            protected void _onError(String message) {
                isZan = !isZan;
                if (isZan){
                    iv_zan.setImageDrawable(getResources().getDrawable(R.mipmap.alreay_prise_icon));
                }else {
                    iv_zan.setImageDrawable(getResources().getDrawable(R.mipmap.news_zan_hei));
                }

                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    private void schoolPraise() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("newsBoxingSchoolId", newsBoxingSchoolId);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().schoolPraise(requestBean)
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
                isZan = !isZan;
                if (isZan){
                    iv_zan.setImageDrawable(getResources().getDrawable(R.mipmap.alreay_prise_icon));
                }else {
                    iv_zan.setImageDrawable(getResources().getDrawable(R.mipmap.news_zan_hei));
                }
                EventBus.getDefault().post(new UpdateSchoolEvent());
                schoolDetail();
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    private void masterDetail() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("newsMasterId", newsBoxingSchoolId);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().masterDetail(requestBean)
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

                if (stringStatusCode != null){
                    masterInfo = stringStatusCode.getData().getMasterInfo();
                }
                setDataTwo();
                dismissLoadingDialog();
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    private void schoolDetail() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("newsBoxingSchoolId", newsBoxingSchoolId);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().schoolDetail(requestBean)
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

                if (stringStatusCode != null){
                    schoolInfo = stringStatusCode.getData().getSchoolInfo();
                }
                setData();
                dismissLoadingDialog();
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    private boolean isZan = false;

    //名师战将详情
    private void setDataTwo() {

        if (masterInfo != null){

            if (!TextUtils.isEmpty(masterInfo.getIsPraise()) &&  masterInfo.getIsPraise().equals("1")){
                isZan = true;
                iv_zan.setImageDrawable(getResources().getDrawable(R.mipmap.alreay_prise_icon));
            }else {
                isZan = false;
                iv_zan.setImageDrawable(getResources().getDrawable(R.mipmap.news_zan_hei));
            }


            if (!TextUtils.isEmpty(masterInfo.getNewsMasterName())){
                tv_name.setText(masterInfo.getNewsMasterName());
            }
            if (!TextUtils.isEmpty(masterInfo.getNewsMasterPraiseCount())){
                tv_zan_count.setText(masterInfo.getNewsMasterPraiseCount());
            }

            if (!TextUtils.isEmpty(masterInfo.getNewsMasterDetail())) {
                String content = masterInfo.getNewsMasterDetail();
                mWebView.loadDataWithBaseURL(null, getNewData(content), "text/html", "UTF-8", null);
            }
        }

    }
    //学校详情
    private void setData() {

        if (schoolInfo != null){

            if (!TextUtils.isEmpty(schoolInfo.getIsPraise()) &&  schoolInfo.getIsPraise().equals("1")){
                isZan = true;
                iv_zan.setImageDrawable(getResources().getDrawable(R.mipmap.alreay_prise_icon));
            }else {
                isZan = false;
                iv_zan.setImageDrawable(getResources().getDrawable(R.mipmap.news_zan_hei));
            }

            if (!TextUtils.isEmpty(schoolInfo.getNewsBoxingSchoolName())){
                tv_name.setText(schoolInfo.getNewsBoxingSchoolName());
            }
            if (!TextUtils.isEmpty(schoolInfo.getNewsBoxingSchoolPraiseCount())){
                tv_zan_count.setText(schoolInfo.getNewsBoxingSchoolPraiseCount());
            }

            if (!TextUtils.isEmpty(schoolInfo.getNewsBoxingSchoolDetail())) {
                String content = schoolInfo.getNewsBoxingSchoolDetail();
                mWebView.loadDataWithBaseURL(null, getNewData(content), "text/html", "UTF-8", null);
            }
        }

    }




}
