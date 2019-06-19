package com.oldwang.boxdemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.gongwen.marqueen.MarqueeView;
import com.gongwen.marqueen.util.OnItemClickListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.AttributeAdpater;
import com.oldwang.boxdemo.adpater.GoodsCommentAdpater;
import com.oldwang.boxdemo.adpater.GoodsPinLunAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.Attribute;
import com.oldwang.boxdemo.bean.CommodityChildCommentData;
import com.oldwang.boxdemo.bean.CommodityCommentInfo;
import com.oldwang.boxdemo.bean.CommodityTeamBuyData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.GoodsOrderData;
import com.oldwang.boxdemo.bean.GoodsOrderDataTwo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.NewCommodity;
import com.oldwang.boxdemo.bean.OrderDetail;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.AttributeEvent;
import com.oldwang.boxdemo.event.GoodsEvent;
import com.oldwang.boxdemo.event.MainJumpEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.ComplexViewMF;
import com.oldwang.boxdemo.view.GoodsBottomDialog;
import com.oldwang.boxdemo.view.MyGridView;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.hutool.core.bean.BeanUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 商品详情
 */
public class GoodsDetailActivity extends BaseActivity {
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.comment_btn1)
    TextView comment_btn1;
    @BindView(R.id.comment_btn2)
    TextView comment_btn2;
    @BindView(R.id.comment_btn3)
    TextView comment_btn3;
    @BindView(R.id.comment_btn4)
    TextView comment_btn4;

    @BindView(R.id.tv_attribute_hint)
    TextView tv_attribute_hint;
    @BindView(R.id.tv_attribute)
    TextView tv_attribute;


//    @BindView(R.id.pinlungridview)
//    MyGridView imagegrideview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;



    @BindView(R.id.id_cb)
    ConvenientBanner banner;

    @BindView(R.id.tv_now_pirce)
    TextView tv_now_pirce;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_goods_name)
    TextView tv_goods_name;
    @BindView(R.id.tv_sale_count)
    TextView tv_sale_count;
    @BindView(R.id.comment_counts)
    TextView comment_counts;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;

    @BindView(R.id.iv_collect)
    ImageView iv_collect;


    @BindView(R.id.ll_pintuan)
    LinearLayout ll_pintuan;

    @BindView(R.id.ll_new_goods)
    LinearLayout ll_new_goods;
    @BindView(R.id.iv_new_goods)
    ImageView iv_new_goods;

    @BindView(R.id.text_getgoods_btn)
    TextView text_getgoods_btn;

    @BindView(R.id.tv_open_team)
    TextView tv_open_team;

    List<TextView> commentbtnlist = new ArrayList<>();

    private Dialog choicegoodstypes;
//    private GoodsPinLunAdpater commentadpater;
    private GoodsCommentAdpater madpater;

    private List<String> commentlistdat = new ArrayList<>();
    private AttributeAdpater oneAdapter;
    private AttributeAdpater twoAdapter;
    private AttributeAdpater threeAdapter;

    private List<Attribute> oneList = new ArrayList<>();
    private List<Attribute> twoList = new ArrayList<>();
    private List<Attribute> threeList = new ArrayList<>();

    private List<String> tempOneList = new ArrayList<>();

    private List<String> tempList = new ArrayList<>();


//    private SizeChoiceAdpater sizeadpater;
//    private SizeChoiceAdpater threeAdapter;

    private String commodityId;
    private boolean isPinTuan;
    private boolean isJoinTeam;

    private CommodityTeamBuyData commodityTeamBuyData;

    private CommodityTeamBuyData commodityInfo;

    List<CommodityChildCommentData> datas = new ArrayList<>();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_goodsdetail);
    }

    @Override
    protected void initData() {
        super.initData();

        oneAdapter = new AttributeAdpater(mContext, oneList, 0);
        twoAdapter = new AttributeAdpater(mContext, twoList, 1);
        threeAdapter = new AttributeAdpater(mContext, threeList, 2);

        commodityId = getIntent().getStringExtra(Contans.INTENT_DATA);
//        commodityId = "178590983520378";
        isPinTuan = getIntent().getBooleanExtra(Contans.INTENT_TYPE, false);
        commodityTeamBuyData = (CommodityTeamBuyData) getIntent().getSerializableExtra(Contans.INTENT_TYPE_TWO);

        if (commodityTeamBuyData != null) {
            isJoinTeam = true;
        } else {
            isJoinTeam = false;
        }


        initWebView();
//        commentadpater = new GoodsPinLunAdpater(mContext, datas);
//        imagegrideview.setAdapter(commentadpater);

//        madpater = new GoodsCommentAdpater(mContext,datas);
                madpater = new GoodsCommentAdpater(mContext,datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(madpater);

        if (isPinTuan) {
            teamBuyCommodityDetail();
            teamList();
        } else {
            text_getgoods_btn.setText("加入购物车");
            tv_open_team.setText("立即购买");
            ll_pintuan.setVisibility(View.VISIBLE);
            commodityDetail();
        }
        commodityCommentInfo();
    }

    public ImageView getDialogImageView() {
        return dialogImage;
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

    @OnClick(R.id.finish_image)
    public void finishactivity() {
        finish();
    }


    private ListData<CommodityChildCommentData> commodityCommentData;
    private CommodityCommentInfo commodityCommentInfo;

    //评论级别标识(0,全部1,好评2中评3差评)
    private int flag = 0;

    /***
     * 商品评论信息
     */
    private void commodityCommentInfo() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commodityId", commodityId);
        jsonObject.addProperty("flag", flag);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", 1);
        jsonObject.addProperty("pageSize", 2);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().commodityCommentInfo(requestBean)
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
                    DataInfo data = stringStatusCode.getData();
                    if (data != null) {
                        commodityCommentData = data.getCommodityCommentData();
                        commodityCommentInfo = data.getCommodityCommentInfo();
                    }

                }


                setCommentData();

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    /***
     * 设置评论信息
     */
    private void setCommentData() {

        if (commodityCommentInfo != null) {

            int allCount = 0;

            String goodCount = commodityCommentInfo.getGoodCount();
            if (!TextUtils.isEmpty(goodCount)) {
                comment_btn2.setText("好评(" + goodCount + ")");
                allCount += Integer.valueOf(goodCount);
            }

            String midCount = commodityCommentInfo.getMidCount();
            if (!TextUtils.isEmpty(midCount)) {
                comment_btn3.setText("中评(" + midCount + ")");
                allCount += Integer.valueOf(midCount);
            }

            String badCount = commodityCommentInfo.getBadCount();
            if (!TextUtils.isEmpty(badCount)) {
                comment_btn4.setText("差评(" + badCount + ")");
                allCount += Integer.valueOf(badCount);
            }

            comment_btn1.setText("全部(" + allCount + ")");
            comment_counts.setText("商品评价(" + allCount + ")");

        }

        if (commodityCommentData != null) {

            List<CommodityChildCommentData> list = commodityCommentData.getList();

            datas.clear();
            if (list != null) {
                datas.addAll(list);
            }
            madpater.notifyDataSetChanged();
        }
    }


    /***
     * 获取拼团列表
     */
    private void teamList() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commodityId", commodityId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", 1);
        jsonObject.addProperty("pageSize", 10);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().teamList(requestBean)
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

                    ListData<CommodityTeamBuyData> commodityTeamBuyData = stringStatusCode.getData().getCommodityTeamBuyData1();
                    List<CommodityTeamBuyData> list = commodityTeamBuyData.getList();


                    if (list != null && list.size() > 0) {
                        ll_pintuan.setVisibility(View.GONE);
                        marqueeView.setVisibility(View.VISIBLE);

                        ComplexViewMF marqueeFactory = new ComplexViewMF(mContext);
                        marqueeFactory.setData(list);
                        marqueeView.setMarqueeFactory(marqueeFactory);
                        marqueeView.startFlipping();
                        marqueeView.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClickListener(View mView, Object mData, int mPosition) {
                                AssmbleListActivity.startactivity(mContext, commodityId);
                            }
                        });
                    } else {
//                        ll_pintuan.setVisibility(View.VISIBLE);
                        marqueeView.setVisibility(View.GONE);
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

    /***
     * 普通商品加入购物车
     */
    private void addCart() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commodityAttributeId", attributeId);
        jsonObject.addProperty("commodityCount", goodsCount);
        jsonObject.addProperty("commodityId", commodityId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().addCart(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<GoodsOrderData>(mContext) {
            @Override
            protected void _onNext(StatusCode<GoodsOrderData> stringStatusCode) {
                dismissLoadingDialog();
                if (stringStatusCode != null) {
//                    finish();
                    ToastUtils.makeText("加入购物车成功");
                    EventBus.getDefault().post(new MainJumpEvent(2));
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    /***
     * 普通商品创建订单
     */
    private void confirmOrderForm() {

        JsonObject jsonObject = new JsonObject();

        JsonObject commoditys = new JsonObject();
        commoditys.addProperty("attributeId", attributeId);
        commoditys.addProperty("commodityId", commodityId);
        commoditys.addProperty("commodityName", commodityInfo.getCommodityName());
        commoditys.addProperty("commodityNum", goodsCount);
        commoditys.addProperty("commodityPrice", samePrice);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(commoditys);
        jsonObject.add("commoditys", jsonArray);


        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().confirmOrderForm(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<GoodsOrderData>(mContext) {
            @Override
            protected void _onNext(StatusCode<GoodsOrderData> stringStatusCode) {
                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    GoodsOrderActivity.startactivity(mContext, stringStatusCode.getData(), false, 0);
//                    finish();
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    /***
     * 拼团商品创建订单
     */
    private void joinTeamAndCreateOrder() {


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("attributeId", attributeId);
        jsonObject.addProperty("commodityId", commodityId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("commodityNum", goodsCount);
        if (commodityTeamBuyData != null) {
            jsonObject.addProperty("teamBuyCode", commodityTeamBuyData.getTeamBuyCode());
            jsonObject.addProperty("teamBuyId", commodityTeamBuyData.getTeamBuyId());
        }

        jsonObject.addProperty("teamBuyPrice", samePrice);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().joinTeamAndCreateOrder(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<GoodsOrderDataTwo>(mContext) {
            @Override
            protected void _onNext(StatusCode<GoodsOrderDataTwo> stringStatusCode) {
                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    GoodsOrderDataTwo data = stringStatusCode.getData();
                    GoodsOrderData goodsOrderData = new GoodsOrderData();
                    goodsOrderData.setTeamBuyId(commodityTeamBuyData.getTeamBuyId());
                    goodsOrderData.setAddress(data.getAddress());
                    goodsOrderData.setCanUseScore(data.getCanUseScore());
                    goodsOrderData.setCommodityNumTotal(data.getCommodityNumTotal());
                    goodsOrderData.setCouponCount(data.getCouponCount());
                    goodsOrderData.setMemberId(data.getMemberId());
                    goodsOrderData.setOrderStatus(data.getOrderStatus());
                    goodsOrderData.setOrderType(data.getOrderType());
                    goodsOrderData.setOrderTypeName(data.getOrderTypeName());
                    List<OrderDetail> orderDetail = new ArrayList<>();
                    orderDetail.add(data.getOrderDetail());
                    goodsOrderData.setOrderDetail(orderDetail);
                    GoodsOrderActivity.startactivity(mContext, goodsOrderData, true, 0);
//                    finish();
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }


    /***
     * 拼团商品创建订单
     */
    private void createTeam() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("attributeId", attributeId);
        jsonObject.addProperty("commodityId", commodityId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("commodityNum", goodsCount);
        jsonObject.addProperty("teamBuyPrice", samePrice);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().createTeam(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<GoodsOrderDataTwo>(mContext) {
            @Override
            protected void _onNext(StatusCode<GoodsOrderDataTwo> stringStatusCode) {
                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    GoodsOrderDataTwo data = stringStatusCode.getData();
                    GoodsOrderData goodsOrderData = new GoodsOrderData();

                    goodsOrderData.setAddress(data.getAddress());
                    goodsOrderData.setCanUseScore(data.getCanUseScore());
                    goodsOrderData.setCommodityNumTotal(data.getCommodityNumTotal());
                    goodsOrderData.setCouponCount(data.getCouponCount());
                    goodsOrderData.setMemberId(data.getMemberId());
                    goodsOrderData.setOrderStatus(data.getOrderStatus());
                    goodsOrderData.setOrderType(data.getOrderType());
                    goodsOrderData.setOrderTypeName(data.getOrderTypeName());
                    List<OrderDetail> orderDetail = new ArrayList<>();
                    orderDetail.add(data.getOrderDetail());
                    goodsOrderData.setOrderDetail(orderDetail);

                    GoodsOrderActivity.startactivity(mContext, goodsOrderData, true, 0);
//                    finish();

                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    private List<Attribute> list;
    /***
     * 拼团商品规格
     */
    private void commodityTeamAttribute() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commodityId", commodityId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().commodityTeamAttribute(requestBean)
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

                     list = stringStatusCode.getData().getCommodityAttributeInfo();
                    tempList.clear();
                    tempOneList.clear();
                    if (list != null) {

                        List<String> temp1 = new ArrayList<>();
                        List<String> temp2 = new ArrayList<>();
                        List<String> temp3 = new ArrayList<>();

                        //颜色
                        List<Attribute> colorList = new ArrayList<>();
                        //规格大小
                        List<Attribute> sizeList = new ArrayList<>();
                        //材质
                        List<Attribute> qualityList = new ArrayList<>();

                        boolean isFirst = true;
                        for (Attribute attribute : list) {
                            if (isFirst) {
                                isFirst = false;
                                String picUrl = attribute.getPicUrl();
                                if (!TextUtils.isEmpty(picUrl)) {
                                    UIUtils.loadImageView(mContext, picUrl, dialogImage);
                                }
                            }
                            attribute.setChoos(true);
                            //库存为空不可选
                            if (TextUtils.isEmpty(attribute.getStockNum())) {
                                attribute.setChoos(false);
                            } else {
                                int count = Integer.valueOf(attribute.getStockNum());
                                //库存为空不可选
                                if (count <= 0) {
                                    attribute.setChoos(false);
                                }
                            }

                            tempList.add(attribute.getAttributeColor() + attribute.getAttributeSize() + attribute.getAttributeQuality());
                            tempOneList.add(attribute.getAttributeColor() + attribute.getAttributeSize());

                            if (!temp1.contains(attribute.getAttributeColor())) {
                                Attribute oneAttribute = new Attribute();
                                BeanUtil.copyProperties(attribute, oneAttribute);
                                temp1.add(attribute.getAttributeColor());
                                colorList.add(oneAttribute);
                            }

                            if (!temp2.contains(attribute.getAttributeSize())) {
                                Attribute twoAttribute = new Attribute();
                                BeanUtil.copyProperties(attribute, twoAttribute);
                                temp2.add(attribute.getAttributeSize());
                                sizeList.add(twoAttribute);
                            }

                            if (!temp3.contains(attribute.getAttributeQuality())) {
                                Attribute threeAttribute = new Attribute();
                                BeanUtil.copyProperties(attribute, threeAttribute);
                                temp3.add(attribute.getAttributeQuality());
                                qualityList.add(threeAttribute);
                            }
                        }

                        oneList.clear();
                        oneList.addAll(colorList);

                        twoList.clear();
                        twoList.addAll(sizeList);

                        threeList.clear();
                        threeList.addAll(qualityList);

                        oneAdapter.setadlldataeType(false, -1);
                        twoAdapter.setadlldataeType(false, -1);
                        threeAdapter.setadlldataeType(false, -1);

                        oneAdapter.notifyDataSetChanged();
                        twoAdapter.notifyDataSetChanged();
                        threeAdapter.notifyDataSetChanged();

                        if (isJoinTeam) {
                            if (choicegoodstypes != null) {
                                isChooseAttribute = false;
                                choicegoodstypes.show();
                            }
                        }

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


    /***
     * 普通商品规格
     */
    private void commodityAttribute() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commodityId", commodityId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().commodityAttribute(requestBean)
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

                     list = stringStatusCode.getData().getCommodityAttributeInfo();
                    tempList.clear();
                    tempOneList.clear();
                    if (list != null) {

                        List<String> temp1 = new ArrayList<>();
                        List<String> temp2 = new ArrayList<>();
                        List<String> temp3 = new ArrayList<>();

                        //颜色
                        List<Attribute> colorList = new ArrayList<>();
                        //规格大小
                        List<Attribute> sizeList = new ArrayList<>();
                        //材质
                        List<Attribute> qualityList = new ArrayList<>();


                        boolean isFirst = true;
                        for (Attribute attribute : list) {
                            if (isFirst) {
                                isFirst = false;
                                String picUrl = attribute.getPicUrl();
                                if (!TextUtils.isEmpty(picUrl)) {
                                    UIUtils.loadImageView(mContext, picUrl, dialogImage);
                                }
                            }


                            attribute.setChoos(true);
                            //库存为空不可选
                            if (TextUtils.isEmpty(attribute.getStockNum())) {
                                attribute.setChoos(false);
                            } else {
                                int count = Integer.valueOf(attribute.getStockNum());
                                //库存为空不可选
                                if (count <= 0) {
                                    attribute.setChoos(false);
                                }
                            }
                            tempList.add(attribute.getAttributeColor() + attribute.getAttributeSize() + attribute.getAttributeQuality());
                            tempOneList.add(attribute.getAttributeColor() + attribute.getAttributeSize());

                            if (!temp1.contains(attribute.getAttributeColor())) {
                                Attribute oneAttribute = new Attribute();
                                BeanUtil.copyProperties(attribute, oneAttribute);
                                temp1.add(attribute.getAttributeColor());
                                colorList.add(oneAttribute);
                            }

                            if (!temp2.contains(attribute.getAttributeSize())) {
                                Attribute twoAttribute = new Attribute();
                                BeanUtil.copyProperties(attribute, twoAttribute);
                                temp2.add(attribute.getAttributeSize());
                                sizeList.add(twoAttribute);
                            }

                            if (!temp3.contains(attribute.getAttributeQuality())) {
                                Attribute threeAttribute = new Attribute();
                                BeanUtil.copyProperties(attribute, threeAttribute);
                                temp3.add(attribute.getAttributeQuality());
                                qualityList.add(threeAttribute);
                            }
                        }

                        oneList.clear();
                        oneList.addAll(colorList);

                        twoList.clear();
                        twoList.addAll(sizeList);

                        threeList.clear();
                        threeList.addAll(qualityList);

                        oneAdapter.setadlldataeType(false, -1);
                        twoAdapter.setadlldataeType(false, -1);
                        threeAdapter.setadlldataeType(false, -1);

                        oneAdapter.notifyDataSetChanged();
                        twoAdapter.notifyDataSetChanged();
                        threeAdapter.notifyDataSetChanged();


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


    /***
     * 普通商品详情
     */
    private void commodityDetail() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commodityId", commodityId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().commodityDetail(requestBean)
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
                    commodityInfo = stringStatusCode.getData().getCommodityInfo();

                    if (commodityInfo != null) {
                        setGoodsData();
                    }
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
                finish();

            }
        }, "", lifecycleSubject, false, true);

    }

    /***
     * 拼团商品详情
     */
    private void teamBuyCommodityDetail() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commodityId", commodityId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().teamBuyCommodityDetail(requestBean)
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
                    commodityInfo = stringStatusCode.getData().getCommodityInfo();

                    if (commodityInfo != null) {
                        setGoodsData();
                    }
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
                finish();

            }
        }, "", lifecycleSubject, false, true);

    }

    /***
     * 设置商品信息
     */
    private void setGoodsData() {
        initDialog();


        setBanner(commodityInfo.getCommodityImgList());

        if (isPinTuan) {
            if (!TextUtils.isEmpty(commodityInfo.getTeamBuyPrice())) {
                tv_now_pirce.setText("¥" + commodityInfo.getTeamBuyPrice());
            }
        } else {
            if (!TextUtils.isEmpty(commodityInfo.getSamePrice())) {
                tv_now_pirce.setText("¥" + commodityInfo.getSamePrice());
            }
        }

        NewCommodity newCommodity = commodityInfo.getNewCommodity();
        if (newCommodity != null) {
            ll_new_goods.setVisibility(View.VISIBLE);
            UIUtils.loadImageView(mContext, newCommodity.getCommodityImgsDetail(), iv_new_goods);
        } else {
            ll_new_goods.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(commodityInfo.getIsFavorite()) && commodityInfo.getIsFavorite().equals("1")) {
            isFavorite = true;
            iv_collect.setImageDrawable(getResources().getDrawable(R.mipmap.already_collcet_icon));
        } else {
            isFavorite = false;
            iv_collect.setImageDrawable(getResources().getDrawable(R.mipmap.goodsdetail_shoucang));
        }


        if (!TextUtils.isEmpty(commodityInfo.getCommodityName())) {

            if (!TextUtils.isEmpty(commodityInfo.getPeopleNum()) && isPinTuan) {

                String temp = "<font color='#FF0000'>【" + commodityInfo.getPeopleNum() + "人团】 </font>" + commodityInfo.getCommodityName();
                tv_goods_name.setText(Html.fromHtml(temp));
            } else {
                tv_goods_name.setText(commodityInfo.getCommodityName());
            }
        }


        if (!TextUtils.isEmpty(commodityInfo.getSalePrice())) {
            tv_price.setText("¥" + commodityInfo.getSalePrice());
        }
        if (!TextUtils.isEmpty(commodityInfo.getSellCount())) {
            tv_sale_count.setText("已售：  " + commodityInfo.getSellCount());
        }

        if (!TextUtils.isEmpty(commodityInfo.getCommodityDescription())) {
            String content = commodityInfo.getCommodityDescription();
            mWebView.loadDataWithBaseURL(null, getNewData(content), "text/html", "UTF-8", null);
        }
        if (isPinTuan) {
            commodityTeamAttribute();
        } else {
            commodityAttribute();
        }


    }


    /***
     * 设置banner信息
     * @param pictureSetData
     */
    private void setBanner(List<String> pictureSetData) {

        if (pictureSetData != null && pictureSetData.size() > 0) {
            banner.setVisibility(View.VISIBLE);

            banner.setPages(new CBViewHolderCreator<ImageViewHolder>() {
                @Override
                public ImageViewHolder createHolder() {
                    return new ImageViewHolder();
                }
            }, pictureSetData)
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL); //设置指示器的方向水平  居中

            if (pictureSetData.size() == 1) {
                banner.setPointViewVisible(false);
            } else {
                banner.setPointViewVisible(true);
            }
        }

    }

    public class ImageViewHolder implements Holder<String> {
        private View mhandeview;
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            mhandeview = UIUtils.inflate(mContext, R.layout.banner_item);
            return mhandeview;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            imageView = mhandeview.findViewById(R.id.image);
            final String pictureUrl = data;

            if (!TextUtils.isEmpty(pictureUrl)) {
                UIUtils.loadImageView(mContext, pictureUrl, imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }

        }
    }


    @OnClick({R.id.comment_btn1, R.id.comment_btn2, R.id.comment_btn3, R.id.comment_btn4})
    public void clickdata(View v) {
        switch (v.getId()) {
            case R.id.comment_btn1:
                updatecommentbtntype(0);
                flag = 0;
                commodityCommentInfo();
                break;
            case R.id.comment_btn2:
                updatecommentbtntype(1);
                flag = 1;
                commodityCommentInfo();
                break;
            case R.id.comment_btn3:
                updatecommentbtntype(2);
                flag = 2;
                commodityCommentInfo();
                break;
            case R.id.comment_btn4:
                updatecommentbtntype(3);
                flag = 3;
                commodityCommentInfo();
                break;
        }
    }


    /**
     * 更多评价
     */
    @OnClick(R.id.goodsdetail_text_morecmment)
    public void gotoGoodsCommentActivity() {
        GoodsDetailCommentActivity.startactivity(this, commodityId, isPinTuan);
    }

    /**
     * 我要拼团
     */
    @OnClick(R.id.btn_assmlelist)
    public void btn_pintuan() {
//        AssmbleListActivity.startactivity(mContext, commodityId);
        GoodsDetailActivity.startactivity(mContext, true, commodityId, null);
        finish();
    }

    private TextView dialogTvNowPrice;
    private TextView dialogTvContent;
    private TextView dialogTvPrice;
    private ImageView dialogImage;
    private TextView et_count;


    private String attributeId;
    private String samePrice;
    private String goodsCount;

    //falese 选择后提交订单 true 选择规格
    private boolean isChooseAttribute = true;

    /**
     * 选择规格
     */
    @OnClick({R.id.layout_choice_goodssize})
    public void choicegoodsize() {
        if (choicegoodstypes != null) {
            //立即购买
            isChooseAttribute = true;
            choicegoodstypes.show();
        } else {
            ToastUtils.makeText("正在加载规格信息请稍后");
        }
    }

    private void initDialog() {
        GoodsBottomDialog.Builder builder = new GoodsBottomDialog.Builder(this);

        choicegoodstypes = builder.setbuttonclicklister(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Attribute> choiceDataOne = oneAdapter.getChoiceData();
                List<Attribute> choiceDataTwo = twoAdapter.getChoiceData();
                List<Attribute> choiceDataThree = threeAdapter.getChoiceData();

                if (choiceDataOne.size() == 0) {
                    ToastUtils.makeText("请选择颜色");
                    return;
                }
                if (choiceDataTwo.size() == 0) {
                    ToastUtils.makeText("请选择规格");
                    return;
                }
                if (choiceDataThree.size() == 0) {
                    ToastUtils.makeText("请选择材质");
                    return;
                }
                choicegoodstypes.dismiss();
                if (chooseAttribut == null){
                     ToastUtils.makeText("规格信息错误");
                    return;
                }
                attributeId =chooseAttribut.getAttributeId();
                samePrice = chooseAttribut.getSamePrice();
                goodsCount = et_count.getText().toString();

                tv_attribute.setVisibility(View.VISIBLE);
                tv_attribute.setText(dialogTvContent.getText());

                if (!isChooseAttribute) {
                    if (isPinTuan) {
                        if (isJoinTeam) {
                            joinTeamAndCreateOrder();
                        } else {
                            createTeam();
                        }
                    } else {
                        if (isAddCart) {
                            addCart();
                        } else {
                            confirmOrderForm();
                        }
                    }
                }

            }
        }).setcolorbaseadpater(oneAdapter).settypebaseadpater(twoAdapter).setThreeAdapter(threeAdapter).create();

        dialogTvNowPrice = builder.getTv_now_pirce();
        dialogTvContent = builder.getTvContent();
        dialogTvPrice = builder.getTvPrice();
        dialogImage = builder.getIv_image();
        TextView tv_reduce = builder.getTv_reduce();
        et_count = builder.getEt_count();
        TextView tv_add = builder.getTv_add();

        tv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = et_count.getText().toString();
                int count = Integer.valueOf(s);
                count--;
                if (count <= 0) {
                    return;
                }
                et_count.setText(count + "");

            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int stockNumCount = -1;
                if (!TextUtils.isEmpty(stockNum)) {
                    stockNumCount = Integer.valueOf(stockNum);
                }
                String s = et_count.getText().toString();
                int count = Integer.valueOf(s);
                count++;
                if (stockNumCount != -1) {
                    if (count > stockNumCount) {
                        ToastUtils.makeText("没有库存啦");
                        return;
                    }

                }
                et_count.setText(count + "");

            }
        });


        if (isPinTuan) {
            if (!TextUtils.isEmpty(commodityInfo.getTeamBuyPrice())) {
                dialogTvNowPrice.setText("¥" + commodityInfo.getTeamBuyPrice());
            }
        } else {
            if (!TextUtils.isEmpty(commodityInfo.getSamePrice())) {
                dialogTvNowPrice.setText("¥" + commodityInfo.getSamePrice());
            }
        }
        if (!TextUtils.isEmpty(commodityInfo.getSalePrice())) {
            dialogTvPrice.setText(commodityInfo.getSalePrice());
        }
    }


    boolean isFavorite;

    /***
     * 商品收藏
     */
    private void commodityCollection() {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("commodityId", commodityId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().commodityCollection(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<GoodsOrderData>(mContext) {
            @Override
            protected void _onNext(StatusCode<GoodsOrderData> stringStatusCode) {
                dismissLoadingDialog();

                isFavorite = !isFavorite;

                if (isFavorite) {
                    iv_collect.setImageDrawable(getResources().getDrawable(R.mipmap.already_collcet_icon));
                } else {
                    iv_collect.setImageDrawable(getResources().getDrawable(R.mipmap.goodsdetail_shoucang));
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    @OnClick({R.id.ll_collect})
    public void collect() {
        //收藏商品
        if (isFavorite) {
            ToastUtils.makeText("你已收藏该商品");
            return;
        }
        commodityCollection();
    }

    private boolean isAddCart = false;

    @OnClick({R.id.text_getgoods_btn})
    public void gotoGoodsOrder() {
        //单独购买
        if (isPinTuan) {
            //进入普通商品页面
            GoodsDetailActivity.startactivity(mContext, false, commodityId, null);
            finish();
        } else {
            //加入购物车
            addShoppingCart();
        }
    }

    @OnClick({R.id.ll_new_goods})
    public void lookNewGoods() {
        if (commodityInfo.getNewCommodity() != null) {
            GoodsDetailActivity.startactivity(mContext, false, commodityInfo.getNewCommodity().getCommodityId(), null);
            finish();
        }
    }


    @OnClick({R.id.iv_share})
    public void share() {

        List<String> commodityImgList = commodityInfo.getCommodityImgList();

        String imageUrl = "";
        if (commodityImgList != null && commodityImgList.size() > 0) {
            imageUrl = commodityImgList.get(0);
        }
        share(2, commodityInfo.getCommodityName(), commodityInfo.getCommodityId(), imageUrl);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
    }

    private void addShoppingCart() {
        if (!TextUtils.isEmpty(attributeId)) {
            addCart();
        } else {
            if (choicegoodstypes != null) {
                isChooseAttribute = false;
                isAddCart = true;
                choicegoodstypes.show();
            } else {
                ToastUtils.makeText("正在加载规格信息请稍后");
            }
        }
    }

    @OnClick({R.id.tv_open_team})
    public void openTeam() {

        if (isPinTuan) {
            if (isJoinTeam) {
                //加入团
                if (!TextUtils.isEmpty(attributeId)) {
                    joinTeamAndCreateOrder();
                } else {
                    if (choicegoodstypes != null) {
                        isChooseAttribute = false;
                        choicegoodstypes.show();
                    } else {
                        ToastUtils.makeText("正在加载规格信息请稍后");
                    }
                }
            } else {
                //我要开团
                if (!TextUtils.isEmpty(attributeId)) {
                    createTeam();
                } else {
                    if (choicegoodstypes != null) {
                        isChooseAttribute = false;
                        choicegoodstypes.show();
                    } else {
                        ToastUtils.makeText("正在加载规格信息请稍后");
                    }
                }
            }

        } else {

            if (!TextUtils.isEmpty(attributeId)) {
                confirmOrderForm();
            } else {
                if (choicegoodstypes != null) {
                    //立即购买
                    isChooseAttribute = false;
                    choicegoodstypes.show();
                } else {
                    ToastUtils.makeText("正在加载规格信息请稍后");
                }
            }


        }

    }


    private void updatecommentbtntype(int position) {

        if (commentbtnlist.size() == 0) {
            commentbtnlist.add(comment_btn1);
            commentbtnlist.add(comment_btn2);
            commentbtnlist.add(comment_btn3);
            commentbtnlist.add(comment_btn4);
        }

        for (int i = 0; i < commentbtnlist.size(); i++) {
            commentbtnlist.get(i).setBackgroundResource(R.drawable.goodsdetail_pinliun_unchoice);
            commentbtnlist.get(i).setTextColor(getResources().getColor(R.color.c_383838));
        }

        commentbtnlist.get(position).setBackgroundResource(R.drawable.goodsdetail_pinlun_choice);
        commentbtnlist.get(position).setTextColor(getResources().getColor(R.color.c_ffffff));

    }


    public static void startactivity(Context mContext, boolean isPinTuan, String commodityId, CommodityTeamBuyData commodityTeamBuyData) {
        Intent mIntent = new Intent(mContext, GoodsDetailActivity.class);
        mIntent.putExtra(Contans.INTENT_TYPE, isPinTuan);
        mIntent.putExtra(Contans.INTENT_DATA, commodityId);
        mIntent.putExtra(Contans.INTENT_TYPE_TWO, commodityTeamBuyData);
        mContext.startActivity(mIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        marqueeView.startFlipping();
        banner.startTurning(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        marqueeView.stopFlipping();
        banner.stopTurning();
    }


    private String stockNum;

    /**
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateAttribute(AttributeEvent event) {

        List<Attribute> choiceData = oneAdapter.getChoiceData();


        String s = et_count.getText().toString();

        int count = Integer.valueOf(s);

        int tag = event.getTag();
        int pos = -1;
        boolean isChosoe = false;

        if (tag == 0) {


            for (int i = 0; i < twoList.size(); i++) {
                Attribute attribute = twoList.get(i);

                if (tempOneList.contains(event.getAttribute().getAttributeColor() + attribute.getAttributeSize())) {
                    attribute.setChoos(true);
                    if (!isChosoe) {
                        isChosoe = true;
                        pos = i;
                    }
                } else {
                    attribute.setChoos(false);
                }
            }

            twoAdapter.setadlldataeType(false, pos);

            pos = -1;
            isChosoe = false;

            if (choiceData != null && choiceData.size() > 0) {
                Attribute attributeOne = choiceData.get(0);

                for (int i = 0; i < threeList.size(); i++) {
                    Attribute attribute = threeList.get(i);

                    if (tempList.contains(attributeOne.getAttributeColor() + event.getAttribute().getAttributeSize() + attribute.getAttributeQuality())) {
                        attribute.setChoos(true);
                        if (!isChosoe) {
                            isChosoe = true;
                            pos = i;
                        }
                    } else {
                        attribute.setChoos(false);
                    }
                }
            }

            threeAdapter.setadlldataeType(false, pos);
        } else if (tag == 1) {
            if (choiceData != null && choiceData.size() > 0) {
                Attribute attributeOne = choiceData.get(0);

                for (int i = 0; i < threeList.size(); i++) {
                    Attribute attribute = threeList.get(i);

                    if (tempList.contains(attributeOne.getAttributeColor() + event.getAttribute().getAttributeSize() + attribute.getAttributeQuality())) {
                        attribute.setChoos(true);
                        if (!isChosoe) {
                            isChosoe = true;
                            pos = i;
                        }
                    } else {
                        attribute.setChoos(false);
                    }
                }
            }


            threeAdapter.setadlldataeType(false, pos);
        }

        String content = "已选：";
        String choseAtttirbute = "";
        if (choiceData != null && choiceData.size() > 0) {
            Attribute attribute = choiceData.get(0);
            stockNum = attribute.getStockNum();
            String picUrl = attribute.getPicUrl();

            if (!TextUtils.isEmpty(picUrl)) {
                UIUtils.loadImageView(mContext, picUrl, dialogImage);
            }
            if (!TextUtils.isEmpty(attribute.getSamePrice())) {
                dialogTvNowPrice.setText("¥" + attribute.getSamePrice());
            }

            if (!TextUtils.isEmpty(attribute.getSalePrice())) {
                dialogTvPrice.setText(attribute.getSalePrice());
            }
            if (!TextUtils.isEmpty(attribute.getAttributeColor())) {
                choseAtttirbute += attribute.getAttributeColor();
                content += "\"" + attribute.getAttributeColor() + "\" ";
            }
        }


        List<Attribute> twoAdapterChoiceData = twoAdapter.getChoiceData();

        if (twoAdapterChoiceData != null && twoAdapterChoiceData.size() > 0) {

            Attribute attribute = twoAdapterChoiceData.get(0);
            stockNum = attribute.getStockNum();

            if (!TextUtils.isEmpty(attribute.getAttributeSize())) {
                choseAtttirbute += attribute.getAttributeSize();
                content += "\"" + attribute.getAttributeSize() + "\" ";
            }
        }

        List<Attribute> threeChoiceDatas = threeAdapter.getChoiceData();

        if (threeChoiceDatas != null && threeChoiceDatas.size() > 0) {
            Attribute attribute = threeChoiceDatas.get(0);
            if (!TextUtils.isEmpty(attribute.getAttributeQuality())) {
                choseAtttirbute += attribute.getAttributeQuality();
                content += "\"" + attribute.getAttributeQuality() + "\" ";
            }
        }
        for (Attribute attribute : list) {

            if ((attribute.getAttributeColor()+attribute.getAttributeSize()+attribute.getAttributeQuality()).equals(choseAtttirbute)){
                chooseAttribut = attribute;
            }

        }
        if (chooseAttribut != null){
            stockNum = chooseAttribut.getStockNum();

            String picUrl = chooseAttribut.getPicUrl();

            if (!TextUtils.isEmpty(picUrl)) {
                UIUtils.loadImageView(mContext, picUrl, dialogImage);
            }
            if (!TextUtils.isEmpty(chooseAttribut.getSamePrice())) {
                dialogTvNowPrice.setText("¥" + chooseAttribut.getSamePrice());
            }
            if (!TextUtils.isEmpty(chooseAttribut.getSalePrice())) {
                dialogTvPrice.setText(chooseAttribut.getSalePrice());
            }
        }

        content += s + "个";

        dialogTvContent.setText(content);
    }
    Attribute chooseAttribut;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateGoods(GoodsEvent goodsEvent) {
        if (goodsEvent != null) {
            //1单独购买2我要开团3加入购物车4立即购买
            switch (goodsEvent.getTag()) {
                case 1:
                    GoodsDetailActivity.startactivity(mContext, false, commodityId, null);
                    finish();
                    break;
                case 2:
                    //我要开团
                    if (!TextUtils.isEmpty(attributeId)) {
                        createTeam();
                    } else {
                        if (choicegoodstypes != null) {
                            isChooseAttribute = false;
                            choicegoodstypes.show();
                        } else {
                            ToastUtils.makeText("正在加载规格信息请稍后");
                        }
                    }
                    break;
                case 3:
                    addShoppingCart();
                    break;
                case 4:
                    if (!TextUtils.isEmpty(attributeId)) {
                        confirmOrderForm();
                    } else {
                        if (choicegoodstypes != null) {
                            //立即购买
                            isChooseAttribute = false;
                            choicegoodstypes.show();
                        } else {
                            ToastUtils.makeText("正在加载规格信息请稍后");
                        }
                    }
                    break;
            }

        }
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


}
