package com.oldwang.boxdemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.AssembleListQuanlianActivity;
import com.oldwang.boxdemo.activity.AuthenticationIDCardActivity;
import com.oldwang.boxdemo.activity.FindForQuanLianActivity;
import com.oldwang.boxdemo.activity.GoodsDetailActivity;
import com.oldwang.boxdemo.activity.HomeFindActivity;
import com.oldwang.boxdemo.adpater.QuanLianDataAdpater;
import com.oldwang.boxdemo.adpater.QuanLianListDataAdpater;
import com.oldwang.boxdemo.adpater.QuanLianTitleAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.BrandData;
import com.oldwang.boxdemo.bean.CommodityTeamBuyData;
import com.oldwang.boxdemo.bean.CommodityTypeData;
import com.oldwang.boxdemo.bean.DataInfo2;
import com.oldwang.boxdemo.bean.MemberInfo;
import com.oldwang.boxdemo.event.BrantEvent;
import com.oldwang.boxdemo.event.JumpTeachers;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.LogUntil;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.YRecycleview;

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

public class QuanLianFragmnet extends BaseFragment {
    @BindView(R.id.title_listdata)
    RecyclerView title_listdata;

    @BindView(R.id.quanlian_image_moredata)
    ImageView quanlian_image_moredata;

    @BindView(R.id.tv_goods_name)
    EditText tv_goods_name;


    @BindView(R.id.tite_goodsdata)
    RecyclerView tite_goodsdata;

    @BindView(R.id.recyclerview)
    YRecycleview recyclerview;

    private QuanLianTitleAdpater titledataadpater;
    private QuanLianDataAdpater tutlemoredataAdpater;
    private QuanLianListDataAdpater mydapater;

    private List<CommodityTypeData> commodityTypeDataListOne = new ArrayList<>();
    private List<CommodityTypeData> commodityTypeDataListTWo = new ArrayList<>();

    private List<CommodityTeamBuyData> datas = new ArrayList<>();


    private int page = 1 ;
    private final int size = 8;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况

    private Dialog mLoadingDialog;


    private String commodityTypePid;
    private String level;

    //商品名称
    private String commodityName;

    //是否是拼团
    private boolean isPingTuan = true;

    private MemberInfo userInfo;
    private boolean showtitlelayout;


    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_quanlian);
    }

    @Override
    protected void initData() {
        super.initData();
//        getUserInfo();
        state = STATE_NORMAL;

        //第一次获取1级分类全部数据
        commodityTypePid = "0";
        level  = "1";

        titledataadpater = new QuanLianTitleAdpater(mContext,commodityTypeDataListOne);
        LinearLayoutManager layutmager = new LinearLayoutManager(mContext);
        layutmager.setOrientation(LinearLayoutManager.HORIZONTAL);
        title_listdata.setLayoutManager(layutmager);
        title_listdata.setItemAnimator(new DefaultItemAnimator());
        title_listdata.setAdapter(titledataadpater);
        titledataadpater.setListonClickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                commodityTypePid = commodityTypeDataListOne.get(position).getCommodityTypeId();
                level = "2";
                if (!commodityTypePid.equals("0")){
                    commodityTypeList();
                    isPingTuan = false;
                }else {
                    isPingTuan = true;
                    tite_goodsdata.setVisibility(View.GONE);
                    quanlian_image_moredata.setImageResource(R.mipmap.user_image_jinatou);
                }
                state = STATE_NORMAL;

                getData();
//                if( commodityTypeDataListOne.get(position).getCommodityTypeName().equals("拼团商品")){
//                    AssembleListQuanlianActivity.startactivity(mContext);
//                }else {
//                }
            }
        });

        tutlemoredataAdpater = new QuanLianDataAdpater(mContext,commodityTypeDataListTWo);
        LinearLayoutManager layutmager2 = new LinearLayoutManager(mContext);
        layutmager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        tite_goodsdata.setLayoutManager(layutmager2);
        tite_goodsdata.setItemAnimator(new DefaultItemAnimator());
        tite_goodsdata.setAdapter(tutlemoredataAdpater);

        tutlemoredataAdpater.SetListClickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
//                commodityTypePid = commodityTypeDataListOne.get(position).getCommodityTypeId();
//                state = STATE_NORMAL;
//                getData();
                AssembleListQuanlianActivity.startactivity(mContext,1,commodityTypeDataListTWo.get(position).getCommodityTypeId(),commodityTypeDataListTWo.get(position).getCommodityTypeName());

            }
        });

        commodityTypeList();
        getData();
//        tv_goods_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HomeFindActivity.startactivity(mContext);
//            }
//        });
        tv_goods_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String trim = tv_goods_name.getText().toString().trim();

                if (!TextUtils.isEmpty(trim)){
                    commodityName = trim;
                }else {
                    commodityName = "";
                }
                state = STATE_NORMAL;
                isShowDialog = false;
                getData();
            }
        });

    }

    private boolean isShowDialog = true;


    @OnClick(R.id.quanlian_image_moredata)
    public void showtitllayout(){
        if(!showtitlelayout){
            tite_goodsdata.setVisibility(View.VISIBLE);
            quanlian_image_moredata.setImageResource(R.mipmap.quanlian_xia_jiantou);
        }else{
            tite_goodsdata.setVisibility(View.GONE);
            quanlian_image_moredata.setImageResource(R.mipmap.user_image_jinatou);
        }
        showtitlelayout = !showtitlelayout;
    }


    @OnClick(R.id.quanlian_findimage)
    public void gotoFindQuanliandata(){
        FindForQuanLianActivity.startactivity(mContext);
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().getUerInfo(requestBean)
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    userInfo = data.getMemberInfo();
                }

            }

            @Override
            protected void _onError(String message) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }


    /**
     * 获取分类列表
     */
    private void commodityTypeList() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commodityTypePid", commodityTypePid);
        jsonObject.addProperty("level", level);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(getContext(), jsonObject);
        Observable observable =
                ApiUtils.getApi().commodityTypeList(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(getContext()) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    List<CommodityTypeData> commodityTypeData = data.getCommodityTypeData();
                    if (level.equals("1")){
                        CommodityTypeData commodityTypeDat = new CommodityTypeData();
                        commodityTypeDat.setCommodityTypeId("0");
                        commodityTypeDat.setCommodityTypeName("拼团商品");
                        commodityTypeDataListOne.add(commodityTypeDat);
                        if (commodityTypeData != null){
                            commodityTypeDataListOne.addAll(commodityTypeData);
                        }
                        titledataadpater.setAlldataFalse(true);
                        titledataadpater.notifyDataSetChanged();
                    }else {
                        commodityTypeDataListTWo.clear();
                        if (commodityTypeData != null){
                            commodityTypeDataListTWo.addAll(commodityTypeData);
                        }
                        tite_goodsdata.setVisibility(View.GONE);
//                        if (commodityTypeDataListTWo.size() > 0){
//                            tite_goodsdata.setVisibility(View.VISIBLE);
//                            showtitlelayout = true;
//                            quanlian_image_moredata.setImageResource(R.mipmap.quanlian_xia_jiantou);
//                        }else {
//                            showtitlelayout = false;
//                            tite_goodsdata.setVisibility(View.GONE);
//                            quanlian_image_moredata.setImageResource(R.mipmap.user_image_jinatou);
//                        }
                        tutlemoredataAdpater.setAlldataFalse(true);
                        tutlemoredataAdpater.notifyDataSetChanged();

                    }

                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);
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

    private BrandData brandData;

    /**
     * 获取拼团商品数据
     */
    private void getData() {

        if (isPingTuan){
            JsonObject jsonObject = new JsonObject();
            if (!TextUtils.isEmpty(commodityName)){
                jsonObject.addProperty("commodityName", commodityName);
            }
            if (brandData != null){
                jsonObject.addProperty("brandName", brandData.getBrandName());
            }

            jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
            jsonObject.addProperty("pageNum", page);
            jsonObject.addProperty("pageSize", size);
            RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
            Observable observable =
                    ApiUtils.getApi().teamBuyList(requestBean)
                            .compose(RxHelper.getObservaleTransformer())
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {

                                    if (isShowDialog){
                                        if (mLoadingDialog == null) {
                                            mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                        }
                                        LoadingDialogUtils.show(mLoadingDialog);
                                    }


                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread());

            HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(mContext) {
                @Override
                protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                    isShowDialog = true;

                    if (state != STATE_MORE){
                        datas.clear();
                    }

                    if (stringStatusCode != null) {
                        DataInfo data = stringStatusCode.getData();
                        if (data != null && data.getCommodityTeamBuyData() != null && data.getCommodityTeamBuyData().getList() != null){
                            datas.addAll(data.getCommodityTeamBuyData().getList());
                            total = data.getCommodityTeamBuyData().getTotal();

                        }
                    }
                    showData();
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.makeText(message);
                    isShowDialog = true;
                    if (state != STATE_MORE){
                        datas.clear();
                    }
                    showData();
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                }
            }, "", lifecycleSubject, false, true);
        }else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("commodityTypeId", commodityTypePid);

            if (brandData != null){
                jsonObject.addProperty("brandName", brandData.getBrandName());
            }

            if (!TextUtils.isEmpty(commodityName)){
                jsonObject.addProperty("keyword", commodityName);
            }
            jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
            jsonObject.addProperty("pageNum", page);
            jsonObject.addProperty("pageSize", size);
            RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
            Observable observable =
                    ApiUtils.getApi().searchCommodity(requestBean)
                            .compose(RxHelper.getObservaleTransformer())
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {
                                    if (isShowDialog){
                                        if (mLoadingDialog == null) {
                                            mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                        }
                                        LoadingDialogUtils.show(mLoadingDialog);
                                    }

                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread());

            HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo2>(mContext) {
                @Override
                protected void _onNext(StatusCode<DataInfo2> stringStatusCode) {
                    isShowDialog = true;

                    if (state != STATE_MORE){
                        datas.clear();
                    }

                    if (stringStatusCode != null) {
                        DataInfo2 data = stringStatusCode.getData();
                        if (data != null && data.getCommodityTeamBuyData() != null && data.getCommodityTeamBuyData().getList() != null){
                            datas.addAll(data.getCommodityTeamBuyData().getList());
                            total = data.getCommodityTeamBuyData().getTotal();

                        }
                    }
                    showData();
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                }

                @Override
                protected void _onError(String message) {
                    isShowDialog = true;
                    ToastUtils.makeText(message);
                    if (state != STATE_MORE){
                        datas.clear();
                    }
                    showData();
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                }
            }, "", lifecycleSubject, false, true);
        }



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
                mydapater = new QuanLianListDataAdpater(mContext,datas);
                mydapater.setPingTuan(isPingTuan);
                mydapater.setListonClickLister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {
                        String commodityId = datas.get(position).getCommodityId();
                        GoodsDetailActivity.startactivity(mContext,isPingTuan,commodityId, null);
                    }
                });
                recyclerview.setLayoutManager(new GridLayoutManager(mContext,2));
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.setAdapter(mydapater);
                break;
            case STATE_REFREH:
                mydapater.setPingTuan(isPingTuan);
                mydapater.notifyDataSetChanged();
                recyclerview.scrollToPosition(0);
                recyclerview.setReFreshComplete();
                break;
            case STATE_MORE:
                mydapater.setPingTuan(isPingTuan);
                mydapater.notifyDataSetChanged();
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
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBrantData(BrantEvent event) {
        brandData = event.getBrandData();
        getData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

}
