package com.oldwang.boxdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.AuthenticationIDCardActivity;
import com.oldwang.boxdemo.activity.GoodsDetailActivity;
import com.oldwang.boxdemo.activity.GoodsOrderActivity;
import com.oldwang.boxdemo.adpater.GoodCarsAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.CommodityData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.GoodsOrderData;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.event.CartCheckEvent;
import com.oldwang.boxdemo.event.CollectCheckEvent;
import com.oldwang.boxdemo.event.MainJumpEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class Goods_CarsFragmnet extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.choice_image)
    ImageView choice_image;


    @BindView(R.id.tv_total_price)
    TextView tv_total_price;


    @BindView(R.id.tv_go)
    TextView tv_go;


    @BindView(R.id.ll_no_goods)
    LinearLayout ll_no_goods;

    @BindView(R.id.ll_carts)
    LinearLayout ll_carts;


    private GoodCarsAdpater madpater;
    private boolean choicealldaa_type = false;

    List<CommodityData> listdata = new ArrayList<>();

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_goodscars);
    }

    private int myPostion = 0;

    @Override
    protected void initData() {
        super.initData();
        madpater = new GoodCarsAdpater(mContext,listdata);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(madpater);

        madpater.setListOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                CommodityData commodityData = listdata.get(position);
                String commodityId = commodityData.getCommodityId();
                String commodityNum = commodityData.getCommodityNum();
                String cartIds = commodityData.getCartId();
                String attributeId = commodityData.getAttributeId();
                Integer temp = Integer.valueOf(commodityNum);
                myPostion = position;

                switch (v.getId()){
                    //跳转到商品详情
                    case R.id.ll_item:
                        GoodsDetailActivity.startactivity(mContext,false, commodityId,null);
                        break;
                        //增加商品数量
                    case R.id.tv_add:
                        temp++;
                        updateCartInfo(cartIds,attributeId,commodityId,String.valueOf(temp),"0");
                        break;
                        //减少商品数量
                    case R.id.tv_reduce:
                        temp--;
                        if (temp < 1){
                            return;
                        }
                        updateCartInfo(cartIds,attributeId,commodityId,String.valueOf(temp),"0");
                        break;

                }
            }
        });

        cartInfo();
    }

    @OnClick(R.id.choice_layout)
    public void choiceclick(){
        choicealldaa_type = !choicealldaa_type;
        if(choicealldaa_type){
            choice_image.setImageResource(R.mipmap.addresslist_choice);
        }else{
            choice_image.setImageResource(R.mipmap.addresslist_unchoice);
        }
        madpater.settadlldataType(choicealldaa_type);
        setTotalPrice();
    }

    @OnClick(R.id.tv_go)
    public void goGoodsList(){
        EventBus.getDefault().post(new MainJumpEvent(1));
    }


    private List<CommodityData> listClickdata;
    @OnClick(R.id.layout_delete)
    public void deletedata(){

        final List<String> cartIds = new ArrayList<>();
        listClickdata = madpater.getListClickdata();
        for (CommodityData listClickdatum : listClickdata) {
            cartIds.add(listClickdatum.getCartId());
        }

        AlertView alertView = new AlertView("提示", "你确定删除购物车？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position==1){
                    batchDelCartInfo(cartIds);
                }

            }
        });
        alertView.show();

    }



    @OnClick(R.id.tv_settlement)
    public void settlement(){

        List<CommodityData> listClickdata = madpater.getListClickdata();
        if (listClickdata.size() < 1){
            ToastUtils.makeText("请选择需要结算的商品");
            return;
        }
        String content = "";
        for (CommodityData listClickdatum : listClickdata) {
            if (!TextUtils.isEmpty(listClickdatum.getShelfStatus()) && listClickdatum.getShelfStatus().equals("0")){
                content = listClickdatum.getCommodityName()+"为下架商品不能提交";
                break;
            }
        }
        if (!TextUtils.isEmpty(content)){
            ToastUtils.makeText(content);
            return;
        }



        confirmOrderForm(listClickdata);
    }



    /***
     * 购物车结算
     */
    private void confirmOrderForm( List<CommodityData> listClickdata) {

        JsonObject jsonObject = new JsonObject();

        JsonArray jsonArray = new JsonArray();

        for (CommodityData listClickdatum : listClickdata) {
            JsonObject commoditys = new JsonObject();
            commoditys.addProperty("attributeId", listClickdatum.getAttributeId());
            commoditys.addProperty("commodityId", listClickdatum.getCommodityId());
            commoditys.addProperty("commodityName", listClickdatum.getCommodityName());
            commoditys.addProperty("commodityNum", listClickdatum.getCommodityNum());
            commoditys.addProperty("commodityPrice", listClickdatum.getSamePrice());
            jsonArray.add(commoditys);
        }

        jsonObject.add("commoditys", jsonArray);


        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("isCart", 1);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().confirmOrderForm(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<GoodsOrderData>(mContext) {
            @Override
            protected void _onNext(StatusCode<GoodsOrderData> stringStatusCode) {
                if (stringStatusCode != null) {
                    GoodsOrderActivity.startactivity(mContext, stringStatusCode.getData(),false,1);
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    /***
     * 批量删除购物车
     */
    private void batchDelCartInfo(List<String> cartId) {

        JsonObject jsonObject = new JsonObject();

        JsonArray jsonArray = new JsonArray();

        String temp = "";
        for (String s : cartId) {
            jsonArray.add(s);
            temp += s+",";
        }

        jsonObject.addProperty("cartIds", temp);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().batchDelCartInfo(requestBean)
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
                    listdata.removeAll(listClickdata);
                    madpater.notifyDataSetChanged();
                    if (listdata.size() < 1){
                        ll_no_goods.setVisibility(View.VISIBLE);
                        ll_carts.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);


    }

    /***
     * 修改购物车数据
     * @param flag 状态标记 1删除，0未购买，2已购买
     */
    private void updateCartInfo(String cartId, String commodityAttributeId, String commodityId, final String commodityNum, final String flag) {

        JsonObject jsonObject = new JsonObject();

        JsonArray jsonArray = new JsonArray();

//        for (String s : cartId) {
//            jsonArray.add(s);
//        }

        jsonObject.addProperty("cartId", cartId);
        if (!TextUtils.isEmpty(commodityAttributeId)){
            jsonObject.addProperty("commodityAttributeId", commodityAttributeId);
        }

        if (!TextUtils.isEmpty(commodityId)){
            jsonObject.addProperty("commodityId", commodityId);
        }

        if (!TextUtils.isEmpty(commodityNum)){
            jsonObject.addProperty("commodityNum", commodityNum);
        }
        jsonObject.addProperty("flag", flag);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().updateCartInfo(requestBean)
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
                    if (flag.equals("0")){
                        CommodityData commodityData = listdata.get(myPostion);
                        commodityData.setCommodityNum(commodityNum);
                    }else {
                        listdata.remove(listClickdata);
                    }
                    madpater.notifyDataSetChanged();
                    setTotalPrice();
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);


    }


    /***
     *  购物车列表
     */
    private void cartInfo() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().cartInfo(requestBean)
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
                listdata.clear();
                if (stringStatusCode != null) {

                    ListData<CommodityData> commodityData = stringStatusCode.getData().getCommodityData();

                    if (commodityData != null){

                        List<CommodityData> list = commodityData.getList();
                        listdata.addAll(list);
                        madpater.settadlldataType(true);
                        setTotalPrice();


                    }
                }
                madpater.notifyDataSetChanged();
                if (listdata.size() > 0){

                    ll_no_goods.setVisibility(View.GONE);
                    ll_carts.setVisibility(View.VISIBLE);
                }else {
                    ll_no_goods.setVisibility(View.VISIBLE);
                    ll_carts.setVisibility(View.GONE);
                }

            }

            @Override
            protected void _onError(String message) {
                listdata.clear();
                if (listdata.size() > 0){
                    ll_no_goods.setVisibility(View.GONE);
                    ll_carts.setVisibility(View.VISIBLE);
                }else {
                    ll_no_goods.setVisibility(View.VISIBLE);
                    ll_carts.setVisibility(View.GONE);
                }
                madpater.notifyDataSetChanged();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    private void setTotalPrice() {

        BigDecimal total = new BigDecimal(BigInteger.ZERO);

        for (CommodityData commodityData : listdata) {

            if (commodityData.isCheck()){

                String samePrice = commodityData.getSamePrice();
                String commodityNum = commodityData.getCommodityNum();
                if (TextUtils.isEmpty(samePrice)){
                    samePrice = "0";
                }
                if (TextUtils.isEmpty(commodityNum)){
                    commodityNum = "1";
                }
                total =  total.add(new BigDecimal(samePrice).multiply(new BigDecimal(commodityNum)));
            }

        }
        total = total.setScale(2,BigDecimal.ROUND_HALF_UP);
        tv_total_price.setText(total.toString());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateListData(CartCheckEvent event){

        choicealldaa_type = event.isCheckAll();
        if(choicealldaa_type){
            choice_image.setImageResource(R.mipmap.addresslist_choice);
        }else{
            choice_image.setImageResource(R.mipmap.addresslist_unchoice);
        }

        setTotalPrice();
    }


    /**
     *
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(MainJumpEvent event){
        int postion = event.getPostion();
        if (postion == 2){
            cartInfo();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
