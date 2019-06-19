package com.oldwang.boxdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.ChoiceLogisticsAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.AddressData;
import com.oldwang.boxdemo.bean.CourierData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.GoodsOrderData;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 选择物流
 */

public class ChoiceLogisticsActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private ChoiceLogisticsAdpater madpater;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_choicelogistics);
    }

    @Override
    protected void initData() {
        super.initData();
        requestBean = (RequestBean) getIntent().getSerializableExtra(Contans.INTENT_DATA);
        titlename.setText("选择物流");
        getLogisticsList();
    }


    /***
     * 获取物流信息
     */
    private void getLogisticsList() {

//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
//        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().getLogisticsList(requestBean)
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
                    List<CourierData> courierData = stringStatusCode.getData().getCourierData();

                    if (courierData != null){
                        madpater = new ChoiceLogisticsAdpater(mContext,courierData);
                        madpater.setAlldataType(false);
                        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                        recyclerview.setItemAnimator(new DefaultItemAnimator());
                        recyclerview.setAdapter(madpater);
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

    /**
     * 确定
     */
    @OnClick({R.id.btn_ok})
    public void ok(){

        if (madpater != null){
            CourierData choicedata = madpater.getchoicedata();

            if (choicedata != null){
                Intent intent=getIntent();
                intent.putExtra("courierData", choicedata);
                setResult(RESULT_OK, intent);
                finish();
            }else {
                ToastUtils.makeText("请选择物流");
            }
        }


    }

    private RequestBean requestBean;

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,ChoiceLogisticsActivity.class);
        mContext.startActivity(mIntent);
    }
    public static void startActivitForResult(Activity activity, RequestBean requestBean, int requestCode) {
        Intent mIntent = new Intent(activity,ChoiceLogisticsActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,requestBean);
        activity.startActivityForResult(mIntent, requestCode);
    }

    public static void startActivitForResult(Activity activity, int requestCode) {
        Intent mIntent = new Intent(activity,ChoiceLogisticsActivity.class);
        activity.startActivityForResult(mIntent, requestCode);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }
}
