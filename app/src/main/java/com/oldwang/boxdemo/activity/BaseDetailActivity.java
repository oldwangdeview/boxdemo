package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.BaseDetail_Bottom_gridviewAdpater1;
import com.oldwang.boxdemo.adpater.BaseDetail_Bottom_gridviewAdpater2;
import com.oldwang.boxdemo.adpater.BaseDetail_Bottom_gridviewAdpater3;
import com.oldwang.boxdemo.adpater.BaseDetailListAdpater;
import com.oldwang.boxdemo.adpater.BaseDetail_NoticeMeaggeAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.NoticeData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ServiceData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.VenuVideo;
import com.oldwang.boxdemo.bean.VenueCommentData;
import com.oldwang.boxdemo.bean.VenueInfo;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.MyGridView;
import com.oldwang.boxdemo.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 基地详情
 */
public class BaseDetailActivity extends BaseActivity {


    @BindView(R.id.iv_image)
    ImageView iv_image;


    @BindView(R.id.tv_venue_name)
    TextView tv_venue_name;


    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_business_time)
    TextView tv_business_time;
    @BindView(R.id.tv_star)
    TextView tv_star;
    @BindView(R.id.tv_prise_count)
    TextView tv_prise_count;



    @BindView(R.id.image_1)
    ImageView image_1;
    @BindView(R.id.image_2)
    ImageView image_2;
    @BindView(R.id.image_3)
    ImageView image_3;
    @BindView(R.id.image_4)
    ImageView image_4;
    @BindView(R.id.image_5)
    ImageView image_5;


    @BindView(R.id.ll_notice)
    LinearLayout ll_notice;

    //公告详情
    @BindView(R.id.mygridview)
    MyGridView mygridview;
    BaseDetail_NoticeMeaggeAdpater mNoticeadpter;

    //场馆设施
    @BindView(R.id.ll_one)
    LinearLayout ll_one;
    @BindView(R.id.text_one_size)
    TextView text_one_size;
    @BindView(R.id.listview_1)
    RecyclerView listview_1;
    BaseDetailListAdpater listviewadpater1;
    List<String> listdata1 = new ArrayList<>();
    List<String> listdataText1 = new ArrayList<>();


    //师资力量
    @BindView(R.id.ll_two)
    LinearLayout ll_two;
    @BindView(R.id.text_two_size)
    TextView text_two_size;
    @BindView(R.id.listview_2)
    RecyclerView listview_2;
    BaseDetailListAdpater listviewadpater2;
    List<String> listdata2 = new ArrayList<>();
    List<String> listdataText2 = new ArrayList<>();


    //成绩贡献
    @BindView(R.id.ll_three)
    LinearLayout ll_three;
    @BindView(R.id.text_three_size)
    TextView text_three_size;
    @BindView(R.id.listview_3)
    RecyclerView listview_3;
    BaseDetailListAdpater listviewadpater3;
    List<String> listdata3 = new ArrayList<>();
    List<String> listdataText3 = new ArrayList<>();

    //教学视频
    @BindView(R.id.ll_four)
    LinearLayout ll_four;
    @BindView(R.id.listview_4)
    RecyclerView listview_4;
    BaseDetailListAdpater listviewadpater4;
    List<String> listdata4 = new ArrayList<>();


    @BindView(R.id.layout_view_1)
    LinearLayout layout_view_1;
    @BindView(R.id.layout_view_gridview1)
    MyGridView layout_view_gridview1;
    @BindView(R.id.view1)
    View view1;
    BaseDetail_Bottom_gridviewAdpater1 layout_view_gridview1adpater;


    @BindView(R.id.layout_view_2)
    LinearLayout layout_view_2;
    @BindView(R.id.layout_view_gridview2)
    MyGridView layout_view_gridview2;
    @BindView(R.id.view2)
    View view2;
    BaseDetail_Bottom_gridviewAdpater2 layout_view_gridview2adpater;
    List<String> layout_view_gridview2listdata = new ArrayList<>();

    @BindView(R.id.tv_name_bottom)
    TextView tv_name_bottom;
    @BindView(R.id.webView)
    WebView mWebView;

    @BindView(R.id.layout_view_3)
    LinearLayout layout_view_3;
    @BindView(R.id.layout_view_gridview3)
    YRecycleview recycleview;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.tv_no_comment)
    TextView tv_no_comment;


    BaseDetail_Bottom_gridviewAdpater3 layout_view_gridview3adpater;

    private String venueId;
    private VenueInfo venueInfo;

    private int page = 1;
    private final int size = 20;
    private int total;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;       //正常情况

    List<VenueCommentData> datas = new ArrayList<>();


    @Override
    protected void initView() {
        setContentView(R.layout.activity_basedetail);
    }

    @Override
    protected void initData() {
        super.initData();
        venueId = getIntent().getStringExtra(Contans.INTENT_DATA);
        noticeList();
        venueCommentList();
        venueDetail();
        initWebView();
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


    @OnClick({R.id.layout_1, R.id.layout_2, R.id.layout_3})
    public void choiceBottomType(View v) {
        switch (v.getId()) {
            case R.id.layout_1:
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view1.setVisibility(View.VISIBLE);
                layout_view_2.setVisibility(View.GONE);
                layout_view_3.setVisibility(View.GONE);
                layout_view_1.setVisibility(View.VISIBLE);

                break;
            case R.id.layout_2:
                view1.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);

                layout_view_1.setVisibility(View.GONE);
                layout_view_3.setVisibility(View.GONE);
                layout_view_2.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_3:
                view2.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
                view3.setVisibility(View.VISIBLE);

                layout_view_2.setVisibility(View.GONE);
                layout_view_1.setVisibility(View.GONE);
                layout_view_3.setVisibility(View.VISIBLE);

                break;
        }
    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity() {
        finish();
    }


    @OnClick({R.id.tv_subscribe, R.id.ll_phone, R.id.ll_report, R.id.tv_more_notice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_report:
                //举报
                ReportActivity.startactivity(mContext, venueId, 0);
                break;
            case R.id.ll_phone:
                //拨打商家电话
                String phone = tv_phone.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    callPhone(phone);
                }
                break;
            case R.id.tv_subscribe:
                //立即预约
                SubscribePlaceActivity.startactivity(mContext, venueId);
                break;
            case R.id.tv_more_notice:
                //公告信息更多
                NoticeListActivity.startactivity(mContext, venueId);
                break;


        }
    }


    public static void startactivity(Context mContext, String venueId) {
        Intent mIntent = new Intent(mContext, BaseDetailActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA, venueId);
        mContext.startActivity(mIntent);
    }

    /***
     * 公告信息
     */
    private void noticeList() {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNo", page);
        jsonObject.addProperty("pageSize", size);
        jsonObject.addProperty("venueId", venueId);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().noticeList(requestBean)
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
                    final ListData<NoticeData> noticeData = stringStatusCode.getData().getNoticeData();

                    if (noticeData != null && noticeData.getList() != null && noticeData.getList().size() > 0) {
                        ll_notice.setVisibility(View.VISIBLE);
                        mNoticeadpter = new BaseDetail_NoticeMeaggeAdpater(mContext, noticeData.getList());
                        mygridview.setAdapter(mNoticeadpter);
                        mygridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                NoticeDetailActivity.startactivity(mContext, noticeData.getList().get(i));

                            }
                        });
                    } else {
                        ll_notice.setVisibility(View.GONE);
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

    /**
     * 公众评价
     */
    private void venueCommentList() {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNo", page);
        jsonObject.addProperty("pageSize", size);
        jsonObject.addProperty("venueId", venueId);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().venueCommentList(requestBean)
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


                if (state != STATE_MORE) {
                    datas.clear();
                }
                dismissLoadingDialog();
                if (stringStatusCode != null) {


                    if (stringStatusCode != null) {
                        DataInfo data = stringStatusCode.getData();
                        ListData<VenueCommentData> venueCommentData = data.getVenueCommentData();
                        datas.addAll(venueCommentData.getList());
                        total = venueCommentData.getTotal();
                    }
                }
                showData();

            }

            @Override
            protected void _onError(String message) {

                if (state != STATE_MORE) {
                    datas.clear();
                }
                showData();
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    private void venueDetail() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("venueId", venueId);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().venueDetail(requestBean)
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
                    venueInfo = stringStatusCode.getData().getVenueInfo();
                    setData();
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    private void setData() {


        if (venueInfo != null) {


            if (!TextUtils.isEmpty(venueInfo.getVenueName())) {
                tv_name_bottom.setText(venueInfo.getVenueName());
                tv_venue_name.setText(venueInfo.getVenueName());
            }

            if (!TextUtils.isEmpty(venueInfo.getVenueIntroduce())) {
                String content = venueInfo.getVenueIntroduce();
                mWebView.loadDataWithBaseURL(null, getNewData(content), "text/html", "UTF-8", null);
            }

            if (!TextUtils.isEmpty(venueInfo.getVenueDetailedAddress())) {
                tv_address.setText(venueInfo.getVenueDetailedAddress());
            }
            if (!TextUtils.isEmpty(venueInfo.getPhone())) {
                tv_phone.setText(venueInfo.getPhone());
            }
            if (!TextUtils.isEmpty(venueInfo.getBusinessTime())) {
                tv_business_time.setText(venueInfo.getBusinessTime());
            }
            if (!TextUtils.isEmpty(venueInfo.getVenueCommentCount())) {
                tv_prise_count.setText(venueInfo.getVenueCommentCount() + "评价");
            }
            setVenueStar();

            //场馆设施
            List<VenuVideo> venueDevicesUrls = venueInfo.getVenueDevicesUrls();
            if (venueDevicesUrls != null) {

                //需要java1.8 android 24 以上，暂不使用
//                List<String> list1 = venueDevicesUrls.stream().map(VenuVideo::getVenueDevice).collect(Collectors.toList());
                listdata1.clear();
                for (VenuVideo venueDevicesUrl : venueDevicesUrls) {
                    listdata1.add(venueDevicesUrl.getVenueDevice());
                    listdataText1.add(venueDevicesUrl.getDesc());
                }
                if (listdata1.size() < 1) {
                    ll_one.setVisibility(View.GONE);
                } else {
                    ll_one.setVisibility(View.VISIBLE);
                    text_one_size.setText("共" + listdata1.size() + "张");
                    listviewadpater1 = new BaseDetailListAdpater(mContext, listdata1);
                    LinearLayoutManager ms = new LinearLayoutManager(mContext);
                    ms.setOrientation(LinearLayoutManager.HORIZONTAL);
                    listview_1.setLayoutManager(ms);
                    listview_1.setItemAnimator(new DefaultItemAnimator());
                    listview_1.setAdapter(listviewadpater1);

                    listviewadpater1.setListOnclickLister(new ListOnclickLister() {
                        @Override
                        public void onclick(View v, int position) {
                            ImageTextLookActivity.startactivity(mContext, new Gson().toJson(listdata1), new Gson().toJson(listdataText1), position);
                        }
                    });

                }

            }


            //师资力量
            List<VenuVideo> venueTeachingUrls = venueInfo.getVenueTeachingUrls();
            if (venueTeachingUrls != null) {

                listdata2.clear();
                for (VenuVideo venueDevicesUrl : venueTeachingUrls) {
                    listdata2.add(venueDevicesUrl.getVenueTeaching());
                    listdataText2.add(venueDevicesUrl.getDesc());
                }
                if (listdata2.size() < 1) {
                    ll_two.setVisibility(View.GONE);
                } else {
                    ll_two.setVisibility(View.VISIBLE);
                    text_two_size.setText("共" + listdata2.size() + "张");
                    listviewadpater2 = new BaseDetailListAdpater(mContext, listdata2);
                    LinearLayoutManager ms1 = new LinearLayoutManager(mContext);
                    ms1.setOrientation(LinearLayoutManager.HORIZONTAL);
                    listview_2.setLayoutManager(ms1);
                    listview_2.setItemAnimator(new DefaultItemAnimator());
                    listview_2.setAdapter(listviewadpater2);

                    listviewadpater2.setListOnclickLister(new ListOnclickLister() {
                        @Override
                        public void onclick(View v, int position) {
                            ImageTextLookActivity.startactivity(mContext, new Gson().toJson(listdata2), new Gson().toJson(listdataText2), position);

                        }
                    });
                }

            }

            //成绩贡献
            List<VenuVideo> venueAchievementUrls = venueInfo.getVenuePicUrls();
            if (venueAchievementUrls != null) {

                listdata3.clear();
                for (VenuVideo venueDevicesUrl : venueAchievementUrls) {
                    listdata3.add(venueDevicesUrl.getVenuePic());
                    listdataText3.add(venueDevicesUrl.getDesc());
                }
                if (listdata3.size() < 1) {
                    ll_three.setVisibility(View.GONE);
                } else {
                    ll_three.setVisibility(View.VISIBLE);
                    text_three_size.setText("共" + listdata3.size() + "张");
                    listviewadpater3 = new BaseDetailListAdpater(mContext, listdata3);
                    LinearLayoutManager ms1 = new LinearLayoutManager(mContext);
                    ms1.setOrientation(LinearLayoutManager.HORIZONTAL);
                    listview_3.setLayoutManager(ms1);
                    listview_3.setItemAnimator(new DefaultItemAnimator());
                    listview_3.setAdapter(listviewadpater3);

                    listviewadpater3.setListOnclickLister(new ListOnclickLister() {
                        @Override
                        public void onclick(View v, int position) {
                            ImageTextLookActivity.startactivity(mContext, new Gson().toJson(listdata3), new Gson().toJson(listdataText3), position);
                        }
                    });
                }

            }

            //教学视频
            List<VenuVideo> venuVideoUrls = venueInfo.getVenuVideoUrls();
            if (venuVideoUrls != null) {

                listdata4.clear();
                for (VenuVideo venueDevicesUrl : venuVideoUrls) {
                    listdata4.add(venueDevicesUrl.getPreviewPicPath());
                }
                if (listdata4.size() < 1) {
                    ll_four.setVisibility(View.GONE);
                } else {
                    ll_four.setVisibility(View.VISIBLE);
                    listviewadpater4 = new BaseDetailListAdpater(mContext, listdata4);
                    listviewadpater4.setShowVideo(true);
                    LinearLayoutManager ms1 = new LinearLayoutManager(mContext);
                    ms1.setOrientation(LinearLayoutManager.HORIZONTAL);
                    listview_4.setLayoutManager(ms1);
                    listview_4.setItemAnimator(new DefaultItemAnimator());
                    listview_4.setAdapter(listviewadpater4);
                }

            }
            //服务项目
            List<ServiceData> serviceData = venueInfo.getServiceData();
            layout_view_gridview1adpater = new BaseDetail_Bottom_gridviewAdpater1(mContext, serviceData);
            layout_view_gridview1.setAdapter(layout_view_gridview1adpater);


            //场馆设施
            layout_view_gridview2listdata.clear();
            if (venueInfo.getDevices() != null) {
                layout_view_gridview2listdata.addAll(venueInfo.getDevices());
            }

            layout_view_gridview2adpater = new BaseDetail_Bottom_gridviewAdpater2(mContext, layout_view_gridview2listdata);
            layout_view_gridview2.setAdapter(layout_view_gridview2adpater);


            if (!TextUtils.isEmpty(venueInfo.getVenueDetailPic())) {
                UIUtils.loadImageView(mContext, venueInfo.getVenueDetailPic(), iv_image);
            }


        }


    }

    /***
     * 设置星级
     */
    private void setVenueStar() {

        String venueStar = venueInfo.getCommentGradeTotal();

        if (TextUtils.isEmpty(venueStar)) {
            venueStar = "0.0";
        }

        tv_star.setText(venueStar + "分");
        double star = Double.valueOf(venueStar);
        if (star < 1) {
            image_1.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
            image_2.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
            image_3.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
            image_4.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
            image_5.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));

        } else if (star < 2) {
            image_1.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_2.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
            image_3.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
            image_4.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
            image_5.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
        } else if (star < 3) {
            image_1.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_2.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_3.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
            image_4.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
            image_5.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
        } else if (star < 4) {
            image_1.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_2.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_3.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_4.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
            image_5.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
        } else if (star < 5) {
            image_1.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_2.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_3.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_4.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_5.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing));
        } else if (star < 6) {
            image_1.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_2.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_3.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_4.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
            image_5.setImageDrawable(getResources().getDrawable(R.mipmap.xingxing2));
        }

    }


    @Override
    protected void initEvent() {
        super.initEvent();
        recycleview.setReFreshEnabled(false);
        recycleview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
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
                    recycleview.setloadMoreComplete();
                }
            }
        });
        venueCommentList();
    }


    /**
     * 加载更多
     */
    private void loadMoreData() {
        state = STATE_MORE;
        page = ++page;
        venueCommentList();
    }

    /**
     * 刷新
     */
    private void refreshData() {
        state = STATE_REFREH;
        page = 1;
        venueCommentList();
    }


    /**
     * 展示数据
     */
    private void showData() {
        switch (state) {
            case STATE_NORMAL:


                if (datas.size() > 0) {
                    tv_no_comment.setVisibility(View.GONE);
                } else {
                    tv_no_comment.setVisibility(View.VISIBLE);
                }
                layout_view_gridview3adpater = new BaseDetail_Bottom_gridviewAdpater3(mContext, datas);
                recycleview.setLayoutManager(new LinearLayoutManager(this));
                recycleview.setItemAnimator(new DefaultItemAnimator());
                recycleview.setAdapter(layout_view_gridview3adpater);
                layout_view_gridview3adpater.setlistonclicklister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {

                    }
                });

                break;
            case STATE_REFREH:
                layout_view_gridview3adpater.notifyDataSetChanged();
                recycleview.scrollToPosition(0);
                recycleview.setReFreshComplete();
                break;
            case STATE_MORE:
                layout_view_gridview3adpater.notifyDataSetChanged();
                recycleview.setloadMoreComplete();
                break;
        }

    }

}
