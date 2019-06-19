package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.TrainingBaseAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.VenueData;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateHistoryEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.view.YRecycleview;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/***
 *  首页场馆搜索结果
 */
public class HomeSearchResultFourActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    YRecycleview recylerview;

    @BindView(R.id.inpout_finddata)
    EditText inpout_finddata;

    private TrainingBaseAdpater madpater;

    private int page = 1 ;
    private final int size = 20;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况

    private ArrayList<VenueData> datas = new ArrayList<>();


    private double latitude;
    private double longitude;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    latitude = amapLocation.getLatitude();
                    longitude = amapLocation.getLongitude();
                    getData();
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
//            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。

        }
    };
    private String memberAccount;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_home_search_result);
    }

    private List<String> historyList = new ArrayList<>();

    @Override
    protected void initData() {
        super.initData();

        keyword = getIntent().getStringExtra(Contans.INTENT_DATA);

        memberAccount = BaseActivity.getUserInfo(mContext).getMemberAccount();
        if (TextUtils.isEmpty(memberAccount)) {
            memberAccount = "";
        }

        String temp = PreferencesUtils.getInstance().getString(Contans.HISTORY_FOUR_USER + memberAccount,"");
        if (!TextUtils.isEmpty(keyword)){
            inpout_finddata.setText(keyword);
            inpout_finddata.setSelection(keyword.length());
        }

        if (!TextUtils.isEmpty(temp)){
            historyList = new Gson().fromJson(temp,new TypeToken<List<String >>() {}.getType());
        }
        inpout_finddata.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = inpout_finddata.getText().toString().trim();
                    state = STATE_REFREH;
                    page = 1;
                    getData();
                }
                return false;
            }
        });

        getLocation();
    }


    public void getLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(mLocationOption);
        //给定位客户端对象设置定位参数
        mLocationOption.setOnceLocation(true);
        //启动定位
        mLocationClient.startLocation();
    }

    public static void startactivity(Context mContext,String keyword){
        Intent mIntent = new Intent(mContext,HomeSearchResultFourActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,keyword);
        mContext.startActivity(mIntent);
    }

    @OnClick({R.id.quxiao_btn,R.id.iv_clear})
    public void switchSearch(View view){
        switch (view.getId()){
            case R.id.quxiao_btn:
               finish();
                break;
            case R.id.iv_clear:
                inpout_finddata.setText("");
                break;
        }


    }



    @Override
    protected void initEvent() {
        super.initEvent();
        recylerview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
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
                    recylerview.setloadMoreComplete();
                }
            }
        });
    }



    private String keyword;

    private void getData() {
        showLoadingDialog();

        JsonObject jsonObject = new JsonObject();

        if (!TextUtils.isEmpty(keyword)){
            jsonObject.addProperty("keyword", keyword);
        }

        jsonObject.addProperty("latitude", latitude);
        jsonObject.addProperty("longitude", longitude);

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);



        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().getVenueList(requestBean)
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
                if (state != STATE_MORE){
                    datas.clear();
                }

                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    ListData<VenueData> venueData = data.getVenueData();
                    List<VenueData> list = venueData.getList();
                    datas.addAll(list);
                    total = data.getVenueData().getTotal();

                    //搜索成功存入本地
                    if (page == 1){
                        if (!TextUtils.isEmpty(keyword) ){
                            if (historyList.contains(keyword)){
                                historyList.remove(keyword);
                            }
                            historyList.add(0,keyword);
                            PreferencesUtils.getInstance().putString(Contans.HISTORY_FOUR_USER + memberAccount,new Gson().toJson(historyList));
                            EventBus.getDefault().post(new UpdateHistoryEvent());
                        }

                    }

                }
                showData();

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                if (state != STATE_MORE){
                    datas.clear();
                }
                dismissLoadingDialog();
                showData();
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

                madpater = new TrainingBaseAdpater(mContext,datas);
                recylerview.setLayoutManager(new LinearLayoutManager(this));
                recylerview.setItemAnimator(new DefaultItemAnimator());
                recylerview.setAdapter(madpater);

                madpater.setlistonclickLister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {

                        VenueData venueData = datas.get(position);

                        switch (v.getId()){
                            case R.id.ll_item:
                                BaseDetailActivity.startactivity(mContext,venueData.getVenueId());
                                break;
                            case R.id.tv_subscribe:
                                SubscribePlaceActivity.startactivity(mContext,venueData.getVenueId());

                                break;
                        }

                    }
                });
                break;
            case STATE_REFREH:
                madpater.notifyDataSetChanged();
                recylerview.scrollToPosition(0);
                recylerview.setReFreshComplete();
                break;
            case STATE_MORE:
                madpater.notifyDataSetChanged();
                recylerview.setloadMoreComplete();
                break;
        }

    }





}
