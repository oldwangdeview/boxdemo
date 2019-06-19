package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.ShadowLayout;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.ExpressAdapter;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.CommodityData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.LogisticsData;
import com.oldwang.boxdemo.bean.MyOrderData;
import com.oldwang.boxdemo.bean.OrderInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 物流信息
 */
public class LogisticsInformationActivity extends BaseActivity {

    @BindView(R.id.tv_state)
    TextView tv_state;

    @BindView(R.id.iv_image)
    ImageView iv_image;

    @BindView(R.id.tv_express_name)
    TextView tv_express_name;

    @BindView(R.id.tv_express_number)
    TextView tv_express_number;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.shadowLayout)
    ShadowLayout shadowLayout;


    private MyOrderData orderData;
    private boolean isList;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_logisticsinformation);
    }

    @Override
    protected void initData() {
        super.initData();
        isList = getIntent().getBooleanExtra(Contans.INTENT_TYPE,false);
        if (!isList){
            dataInfo1 = (DataInfo1) getIntent().getSerializableExtra(Contans.INTENT_DATA);
        }else {
            orderData = (MyOrderData) getIntent().getSerializableExtra(Contans.INTENT_DATA);
        }

        if (dataInfo1 != null){
            OrderInfo orderInfo = dataInfo1.getOrderInfo();
            List<LogisticsData> logisticsData = dataInfo1.getLogisticsData();

            if (logisticsData != null && logisticsData.size() > 0){
                logisticsData.get(0).setTop(true);
                LogisticsData logisticsData1 = new LogisticsData();
                logisticsData1.setLogisticsNote(orderInfo.getReceivingAddress());
                logisticsData1.setFirst(true);
                logisticsData.add(0,logisticsData1);
            }


            ExpressAdapter adapter = new ExpressAdapter(mContext,logisticsData);
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
            recyclerview.setItemAnimator(new DefaultItemAnimator());
            recyclerview.setAdapter(adapter);

            if (orderInfo != null){
                List<CommodityData> commoditys = orderInfo.getCommoditys();

                if (commoditys != null && commoditys.size() > 0){
                    CommodityData commodityData = commoditys.get(0);

                    if (!TextUtils.isEmpty(commodityData.getCommodityPicUrl())){
                        UIUtils.loadImageView(mContext,commodityData.getCommodityPicUrl(),iv_image);
                    }
                }
                if (!TextUtils.isEmpty(orderInfo.getLogisticsStatus())){
                    tv_state.setText(orderInfo.getLogisticsStatus());
                }
                if (!TextUtils.isEmpty(orderInfo.getCourierName())){
                    tv_express_name.setText(orderInfo.getCourierName());
                }
                if (!TextUtils.isEmpty(orderInfo.getCourierNo())){
                    tv_express_number.setText(orderInfo.getCourierNo());
                }

            }else {
                shadowLayout.setVisibility(View.GONE);
            }
        }else {
            logisticsInfo();
        }

    }

    /**
     * 物流信息
     */
    private void logisticsInfo() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courierNo", orderData.getCourierNo());
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("orderNo", orderData.getOrderNo());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().logisticsInfo(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {

                if (stringStatusCode != null) {
                    dataInfo1 = stringStatusCode.getData();
                    OrderInfo orderInfo = dataInfo1.getOrderInfo();
                    List<LogisticsData> logisticsData = dataInfo1.getLogisticsData();

                    if (logisticsData != null && logisticsData.size() > 0){
                        logisticsData.get(0).setTop(true);
                        LogisticsData logisticsData1 = new LogisticsData();
                        logisticsData1.setLogisticsNote(orderInfo.getReceivingAddress());
                        logisticsData1.setFirst(true);
                        logisticsData.add(0,logisticsData1);
                    }


                    ExpressAdapter adapter = new ExpressAdapter(mContext,logisticsData);
                    recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                    recyclerview.setItemAnimator(new DefaultItemAnimator());
                    recyclerview.setAdapter(adapter);

                    if (orderInfo != null){
                        List<CommodityData> commoditys = orderInfo.getCommoditys();

                        if (commoditys != null && commoditys.size() > 0){
                            CommodityData commodityData = commoditys.get(0);

                            if (!TextUtils.isEmpty(commodityData.getCommodityPicUrl())){
                                UIUtils.loadImageView(mContext,commodityData.getCommodityPicUrl(),iv_image);
                            }
                        }
                        if (!TextUtils.isEmpty(orderInfo.getLogisticsStatus())){
                            tv_state.setText(orderInfo.getLogisticsStatus());
                        }
                        if (!TextUtils.isEmpty(orderInfo.getCourierName())){
                            tv_express_name.setText(orderInfo.getCourierName());
                        }
                        if (!TextUtils.isEmpty(orderInfo.getCourierNo())){
                            tv_express_number.setText(orderInfo.getCourierNo());
                        }

                    }
                }
                dismissLoadingDialog();
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }

    private DataInfo1 dataInfo1;

    public static void startactivity(Context mContext,DataInfo1 dataInfo1){
        Intent mIntent = new Intent(mContext,LogisticsInformationActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,dataInfo1);
        mContext.startActivity(mIntent);
    }

    public static void startactivity(Context mContext,MyOrderData orderData,boolean isList){
        Intent mIntent = new Intent(mContext,LogisticsInformationActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,orderData);
        mIntent.putExtra(Contans.INTENT_TYPE,isList);
        mContext.startActivity(mIntent);
    }

    @OnClick(R.id.image_retuen)
    public void image_return(){
        finish();
    }


    //联系商家
    @OnClick(R.id.tv_call)
    public void call(){


    }
}
