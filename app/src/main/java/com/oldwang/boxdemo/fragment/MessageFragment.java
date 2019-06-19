package com.oldwang.boxdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.MainActivity;
import com.oldwang.boxdemo.adpater.OnthespotRecordPagerAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.BrandData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RegionData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ScreenChildData;
import com.oldwang.boxdemo.bean.ScreenData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.TypeData;
import com.oldwang.boxdemo.event.ShowTitleEvent;
import com.oldwang.boxdemo.event.UpdateAreaEvent;
import com.oldwang.boxdemo.event.UpdateMessageEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ChoiceDataReturnLister;
import com.oldwang.boxdemo.interfice.ChoicePriceLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.ChoiceView;
import com.oldwang.boxdemo.view.RightChoiceMoreDialog;
import com.oldwang.boxdemo.view.RightSideslipLay;

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

public class MessageFragment extends BaseFragment {

    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.view1)
    View view1;

    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.view2)
    View view2;

    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.view3)
    View view3;

    @BindView(R.id.text4)
    TextView text4;
    @BindView(R.id.view4)
    View view4;




    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private OnthespotRecordPagerAdpater mpageradpater;

    List<TextView> textlistdata = new ArrayList<>();
    List<View> viewlistdata = new ArrayList<>();

    private int nowPostion;

    private List<TypeData> list;
    private List<RegionData> provicelistdata = new ArrayList<>();
    private RightChoiceMoreDialog rightchoicpath;

    private RightSideslipLay menuHeaderView;

    private List<RegionData> provicelistdataFirst = new ArrayList<>();
    private RightChoiceMoreDialog mdialog;
    private List<BrandData> brandlistdat = new ArrayList<>();
    private String brandId;

    DrawerLayout drawer;
    LinearLayout navigationView;

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragmnet_message);
    }



    @Override
    protected void initData() {
        super.initData();
        drawer = ((MainActivity)mContext).getDrawer();
        navigationView = ((MainActivity)mContext).getNavigationView();
        menuHeaderView = new RightSideslipLay(mContext);
        navigationView.addView(menuHeaderView);
        menuHeaderView.setCloseMenuCallBack(new RightSideslipLay.CloseMenuCallBack() {
            @Override
            public void setupCloseMean(boolean isReset) {
                //重置信息
                if (isReset){
                    ScreenData screenData = screenDataList.get(1);
                    List<ScreenChildData> screenChildDataList = screenData.getScreenChildDataList();
                    screenChildDataList.clear();
                    for (RegionData provicelistdatum : provicelistdataFirst) {
                        String name = "";
                        if (!TextUtils.isEmpty(provicelistdatum.getFullname())) {
                            name = provicelistdatum.getFullname();
                        }

                        if (!TextUtils.isEmpty(provicelistdatum.getRegionName())) {
                            name = provicelistdatum.getRegionName();
                        }

                        String regionId = provicelistdatum.getAreaId();
                        if (TextUtils.isEmpty(regionId)) {
                            regionId = provicelistdatum.getRegionId();
                        }
                        ScreenChildData screenChildData = new ScreenChildData();
                        screenChildData.setId(regionId);
                        screenChildData.setName(name);
                        screenChildData.setTag(0);
                        screenChildDataList.add(screenChildData);
                    }

                    menuHeaderView.setNewScreenDataList(screenDataList);
                }else {

                     String typeId = "";
                     String areaId = "";
                     int tag = 0;
                    for (ScreenChildData screenChildData : screenDataList.get(0).getScreenChildDataList()) {
                        if (screenChildData.isCheck()){
                            typeId = screenChildData.getId();
                        }
                    }
                    for (ScreenChildData screenChildData : screenDataList.get(1).getScreenChildDataList()) {
                        if (screenChildData.isCheck()){
                            areaId = screenChildData.getId();
                            tag = screenChildData.getTag();
                        }
                    }


                    UpdateMessageEvent updateMessageEvent = new UpdateMessageEvent();
                    updateMessageEvent.setPostion(nowPostion);
                    updateMessageEvent.setTypId(typeId);
                    updateMessageEvent.setTag(tag);
                    updateMessageEvent.setAreaId(areaId);


                    EventBus.getDefault().post(updateMessageEvent);

                    closeMenu();
                }
            }
        });


        textlistdata.add(text1);
        textlistdata.add(text2);
        textlistdata.add(text3);
        textlistdata.add(text4);
        viewlistdata.add(view1);
        viewlistdata.add(view2);
        viewlistdata.add(view3);
        viewlistdata.add(view4);

        mpageradpater = new OnthespotRecordPagerAdpater(getChildFragmentManager());
        viewpager.setOffscreenPageLimit(4);
        viewpager.setAdapter(mpageradpater);
        viewpager.setCurrentItem(0);
        types();
        getData();

    }

    public void closeMenu() {
        drawer.closeDrawer(GravityCompat.END);
    }

    public void openMenu() {
        drawer.openDrawer(GravityCompat.END);
    }

    @OnClick({R.id.layout_1, R.id.layout_2, R.id.layout_3, R.id.layout_4})
    public void Clicklayout(View v) {
        switch (v.getId()) {
            case R.id.layout_1:
                viewpager.setCurrentItem(0);
                break;
            case R.id.layout_2:
                viewpager.setCurrentItem(1);
                break;
            case R.id.layout_3:
                viewpager.setCurrentItem(2);
                break;
            case R.id.layout_4:
                viewpager.setCurrentItem(3);
                break;
        }
    }


    @Override
    protected void initEvent() {
        super.initEvent();
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateselected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 获取品牌数据
     */
    private void getData() {


        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", 1);
        jsonObject.addProperty("pageSize", 1000000);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().getBrandList(requestBean)
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
                    ListData<BrandData> brandData = data.getBrandData();
                    final List<BrandData> list = brandData.getList();
                    brandlistdat.clear();
                    brandlistdat.addAll(list);
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }

    /**
     * 选择馆校/战将名师类型
     */
    private void types() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(getContext(), jsonObject);
        Observable observable =
                ApiUtils.getApi().types(requestBean)
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
                    ListData<TypeData> typeData = data.getTypeData();
                    //列表数据
                    list = typeData.getList();
                    getAreaData("", 0);
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);
    }

    private void updateselected(int position) {
        nowPostion = position;
        for (int i = 0; i < textlistdata.size(); i++) {
            if (position == i) {
                textlistdata.get(position).setTextColor(getResources().getColor(R.color.c_d52e21));
                viewlistdata.get(position).setVisibility(View.VISIBLE);
            } else {
                textlistdata.get(i).setTextColor(getResources().getColor(R.color.c_525259));
                viewlistdata.get(i).setVisibility(View.INVISIBLE);
            }
        }


    }

   private List<ScreenData> screenDataList = new ArrayList<>();

    private boolean showtite = true;

    private double maxBuyPrice = -1;
    private double minBuyPrice = -1;
    String typeId = "";

    @OnClick(R.id.find_image)
    public void findmoredata() {

        EventBus.getDefault().post(new ShowTitleEvent(showtite));
        showtite = !showtite;

        if (nowPostion == 0 || nowPostion == 2) {
            List<ChoiceView> choiceviewlist = new ArrayList<>();

            ChoiceView choiceView1 = new ChoiceView<TypeData>(mContext, list, "类型", false, new ChoiceDataReturnLister<TypeData>() {
                @Override
                public void getChoicedata(List<TypeData> listdata) {

                    UpdateMessageEvent updateMessageEvent = new UpdateMessageEvent();
                    updateMessageEvent.setPostion(nowPostion);

                    if (listdata != null && listdata.size() > 0) {
                        updateMessageEvent.setTypId(listdata.get(0).getTypeId());
                    } else {
                        updateMessageEvent.setTypId("");
                    }
                    EventBus.getDefault().post(updateMessageEvent);
                }

            });

            choiceviewlist.add(choiceView1);
            new RightChoiceMoreDialog.Builder(mContext).setconntentview(choiceviewlist).create().show();
        }else if (nowPostion == 1){
            openMenu();
        }else if (nowPostion == 3){
            if(mdialog==null){
                List<ChoiceView> viewlist = new ArrayList<>();
                //价格筛选
                ChoiceView choiceprice = new ChoiceView(mContext, "评分区间", new ChoicePriceLister() {
                    @Override
                    public void returndata(String minprice, String maxprice) {
                        if(!TextUtils.isEmpty(maxprice)) {
                            maxBuyPrice = Double.parseDouble(maxprice);
                        }
                        if(!TextUtils.isEmpty(minprice)){
                            minBuyPrice =Double.parseDouble(minprice);
                        }
                    }
                });
                viewlist.add(choiceprice);

                //品牌
                if(brandlistdat.size()>0){

                    ChoiceView choicebrand = new ChoiceView<BrandData>(mContext, brandlistdat, "品牌", true, new ChoiceDataReturnLister<BrandData>() {
                        @Override
                        public void getChoicedata(List<BrandData> listdata) {
                            if(listdata.size()>0) {
                                brandId = listdata.get(0).getBrandId();
                            }else{
                                brandId = "";
                            }
                        }
                    },false);
                    viewlist.add(choicebrand);

                }

                ChoiceView choiceView1 = new ChoiceView<TypeData>(mContext, list, "类型", false, new ChoiceDataReturnLister<TypeData>() {
                    @Override
                    public void getChoicedata(List<TypeData> listdata) {


                        if (listdata != null && listdata.size() > 0) {
                             typeId =  listdata.get(0).getTypeId();
                        } else {
                            typeId = "";
                        }
                    }

                });
                viewlist.add(choiceView1);


                mdialog = new RightChoiceMoreDialog.Builder(mContext).setOkButtonClickLister(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UpdateMessageEvent updateMessageEvent = new UpdateMessageEvent();
                        updateMessageEvent.setPostion(nowPostion);
                        updateMessageEvent.setTypId(typeId);
                        if (maxBuyPrice >= 0){
                            updateMessageEvent.setMaxScore(maxBuyPrice+"");
                        }
                        if (minBuyPrice >= 0){
                            updateMessageEvent.setMinScore(minBuyPrice+"");
                        }
                        updateMessageEvent.setBrandId(brandId);

                        EventBus.getDefault().post(updateMessageEvent);


                    }
                }).setconntentview(viewlist).create();
            }
            mdialog.show();
        }


    }

    private void initAreaScreen() {
        ScreenData screenData = new ScreenData();
        screenData.setArea(false);
        screenData.setTypeName("类型");
        List<ScreenChildData> screenChildDataList = new ArrayList<>();
        screenData.setScreenChildDataList(screenChildDataList);
        if (list != null) {
            for (TypeData typeData : list) {
                ScreenChildData screenChildData = new ScreenChildData();
                screenChildData.setId(typeData.getTypeId());
                screenChildData.setName(typeData.getTypeName());
                screenChildDataList.add(screenChildData);
            }
        }

        ScreenData screenDataTwo = new ScreenData();
        screenDataTwo.setArea(true);
        screenDataTwo.setTypeName("距离");
        List<ScreenChildData> screenChildDataListTwo = new ArrayList<>();

        for (RegionData provicelistdatum : provicelistdata) {
            String name = "";
            if (!TextUtils.isEmpty(provicelistdatum.getFullname())) {
                name = provicelistdatum.getFullname();
            }

            if (!TextUtils.isEmpty(provicelistdatum.getRegionName())) {
                name = provicelistdatum.getRegionName();
            }

            String regionId = provicelistdatum.getAreaId();
            if (TextUtils.isEmpty(regionId)) {
                regionId = provicelistdatum.getRegionId();
            }
            ScreenChildData screenChildData = new ScreenChildData();
            screenChildData.setId(regionId);
            screenChildData.setTag(0);
            screenChildData.setName(name);
            screenChildDataListTwo.add(screenChildData);
        }

        screenDataTwo.setScreenChildDataList(screenChildDataListTwo);

        screenDataList.clear();
        screenDataList.add(screenData);
        screenDataList.add(screenDataTwo);
        menuHeaderView.setScreenDataList(screenDataList);
    }

    private boolean isFirst = true;


    /**
     * 区域列表
     */
    private void getAreaData(String regionId, final int tag) {


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        if (!TextUtils.isEmpty(regionId) && tag != 0) {
            jsonObject.addProperty("regionId", regionId);
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


                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    List<RegionData> datalist = data.getRegionData();
                    provicelistdata.clear();
                    provicelistdata.addAll(datalist);

                    if (isFirst){
                        initAreaScreen();
                        isFirst = false;
                        provicelistdataFirst.clear();
                        provicelistdataFirst.addAll(datalist);
                    }else {
                        if (screenDataList.size() > 1){
                            ScreenData screenData = screenDataList.get(1);
                            List<ScreenChildData> screenChildDataList = screenData.getScreenChildDataList();
                            screenChildDataList.clear();
                            for (RegionData provicelistdatum : provicelistdata) {
                                String name = "";
                                if (!TextUtils.isEmpty(provicelistdatum.getFullname())) {
                                    name = provicelistdatum.getFullname();
                                }

                                if (!TextUtils.isEmpty(provicelistdatum.getRegionName())) {
                                    name = provicelistdatum.getRegionName();
                                }

                                String regionId = provicelistdatum.getAreaId();
                                if (TextUtils.isEmpty(regionId)) {
                                    regionId = provicelistdatum.getRegionId();
                                }
                                ScreenChildData screenChildData = new ScreenChildData();
                                screenChildData.setId(regionId);
                                screenChildData.setTag(tag);
                                screenChildData.setName(name);
                                screenChildDataList.add(screenChildData);
                            }
                            menuHeaderView.setScreenDataList(screenDataList);

                        }
                    }

                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    /**
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateArea(UpdateAreaEvent event) {
            getAreaData(event.getId(),event.getTag());
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
