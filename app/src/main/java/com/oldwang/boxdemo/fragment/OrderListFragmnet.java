package com.oldwang.boxdemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alipay.sdk.app.PayTask;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.LogisticsInformationActivity;
import com.oldwang.boxdemo.activity.MyOrderListActivity;
import com.oldwang.boxdemo.activity.OrderDetailActivity;
import com.oldwang.boxdemo.activity.PaySuccessActivity;
import com.oldwang.boxdemo.adpater.OrderListAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.CommodityData;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.MyOrderData;
import com.oldwang.boxdemo.bean.OrderInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.SignInfo;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.event.JumpTeachers;
import com.oldwang.boxdemo.event.MainJumpEvent;
import com.oldwang.boxdemo.event.UpdateOrderListEvent;
import com.oldwang.boxdemo.event.WeChartPayEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.OrderAdpaterClickGoodsLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.util.WXPayUtils;
import com.oldwang.boxdemo.view.OrderNoMessageView;
import com.oldwang.boxdemo.view.PayForBottomDialog;
import com.oldwang.boxdemo.view.YRecycleview;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class OrderListFragmnet extends BaseFragment {

    @BindView(R.id.recyclerview)
    YRecycleview recyclerview;

//    //没有数据时显示view
//    @BindView(R.id.nomessage_view)
//    LinearLayout nomessage_view;
    private OrderListAdpater madpater;
    private String tag = "";


    private int page = 1;
    private final int size = 20;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;       //正常情况

    private ArrayList<MyOrderData> datas = new ArrayList<>();
    private Dialog mLoadingDialog;
    private PayForBottomDialog payForBottomDialog;
    private OrderInfo orderInfo;
    private boolean isZhifuBao = true;

    private OrderNoMessageView noMessageview;

    @Override
    public View initView(Context context) {
        if (getArguments() != null) {
            tag = getArguments().getString("position");
        }
        return UIUtils.inflate(mContext, R.layout.fragmnet_orderlist);
    }

    private ImageView oneImageView;
    private ImageView twoImageView;
//
//    @OnClick(R.id.btn_go)
//    public void goGoods(){
//        ((MyOrderListActivity)mContext).finish();
//        EventBus.getDefault().post(new MainJumpEvent(1));
//    }
    @Override
    protected void initData() {

        if (tag.equals("0")){
            PayForBottomDialog.Builder builder = new PayForBottomDialog.Builder(mContext);
            noMessageview = new OrderNoMessageView(mContext);


            payForBottomDialog =  builder.setClicklister(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        //支付宝支付
                        case R.id.layout_ali:
                            isZhifuBao = true;
                            oneImageView.setVisibility(View.VISIBLE);
                            twoImageView.setVisibility(View.GONE);
                            break;
                        //微信支付
                        case R.id.layout_wechart:
                            isZhifuBao = false;
                            oneImageView.setVisibility(View.GONE);
                            twoImageView.setVisibility(View.VISIBLE);
                            break;
                        case R.id.ojbk_btn:
                            if (isZhifuBao){
                                alipay();
                            }else {
                                weixin();
                            }
                            break;

                    }
                }
            }).create();
            oneImageView = builder.getOneImageView();
            twoImageView = builder.getTwoImageView();
        }
        super.initData();
    }

    protected void initEvent() {
        super.initEvent();
        recyclerview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }

            @Override
            public void onLoadMore() {
                int totalPaeg = total / size + (total % size == 0 ? 0 : 1);
                if (page < totalPaeg) {
                    loadMoreData();
                } else {
                    recyclerview.setloadMoreComplete();
                }
            }
        });
        getData(tag);
    }

    /**
     */
    private void getData(String position) {

        JsonObject jsonObject = new JsonObject();

        if (position.equals("4")) {
            jsonObject.addProperty("exchangeFlag", 1);
        }

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        int orderStatus = 0;
        if (position.equals("0")) {
            orderStatus = 1;
        } else if (position.equals("1")) {
            orderStatus = 3;
        } else if (position.equals("2")) {
            orderStatus = 4;
        } else if (position.equals("3")) {
            orderStatus = 5;
            jsonObject.addProperty("isEvaluate", 1);
        } else if (position.equals("5")) {
            orderStatus = 6;
        }else if (position.equals("-1")){
            jsonObject.addProperty("isShare", 1);
        }

        if (orderStatus > 0) {
            //orderStatus 订单状态（（01未付款02以付款03待发货04以发货05以收货06完结订单07取消订单））
            jsonObject.addProperty("orderStatus", orderStatus);
        }


        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().myOrderList(requestBean)
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                if (state != STATE_MORE) {
                    datas.clear();
                }
                if (stringStatusCode != null) {
                    DataInfo1 data = stringStatusCode.getData();
                    if (data != null && data.getOrderData() != null && data.getOrderData().getList() != null) {
                        datas.addAll(data.getOrderData().getList());
                        total = data.getOrderData().getTotal();
                    }
                }
                showData();
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                if (state != STATE_MORE) {
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
        getData(tag);
    }

    /**
     * 刷新
     */
    private void refreshData() {
        state = STATE_REFREH;
        page = 1;
        getData(tag);
    }

    /**
     * 展示数据
     */
    private void showData() {
        switch (state) {
            case STATE_NORMAL:


                madpater = new OrderListAdpater(mContext, datas);
                madpater.setPostion(Integer.valueOf(tag));
                recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.setAdapter(madpater);
                if (datas.size() > 0) {
                    recyclerview.setVisibility(View.VISIBLE);
                } else {
                    if(noMessageview==null){
                        noMessageview = new OrderNoMessageView(mContext);
                    }
                    recyclerview.addHeadView(noMessageview);
                }
                madpater.setOrderAdpaterClickGoodsLister(new OrderAdpaterClickGoodsLister() {
                    @Override
                    public void click(int fposition, View view, int position) {

                        final MyOrderData myOrderData = datas.get(fposition);

                        switch (view.getId()) {
                            case R.id.btn_1:
                                //取消订单
                                if (tag.equals("0")) {
                                    AlertView alertView = new AlertView("提示", "确定取消订单吗？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(Object o, int position) {
                                            if (position == 1) {
                                                //调用接口
                                                orderCancel(myOrderData.getOrderNo(), myOrderData.getOrderStatus());
                                            }

                                        }
                                    });
                                    alertView.show();
                                }else if (tag.equals("2")){
                                    AlertView alertView = new AlertView("提示", "确定确认收货吗？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(Object o, int position) {
                                            if (position == 1) {
                                                //调用接口
                                                confirmTake(myOrderData.getOrderNo(), myOrderData.getOrderStatus());
                                            }

                                        }
                                    });
                                    alertView.show();
                                }else if (tag.equals("5")){
                                    AlertView alertView = new AlertView("提示", "确定删除订单吗？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(Object o, int position) {
                                            if (position == 1) {
                                                //调用接口
                                                orderDel(myOrderData.getOrderNo());
                                            }

                                        }
                                    });
                                    alertView.show();
                                }
                                break;
                            case R.id.btn_2:
                                //付款
                                if (tag.equals("0")) {
                                    orderInfo = new OrderInfo();
                                    orderInfo.setOrderNo(myOrderData.getOrderNo());
                                    orderInfo.setOrderPayPrice(myOrderData.getOrderPriceRealPay());
                                    payForBottomDialog.show();
                                }else if (tag.equals("2")){
                                    LogisticsInformationActivity.startactivity(mContext,myOrderData,true);
                                }else if (tag.equals("-1")){
                                    List<CommodityData> commoditys = myOrderData.getOrderDetail();

                                    if (commoditys != null && commoditys.size() > 0){
                                        CommodityData commodityInfo = commoditys.get(0);
                                        String imageUrl = commodityInfo.getCommodityPicUrl();
                                        ((BaseActivity)getActivity()).share(3,commodityInfo.getCommodityName(),commodityInfo.getCommodityId(),imageUrl);
                                    }
                                }
                                break;
                            default: //订单详情
                                OrderDetailActivity.startactivity(mContext,myOrderData.getOrderNo(),Integer.valueOf(tag),myOrderData.getServiceNo());
                                break;
                        }
                    }
                });
                break;
            case STATE_REFREH:
                if (datas.size() > 0) {
                    recyclerview.setLoadingEmptyViewGone();
                } else {
                    recyclerview.addHeadView(noMessageview);
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

    /**
     * 取消订单
     */
    private void orderCancel(String orderNo, String orderStatus) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("orderNo", orderNo);
        jsonObject.addProperty("orderStatus", orderStatus);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().orderCancel(requestBean)
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                EventBus.getDefault().post(new UpdateOrderListEvent());

            }

            @Override
            protected void _onError(String message) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }

    /**
     * 确认收货
     */
    private void confirmTake(String orderNo, String orderStatus) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("orderNo", orderNo);
        jsonObject.addProperty("orderStatus", orderStatus);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().confirmTake(requestBean)
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                EventBus.getDefault().post(new UpdateOrderListEvent());

            }

            @Override
            protected void _onError(String message) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }

    /**
     * 删除订单
     */
    private void orderDel(String orderNo) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("delFlag", 1);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("orderNo", orderNo);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().orderDel(requestBean)
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                EventBus.getDefault().post(new UpdateOrderListEvent());
            }

            @Override
            protected void _onError(String message) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }

    /**
     * 微信支付
     */
    private void weixin() {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("orderNo", orderInfo.getOrderNo());
        jsonObject.addProperty("totalAmount", orderInfo.getOrderPayPrice());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);


        Observable observable =
                ApiUtils.getApi().weixin(requestBean)
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if (stringStatusCode != null) {
                    SignInfo signInfo = stringStatusCode.getData().getSignInfo();

                    WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
                    builder.setAppId(signInfo.getAppid())
                            .setPartnerId(signInfo.getPartnerid())
                            .setPrepayId(signInfo.getPrepayid())
                            .setPackageValue("Sign=WXPay")
                            .setNonceStr(signInfo.getNoncestr())
                            .setTimeStamp(signInfo.getTimestamp())
                            .setSign(signInfo.getSign())
                            .build().toWXPayNotSign(getActivity());
                    payForBottomDialog.dismiss();

                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }


    /**
     * 支付宝支付
     */
    private void alipay() {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("orderNo", orderInfo.getOrderNo());
        jsonObject.addProperty("totalAmount", orderInfo.getOrderPayPrice());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);


        Observable observable =
                ApiUtils.getApi().alipay(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);


                if (stringStatusCode != null) {

                    OrderInfo orderInfo1 = stringStatusCode.getData().getOrderInfo();

                    String sign = orderInfo1.getSign();
                    if (!TextUtils.isEmpty(sign)) {
                        payForAli(sign);
                        payForBottomDialog.dismiss();
                    }
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }

    private void payForAli(final String orderinfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                Map<String, String> result = alipay.payV2(orderinfo, true);
                Message msg = new Message();
                msg.what = 101;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Map<String, String> result = ((Map<String, String>) msg.obj);
            String requestcode = result.get("resultStatus");
            if (!TextUtils.isEmpty(requestcode)) {
                try {

                    switch (Integer.parseInt(requestcode)) {
                        case 9000:
                            PaySuccessActivity.startactivity(mContext,orderInfo);
                            EventBus.getDefault().post(new UpdateOrderListEvent());
//                            startActivity(new Intent(GoodsOrderActivity.this,GoldPaySuccessActivity.class).putExtra("data",data));
                            break;
                        default:
                            ToastUtils.makeText("支付宝支付失败");
                            break;
                    }

                } catch (Exception e) {

                }
            }
        }

        ;
    };

    long time;

    /**
     * 微信支付结果
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void weixinPayResult(WeChartPayEvent event) {


        if (System.currentTimeMillis() - time < 1000){
            return;
        }

            time = System.currentTimeMillis();

        int errCode = event.getErrCode();
        if (errCode == 0) {
            PaySuccessActivity.startactivity(mContext,orderInfo);
            EventBus.getDefault().post(new UpdateOrderListEvent());
        } else if (errCode == -2) {
            //用户取消
            ToastUtils.makeText("取消支付");
        } else if (errCode == -1) {
            //支付失败
            ToastUtils.makeText("支付失败");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateList(UpdateOrderListEvent event) {
        refreshData();
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
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
        }

        if (payForBottomDialog != null) {
            if (payForBottomDialog.isShowing()) {
                payForBottomDialog.dismiss();
            }
            payForBottomDialog = null;
        }
    }
}
