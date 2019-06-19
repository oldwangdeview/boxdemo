package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.AreaAdapter;
import com.oldwang.boxdemo.adpater.ChooseAreaAdapter;
import com.oldwang.boxdemo.adpater.TrainingBaseAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RegionData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.TypeData;
import com.oldwang.boxdemo.bean.VenueData;
import com.oldwang.boxdemo.event.UpdateAddress;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.KeybordS;
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

/***
 * 训练基地列表
 */
public class TrainingBaseActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    YRecycleview recylerview;
    @BindView(R.id.choice_path)
    TextView choice_path;

    @BindView(R.id.ll_top)
    LinearLayout ll_top;

    @BindView(R.id.inpout_finddata)
    EditText inpout_finddata;

    @BindView(R.id.rl_title)
    RelativeLayout rl_title;

    @BindView(R.id.ll_search)
    LinearLayout ll_search;


    private TrainingBaseAdpater madpater;
    private ChooseAreaAdapter areaAdapter;



    private int page = 1 ;
    private final int size = 20;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况

    private ArrayList<VenueData> datas = new ArrayList<>();

    private List<RegionData> disantcList = new ArrayList<>();
    private String[] distancData = new String[]{"0-500m","500m-1km","1km-3km","3km-10km","10km以上"};

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
    private List<RegionData> regionData = new ArrayList<>();


    @Override
    protected void initView() {
        setContentView(R.layout.activity_trainingbase);
    }

    private int tag = 0;
    private boolean isShowDialog = true;

    @Override
    protected void initData() {
        super.initData();

        for (int i = 0; i < distancData.length; i++) {


            //共用这个实体类
            RegionData regionData = new RegionData();
            regionData.setRegionName(distancData[i]);

            if (i == 0){
                regionData.setRegionId("500");
            }else if (i == 1){
                regionData.setRegionId("1000");
            }else if (i == 2){
                regionData.setRegionId("3000");
            }else if (i == 3){
                regionData.setRegionId("10000");
            }else if (i == 4){
                regionData.setRegionId("10001");
            }
            disantcList.add(regionData);
        }


        inpout_finddata.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                keyword = inpout_finddata.getText().toString().trim();
                isShowDialog = false;
                getData();
            }
        });

        initOnePopupWindow(ll_top);
        types();
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

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,TrainingBaseActivity.class);
        mContext.startActivity(mIntent);
    }

    @OnClick({R.id.quxiao_btn,R.id.find_image,R.id.iv_clear,R.id.iv_back_activity_basepersoninfo})
    public void switchSearch(View view){
        switch (view.getId()){
            case R.id.quxiao_btn:
                rl_title.setVisibility(View.VISIBLE);
                ll_search.setVisibility(View.GONE);
                inpout_finddata.setText("");
                KeybordS.closeKeybord(inpout_finddata,mContext);
                break;
            case R.id.find_image:
                rl_title.setVisibility(View.GONE);
                ll_search.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_clear:
                inpout_finddata.setText("");
                break;
                case R.id.iv_back_activity_basepersoninfo:
                    finish();
                    break;
        }


    }

    private List<TypeData> typeDataList = new ArrayList<>();
    private List<RegionData> typeTempDataList = new ArrayList<>();


    /**
     * 选择馆校/战将名师类型
     */
    private void types() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().types(requestBean)
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
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    ListData<TypeData> typeData = data.getTypeData();
                    //列表数据
                    typeDataList = typeData.getList();

                    for (TypeData temp : typeDataList) {
                        //共用这个实体类
                        RegionData regionDataOne = new RegionData();
                        regionDataOne.setRegionName(temp.getTypeName());
                        regionDataOne.setRegionId(temp.getTypeId());
                        typeTempDataList.add(regionDataOne);
                    }
                }

            }
            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);
    }



    @OnClick({R.id.choice_path,R.id.choose_distance,R.id.choose_type})
    public void choose(View view){
        regionData.clear();
        rl_top.setVisibility(View.GONE);

        switch (view.getId()){
            case R.id.choice_path:
                rl_top.setVisibility(View.VISIBLE);
                type = 0;
                getAreaData();
                break;
            case R.id.choose_distance:
                type = 1;
                regionData.addAll(disantcList);
                if (!TextUtils.isEmpty(distance)){
                    areaAdapter.setRegionId(distance);
                }
                areaAdapter.notifyDataSetChanged();
                break;
            case R.id.choose_type:
                type = 2;
                regionData.addAll(typeTempDataList);
                if (!TextUtils.isEmpty(venueTypeId)){
                    areaAdapter.setRegionId(venueTypeId);
                }
                areaAdapter.notifyDataSetChanged();

                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //对他进行便宜
            mPopupWindowOne.showAsDropDown(ll_top, -10, 50, Gravity.BOTTOM);
        }
        Window window = getWindow();
        ColorDrawable cd = new ColorDrawable(0x000000);
        window.setBackgroundDrawable(cd);
        // 产生背景变暗效果，设置透明度
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.4f;

        //之前不写这一句也是可以实现的，这次突然没效果了。网搜之后加上了这一句就好了。据说是因为popUpWindow没有getWindow()方法。
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);

        //对popupWindow进行显示
        mPopupWindowOne.update();


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



    private String distance;
    private String keyword;
    private String regionId;
    private String venueTypeId;

    /**
     */
    private void getData() {

        //搜索不显示dialog
        if (isShowDialog){
            showLoadingDialog();
        }
        isShowDialog = true;
        JsonObject jsonObject = new JsonObject();

        if (!TextUtils.isEmpty(distance)){
            jsonObject.addProperty("distance", distance);
        }
        if (!TextUtils.isEmpty(keyword)){
            jsonObject.addProperty("keyword", keyword);
        }

        jsonObject.addProperty("latitude", latitude);
        jsonObject.addProperty("longitude", longitude);

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        if (!TextUtils.isEmpty(regionId)){
            jsonObject.addProperty("regionId", regionId);
        }
        if (!TextUtils.isEmpty(venueTypeId)){
            jsonObject.addProperty("venueTypeId", venueTypeId);
        }



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
    private PopupWindow mPopupWindowOne;
    private RecyclerView areaRecyclerView;
    private TextView tv_one;
    private TextView tv_two;
    private TextView tv_three;
    private TextView tv_four;

    private View line_one;
    private View line_two;
    private View line_three;
    private View line_four;

    private LinearLayout rl_top;


    private String oneRregiond = "";
    private String twoRregiond = "";
    private String threeRregiond = "";
    private String fourRregiond = "";

    //0 区域 1 距离 2 类型
    private int type = 0;

    private void initOnePopupWindow(View view) {
        //加载布局
        if(mPopupWindowOne ==null) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.trainimg_pop, null);
            //更改背景颜色
            inflate.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            mPopupWindowOne = new PopupWindow(inflate);
            mPopupWindowOne.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            mPopupWindowOne.setHeight(UIUtils.dip2px(325));

            rl_top = inflate.findViewById(R.id.ll_top);

            tv_one = inflate.findViewById(R.id.tv_one);
             tv_two = inflate.findViewById(R.id.tv_two);
             tv_three = inflate.findViewById(R.id.tv_three);
             tv_four = inflate.findViewById(R.id.tv_four);

             line_one = inflate.findViewById(R.id.line_one);
             line_two = inflate.findViewById(R.id.line_two);
             line_three = inflate.findViewById(R.id.line_three);
             line_four = inflate.findViewById(R.id.line_four);


            tv_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (type == 0){
                        tag = 0;
                        regionId = "";
                        getAreaData();
                        tv_one.setTextColor(getResources().getColor(R.color.c_FFD52E21));
                        tv_one.setVisibility(View.VISIBLE);
                        line_one.setVisibility(View.VISIBLE);

                        tv_two.setVisibility(View.GONE);
                        tv_three.setVisibility(View.GONE);
                        tv_four.setVisibility(View.GONE);

                        line_two.setVisibility(View.GONE);
                        line_three.setVisibility(View.GONE);
                        line_four.setVisibility(View.GONE);
                    }



                }
            });
            tv_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type == 0){
                        regionId = oneRregiond;
                        tag = 1;
                        getAreaData();
                        tv_two.setTextColor(getResources().getColor(R.color.c_FFD52E21));
                        tv_two.setVisibility(View.VISIBLE);
                        tv_three.setVisibility(View.VISIBLE);

                        tv_three.setVisibility(View.GONE);
                        tv_four.setVisibility(View.GONE);

                        line_one.setVisibility(View.GONE);
                        line_three.setVisibility(View.GONE);
                        line_four.setVisibility(View.GONE);

                        tv_one.setTextColor(getResources().getColor(R.color.c_FF595959));
                        tv_three.setTextColor(getResources().getColor(R.color.c_FF595959));
                        tv_four.setTextColor(getResources().getColor(R.color.c_FF595959));
                    }

                }
            });

            tv_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type == 0) {

                        regionId = twoRregiond;
                        tag = 2;
                        getAreaData();
                        tv_three.setTextColor(getResources().getColor(R.color.c_FFD52E21));
                        tv_three.setVisibility(View.VISIBLE);
                        tv_four.setVisibility(View.VISIBLE);

                        line_one.setVisibility(View.GONE);
                        line_two.setVisibility(View.GONE);
                        line_four.setVisibility(View.GONE);

                        tv_four.setVisibility(View.GONE);

                        tv_one.setTextColor(getResources().getColor(R.color.c_FF595959));
                        tv_two.setTextColor(getResources().getColor(R.color.c_FF595959));
                        tv_four.setTextColor(getResources().getColor(R.color.c_FF595959));
                    }
                }
            });


            tv_four.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type == 0) {

                        regionId = threeRregiond;
                        tag = 3;
                        getAreaData();
                        tv_four.setTextColor(getResources().getColor(R.color.c_FFD52E21));
                        tv_four.setVisibility(View.VISIBLE);
                        line_four.setVisibility(View.VISIBLE);

                        line_one.setVisibility(View.GONE);
                        line_two.setVisibility(View.GONE);
                        line_three.setVisibility(View.GONE);

                        tv_one.setTextColor(getResources().getColor(R.color.c_FF595959));
                        tv_two.setTextColor(getResources().getColor(R.color.c_FF595959));
                        tv_three.setTextColor(getResources().getColor(R.color.c_FF595959));
                    }
                }
            });

             areaRecyclerView = inflate.findViewById(R.id.recyclerview);

            areaAdapter = new ChooseAreaAdapter(mContext,regionData);
            areaRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            areaRecyclerView.setItemAnimator(new DefaultItemAnimator());
            areaRecyclerView.setAdapter(areaAdapter);


            areaAdapter.setListClickLister(new ListOnclickLister() {
                @Override
                public void onclick(View v, int position) {
                    RegionData regionData1 = regionData.get(position);

                    if (type == 0) {
//                        venueTypeId = "";
//                        distance = "";
                        regionId = regionData1.getAreaId();
                        if (TextUtils.isEmpty(regionId)){
                            regionId = regionData1.getRegionId();
                        }
                        areaAdapter.setRegionId(regionId);
                        switch (tag) {
                            case 0:
                                oneRregiond = regionId;
                                tv_one.setText(regionData1.getFullname());
                                tv_one.setTextColor(getResources().getColor(R.color.c_FF595959));
                                tv_two.setText("请选择");
                                tv_two.setVisibility(View.VISIBLE);
                                line_two.setVisibility(View.VISIBLE);
                                tv_two.setTextColor(getResources().getColor(R.color.c_FFD52E21));
                                line_one.setVisibility(View.GONE);
                                break;

                            case 1:


                                fourRregiond = regionId;
                                tv_four.setText(regionData1.getRegionName());
                                mPopupWindowOne.dismiss();

//                                twoRregiond = regionId;
//                                tv_two.setText(regionData1.getRegionName());
//                                tv_two.setVisibility(View.VISIBLE);
//                                tv_two.setTextColor(getResources().getColor(R.color.c_FF595959));
//                                line_two.setVisibility(View.GONE);
//
//                                tv_three.setText("请选择");
//                                tv_three.setVisibility(View.VISIBLE);
//                                line_three.setVisibility(View.VISIBLE);
//                                tv_three.setTextColor(getResources().getColor(R.color.c_FFD52E21));
                                break;
                            case 2:
                                threeRregiond = regionId;
                                tv_three.setText(regionData1.getRegionName());
                                tv_three.setVisibility(View.VISIBLE);
                                tv_three.setTextColor(getResources().getColor(R.color.c_FF595959));
                                line_three.setVisibility(View.GONE);

                                tv_four.setText("请选择");
                                tv_four.setVisibility(View.VISIBLE);
                                line_four.setVisibility(View.VISIBLE);
                                tv_four.setTextColor(getResources().getColor(R.color.c_FFD52E21));
                                break;

                            case 3:
                                fourRregiond = regionId;
                                tv_four.setText(regionData1.getRegionName());
                                mPopupWindowOne.dismiss();
                                break;
                        }
                        tag++;
                        getAreaData();
                    }else if (type == 1){
                        distance = regionData1.getAreaId();
//                        venueTypeId = "";
//                        regionId = "";
                        if (TextUtils.isEmpty(distance)){
                            distance = regionData1.getRegionId();
                        }
                        areaAdapter.setRegionId(distance);
                        mPopupWindowOne.dismiss();
                    }else if (type == 2){
//                        distance = "";
//                        regionId = "";
                        venueTypeId = regionData1.getAreaId();
                        if (TextUtils.isEmpty(venueTypeId)){
                            venueTypeId = regionData1.getRegionId();
                        }
                        areaAdapter.setRegionId(venueTypeId);
                        mPopupWindowOne.dismiss();
                    }

                }
            });

            //点击其他地方隐藏,false为无反应
            mPopupWindowOne.setFocusable(true);
        }


        mPopupWindowOne.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha=1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
                if (!TextUtils.isEmpty(regionId) || !TextUtils.isEmpty(venueTypeId) || !TextUtils.isEmpty(distance)){
                    getData();
                }

            }
        });

    }





    /**
     * 区域列表
     */
    private void getAreaData() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        if (!TextUtils.isEmpty(regionId)) {
            if (tag == 4 && !TextUtils.isEmpty(threeRregiond)){
                jsonObject.addProperty("regionId", threeRregiond);
            }else {
                jsonObject.addProperty("regionId", regionId);
            }
        }
        jsonObject.addProperty("tag", tag);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().regionList(requestBean)
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
                    regionData.clear();
                    regionData.addAll(data.getRegionData());
                    switch (tag){
                        case 0:
                            areaAdapter.setRegionId(oneRregiond);
                            break;
                        case 1:
                            areaAdapter.setRegionId(twoRregiond);
                            break;
                        case 2:
                            areaAdapter.setRegionId(threeRregiond);
                            break;
                        case 3:
                            areaAdapter.setRegionId(fourRregiond);
                            break;
                    }

                    areaAdapter.notifyDataSetChanged();
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                dismissLoadingDialog();
            }
        }, "", lifecycleSubject, false, true);

    }



}
