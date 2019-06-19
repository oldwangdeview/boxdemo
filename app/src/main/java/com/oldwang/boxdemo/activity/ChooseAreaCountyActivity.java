package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.AreaAdapter;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.AreaTempData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RegionData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.view.YRecycleview;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 选择收货地址区域列表-省
 */
public class ChooseAreaCountyActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.tv_small_title_layout_head)
    TextView rightext;
    @BindView(R.id.recyclerview)
    YRecycleview recylerview;


    AreaAdapter madpater;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_choose_area);
    }


    private AreaTempData areaTempData;

    @Override
    protected void initData() {
        super.initData();
        areaTempData = (AreaTempData) getIntent().getSerializableExtra(Contans.INTENT_DATA);

        titlename.setText("选择区域");
        recylerview.setLoadMoreEnabled(false);
        recylerview.setRefreshing(false);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        getData(3,areaTempData.getCityId());
    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }

    public static void startactivity(Context mContext, AreaTempData areaTempData){
        Intent mIntent = new Intent(mContext,ChooseAreaCountyActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,areaTempData);
        mContext.startActivity(mIntent);
    }



    /**
     * 地址列表
     */
    private void getData(int tag,String regionId) {

        showLoadingDialog();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        if (!TextUtils.isEmpty(regionId)) {
            jsonObject.addProperty("regionId", regionId);
        }
        jsonObject.addProperty("tag", tag);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().regionList(requestBean)
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
                    final List<RegionData> regionData = data.getRegionData();

                    madpater = new AreaAdapter(mContext,regionData);
                    recylerview.setLayoutManager(new LinearLayoutManager(mContext));
                    recylerview.setItemAnimator(new DefaultItemAnimator());
                    recylerview.setAdapter(madpater);
                    madpater.setListClickLister(new ListOnclickLister() {
                        @Override
                        public void onclick(View v, int position) {
                            RegionData regionData1 = regionData.get(position);
                            areaTempData.setCountyId(regionData1.getRegionId());
                            areaTempData.setName(areaTempData.getName()+regionData1.getRegionName());
                            EventBus.getDefault().post(areaTempData);
                            finish();

                        }
                    });
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                dismissLoadingDialog();
            }
        }, "", lifecycleSubject, false, true);

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

    /**
     * 跳转名师或战将页面
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void jumpTeachers(AreaTempData event) {
        finish();
    }

}
