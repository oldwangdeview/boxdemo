package com.oldwang.boxdemo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.CunponAdpater;
import com.oldwang.boxdemo.adpater.CunponChooseAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.CouponData;
import com.oldwang.boxdemo.bean.CourierData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.GoodsOrderData;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.OrderDetail;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class OrderChooseCoponActivity extends BaseActivity {


    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.tv_hint)
    TextView tv_hint;


    private String id =  "";

    @BindView(R.id.recyclerview)
    YRecycleview recyclerview;
    CunponChooseAdpater madpater;
    private List<String> mlistdata = new ArrayList<>();



    private int page = 1 ;
    private final int size = 20;
    private int total;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况

    private ArrayList<CouponData> datas = new ArrayList<>();
    private Dialog mLoadingDialog;

    private GoodsOrderData data;

    @Override
    protected void initView() {
        setContentView(R.layout.actiivity_choose_coupon);
    }


    @Override
    protected void initData() {
        super.initData();
        id = getIntent().getStringExtra(Contans.INTENT_TYPE);
        data = (GoodsOrderData) getIntent().getSerializableExtra(Contans.INTENT_DATA);
        titlename.setText("选择优惠券");
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }


    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,OrderChooseCoponActivity.class);
        mContext.startActivity(mIntent);
    }

    public static void startActivitForResult(Activity activity, String id, GoodsOrderData data, int requestCode) {
        Intent mIntent = new Intent(activity,OrderChooseCoponActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,data);
        mIntent.putExtra(Contans.INTENT_TYPE,id);
        activity.startActivityForResult(mIntent, requestCode);
    }


    @Override
    protected void initEvent() {
        super.initEvent();
        recyclerview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }

            @Override
            public void onLoadMore() {
                int totalPaeg = total / size + (total % size == 0 ? 0:1);
                if (page < totalPaeg) {
                    loadMoreData();
                } else {
                    recyclerview.setloadMoreComplete();
                }
            }
        });
        getData();
    }

    /**
     * 获取订单可用优惠券
     */
    private void getData() {


        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        for (OrderDetail orderDetail : data.getOrderDetail()) {
            JsonObject commoditys = new JsonObject();
            commoditys.addProperty("commodityId", orderDetail.getCommodityId());
            commoditys.addProperty("commodityNum", orderDetail.getCommodityNum());
            if (!TextUtils.isEmpty(orderDetail.getCommodityPrice())){
                commoditys.addProperty("commodityPrice", orderDetail.getCommodityPrice());
            }else {
                commoditys.addProperty("commodityPrice", orderDetail.getTeamBuyPrice());
            }
            jsonArray.add(commoditys);
        }

        jsonObject.add("commoditys",jsonArray);

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().getCouponList(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<ListData<CouponData>>(mContext) {
            @Override
            protected void _onNext(StatusCode<ListData<CouponData>> stringStatusCode) {

                if (state != STATE_MORE){
                    datas.clear();
                }

                if (stringStatusCode != null) {
                    ListData<CouponData> data = stringStatusCode.getData();
                    if (data != null && data.getList() != null){
                        datas.addAll(data.getList());
                        total = data.getTotal();

                    }
                }
                showData();
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                if (state != STATE_MORE){
                    datas.clear();
                }
                showData();
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);

    }


    /**
     * 加载更多
     */
    private void loadMoreData() {
        state = STATE_MORE;
        page = ++page;
        getData();
    }

    /**
     * 刷新
     */
    private void refreshData() {
        state = STATE_REFREH;
        page = 1;
        getData();
    }

    /**
     * 展示数据
     */
    private void showData() {
        switch (state) {
            case STATE_NORMAL:

                if (datas.size() < 1){
                    tv_hint.setVisibility(View.VISIBLE);
                    recyclerview.setVisibility(View.GONE);
                }else {
                    tv_hint.setVisibility(View.GONE);
                    recyclerview.setVisibility(View.VISIBLE);
                }
                madpater = new CunponChooseAdpater(mContext,datas,0);
                madpater.setId(id);
                recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.setAdapter(madpater);
                madpater.setListClickLister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {
                        CouponData couponData = datas.get(position);
                        if (couponData != null){
                            Intent intent=getIntent();
                            intent.putExtra("couponData", couponData);
                            setResult(RESULT_OK, intent);
                            finish();
                        }else {
                            ToastUtils.makeText("请选择优惠券");
                        }
                    }
                });
                break;
            case STATE_REFREH:
                if (datas.size() < 1){
                    tv_hint.setVisibility(View.VISIBLE);
                    recyclerview.setVisibility(View.GONE);
                }else {
                    tv_hint.setVisibility(View.GONE);
                    recyclerview.setVisibility(View.VISIBLE);
                }
                madpater.notifyDataSetChanged();
                recyclerview.scrollToPosition(0);
                recyclerview.setReFreshComplete();
                break;
            case STATE_MORE:
                madpater.notifyDataSetChanged();
                recyclerview.setloadMoreComplete();
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()){
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
        }
    }

}
