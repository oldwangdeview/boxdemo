package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.EvaluationData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class EvaluationDeatilActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_score)
    TextView tv_score;
    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.webView)
    WebView mWebView;




    private String evaluationDetailsId;

    private EvaluationData evaluationInfo;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_evaluationdetail);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("详情");
        evaluationDetailsId = getIntent().getStringExtra(Contans.INTENT_DATA);
        initWebView();
        evaluationDetail();

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

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }


    @OnClick(R.id.tv_to_goods)
    public void toGoods() {
        GoodsDetailActivity.startactivity(mContext,false,evaluationInfo.getCommodityId(),null);
    }


    public static void startactivity(Context mContext, String evaluationDetailsId) {
        Intent mIntent = new Intent(mContext, EvaluationDeatilActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA, evaluationDetailsId);
        mContext.startActivity(mIntent);
    }


    private void evaluationDetail() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("evaluationDetailsId", evaluationDetailsId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().evaluationDetail(requestBean)
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
                    evaluationInfo = stringStatusCode.getData().getEvaluationInfo();
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

    private void setData() {

        if (evaluationInfo != null){

            if (!TextUtils.isEmpty(evaluationInfo.getEvaluationDetailsName())){
                tv_name.setText(evaluationInfo.getEvaluationDetailsName());
            }
            if (!TextUtils.isEmpty(evaluationInfo.getEvaluationDetailsScore())){
                tv_score.setText("评测总分： "+evaluationInfo.getEvaluationDetailsScore()+"分");
            }

            if (!TextUtils.isEmpty(evaluationInfo.getEvaluationTime())) {
                tv_time.setText(DateTools.getFormat(Long.parseLong(evaluationInfo.getEvaluationTime())));
            }

            if (!TextUtils.isEmpty(evaluationInfo.getEvaluationDetailsDetail())) {
                String content = evaluationInfo.getEvaluationDetailsDetail();
                mWebView.loadDataWithBaseURL(null, getNewData(content), "text/html", "UTF-8", null);
            }
        }

    }
}
