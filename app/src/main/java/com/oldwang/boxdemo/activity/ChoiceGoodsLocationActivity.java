package com.oldwang.boxdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.ChicePathAdpater;
import com.oldwang.boxdemo.adpater.ChoiceLogisticsAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.AddressData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.event.UpdateAddress;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ChoiceGoodsLocationActivity  extends BaseActivity{

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;



    @BindView(R.id.tv_small_title_layout_head)
    TextView rightdata;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private ChicePathAdpater madpater;
    private int page = 1 ;
    private final int size = 20;
    List<AddressData> listdata = new ArrayList<>();
    @Override
    protected void initView() {
        setContentView(R.layout.activity_choicelogistics);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("选择收货地址");
        rightdata.setVisibility(View.VISIBLE);
        rightdata.setText("添加地址");
        madpater = new ChicePathAdpater(mContext,listdata);
        madpater.setListOnclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                AddPathActivity.startactivity(mContext,2,listdata.get(position));
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(madpater);
        getData();
    }


    /**
     * 添加地址
     */
    @OnClick({R.id.tv_small_title_layout_head})
    public void Addpath(){
        AddPathActivity.startactivity(mContext,1);
    }
    /**
     * 确定
     */
    @OnClick({R.id.btn_ok})
    public void ok(){

        AddressData choicedata = madpater.getChoicedata();

        if (choicedata != null){
            Intent intent=getIntent();
            intent.putExtra("address", choicedata);
            setResult(RESULT_OK, intent);
            finish();
        }else {
            ToastUtils.makeText("请选择地址");
        }

    }

    /**
     * 地址列表
     */
    private void getData() {

        showLoadingDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().addressList(requestBean)
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
                    ListData<AddressData> addressData = data.getAddressData();
                    List<AddressData> list = addressData.getList();
                    madpater.setdata(list);
                }


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

                dismissLoadingDialog();

            }
        }, "", lifecycleSubject, false, true);

    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,ChoiceGoodsLocationActivity.class);
        mContext.startActivity(mIntent);
    }

    public static void startActivitForResult(Activity activity, int requestCode) {
        Intent mIntent = new Intent(activity,ChoiceGoodsLocationActivity.class);
        activity.startActivityForResult(mIntent, requestCode);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void jumpTeachers(UpdateAddress event) {
        getData();
    }
}
