package com.oldwang.boxdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.ArtsSchoolDetailActivity;
import com.oldwang.boxdemo.activity.AssmbleListActivity;
import com.oldwang.boxdemo.activity.BaseDetailActivity;
import com.oldwang.boxdemo.activity.BoxVideoListActivity;
import com.oldwang.boxdemo.activity.ChoiceMainPathActivity;
import com.oldwang.boxdemo.activity.EvaluationDeatilActivity;
import com.oldwang.boxdemo.activity.GoodsDetailActivity;
import com.oldwang.boxdemo.activity.HomeFindActivity;
import com.oldwang.boxdemo.activity.LoginForPhoneActivity;
import com.oldwang.boxdemo.activity.MainActivity;
import com.oldwang.boxdemo.activity.MyCaptureActivity;
import com.oldwang.boxdemo.activity.MyMessageActivity;
import com.oldwang.boxdemo.activity.NewsDeatilActivity;
import com.oldwang.boxdemo.activity.TrainingBaseActivity;
import com.oldwang.boxdemo.activity.VideoDetailActivity;
import com.oldwang.boxdemo.adpater.HomeAdptaer;
import com.oldwang.boxdemo.adpater.HomeMasterAdpater;
import com.oldwang.boxdemo.adpater.Home_XLadpater;
import com.oldwang.boxdemo.adpater.Home_other_adpater1;
import com.oldwang.boxdemo.adpater.Home_other_adpater2;
import com.oldwang.boxdemo.adpater.Home_other_adpater3;
import com.oldwang.boxdemo.adpater.Home_other_adpater4;
import com.oldwang.boxdemo.adpater.Home_wdAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.BaseData;
import com.oldwang.boxdemo.bean.EvaluationData;
import com.oldwang.boxdemo.bean.MasterData;
import com.oldwang.boxdemo.bean.MasterDataDTO;
import com.oldwang.boxdemo.bean.MsgInfo;
import com.oldwang.boxdemo.bean.SchoolData;
import com.oldwang.boxdemo.bean.UserInfo;
import com.oldwang.boxdemo.bean.VenueData;
import com.oldwang.boxdemo.bean.CommodityData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.NewsData;
import com.oldwang.boxdemo.bean.PictureSetData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.VideoData;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.ExitLoginSuccess;
import com.oldwang.boxdemo.event.JumpTeachers;
import com.oldwang.boxdemo.event.MainJumpEvent;
import com.oldwang.boxdemo.event.UpdateNoticeEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LogUntil;
import com.oldwang.boxdemo.util.LogUtils;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.MyGridView;
import com.oldwang.boxdemo.view.YRecycleview;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class HomeFragment extends BaseFragment {


    @BindView(R.id.myrcycleview)
    YRecycleview myrcycleview;

    @BindView(R.id.tv_msg_count)
    TextView tv_msg_count;

    private View heandview;
    private HomeAdptaer madpter;


    private RecyclerView recyclerview;//武动拳民
    private Home_wdAdpater wdadpater;


    private RecyclerView recyclerview1;//训练基地
    private Home_XLadpater xladpater;


    //拳联记实
    private TextView home_text_1;
    private TextView home_text_2;
    private TextView home_text_3;
    private TextView home_text_4;

    //商品推荐数量
    private final String commodityNum = "4";
    //轮播图数量
    private final String pictureSetNum = "3";


    LinearLayout head_layout;

    View Other_view1;
    RecyclerView other_recyclerview;
    Home_other_adpater1 other1madpater;


    View Other_view2;
    TextView Other_view2_name;
    TextView Other_view2_text1;
    TextView Other_view2_text2;
    RecyclerView other2_recyclerview;

    List<BaseData> mlistdata = new ArrayList<>();
    List<BaseData> mlistdata1 = new ArrayList<>();
    List<BaseData> mlistdata2 = new ArrayList<>();


    List<MasterData> mMasterDatalistdata = new ArrayList<>();
    List<MasterData> mMasterDatalistdata1 = new ArrayList<>();
    List<MasterData> mMasterDatalistdata2 = new ArrayList<>();


    Home_other_adpater2 other2madpater;

    View Other_view3;
    TextView Other_view3_text1;
    TextView Other_view3_text2;


    List<EvaluationData> evaluationDataList = new ArrayList<>();

    List<VenueData> venueDataList = new ArrayList<>();


    View Other_view4;
    RecyclerView other4_recyclerview;
    Home_other_adpater4 other4madpater;

    private ConvenientBanner banner;
    private RelativeLayout rl_cb;
    private ImageView iv_adv;

    private LinearLayout ll_count;

    private MyGridView mygridview;
    private HomeMasterAdpater mygrideadpater;
    private String readCount;


    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_home);
    }

    @Override
    protected void initData() {


        super.initData();
        heandview = UIUtils.inflate(mContext, R.layout.layout_homehead);

        //拳联装备
        heandview.findViewById(R.id.ll_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().post(new MainJumpEvent(1));
            }
        });
        //武动拳名
        heandview.findViewById(R.id.ll_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxVideoListActivity.startactivity(mContext);

            }
        });
        heandview.findViewById(R.id.tv_wudong_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxVideoListActivity.startactivity(mContext);

            }
        });


        //训练基地
        heandview.findViewById(R.id.trainingbase_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTrainingBaseActivity();
            }
        });
        heandview.findViewById(R.id.tv_jidi_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTrainingBaseActivity();
            }
        });


        //拳联纪实
        heandview.findViewById(R.id.ll_four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MainJumpEvent(3));
            }
        });

        //拳联纪实
        heandview.findViewById(R.id.tv_jishi_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MainJumpEvent(3));
            }
        });


        banner = heandview.findViewById(R.id.id_cb);
        rl_cb = heandview.findViewById(R.id.rl_cb);


        iv_adv = heandview.findViewById(R.id.iv_adv);


        //训练基地
        recyclerview1 = heandview.findViewById(R.id.recyclerview1);
        LinearLayoutManager ms2 = new LinearLayoutManager(mContext);
        ms2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview1.setLayoutManager(ms2);
        recyclerview1.setItemAnimator(new DefaultItemAnimator());
        xladpater = new Home_XLadpater(mContext, venueDataList);
        recyclerview1.setAdapter(xladpater);

        //拳联纪实
        head_layout = heandview.findViewById(R.id.head_layout);
        home_text_1 = heandview.findViewById(R.id.home_text_1);
        home_text_2 = heandview.findViewById(R.id.home_text_2);
        home_text_3 = heandview.findViewById(R.id.home_text_3);
        home_text_4 = heandview.findViewById(R.id.home_text_4);
        home_text_1.setOnClickListener(clicklister);
        home_text_2.setOnClickListener(clicklister);
        home_text_3.setOnClickListener(clicklister);
        home_text_4.setOnClickListener(clicklister);

        Other_view1 = UIUtils.inflate(mContext, R.layout.fragment_item_home_other1);
        other_recyclerview = Other_view1.findViewById(R.id.recyclerview);


        Other_view2 = UIUtils.inflate(mContext, R.layout.fragment_item_home_other2);

        ll_count = Other_view2.findViewById(R.id.ll_count);
        ll_count.setVisibility(View.GONE);

        Other_view2_name = Other_view2.findViewById(R.id.name);

        other2_recyclerview = Other_view2.findViewById(R.id.recyclerview);
        Other_view2_text1 = Other_view2.findViewById(R.id.text1);
        Other_view2_text2 = Other_view2.findViewById(R.id.text2);
        Other_view2_text1.setOnClickListener(Other2_click);
        Other_view2_text2.setOnClickListener(Other2_click);

        other2madpater = new Home_other_adpater2(mContext, mlistdata);

        other2_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        other2_recyclerview.setItemAnimator(new DefaultItemAnimator());
        other2_recyclerview.setAdapter(other2madpater);
        other2madpater.setListOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                //学校
                if (mlistdata.get(position).getPositon() == 1){
                    ArtsSchoolDetailActivity.startactivity(mContext,mlistdata.get(position).getNewsBoxingSchoolId(), false);
                }else {
                    //场馆
                    BaseDetailActivity.startactivity(mContext,mlistdata.get(position).getNewsMasterId());
                }
            }
        });


        Other_view3 = UIUtils.inflate(mContext, R.layout.fragment_item_home_other3);

        mygridview = Other_view3.findViewById(R.id.mygridview);
        Other_view3_text1 = Other_view3.findViewById(R.id.text1);
        Other_view3_text2 = Other_view3.findViewById(R.id.text2);
        Other_view3_text1.setOnClickListener(Other3_click);
        Other_view3_text2.setOnClickListener(Other3_click);
        mygrideadpater = new HomeMasterAdpater(mContext,mMasterDatalistdata);
        mygrideadpater.setListonclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                ArtsSchoolDetailActivity.startactivity(mContext, mMasterDatalistdata.get(position).getNewsMasterId(),true);

            }
        });
        mygridview.setAdapter(mygrideadpater);


        Other_view4 = UIUtils.inflate(mContext, R.layout.fragment_item_home_other4);
        other4_recyclerview = Other_view4.findViewById(R.id.recyclerview);
        other4madpater = new Home_other_adpater4(mContext,evaluationDataList);
        other4_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        other4_recyclerview.setItemAnimator(new DefaultItemAnimator());
        other4_recyclerview.setAdapter(other4madpater);
        other4madpater.setListOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                EvaluationDeatilActivity.startactivity(mContext,evaluationDataList.get(position).getEvaluationDetailsId());
            }
        });


        updatetitleindex(0);
        head_layout.removeAllViews();
        head_layout.addView(Other_view1);


        madpter = new HomeAdptaer(mContext, BaseActivity.gettestdata(1));

        myrcycleview.setLayoutManager(new LinearLayoutManager(mContext));
        myrcycleview.setItemAnimator(new DefaultItemAnimator());
        myrcycleview.setRefreshAndLoadMoreListener(mlister);
        myrcycleview.setLoadMoreEnabled(false);
        myrcycleview.setAdapter(madpter);
        myrcycleview.addHeadView(heandview);

        String user = PreferencesUtils.getInstance().getString(Contans.userInfo, "");
        if (!TextUtils.isEmpty(user)){
            UserInfo userInfo = new Gson().fromJson(user,UserInfo.class);
            if (userInfo != null){
                if (TextUtils.isEmpty(userInfo.getRegionIds())){
                    ChoiceMainPathActivity.startactivity(mContext);
                }
            }
        }

        getBannerAndGoods();
        getPage03();
        getPage02();
        getPage08();
        getPage07();
        getPage05(0);
        getPage05(1);
        getPage06();
        getPage04();
        isMsgRead();
    }


    YRecycleview.OnRefreshAndLoadMoreListener mlister = new YRecycleview.OnRefreshAndLoadMoreListener() {
        @Override
        public void onRefresh() {
            getBannerAndGoods();
            getPage03();
            getPage02();
            getPage08();
            getPage07();
            getPage05(0);
            getPage05(1);
            getPage06();
            getPage04();
        }

        @Override
        public void onLoadMore() {
//            myrcycleview.setloadMoreComplete();
        }
    };


    //拳联纪实的title点击
    View.OnClickListener clicklister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.home_text_1:
                    updatetitleindex(0);
                    head_layout.removeAllViews();
                    head_layout.addView(Other_view1);
                    break;
                case R.id.home_text_2:
                    updatetitleindex(1);
                    head_layout.removeAllViews();
                    head_layout.addView(Other_view2);//
                    break;
                case R.id.home_text_3:
                    updatetitleindex(2);
                    head_layout.removeAllViews();
                    head_layout.addView(Other_view3);
                    break;
                case R.id.home_text_4:
                    updatetitleindex(3);
                    head_layout.removeAllViews();
                    head_layout.addView(Other_view4);
                    break;

            }
        }
    };

    private void settitleTextcolorallbalk() {
        home_text_1.setTextColor(getResources().getColor(R.color.c_525259));
        home_text_2.setTextColor(getResources().getColor(R.color.c_525259));
        home_text_3.setTextColor(getResources().getColor(R.color.c_525259));
        home_text_4.setTextColor(getResources().getColor(R.color.c_525259));
    }

    private void updatetitleindex(int index) {
        settitleTextcolorallbalk();
        switch (index) {
            case 0:
                home_text_1.setTextColor(getResources().getColor(R.color.c_d52e21));
                break;
            case 1:
                home_text_2.setTextColor(getResources().getColor(R.color.c_d52e21));
                break;
            case 2:
                home_text_3.setTextColor(getResources().getColor(R.color.c_d52e21));
                break;
            case 3:
                home_text_4.setTextColor(getResources().getColor(R.color.c_d52e21));
                break;
        }
    }

    private int position = 0;


    private View.OnClickListener Other2_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text1:
                    position = 0;
                    mlistdata.clear();
                    mlistdata.addAll(mlistdata1);
                    ll_count.setVisibility(View.GONE);
                    Other_view2_name.setText("场馆名");
                    Other_view2_text1.setTextColor(getResources().getColor(R.color.c_d52e21));
                    Other_view2_text2.setTextColor(getResources().getColor(R.color.c_525259));
                    break;
                case R.id.text2:
                    position = 1;
                    mlistdata.clear();
                    mlistdata.addAll(mlistdata2);
                    Other_view2_name.setText("学校名");
                    ll_count.setVisibility(View.VISIBLE);
                    Other_view2_text2.setTextColor(getResources().getColor(R.color.c_d52e21));
                    Other_view2_text1.setTextColor(getResources().getColor(R.color.c_525259));
                    break;
            }
            other2madpater.notifyDataSetChanged();
        }
    };


    private int teachtherPosition = 0;


    private View.OnClickListener Other3_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text1:
                    teachtherPosition = 0;
                    mMasterDatalistdata.clear();
                    mMasterDatalistdata.addAll(mMasterDatalistdata1);
                    Other_view3_text1.setTextColor(getResources().getColor(R.color.c_d52e21));
                    Other_view3_text2.setTextColor(getResources().getColor(R.color.c_525259));
                    break;
                case R.id.text2:
                    teachtherPosition = 1;
                    mMasterDatalistdata.clear();
                    mMasterDatalistdata.addAll(mMasterDatalistdata2);
                    Other_view3_text2.setTextColor(getResources().getColor(R.color.c_d52e21));
                    Other_view3_text1.setTextColor(getResources().getColor(R.color.c_525259));
                    break;
            }
            mygrideadpater.notifyDataSetChanged();
        }
    };

    /**
     * 搜索
     */
    @OnClick(R.id.home_image_find)
    public void gotoHomeFindActivity() {
        HomeFindActivity.startactivity(mContext);
    }

    /***
     * 训练基地
     */
    public void gotoTrainingBaseActivity() {
        TrainingBaseActivity.startactivity(mContext);
    }


    /**
     * 获取banner和商品推荐
     */
    private void getBannerAndGoods() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commodityNum", commodityNum);
//        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pictureSetNum", pictureSetNum);
        RequestBean requestBean = HttpUtil.getRequsetBean(getContext(), jsonObject);
        Observable observable =
                ApiUtils.getApi().getBannerAndGoods(requestBean)
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

                    if (data != null) {
                        List<PictureSetData> pictureSetData = data.getPictureSetData();
                        List<CommodityData> commodityData = data.getCommodityData();

                        setBannerAndGoodsData(pictureSetData, commodityData);

                    }

                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    private List<PictureSetData> bannerList;

    private void setBannerAndGoodsData(List<PictureSetData> pictureSetData, List<CommodityData> commodityData) {
        if (pictureSetData != null && pictureSetData.size() > 0) {
            bannerList = pictureSetData;
            rl_cb.setVisibility(View.VISIBLE);
            banner.setVisibility(View.VISIBLE);

            banner.setPages(new CBViewHolderCreator<ImageViewHolder>() {
                @Override
                public ImageViewHolder createHolder() {
                    return new ImageViewHolder();
                }
            }, pictureSetData).setPageIndicator(new int[]{R.mipmap.icon_8,R.mipmap.icon_9})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL).setPointViewVisible(true); //设置指示器的方向水平  居中
//            if (pictureSetData.size() == 1) {
//                banner.setPointViewVisible(false);
//            } else {
//                banner.setPointViewVisible(true);
//            }
        } else {
            rl_cb.setVisibility(View.GONE);
            banner.setVisibility(View.GONE);
        }

        madpter.setData(commodityData);
        madpter.notifyDataSetChanged();

    }


    public class ImageViewHolder implements Holder<PictureSetData> {
        private View mhandeview;
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            mhandeview = UIUtils.inflate(mContext, R.layout.banner_item);
            return mhandeview;
        }

        @Override
        public void UpdateUI(Context context, int position, PictureSetData data) {
            imageView = mhandeview.findViewById(R.id.image);
            final String pictureUrl = data.getPictureUrl();

            final String type = data.getType();
            final String info = data.getInfo();


            if (!TextUtils.isEmpty(pictureUrl)){
                UIUtils.loadImageView(mContext, pictureUrl, imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(info)){

                        //（out-外部链接 shop-普通商品 shopTeam-团购商品 video-视频详情 news-功夫快讯详情 venue-场馆详情 evaluation-装备测评详情）
                        switch (type){
                            case "out":
                                UIUtils.openWebUrl(mContext, info);
                                break;
                            case "shop":
                                GoodsDetailActivity.startactivity(mContext,false,info,null);
                                break;
                            case "shopTeam":
                                GoodsDetailActivity.startactivity(mContext,true,info,null);
                                break;
                            case "video":
                                VideoDetailActivity.startactivity(mContext,info);
                                break;
                            case "news":
                                NewsDeatilActivity.startactivity(mContext,info);
                                break;
                            case "venue":
                                BaseDetailActivity.startactivity(mContext,info);
                                break;
                            case "evaluation":
                                EvaluationDeatilActivity.startactivity(mContext,info);
                                break;
                        }

                    }

//                    if(!TextUtils.isEmpty(pictureUrl)) {
//                        UIUtils.openWebUrl(mContext, pictureUrl);
//                    }else{
//                        new LogUntil(mContext,TAG,"weburl__:"+pictureUrl);
//                    }
                    }
                });
            }

        }
    }


    /**
     * 获取武动拳名数据
     */
    private void getPage03() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", 1);
        jsonObject.addProperty("pageSize", 4);
        RequestBean requestBean = HttpUtil.getRequsetBean(getContext(), jsonObject);
        Observable observable =
                ApiUtils.getApi().getPage03(requestBean)
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
                    ListData<VideoData> videoData = data.getVideoData();
                    setPage03Data(videoData);
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    private void setPage03Data(final ListData<VideoData> videoData) {

        //武动拳名
        wdadpater = new Home_wdAdpater(mContext, videoData.getList());
        recyclerview = heandview.findViewById(R.id.recyclerview);
        LinearLayoutManager ms = new LinearLayoutManager(mContext);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(ms);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(wdadpater);

        wdadpater.setListOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                VideoData videoData1 = videoData.getList().get(position);
                VideoDetailActivity.startactivity(mContext, videoData1.getBoxingVideoId());

            }
        });



    }

    /**
     * 获取功夫快讯数据
     */
    private void getPage02() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", 1);
        jsonObject.addProperty("pageSize", 2);
        RequestBean requestBean = HttpUtil.getRequsetBean(getContext(), jsonObject);
        Observable observable =
                ApiUtils.getApi().getPage02(requestBean)
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
                    ListData<NewsData> newsDataListData = data.getNewsData();
                    setPage02Data(newsDataListData);
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    public void setPage02Data(ListData<NewsData> page02Data) {

        final List<NewsData> list = page02Data.getList();
        other1madpater = new Home_other_adpater1(mContext, list);
        other_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        other_recyclerview.setItemAnimator(new DefaultItemAnimator());
        other_recyclerview.setAdapter(other1madpater);
        other1madpater.setListOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                NewsDeatilActivity.startactivity(mContext,list.get(position).getNewsId());

            }
        });

    }


    /**
     * 获取十佳馆校-场馆
     */
    private void getPage08() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", 1);
        jsonObject.addProperty("pageSize", 3);
        RequestBean requestBean = HttpUtil.getRequsetBean(getContext(), jsonObject);
        Observable observable =
                ApiUtils.getApi().getPage08(requestBean)
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

                    ListData<VenueData> venueData = data.getVenueData();
                    mlistdata1.clear();
                    for (VenueData venueData1 : venueData.getList()) {
                        BaseData baseData = new BaseData();
                        baseData.setPositon(0);
                        baseData.setNewsMasterId(venueData1.getVenueId());
                        baseData.setName(venueData1.getVenueName());
                        mlistdata1.add(baseData);
                    }

                    if (position == 0) {
                        mlistdata.clear();
                        mlistdata.addAll(mlistdata1);
                        madpter.notifyDataSetChanged();
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
     * 获取十佳馆校-学校
     */
    private void getPage07() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", 1);
        jsonObject.addProperty("pageSize", 3);
        RequestBean requestBean = HttpUtil.getRequsetBean(getContext(), jsonObject);
        Observable observable =
                ApiUtils.getApi().getPage07(requestBean)
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

                    ListData<SchoolData> schoolData = data.getSchoolData();
                    mlistdata2.clear();
                    for (SchoolData schoolData1 : schoolData.getList()) {
                        BaseData baseData = new BaseData();
                        baseData.setPositon(1);
                        baseData.setName(schoolData1.getNewsBoxingSchoolName());
                        baseData.setCount(schoolData1.getNewsBoxingSchoolPraiseCount());
                        baseData.setNewsBoxingSchoolId(schoolData1.getNewsBoxingSchoolId());
                        mlistdata2.add(baseData);
                    }

                    if (position == 1) {
                        mlistdata.clear();
                        mlistdata.addAll(mlistdata2);
                        madpter.notifyDataSetChanged();
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
     * 获取名师战将数据
     */
    private void getPage05(final int newsMasterType) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("newsMasterType", newsMasterType);
        jsonObject.addProperty("pageNum", 1);
        jsonObject.addProperty("pageSize", 3);
        RequestBean requestBean = HttpUtil.getRequsetBean(getContext(), jsonObject);
        Observable observable =
                ApiUtils.getApi().getPage05(requestBean)
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
                    ListData<MasterData> masterData = data.getMasterData();

                    mMasterDatalistdata.clear();


                    if (newsMasterType == 0){
                        mMasterDatalistdata1.clear();
                        mMasterDatalistdata1.addAll(masterData.getList());
                    }else {
                        mMasterDatalistdata2.clear();
                        mMasterDatalistdata2.addAll(masterData.getList());
                    }

                    if(teachtherPosition == 0){
                        mMasterDatalistdata.addAll(mMasterDatalistdata1);
                        mygrideadpater.notifyDataSetChanged();
                    }else {
                        mMasterDatalistdata.addAll(mMasterDatalistdata2);
                        mygrideadpater.notifyDataSetChanged();
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
     * 获取品牌评分
     */
    private void getPage06() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", 1);
        jsonObject.addProperty("pageSize", 3);
        RequestBean requestBean = HttpUtil.getRequsetBean(getContext(), jsonObject);
        Observable observable =
                ApiUtils.getApi().getPage06(requestBean)
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
                    ListData<EvaluationData> evaluationData = data.getEvaluationData();
                    evaluationDataList.clear();
                    evaluationDataList.addAll(evaluationData.getList());
                    other4madpater.notifyDataSetChanged();
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    /**
     * 训练基地
     */
    private void getPage04() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", 1);
        jsonObject.addProperty("pageSize", 4);
        RequestBean requestBean = HttpUtil.getRequsetBean(getContext(), jsonObject);
        Observable observable =
                ApiUtils.getApi().getPage04(requestBean)
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
                myrcycleview.setReFreshComplete();
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    ListData<VenueData> venueDataListData = data.getBaseData();
                    venueDataList.clear();
                    venueDataList.addAll(venueDataListData.getList());
                    xladpater.notifyDataSetChanged();
                }
                myrcycleview.setReFreshComplete();
                myrcycleview.scrollToPosition(0);
            }

            @Override
            protected void _onError(String message) {
                myrcycleview.setReFreshComplete();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }


    /**
     * 获取未读消息条数
     */
    private void isMsgRead() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().isMsgRead(requestBean)
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
                    MsgInfo msgInfo = data.getMsgInfo();

                    if (msgInfo != null){
                        if (!TextUtils.isEmpty(msgInfo.getReadCount())){
                            readCount = msgInfo.getReadCount();
                        }
                    }
                    int count = 0;
                    if (!TextUtils.isEmpty(readCount)){
                        count = new BigDecimal(readCount).intValue();
                    }

                    if (count > 0){
                        tv_msg_count.setVisibility(View.VISIBLE);
                        tv_msg_count.setText(readCount);
                    }else {
                        tv_msg_count.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }



    @OnClick(R.id.home_image_sss)
    public void gotoAssb(){
        MyCaptureActivity.start(mContext);
    }
    @OnClick(R.id.rl_notice)
    public void goToNotice(){
        MyMessageActivity.startactivity(mContext);
    }


    //控制物理返回键
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;


    @Override
    public void onResume() {
        super.onResume();
        banner.startTurning(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopTurning();
    }

    /**
     * 跳转名师或战将页面
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void jumpTeachers(JumpTeachers event) {
        int type = event.getType();
        String id = event.getId();
        LogUntil.show(getContext(), "测试", "type=" + type + "id=" + id);
        //名师页面
        if (type == 1) {

        } else {
            //战将页面
        }
    }

    /**
     * 更新消息未读数量
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateList(UpdateNoticeEvent event) {
        isMsgRead();
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
