package com.oldwang.boxdemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.ArtsSchoolDetailActivity;
import com.oldwang.boxdemo.activity.BaseDetailActivity;
import com.oldwang.boxdemo.adpater.OnthespotRecordAdpater2;
import com.oldwang.boxdemo.adpater.OnthespotRecordAdpater3;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.SchoolData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.VenueData;
import com.oldwang.boxdemo.event.UpdateMessageEvent;
import com.oldwang.boxdemo.event.UpdateSchoolEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.YRecycleview;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class OnthespotRecordFragmnet2 extends BaseFragment {
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;

    @BindView(R.id.name)
    TextView Other_view2_name;


    @BindView(R.id.recyclerview)
    YRecycleview recyclerview;


    @BindView(R.id.recyclerview2)
    YRecycleview recyclerview2;


    private OnthespotRecordAdpater2  madpater;
    private OnthespotRecordAdpater3  madpater2;


    @BindView(R.id.ll_count)
    LinearLayout ll_count;


    private int page = 1 ;
    private final int size = 20;
    private int total;

    private int page1 = 1 ;
    private int total1;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况
    private int state1 = STATE_NORMAL;       //正常情况


    //场馆数据
    private ArrayList<VenueData> datas = new ArrayList<>();

    private ArrayList<SchoolData> datas2 = new ArrayList<>();

    private Dialog mLoadingDialog;

    //0场馆 1 学校
    private int postion = 0;

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_item_home_other2);
    }

    @Override
    protected void initData() {
        super.initData();
        ll_count.setVisibility(View.GONE);
        state = STATE_NORMAL;
        state1 = STATE_NORMAL;
    }

    private boolean isFirst = true;
    @OnClick({R.id.text1,R.id.text2})
    public void titleclick(View v){
        switch (v.getId()){
            case R.id.text1:
                postion = 0;
                Other_view2_name.setText("场馆名");
                ll_count.setVisibility(View.GONE);
                recyclerview.setVisibility(View.VISIBLE);
                recyclerview2.setVisibility(View.GONE);
                text1.setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
                text2.setTextColor(mContext.getResources().getColor(R.color.c_525259));
                break;

            case R.id.text2:
                Other_view2_name.setText("学校名");
                postion = 1;
                ll_count.setVisibility(View.VISIBLE);
                recyclerview2.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.GONE);
                text2.setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
                text1.setTextColor(mContext.getResources().getColor(R.color.c_525259));
                if (isFirst){
                    isFirst = false;
                    getData();
                }
                break;
        }
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
                if (postion == 0){
                    int totalPaeg = total / size + (total % size == 0 ? 0:1);
                    if (page < totalPaeg) {
                        loadMoreData();
                    } else {
                        recyclerview.setloadMoreComplete();
                    }
                }else {
                    int totalPaeg = total1 / size + (total1 % size == 0 ? 0:1);
                    if (page1 < totalPaeg) {
                        loadMoreData();
                    } else {
                        recyclerview2.setloadMoreComplete();
                    }
                }

            }
        });

        recyclerview2.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }

            @Override
            public void onLoadMore() {
                if (postion == 0){
                    int totalPaeg = total / size + (total % size == 0 ? 0:1);
                    if (page < totalPaeg) {
                        loadMoreData();
                    } else {
                        recyclerview.setloadMoreComplete();
                    }
                }else {
                    int totalPaeg = total1 / size + (total1 % size == 0 ? 0:1);
                    if (page1 <totalPaeg) {
                        loadMoreData();
                    } else {
                        recyclerview2.setloadMoreComplete();
                    }
                }

            }
        });

        getData();
    }


    //城市ID
    private String cityId = "";

    //区域ID
    private String districtId = "";

    //省ID
    private String provinceId = "";

    //乡镇ID
    private String townshipId = "";

    //场馆类别ID
    private String typeId = "";

    /**
     */
    private void getData() {

        if (postion == 0){
            JsonObject jsonObject = new JsonObject();
            if (!TextUtils.isEmpty(cityId)){
                jsonObject.addProperty("cityId", cityId);
            }
            if (!TextUtils.isEmpty(districtId)){
                jsonObject.addProperty("districtId", districtId);
            }

            jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
            jsonObject.addProperty("pageNum", page);
            jsonObject.addProperty("pageSize", size);

            if (!TextUtils.isEmpty(provinceId)){
                jsonObject.addProperty("provinceId", provinceId);
            }
            if (!TextUtils.isEmpty(townshipId)){
                jsonObject.addProperty("townshipId", townshipId);
            }
            if (!TextUtils.isEmpty(typeId)){
                jsonObject.addProperty("typeId", typeId);
            }


            RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
            Observable observable =
                    ApiUtils.getApi().venueList(requestBean)
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

                    if (state != STATE_MORE){
                        datas.clear();
                    }

                    if (stringStatusCode != null) {
                        DataInfo data = stringStatusCode.getData();

                        if (data != null){

                            ListData<VenueData> venueData = data.getVenueData();

                            if ( venueData != null && venueData.getList() != null){
                                datas.addAll(venueData.getList());
                                total = venueData.getTotal();
                            }



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
        }else {
            JsonObject jsonObject = new JsonObject();
            if (!TextUtils.isEmpty(cityId)){
                jsonObject.addProperty("cityId", cityId);
            }
            if (!TextUtils.isEmpty(districtId)){
                jsonObject.addProperty("districtId", districtId);
            }

            jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
            jsonObject.addProperty("pageNum", page);
            jsonObject.addProperty("pageSize", size);

            if (!TextUtils.isEmpty(provinceId)){
                jsonObject.addProperty("provinceId", provinceId);
            }
            if (!TextUtils.isEmpty(townshipId)){
                jsonObject.addProperty("townshipId", townshipId);
            }
            if (!TextUtils.isEmpty(typeId)){
                jsonObject.addProperty("typeId", typeId);
            }


            RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
            Observable observable =
                    ApiUtils.getApi().schoolList(requestBean)
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

                    if (state1 != STATE_MORE){
                        datas2.clear();
                    }

                    if (stringStatusCode != null) {
                        DataInfo data = stringStatusCode.getData();

                        if (data != null){

                            ListData<SchoolData> venueData = data.getSchoolData();

                            if ( venueData != null && venueData.getList() != null){
                                datas2.addAll(venueData.getList());
                                total1 = venueData.getTotal();
                            }



                        }
                    }
                    showData1();
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.makeText(message);
                    if (state1 != STATE_MORE){
                        datas2.clear();
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

        if (postion == 0){
            state = STATE_MORE;
            page = ++page;
        }else {
            state1 = STATE_MORE;
            page1 = ++page1;
        }

        getData();
    }

    /**
     * 刷新
     */
    private void refreshData() {

        if (postion == 0){
            state = STATE_REFREH;
            page = 1;
        }else {
            state1 = STATE_REFREH;
            page1 = 1;
        }

        getData();
    }

    /**
     * 展示数据
     */
    private void showData() {
        switch (state) {
            case STATE_NORMAL:
                madpater = new OnthespotRecordAdpater2(mContext,datas);
                recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.setAdapter(madpater);
                madpater.setlistonclicklister(new ListOnclickLister() {

                    @Override
                    public void onclick(View v, int position) {
                        BaseDetailActivity.startactivity(mContext,datas.get(position).getVenueId());
                    }
                });
                break;
            case STATE_REFREH:
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
     * 展示数据
     */
    private void showData1() {
        switch (state1) {
            case STATE_NORMAL:
                madpater2 = new OnthespotRecordAdpater3(mContext,datas2);
                recyclerview2.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerview2.setItemAnimator(new DefaultItemAnimator());
                recyclerview2.setAdapter(madpater2);
                madpater2.setlistonclicklister(new ListOnclickLister() {

                    @Override
                    public void onclick(View v, int position) {
                        ArtsSchoolDetailActivity.startactivity(mContext,datas2.get(position).getNewsBoxingSchoolId(), false);
                    }
                });
                break;
            case STATE_REFREH:
                madpater2.notifyDataSetChanged();
                recyclerview2.scrollToPosition(0);
                recyclerview2.setReFreshComplete();
                break;
            case STATE_MORE:
                madpater2.notifyDataSetChanged();
                recyclerview2.setloadMoreComplete();
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
    public void updateSchool(UpdateSchoolEvent event) {
        refreshData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(UpdateMessageEvent event) {
        if (event != null){
            if (event.getPostion() == 1){
                typeId = "";
                provinceId = "";
                cityId = "";
                districtId = "";
                townshipId = "";

                typeId = event.getTypId();
                int tag = event.getTag();

                switch (tag){
                    case 0:
                        provinceId = event.getAreaId();
                        break;
                    case 1:
                        cityId = event.getAreaId();
                        break;
                    case 2:
                        districtId = event.getAreaId();
                        break;
                    case 3:
                        townshipId = event.getAreaId();
                        break;
                }


                refreshData();
            }
        }
    }

}
