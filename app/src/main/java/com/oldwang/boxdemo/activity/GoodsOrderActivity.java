package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.GoodsOrderAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.AddressData;
import com.oldwang.boxdemo.bean.CouponData;
import com.oldwang.boxdemo.bean.CourierData;
import com.oldwang.boxdemo.bean.CourierInfo;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.GoodsOrderData;
import com.oldwang.boxdemo.bean.OrderDetail;
import com.oldwang.boxdemo.bean.OrderInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ScoresInfo;
import com.oldwang.boxdemo.bean.SignInfo;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.MainJumpEvent;
import com.oldwang.boxdemo.event.WeChartPayEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.WXPayUtils;
import com.oldwang.boxdemo.view.MyDialog;
import com.oldwang.boxdemo.view.PayForBottomDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 商品订单
 */
public class GoodsOrderActivity extends BaseActivity {

    private final static int GET_LOCATION = 0x0021;
    private final static int GET_EXPRESS = 0x0022;
    private final static int GET_COUPON = 0x0023;

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;


    @BindView(R.id.ll_choose_address)
    LinearLayout ll_choose_address;

    @BindView(R.id.layout_location)
    LinearLayout layout_location;

    @BindView(R.id.tv_name_phone)
    TextView tv_name_phone;

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.tv_order_num)
    TextView tv_order_num;


    @BindView(R.id.et_remark)
    EditText et_remark;

    @BindView(R.id.tv_goods_total)
    TextView tv_goods_total;

    @BindView(R.id.tv_goods_count)
    TextView tv_goods_count;

    @BindView(R.id.tv_total_price)
    TextView tv_total_price;

    @BindView(R.id.tv_express_name)
    TextView tv_express_name;

    @BindView(R.id.tv_express_price)
    TextView tv_express_price;

    @BindView(R.id.tv_coupon_name)
    TextView tv_coupon_name;

    @BindView(R.id.tv_score)
    TextView tv_score;

    @BindView(R.id.ll_score)
    LinearLayout ll_score;


    private GoodsOrderData data;

    private GoodsOrderAdpater madpater;

    private AddressData address;

    private CourierData courierData;

    private CouponData couponData;

    private MyDialog mMyDialog;

    private TextView tv_score_most;
    private TextView tv_score_use;
    private EditText et_score_now;
    private TextView tv_cancle;
    private TextView tv_ok;

    //积分抵扣金额
    private double scoreMoney;
    private String inputSocre;


    private PayForBottomDialog payForBottomDialog;

    private boolean isZhifuBao = true;
    private ImageView oneImageView;
    private ImageView twoImageView;
    private boolean isPinTuan;
    private int isCart;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_goodsorderdetail);
    }

    @Override
    protected void initData() {
        super.initData();
        isPinTuan = getIntent().getBooleanExtra(Contans.INTENT_TYPE, false);
        data = (GoodsOrderData) getIntent().getSerializableExtra(Contans.INTENT_DATA);
        isCart = getIntent().getIntExtra("isCart",0);
        titlename.setText("订单详情");


        PayForBottomDialog.Builder builder = new PayForBottomDialog.Builder(mContext);

        payForBottomDialog = builder.setClicklister(new View.OnClickListener() {
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
                        if (isZhifuBao) {
                            alipay();
                        } else {
                            weixin();
                        }
                        break;

                }
            }
        }).create();
        oneImageView = builder.getOneImageView();
        twoImageView = builder.getTwoImageView();

        payForBottomDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isCanclePay) {
                    AlertView alertView = new AlertView("确定放弃购买？", "您的订单在一段时间内未支付降被关闭，请尽快支付。", null, null, new String[]{"继续支付", "确认离开"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position == 1) {
                                MyOrderListActivity.startactivity(mContext, 0);
                            } else {
                                payForBottomDialog.show();
                            }

                        }
                    });
                    alertView.show();
                }

            }
        });

        setData();

        scoreInfo();
    }

    private void setData() {
        address = data.getAddress();

        setAddressData();

        View view = getLayoutInflater().inflate(R.layout.order_score_dialog, null);
        tv_score_most = view.findViewById(R.id.tv_score_most);
        tv_score_use = view.findViewById(R.id.tv_score_use);
        et_score_now = view.findViewById(R.id.et_score_now);
        tv_cancle = view.findViewById(R.id.tv_cancle);
        tv_ok = view.findViewById(R.id.tv_ok);

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMyDialog != null) {
                    if (mMyDialog.isShowing()) {
                        mMyDialog.dismiss();
                    }
                }
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputSocre = et_score_now.getText().toString();
                String totalSocre = tv_score_most.getText().toString();

                String useScore = tv_score_use.getText().toString();

                if (TextUtils.isEmpty(useScore)) {
                    useScore = "0";
                }
                if (TextUtils.isEmpty(totalSocre)) {
                    totalSocre = "0";
                }
                if (TextUtils.isEmpty(inputSocre)) {
                    inputSocre = "0";
                }

                int inputSocreNumber = Integer.valueOf(inputSocre);
                int useScoreNumber = Integer.valueOf(useScore);
                int totalSocreNumber = Integer.valueOf(totalSocre);


                if (inputSocreNumber > totalSocreNumber) {
                    ToastUtils.makeText("不能大于可抵扣积分");
                    return;
                }

                if (inputSocreNumber > useScoreNumber) {
                    ToastUtils.makeText("不能大于账户可用积分");
                    return;
                }


                BigDecimal bigDecimalScore = new BigDecimal(inputSocre);
                BigDecimal money = bigDecimalScore.multiply(new BigDecimal(cash)).divide(new BigDecimal(scores), 2, BigDecimal.ROUND_HALF_UP);
                scoreMoney = money.doubleValue();
                if (inputSocreNumber <= 0) {
                    tv_score.setText("未选择");
                } else {
                    tv_score.setText(inputSocreNumber + "积分 " + "抵扣 ¥" + money);

                }
                setTotalPrice();

                if (mMyDialog != null) {
                    if (mMyDialog.isShowing()) {
                        mMyDialog.dismiss();
                    }
                }
            }
        });


        mMyDialog = new MyDialog(this, 0, 0, view, R.style.DialogTheme);
        mMyDialog.setCancelable(true);

        String commodityNumTotal = data.getCommodityNumTotal();
        if (!TextUtils.isEmpty(commodityNumTotal)) {
            BigDecimal total = new BigDecimal(BigInteger.ZERO);
            if (Double.valueOf(commodityNumTotal) <= 0) {
                for (OrderDetail orderDetail : data.getOrderDetail()) {
                    total = total.add(new BigDecimal(orderDetail.getTotalPrice()));
                }
                data.setCommodityNumTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
            tv_goods_total.setText("¥ " + commodityNumTotal);
            tv_total_price.setText("¥ " + commodityNumTotal);
        } else {
            BigDecimal total = new BigDecimal(BigInteger.ZERO);

            for (OrderDetail orderDetail : data.getOrderDetail()) {
                total = total.add(new BigDecimal(orderDetail.getTotalPrice()));
            }
            data.setCommodityNumTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            tv_goods_total.setText("¥ " + data.getCommodityNumTotal());
            tv_total_price.setText("¥ " + data.getCommodityNumTotal());
        }

        tv_goods_count.setText("共" + data.getOrderDetail().size() + "件商品");

        madpater = new GoodsOrderAdpater(mContext, data.getOrderDetail());
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(madpater);
    }

    private void setAddressData() {
        if (address == null) {
            layout_location.setVisibility(View.GONE);
            ll_choose_address.setVisibility(View.VISIBLE);
        } else {

            String namePhone = "";
            if (!TextUtils.isEmpty(address.getMemberName())) {
                namePhone = address.getMemberName() + "    ";
            }

            if (!TextUtils.isEmpty(address.getMemberPhone())) {
                namePhone += address.getMemberPhone();
            }
            if (!TextUtils.isEmpty(address.getAddressFull())) {
                tv_address.setText(address.getAddressFull());
            } else {

                String adddress = "";

                if (!TextUtils.isEmpty(address.getProvince())) {
                    adddress = address.getProvince();
                }
                if (!TextUtils.isEmpty(address.getCity())) {
                    adddress += address.getCity();
                }
                if (!TextUtils.isEmpty(address.getDistrict())) {
                    adddress += address.getDistrict();
                }
                if (!TextUtils.isEmpty(address.getAddressDetail())) {
                    adddress += address.getAddressDetail();
                }
                tv_address.setText(adddress);
            }

            tv_name_phone.setText(namePhone);

            tv_order_num.setText("订单编号：");

            layout_location.setVisibility(View.VISIBLE);
            ll_choose_address.setVisibility(View.GONE);
        }
    }

    private String scores;
    private String cash;

    /**
     * 获取积分折算规则
     */
    private void scoreRuleInfo() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().scoreRuleInfo(requestBean)
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

                    if (data != null) {
                        ScoresInfo scoresInfo = data.getScoresInfo();

                        if (scoresInfo != null) {
                            scores = scoresInfo.getConversion().getScores();
                            cash = scoresInfo.getConversion().getCash();
                        }
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

    private String freightPrice = "";

    /**
     * 计算运费
     */
    private void computeFreightPrice() {

        showLoadingDialog();

        JsonObject jsonObject = new JsonObject();

        JsonArray jsonArray = new JsonArray();

        for (OrderDetail orderDetail : data.getOrderDetail()) {
            JsonObject commoditys = new JsonObject();
            commoditys.addProperty("commodityId", orderDetail.getCommodityId());
            commoditys.addProperty("commodityNum", orderDetail.getCommodityNum());
            jsonArray.add(commoditys);
        }
        jsonObject.add("commoditys", jsonArray);


        jsonObject.addProperty("courierId", courierData.getCourierId());
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("receivingAddressId", address.getReceivingAddressId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().computeFreightPrice(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {


                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    CourierInfo courierInfo = stringStatusCode.getData().getCourierInfo();

                    if (courierInfo == null) {
                        freightPrice = "0";
                    } else {
                        freightPrice = courierInfo.getFreightPrice();
                    }

                    if (!TextUtils.isEmpty(freightPrice)) {
                        tv_express_price.setText("¥ " + freightPrice);
                        setTotalPrice();
                    } else {
                        freightPrice = "0";
                        tv_express_price.setText("¥ " + freightPrice);
                        setTotalPrice();
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

    private CourierInfo courierInfo;

    /**
     * 获取订单可用积分
     */
    private void scoreInfo() {

        showLoadingDialog();

        JsonObject jsonObject = new JsonObject();

        List<String> temps = new ArrayList<>();
        for (OrderDetail orderDetail : data.getOrderDetail()) {
            temps.add(orderDetail.getAttributeId());
        }
        jsonObject.addProperty("attributeIds", new Gson().toJson(temps));
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().scoreInfo(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {


                if (stringStatusCode != null) {
                    courierInfo = stringStatusCode.getData().getOrderScoreInfo();
                    if (courierInfo != null) {

                        if (!TextUtils.isEmpty(courierInfo.getCanUseSore())) {
                            tv_score_use.setText(courierInfo.getCanUseSore());
                            BigDecimal bigDecimal = new BigDecimal(courierInfo.getCanUseSore());
                            if (bigDecimal.compareTo(BigDecimal.ZERO) <= 0) {
                                ll_score.setVisibility(View.GONE);
                            }
                        } else {
                            ll_score.setVisibility(View.GONE);
                        }

                        if (!TextUtils.isEmpty(courierInfo.getTotalScore())) {
                            tv_score_most.setText(courierInfo.getTotalScore());
                            BigDecimal bigDecimal = new BigDecimal(courierInfo.getTotalScore());
                            if (bigDecimal.compareTo(BigDecimal.ZERO) <= 0) {
                                ll_score.setVisibility(View.GONE);
                            }
                        } else {
                            ll_score.setVisibility(View.GONE);
                        }
                    }
                }
                scoreRuleInfo();


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

                dismissLoadingDialog();

            }
        }, "", lifecycleSubject, false, true);

    }


    /**
     * 提交订单
     */
    @OnClick(R.id.btn_submit_order)
    public void sumbitOrder() {

        if (address == null) {
            ToastUtils.makeText("请选择收货地址");
            return;
        }
        if (courierData == null) {
            ToastUtils.makeText("请选择配送物流");
            return;
        }

        //普通商品
        if (!isPinTuan) {
            sumbitNoramlOrder();
        } else {
            sumbitTeamOrder();
        }
    }

    private OrderInfo orderInfo;

    /**
     * 提交拼团订单
     */
    private void sumbitTeamOrder() {
        JsonObject orderJsonObject = new JsonObject();

        JsonObject addressJsonObject = new JsonObject();
        addressJsonObject.addProperty("phone", address.getMemberPhone());
        addressJsonObject.addProperty("receiverAddressId", address.getReceivingAddressId());
        addressJsonObject.addProperty("receiverMember", address.getMemberName());
        addressJsonObject.addProperty("receivingAddress", tv_address.getText().toString());
        orderJsonObject.add("address", addressJsonObject);

        //商品总金额
        orderJsonObject.addProperty("commodityNumTotal", data.getCommodityNumTotal());
        if (couponData != null) {
            orderJsonObject.addProperty("couponId", couponData.getCouponGetId());
            orderJsonObject.addProperty("couponPrice", scoreMoney);
        }


        if (courierData != null) {
            orderJsonObject.addProperty("courierId", courierData.getCourierId());
//            orderJsonObject.addProperty("couponPrice",courierData.getCourierName());
        }
        orderJsonObject.addProperty("isCart", isCart);

        orderJsonObject.addProperty("teamBuyId", data.getTeamBuyId());

        orderJsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        JsonArray jsonArray = new JsonArray();

        for (OrderDetail orderDetail : data.getOrderDetail()) {
            JsonObject commoditys = new JsonObject();
            commoditys.addProperty("attributeId", orderDetail.getAttributeId());
            commoditys.addProperty("commodityId", orderDetail.getCommodityId());
            commoditys.addProperty("commodityName", orderDetail.getCommodityName());
            commoditys.addProperty("commodityNum", orderDetail.getCommodityNum());
            commoditys.addProperty("commodityPrice", orderDetail.getTeamBuyPrice());
            commoditys.addProperty("totalPrice", orderDetail.getTotalPrice());
            jsonArray.add(commoditys);
        }

        orderJsonObject.add("orderDetail", jsonArray);

        String orderMsg = et_remark.getText().toString().trim();

        if (!TextUtils.isEmpty(orderMsg)) {
            orderJsonObject.addProperty("orderMsg", orderMsg);
        }


        String orderPriceRealPay = tv_total_price.getText().toString();
        orderPriceRealPay = orderPriceRealPay.replace("¥ ", "");
        orderJsonObject.addProperty("orderPriceTotal", data.getCommodityNumTotal());
        orderJsonObject.addProperty("orderPriceRealPay", orderPriceRealPay);
        orderJsonObject.addProperty("orderPriceFreight", freightPrice);
        if (scoreMoney > 0) {
            orderJsonObject.addProperty("orderScoreUse", inputSocre);
            orderJsonObject.addProperty("scorePrice", scoreMoney);
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("orderInfo", orderJsonObject);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);


        Observable observable =
                ApiUtils.getApi().submitOrder(requestBean)
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
//                EventBus.getDefault().post(new MainJumpEvent(2));
                if (stringStatusCode != null) {
                    orderInfo = stringStatusCode.getData().getOrderInfo();
                    orderInfo.setPinTuan(isPinTuan);
                    isCanclePay = true;
                    payForBottomDialog.show();
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
     * 提交普通订单
     */
    private void sumbitNoramlOrder() {
        JsonObject orderJsonObject = new JsonObject();

        JsonObject addressJsonObject = new JsonObject();
        addressJsonObject.addProperty("phone", address.getMemberPhone());
        addressJsonObject.addProperty("receiverAddressId", address.getReceivingAddressId());
        addressJsonObject.addProperty("receiverMember", address.getMemberName());
        addressJsonObject.addProperty("receivingAddress", tv_address.getText().toString());
        orderJsonObject.add("address", addressJsonObject);

        //商品总金额
        orderJsonObject.addProperty("commodityNumTotal", data.getCommodityNumTotal());
        if (couponData != null) {
            orderJsonObject.addProperty("couponId", couponData.getCouponGetId());
            orderJsonObject.addProperty("couponPrice", scoreMoney);
        }


        if (courierData != null) {
            orderJsonObject.addProperty("courierId", courierData.getCourierId());
//            orderJsonObject.addProperty("couponPrice",courierData.getCourierName());
        }

        orderJsonObject.addProperty("isCart", isCart);

        orderJsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        JsonArray jsonArray = new JsonArray();

        for (OrderDetail orderDetail : data.getOrderDetail()) {
            JsonObject commoditys = new JsonObject();
            commoditys.addProperty("attributeId", orderDetail.getAttributeId());
            commoditys.addProperty("commodityId", orderDetail.getCommodityId());
            commoditys.addProperty("commodityName", orderDetail.getCommodityName());
            commoditys.addProperty("commodityNum", orderDetail.getCommodityNum());
            commoditys.addProperty("commodityPrice", orderDetail.getCommodityPrice());
            commoditys.addProperty("totalPrice", orderDetail.getTotalPrice());
            jsonArray.add(commoditys);
        }

        orderJsonObject.add("orderDetail", jsonArray);

        String orderMsg = et_remark.getText().toString().trim();

        if (!TextUtils.isEmpty(orderMsg)) {
            orderJsonObject.addProperty("orderMsg", orderMsg);
        }


        String orderPriceRealPay = tv_total_price.getText().toString();
        orderPriceRealPay = orderPriceRealPay.replace("¥ ", "");
        orderJsonObject.addProperty("orderPriceTotal", data.getCommodityNumTotal());
        orderJsonObject.addProperty("orderPriceRealPay", orderPriceRealPay);
        orderJsonObject.addProperty("orderPriceFreight", freightPrice);


        if (scoreMoney > 0) {
            orderJsonObject.addProperty("orderScoreUse", inputSocre);
            orderJsonObject.addProperty("scorePrice", scoreMoney);
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("orderInfo", orderJsonObject);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);


        Observable observable =
                ApiUtils.getApi().submitOrderForm(requestBean)
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
                EventBus.getDefault().post(new MainJumpEvent(2));

                if (stringStatusCode != null) {
                    orderInfo = stringStatusCode.getData().getOrderInfo();
                    orderInfo.setPinTuan(isPinTuan);
                    isCanclePay = true;
                    payForBottomDialog.show();
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
                            .build().toWXPayNotSign(GoodsOrderActivity.this);

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
                PayTask alipay = new PayTask(GoodsOrderActivity.this);
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

    private boolean isCanclePay = true;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Map<String, String> result = ((Map<String, String>) msg.obj);
            String requestcode = result.get("resultStatus");

            if (!TextUtils.isEmpty(requestcode)) {
                try {

                    switch (Integer.parseInt(requestcode)) {
                        case 9000:
                            isCanclePay = false;
                            PaySuccessActivity.startactivity(mContext, orderInfo);
//                            startActivity(new Intent(GoodsOrderActivity.this,GoldPaySuccessActivity.class).putExtra("data",data));
                            finish();
                            break;
                        case 6001:
                            ToastUtils.makeText("取消支付");
                            break;
                        default:
                            isCanclePay = false;
                            ToastUtils.makeText("支付宝支付失败");
                            break;
                    }

                } catch (Exception e) {

                }
            }
            payForBottomDialog.dismiss();

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
            finish();
            isCanclePay = false;
        } else if (errCode == -2) {
            //用户取消
            ToastUtils.makeText("取消支付");
        } else if (errCode == -1) {
            //支付失败
            ToastUtils.makeText("支付失败");
            isCanclePay = false;
        }
        payForBottomDialog.dismiss();
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
        if (mMyDialog != null) {
            if (mMyDialog.isShowing()) {
                mMyDialog.dismiss();
            }
            mMyDialog = null;
        }

        if (payForBottomDialog != null) {
            if (payForBottomDialog.isShowing()) {
                payForBottomDialog.dismiss();
            }
            payForBottomDialog = null;
        }
    }

    /**
     * 物流选择
     */
    @OnClick(R.id.layout_logistics)
    public void gotoChoiceLogisticsActivity() {
        if (address == null) {
            ToastUtils.makeText("请先选择收货地址");
            return;
        }

        JsonObject jsonObject = new JsonObject();

        JsonArray jsonArray = new JsonArray();

        for (OrderDetail orderDetail : data.getOrderDetail()) {
            JsonObject commoditys = new JsonObject();
            commoditys.addProperty("commodityId", orderDetail.getCommodityId());
            commoditys.addProperty("commodityNum", orderDetail.getCommodityNum());
            jsonArray.add(commoditys);
        }
        jsonObject.add("commoditys", jsonArray);


//        jsonObject.addProperty("courierId", courierData.getCourierId());
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("receivingAddressId", address.getReceivingAddressId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);

        ChoiceLogisticsActivity.startActivitForResult(this, requestBean, GET_EXPRESS);
    }


    /**
     * 选择地址
     */
    @OnClick({R.id.layout_location, R.id.ll_choose_address})
    public void gotoChoiceGoodsLocationActivity() {
        ChoiceGoodsLocationActivity.startActivitForResult(GoodsOrderActivity.this, GET_LOCATION);
    }


    /**
     * 优惠券选择
     */
    @OnClick(R.id.ll_chooase_coupon)
    public void chooseCoupon() {
        String id = "";
        if (couponData != null) {
            id = couponData.getCouponGetId();
        }
        OrderChooseCoponActivity.startActivitForResult(GoodsOrderActivity.this, id, data, GET_COUPON);
    }

    /**
     * 积分选择
     */
    @OnClick(R.id.ll_score)
    public void chooseScore() {
        if (mMyDialog != null) {
            if (!mMyDialog.isShowing()) {
                mMyDialog.show();
            }
        }
    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }


    public static void startactivity(Context mCOntext, GoodsOrderData data, boolean isPinTuan, int isCart) {
        Intent mIntent = new Intent(mCOntext, GoodsOrderActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA, data);
        mIntent.putExtra(Contans.INTENT_TYPE, isPinTuan);
        mIntent.putExtra("isCart", isCart);
        mCOntext.startActivity(mIntent);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //与startActivityForResult配套
        super.onActivityResult(requestCode, resultCode, data); //requestCode(申请码), 用于判断

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GET_LOCATION:
                    AddressData addressTemp = (AddressData) data.getSerializableExtra("address");

                    if (addressTemp != null) {
                        address = addressTemp;
                    }
                    courierData = null;
                    freightPrice = "0";
                    tv_express_price.setText("请重新选择");
                    tv_express_name.setText("");
                    setAddressData();
                    setTotalPrice();
                    break;
                case GET_EXPRESS:

                    CourierData courierDataTemp = (CourierData) data.getSerializableExtra("courierData");
                    if (courierDataTemp != null) {
                        courierData = courierDataTemp;
                    }
                    courierData = (CourierData) data.getSerializableExtra("courierData");
                    tv_express_name.setText(courierData.getCourierName());
//                    computeFreightPrice();
                    freightPrice = courierData.getFreightPrice();

                    if (!TextUtils.isEmpty(freightPrice)) {
                        tv_express_price.setText("¥ " + freightPrice);
                        setTotalPrice();
                    } else {
                        freightPrice = "0";
                        tv_express_price.setText("¥ " + freightPrice);
                        setTotalPrice();
                    }

                    break;
                case GET_COUPON:

                    CouponData couponDataTmep = (CouponData) data.getSerializableExtra("couponData");
                    if (couponDataTmep != null) {
                        couponData = couponDataTmep;
                    }
                    couponData = (CouponData) data.getSerializableExtra("couponData");
                    tv_coupon_name.setText(couponData.getCouponName());
                    setTotalPrice();
                    break;


            }

        }
    }

    private void setTotalPrice() {

        if (TextUtils.isEmpty(freightPrice)) {
            freightPrice = "0";
        }
        if (couponData != null) {
            //满减
            if (!TextUtils.isEmpty(couponData.getCouponType()) && couponData.getCouponType().equals("1")) {
                BigDecimal temp = new BigDecimal(this.data.getCommodityNumTotal()).subtract(new BigDecimal(couponData.getCouponValue())).add(new BigDecimal(freightPrice)).setScale(2, BigDecimal.ROUND_HALF_UP);
                ;
                tv_total_price.setText("¥ " + temp.toString());
            } else {

                //打折计算
                BigDecimal temp = new BigDecimal(this.data.getCommodityNumTotal())
                        .divide(new BigDecimal(10), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(couponData.getCouponDiscount()));

                //加上运费
                temp = temp.add(new BigDecimal(freightPrice));

                //减去积分抵扣金额
                if (scoreMoney > 0) {
                    temp = temp.subtract(new BigDecimal(scoreMoney));
                }
                temp = temp.setScale(2, BigDecimal.ROUND_HALF_UP);
                tv_total_price.setText("¥ " + temp.toString());

            }
        } else {
            BigDecimal add = new BigDecimal(data.getCommodityNumTotal()).add(new BigDecimal(freightPrice)).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (scoreMoney > 0) {
                if (scoreMoney > 0) {
                    add = add.subtract(new BigDecimal(scoreMoney));
                }
            }
            add = add.setScale(2, BigDecimal.ROUND_HALF_UP);
            tv_total_price.setText("¥ " + add.toString());
        }


    }


}
