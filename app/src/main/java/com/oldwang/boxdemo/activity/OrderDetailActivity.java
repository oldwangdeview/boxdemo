package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.OrderDetail_Adpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.AddressData;
import com.oldwang.boxdemo.bean.CommodityData;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.DataInfo2;
import com.oldwang.boxdemo.bean.LogisticsData;
import com.oldwang.boxdemo.bean.OrderInfo;
import com.oldwang.boxdemo.bean.OrderInfoDetail;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.SignInfo;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateOrderListEvent;
import com.oldwang.boxdemo.event.WeChartPayEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.TimeTools;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.util.WXPayUtils;
import com.oldwang.boxdemo.view.MyGridView;
import com.oldwang.boxdemo.view.PayForBottomDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.mygridview)
    MyGridView mygridview;

    @BindView(R.id.iv_state)
    ImageView iv_state;

    @BindView(R.id.iv_address)
    ImageView iv_address;


    @BindView(R.id.tv_state_right)
    TextView tv_state_right;

    @BindView(R.id.iv_arrow)
    ImageView iv_arrow;

    @BindView(R.id.tv_address_user)
    TextView tv_address_user;

    @BindView(R.id.tv_order_state)
    TextView tv_order_state;

    @BindView(R.id.tv_address_detail)
    TextView tv_address_detail;

    @BindView(R.id.tv_goods_type)
    TextView tv_goods_type;

    @BindView(R.id.ll_yunfei)
    LinearLayout ll_yunfei;

    @BindView(R.id.tv_yunfei_price)
    TextView tv_yunfei_price;

    @BindView(R.id.ll_coupon)
    LinearLayout ll_coupon;

    @BindView(R.id.tv_coupon_price)
    TextView tv_coupon_price;

    @BindView(R.id.ll_jifen)
    LinearLayout ll_jifen;


    @BindView(R.id.tv_jifen_price)
    TextView tv_jifen_price;


    @BindView(R.id.tv_real_price)
    TextView tv_real_price;


    @BindView(R.id.tv_order_num)
    TextView tv_order_num;


    @BindView(R.id.ll_create_time)
    LinearLayout ll_create_time;

    @BindView(R.id.tv_createTime)
    TextView tv_createTime;


    @BindView(R.id.ll_pay_time)
    LinearLayout ll_pay_time;

    @BindView(R.id.tv_pay_time)
    TextView tv_pay_time;

    @BindView(R.id.ll_send_time)
    LinearLayout ll_send_time;
    @BindView(R.id.tv_send_time)
    TextView tv_send_time;


    @BindView(R.id.ll_finish_time)
    LinearLayout ll_finish_time;
    @BindView(R.id.tv_finish_time)
    TextView tv_finish_time;


    @BindView(R.id.ll_address_bottom)
    LinearLayout ll_address_bottom;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.tv_user_phone)
    TextView tv_user_phone;

    @BindView(R.id.ll_address_detail_bottom)
    LinearLayout ll_address_detail_bottom;
    @BindView(R.id.tv_address_detail_bottom)
    TextView tv_address_detail_bottom;

    @BindView(R.id.ll_contact_merchant)
    LinearLayout ll_contact_merchant;


    @BindView(R.id.ll_merchant_phone)
    LinearLayout ll_merchant_phone;


    @BindView(R.id.orderdetail_btn_addordernum)
    TextView orderdetail_btn_addordernum;

    @BindView(R.id.orderdetail_btn_looklog)
    TextView orderdetail_btn_looklog;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;


    @BindView(R.id.tv_goods_state)
    TextView tv_goods_state;


    private CountDownTimer countDownTimer;


    private OrderDetail_Adpater orderadpater;
    private String orderNo;
    private String serviceNo;
    private OrderInfoDetail orderDetail;
    //0待付款1待发货2待收货3待评价4退款、售后5已完成
    private int type;
    private PayForBottomDialog payForBottomDialog;

    private boolean isZhifuBao = true;
    private ImageView oneImageView;
    private ImageView twoImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        UIUtils.showFullScreen(OrderDetailActivity.this, true);
        updateactionbar();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_orderdetail);
    }




    @Override
    protected void initData() {
        super.initData();
        orderNo = getIntent().getStringExtra(Contans.INTENT_DATA);
        type = getIntent().getIntExtra(Contans.INTENT_TYPE, 0);
        serviceNo = getIntent().getStringExtra(Contans.INTENT_TYPE_TWO);
        titlename.setText("订单详情");


        if (type == 0) {
            PayForBottomDialog.Builder builder = new PayForBottomDialog.Builder(mContext);



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
        orderDetail();

    }


    /**
     * 订单详情
     */
    private void orderDetail() {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("orderNo", orderNo);
        if (type == 4){
            jsonObject.addProperty("serviceNo", serviceNo);
        }


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().orderDetail(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo2>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo2> stringStatusCode) {

                if (stringStatusCode != null) {
                    DataInfo2 data = stringStatusCode.getData();
                    if (data != null) {
                        orderDetail = data.getOrderInfo();
                        if (type == 2 || type == 3)
                        logisticsInfo();
                        else
                            dismissLoadingDialog();
                    }
                }
                setData();
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }

    private List<LogisticsData> logisticsData;

    private DataInfo1 data1;
    /**
     * 物流信息
     */
    private void logisticsInfo() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courierNo", orderDetail.getCourierNo());
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("orderNo", orderNo);


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
                     data1 = stringStatusCode.getData();
                    OrderInfo orderInfo = data1.getOrderInfo();
                    if (orderInfo != null){
                        orderInfo.setCourierNo(orderDetail.getCourierNo());
                        if (data1 != null) {
                            logisticsData = data1.getLogisticsData();

                            if (logisticsData != null && logisticsData.size() > 0){
                                tv_address_user.setText(logisticsData.get(0).getLogisticsNote());
                                tv_address_detail.setText(logisticsData.get(0).getLogisticsTime());
                            }else {
                                tv_address_user.setText("暂未获取到物流信息");
                                tv_address_detail.setVisibility(View.GONE);
                            }


                        }
                    }

                }
                dismissLoadingDialog();
                setData();
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }

    /***
     * 设置订单数据
     */
    private void setData() {

        if (orderDetail != null) {
            AddressData address = orderDetail.getAddress();
            boolean isPinTuan = false;

            if (!TextUtils.isEmpty(orderDetail.getOrderType()) && orderDetail.getOrderType().equals("2")) {
                tv_goods_type.setText("拼团商品");
                isPinTuan = true;
            } else {
                tv_goods_type.setText("普通商品");
                isPinTuan = false;
            }

            if (!TextUtils.isEmpty(orderDetail.getOrderPriceRealPay())) {
                tv_real_price.setText(orderDetail.getOrderPriceRealPay());
            }

            final List<CommodityData> commoditys = orderDetail.getCommoditys();

            if (commoditys != null) {
                orderadpater = new OrderDetail_Adpater(mContext, commoditys);
                orderadpater.setState(type == 3 ? true : false, isPinTuan);
                final boolean finalIsPinTuan = isPinTuan;
                orderadpater.setMlister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {
                        switch (v.getId()) {
                            //申请售后
                            case R.id.btn_one:
                                AfterSaleActivity.startactivity(mContext,commoditys.get(position),orderNo);
                                break;
                            //评价
                            case R.id.btn_two:
                                OrderEvaluateActivity.startactivity(mContext,commoditys.get(position).getCommodityId(),orderNo,commoditys.get(position).getCommodityPicUrl());
                                break;
                            default:
                                GoodsDetailActivity.startactivity(mContext, finalIsPinTuan, commoditys.get(position).getCommodityId(), null);
                                break;
                        }
                    }
                });

                mygridview.setAdapter(orderadpater);
            }

            if (TextUtils.isEmpty(orderDetail.getOrderPriceFreight())) {
                ll_yunfei.setVisibility(View.VISIBLE);
                tv_yunfei_price.setText("¥ 0.0");

            } else {
                ll_yunfei.setVisibility(View.VISIBLE);
                tv_yunfei_price.setText("¥ " + orderDetail.getOrderPriceFreight());
            }

            if (TextUtils.isEmpty(orderDetail.getCouponPrice())) {
                ll_coupon.setVisibility(View.VISIBLE);
                tv_coupon_price.setText("- ¥ 0.0");
            } else {
                ll_coupon.setVisibility(View.VISIBLE);
                tv_coupon_price.setText("- ¥ " + orderDetail.getCouponPrice());
            }

            if (TextUtils.isEmpty(orderDetail.getScorePrice())) {
                ll_jifen.setVisibility(View.GONE);
            } else {
                ll_jifen.setVisibility(View.VISIBLE);
                tv_jifen_price.setText("- ¥ " + orderDetail.getScorePrice());
            }


            tv_order_num.setText(orderNo);

            if (TextUtils.isEmpty(orderDetail.getOrderTime())) {
                ll_create_time.setVisibility(View.GONE);
            } else {
                ll_create_time.setVisibility(View.VISIBLE);
                if (orderDetail.getOrderTime().contains("-")) {
                    tv_createTime.setText(orderDetail.getOrderTime());
                } else {
                    tv_createTime.setText(DateTools.getFormat(Long.valueOf(orderDetail.getOrderTime()), "yyyy-MM-dd HH:mm:ss"));
                }
            }

            if (TextUtils.isEmpty(orderDetail.getPayTime())) {
                ll_pay_time.setVisibility(View.GONE);
            } else {
                ll_pay_time.setVisibility(View.VISIBLE);
                if (orderDetail.getPayTime().contains("-")) {
                    tv_pay_time.setText(orderDetail.getPayTime());
                } else {
                    tv_pay_time.setText(DateTools.getFormat(Long.valueOf(orderDetail.getPayTime()), "yyyy-MM-dd HH:mm:ss"));
                }

            }
            if (TextUtils.isEmpty(orderDetail.getDeliverSendTime())) {
                ll_send_time.setVisibility(View.GONE);
            } else {
                ll_send_time.setVisibility(View.VISIBLE);

                if (orderDetail.getDeliverSendTime().contains("-")) {
                    tv_send_time.setText(orderDetail.getDeliverSendTime());
                } else {
                    tv_send_time.setText(DateTools.getFormat(Long.valueOf(orderDetail.getDeliverSendTime()), "yyyy-MM-dd HH:mm:ss"));
                }

            }

            if (TextUtils.isEmpty(orderDetail.getDeliverReceiverTime())) {
                ll_finish_time.setVisibility(View.GONE);
            } else {
                ll_finish_time.setVisibility(View.VISIBLE);

                if (orderDetail.getDeliverReceiverTime().contains("-")) {
                    tv_finish_time.setText(orderDetail.getDeliverReceiverTime());
                } else {
                    tv_finish_time.setText(DateTools.getFormat(Long.valueOf(orderDetail.getDeliverReceiverTime()), "yyyy-MM-dd HH:mm:ss"));
                }
            }

            ll_address_bottom.setVisibility(View.GONE);
            ll_address_detail_bottom.setVisibility(View.GONE);

            // 0待付款1待发货2待收货3待评价4退款、售后5已完成 -1待分享
            switch (type) {
                case -1:
                    iv_state.setImageDrawable(getResources().getDrawable(R.mipmap.wait_icon));
                    tv_order_state.setText("待分享");
                    tv_state_right.setText("拼团倒计时");
                    tv_goods_state.setText("待分享");

                    String teamEndTime = orderDetail.getTeamEndTime();
                    if (!TextUtils.isEmpty(teamEndTime)) {

                        teamEndTime = new BigDecimal(teamEndTime).setScale(0,BigDecimal.ROUND_HALF_UP).toString();

                        countDownTimer = new CountDownTimer(Long.valueOf(teamEndTime), 1000) {
                            public void onTick(long millisUntilFinished) {
                                tv_state_right.setText("拼团倒计时 " + TimeTools.getCountTimeByLong(millisUntilFinished));
                            }

                            public void onFinish() {
                                tv_state_right.setText("拼团倒计时 00:00:00");
                            }
                        }.start();

                    } else {
                        tv_state_right.setText("拼团倒计时 00:00:00");
                    }

                    iv_address.setVisibility(View.VISIBLE);

                    if (address != null) {

                        String userNamePhone = "";
                        if (!TextUtils.isEmpty(address.getReceiverMember())) {
                            userNamePhone = address.getReceiverMember() + "   ";
                        }

                        if (!TextUtils.isEmpty(address.getPhone())) {
                            userNamePhone += address.getPhone();
                        }
                        tv_address_user.setText(userNamePhone);

                        if (!TextUtils.isEmpty(address.getReceivingAddress())) {
                            tv_address_detail.setText(address.getReceivingAddress());
                        }

                    }
                    orderdetail_btn_addordernum.setVisibility(View.GONE);
                    orderdetail_btn_looklog.setVisibility(View.GONE);
//                    orderdetail_btn_looklog.setText("取消订单");
                    tv_confirm.setText("去分享");

                    break;
                case 0:
                    iv_state.setImageDrawable(getResources().getDrawable(R.mipmap.wait_pay_icon));
                    tv_order_state.setText("待付款");
                    tv_state_right.setText("订单关闭倒计时");
                    tv_goods_state.setText("待付款");

                    String surplusCloseTime = orderDetail.getSurplusCloseTime();
                    if (!TextUtils.isEmpty(surplusCloseTime)) {

                        surplusCloseTime = new BigDecimal(surplusCloseTime).setScale(0,BigDecimal.ROUND_HALF_UP).toString();

                        countDownTimer = new CountDownTimer(Long.valueOf(surplusCloseTime), 1000) {
                            public void onTick(long millisUntilFinished) {
                                tv_state_right.setText("订单关闭倒计时 " + TimeTools.getCountTimeByLong(millisUntilFinished));
                            }

                            public void onFinish() {
                                tv_state_right.setText("订单关闭倒计时 00:00:00");
                            }
                        }.start();

                    } else {
                        tv_state_right.setText("订单关闭倒计时 00:00:00");
                    }

                    iv_address.setVisibility(View.VISIBLE);

                    if (address != null) {

                        String userNamePhone = "";
                        if (!TextUtils.isEmpty(address.getReceiverMember())) {
                            userNamePhone = address.getReceiverMember() + "   ";
                        }

                        if (!TextUtils.isEmpty(address.getPhone())) {
                            userNamePhone += address.getPhone();
                        }
                        tv_address_user.setText(userNamePhone);

                        if (!TextUtils.isEmpty(address.getReceivingAddress())) {
                            tv_address_detail.setText(address.getReceivingAddress());
                        }

                    }
                    orderdetail_btn_addordernum.setVisibility(View.GONE);
                    orderdetail_btn_looklog.setText("取消订单");
                    tv_confirm.setText("去付款");

                    break;
                case 1:
                    iv_state.setImageDrawable(getResources().getDrawable(R.mipmap.wait_pay_icon));
                    tv_order_state.setText("待发货");
                    tv_state_right.setVisibility(View.GONE);
                    tv_goods_state.setText("待发货");

                    iv_address.setVisibility(View.VISIBLE);

                    if (address != null) {

                        String userNamePhone = "";
                        if (!TextUtils.isEmpty(address.getReceiverMember())) {
                            userNamePhone = address.getReceiverMember() + "   ";
                        }

                        if (!TextUtils.isEmpty(address.getPhone())) {
                            userNamePhone += address.getPhone();
                        }
                        tv_address_user.setText(userNamePhone);

                        if (!TextUtils.isEmpty(address.getReceivingAddress())) {
                            tv_address_detail.setText(address.getReceivingAddress());
                        }

                    }
                    orderdetail_btn_addordernum.setVisibility(View.GONE);
                    orderdetail_btn_looklog.setVisibility(View.GONE);
                    tv_confirm.setVisibility(View.GONE);

                    break;
                case 2:
                    iv_address.setImageDrawable(getResources().getDrawable(R.mipmap.logistics_icon));
                    iv_state.setImageDrawable(getResources().getDrawable(R.mipmap.wait_icon));
                    tv_order_state.setText("待收货");
                    tv_state_right.setVisibility(View.GONE);
                    tv_goods_state.setText("待收货");
                    tv_address_user.setTextColor(Color.parseColor("#FFFDA100"));

                    ll_address_bottom.setVisibility(View.VISIBLE);
                    ll_address_detail_bottom.setVisibility(View.VISIBLE);

                    iv_address.setVisibility(View.VISIBLE);
                    //查看物流信息
                    iv_arrow.setVisibility(View.VISIBLE);
                    if (address != null) {

                        String userNamePhone = "";
                        if (!TextUtils.isEmpty(address.getReceiverMember())) {
                            userNamePhone = address.getReceiverMember() + "   ";
                            tv_user_name.setText(address.getReceiverMember());
                        }

                        if (!TextUtils.isEmpty(address.getPhone())) {
                            userNamePhone += address.getPhone();
                            tv_user_phone.setText(address.getPhone());

                        }
//                        tv_address_user.setText(userNamePhone);

                        if (!TextUtils.isEmpty(address.getReceivingAddress())) {
//                            tv_address_detail.setText(address.getReceivingAddress());
                            tv_address_detail_bottom.setText(address.getReceivingAddress());

                        }

                    }
                    orderdetail_btn_addordernum.setVisibility(View.GONE);
                    orderdetail_btn_looklog.setVisibility(View.VISIBLE);
                    tv_confirm.setVisibility(View.VISIBLE);
                    orderdetail_btn_looklog.setText("查看物流");
                    tv_confirm.setText("确认收货");
                    break;

                case 3:
                    iv_address.setImageDrawable(getResources().getDrawable(R.mipmap.logistics_icon));
                    iv_state.setImageDrawable(getResources().getDrawable(R.mipmap.order_finish_icon));
                    tv_order_state.setText("交易成功");
                    tv_state_right.setVisibility(View.GONE);
                    tv_goods_state.setText("待评价");
                    tv_address_user.setTextColor(Color.parseColor("#FFFDA100"));

                    ll_address_bottom.setVisibility(View.VISIBLE);
                    ll_address_detail_bottom.setVisibility(View.VISIBLE);

                    iv_address.setVisibility(View.VISIBLE);
                    //查看物流信息
                    iv_arrow.setVisibility(View.VISIBLE);
                    if (address != null) {

                        String userNamePhone = "";
                        if (!TextUtils.isEmpty(address.getReceiverMember())) {
                            userNamePhone = address.getReceiverMember() + "   ";
                            tv_user_name.setText(address.getReceiverMember());
                        }

                        if (!TextUtils.isEmpty(address.getPhone())) {
                            userNamePhone += address.getPhone();
                            tv_user_phone.setText(address.getPhone());

                        }
//                        tv_address_user.setText(userNamePhone);

                        if (!TextUtils.isEmpty(address.getReceivingAddress())) {
//                            tv_address_detail.setText(address.getReceivingAddress());
                            tv_address_detail_bottom.setText(address.getReceivingAddress());

                        }

                    }
                    orderdetail_btn_addordernum.setVisibility(View.GONE);
                    orderdetail_btn_looklog.setVisibility(View.GONE);
                    tv_confirm.setVisibility(View.GONE);
                    break;

                case 4:
                    tv_order_state.setText("申请售后");

                    orderdetail_btn_addordernum.setVisibility(View.GONE);


                    //	1平台已审核，2客户已回寄，3平台已确认收货，4平台已退款，5平台已回寄
                    if (!TextUtils.isEmpty(orderDetail.getServiceStatus())){
                        tv_state_right.setVisibility(View.VISIBLE);

                        if (orderDetail.getServiceStatus().equals("1")){
                            if (!(!TextUtils.isEmpty(orderDetail.getServiceType()) && orderDetail.getServiceType().equals("3"))){
                                orderdetail_btn_addordernum.setVisibility(View.VISIBLE);
                            }
                            tv_state_right.setText("平台已审核");
                        }else if (orderDetail.getServiceStatus().equals("2")){
                            tv_state_right.setText("客户已回寄");
                        }else if (orderDetail.getServiceStatus().equals("3")){
                            tv_state_right.setText("平台已确认收货");
                        }else if (orderDetail.getServiceStatus().equals("4")){
                            tv_state_right.setText("平台已退款");
                        }else if (orderDetail.getServiceStatus().equals("5")){
                            tv_state_right.setText("平台已回寄");
                        }else {
                            tv_state_right.setText("平台正在处理中");
                        }
                    }else {
                        tv_state_right.setText("");
                    }



                    if (!TextUtils.isEmpty(orderDetail.getReason())){
                        tv_address_detail.setText(orderDetail.getReason());
                    }else {
                        tv_address_detail.setText("");
                    }


                    if (!TextUtils.isEmpty(orderDetail.getServiceType())){

                        //(01退货02换货03退款)
                        if (orderDetail.getServiceType().equals("1")){
                            tv_address_user.setText("售后类型: "+"退货");
                        }
                        if (orderDetail.getServiceType().equals("2")){
                            tv_address_user.setText("售后类型: "+"换货");
                        }

                        if (orderDetail.getServiceType().equals("3")){
                            tv_address_user.setText("售后类型: "+"退款并退货");
                        }
                    }

                    if (!TextUtils.isEmpty(orderDetail.getServiceTypeDes())) {
                        tv_user_phone.setText(orderDetail.getServiceTypeDes());
                    }


                    tv_goods_state.setVisibility(View.GONE);

                    iv_address.setVisibility(View.GONE);

                    ll_address_bottom.setVisibility(View.VISIBLE);
                    ll_address_detail_bottom.setVisibility(View.VISIBLE);




                    iv_arrow.setVisibility(View.VISIBLE);
                    if (address != null) {
                        if (!TextUtils.isEmpty(address.getReceiverMember())) {
                            tv_user_name.setText(address.getReceiverMember());
                        }

                        if (!TextUtils.isEmpty(address.getPhone())) {
                            tv_user_phone.setText(address.getPhone());

                        }

                        if (!TextUtils.isEmpty(address.getReceivingAddress())) {
                            tv_address_detail_bottom.setText(address.getReceivingAddress());
                        }

                    }

                    orderdetail_btn_looklog.setVisibility(View.GONE);
                    tv_confirm.setVisibility(View.GONE);
                    break;
                case 5:
                    iv_state.setImageDrawable(getResources().getDrawable(R.mipmap.order_finish_icon));
                    tv_order_state.setText("已完成");
                    tv_state_right.setVisibility(View.GONE);
                    tv_goods_state.setText("已完成");

                    iv_address.setVisibility(View.VISIBLE);

                    if (address != null) {

                        String userNamePhone = "";
                        if (!TextUtils.isEmpty(address.getReceiverMember())) {
                            userNamePhone = address.getReceiverMember() + "   ";
                        }

                        if (!TextUtils.isEmpty(address.getPhone())) {
                            userNamePhone += address.getPhone();
                        }
                        tv_address_user.setText(userNamePhone);

                        if (!TextUtils.isEmpty(address.getReceivingAddress())) {
                            tv_address_detail.setText(address.getReceivingAddress());
                        }

                    }
                    orderdetail_btn_addordernum.setVisibility(View.GONE);
                    orderdetail_btn_looklog.setVisibility(View.GONE);
                    tv_confirm.setVisibility(View.GONE);

                    break;
            }
        }

    }


    @OnClick({R.id.orderdetail_btn_addordernum, R.id.tv_confirm, R.id.orderdetail_btn_looklog, R.id.ll_contact_merchant, R.id.ll_merchant_phone, R.id.rl_wuliu})
    public void onClick(View view) {
        switch (view.getId()) {
            //联系商家
            case R.id.ll_contact_merchant:
                break;
            //拨打电话
            case R.id.ll_merchant_phone:
                if (!TextUtils.isEmpty(orderDetail.getCallPhone())){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + orderDetail.getCallPhone());
                    intent.setData(data);
                    startActivity(intent);
                }
                break;
            //底部第一个按钮
            case R.id.orderdetail_btn_addordernum:
                if (orderDetail != null){
                    //填写单号
                    AddOrderNumActivity.startactivity(this,orderNo,serviceNo, orderDetail);
                }

                break;
            //底部第二个按钮
            case R.id.orderdetail_btn_looklog:
                switch (type) {
                    case 0:
                        AlertView alertView = new AlertView("提示", "确定取消订单吗？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                if (position == 1) {
                                    //调用接口
                                    orderCancel(orderDetail.getOrderNo(), orderDetail.getOrderStatus());
                                }

                            }
                        });
                        alertView.show();
                        break;
                    case 2:
                        //查看物流
                        LogisticsInformationActivity.startactivity(mContext,data1);
                        break;
                }

                break;
            //底部第三个按钮
            case R.id.tv_confirm:
                switch (type) {
                    case -1:
                        List<CommodityData> commoditys = orderDetail.getCommoditys();

                        if (commoditys != null && commoditys.size() > 0){
                            CommodityData commodityInfo = commoditys.get(0);
                            String imageUrl = commodityInfo.getCommodityPicUrl();
                            share(3,commodityInfo.getCommodityName(),commodityInfo.getCommodityId(),imageUrl);
                        }


                        break;
                    case 0:
                        orderInfo = new OrderInfo();
                        orderInfo.setOrderNo(orderDetail.getOrderNo());
                        orderInfo.setOrderPayPrice(orderDetail.getOrderPriceRealPay());
                        payForBottomDialog.show();
                        break;
                    case 2:
                        AlertView alertView = new AlertView("提示", "确定确认收货吗？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                if (position == 1) {
                                    //调用接口
                                    confirmTake(orderDetail.getOrderNo(), orderDetail.getOrderStatus());

                                }

                            }
                        });
                        alertView.show();
                        break;
                    case 3:
//                        OrderEvaluateActivity.startactivity(mContext);
                        break;
                }
                break;
            case R.id.rl_wuliu:
                if (type == 2 || type == 3){
                    //查看物流
                    if (data1 != null){
                        LogisticsInformationActivity.startactivity(mContext,data1);
                    }
                }

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

                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                dismissLoadingDialog();
                finish();
                EventBus.getDefault().post(new UpdateOrderListEvent());

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
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

                                dismissLoadingDialog();

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                ToastUtils.makeText("收货成功");
                finish();
                dismissLoadingDialog();
                orderDetail();
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
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
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                dismissLoadingDialog();
                finish();
                EventBus.getDefault().post(new UpdateOrderListEvent());
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }

    private OrderInfo orderInfo;

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
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                dismissLoadingDialog();


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
                            .build().toWXPayNotSign(OrderDetailActivity.this);
                    payForBottomDialog.dismiss();

                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                dismissLoadingDialog();

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
                                showLoadingDialog();

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                dismissLoadingDialog();


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
                dismissLoadingDialog();

            }
        }, "", lifecycleSubject, false, true);
    }

    private void payForAli(final String orderinfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderDetailActivity.this);
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
                            PaySuccessActivity.startactivity(mContext, orderInfo);
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


    /**
     * 微信支付结果
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void weixinPayResult(WeChartPayEvent event) {

        int errCode = event.getErrCode();
        if (errCode == 0) {
            PaySuccessActivity.startactivity(mContext, orderInfo);
            EventBus.getDefault().post(new UpdateOrderListEvent());
        } else if (errCode == -2) {
            //用户取消
            ToastUtils.makeText("取消支付");
        } else if (errCode == -1) {
            //支付失败
            ToastUtils.makeText("支付失败");
        }
    }


    public static void startactivity(Context mContext, String orderNo, int type,String serviceNo) {
        Intent mIntent = new Intent(mContext, OrderDetailActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA, orderNo);
        mIntent.putExtra(Contans.INTENT_TYPE, type);
        mIntent.putExtra(Contans.INTENT_TYPE_TWO, serviceNo);


        mContext.startActivity(mIntent);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (payForBottomDialog != null) {
            if (payForBottomDialog.isShowing()) {
                payForBottomDialog.dismiss();
            }
            payForBottomDialog = null;
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateList(UpdateOrderListEvent event) {
        orderDetail();
    }
}
