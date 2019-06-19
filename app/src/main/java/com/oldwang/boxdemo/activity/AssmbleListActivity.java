package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.AssembleTimeAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.CommodityTeamBuyData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.TeamBuyMembers;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateAddress;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
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

public class AssmbleListActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.recyclerview)
    YRecycleview recylerview;
    private AssembleTimeAdpater madpater;

    private String commodityId;

    private int page = 1 ;
    private final int size = 20;
    private int total;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况

    private ArrayList<CommodityTeamBuyData> datas = new ArrayList<>();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_assemblelist);
    }

    @Override
    protected void initData() {
        super.initData();

        commodityId = getIntent().getStringExtra(Contans.INTENT_DATA);
        titlename.setText("拼团列表");
        getData();

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

    /**
     * 拼团列表
     */
    private void getData() {

        showLoadingDialog();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commodityId", commodityId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);

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
                if (state != STATE_MORE){
                    datas.clear();
                }

                dismissLoadingDialog();
                if (stringStatusCode != null) {

                    ListData<CommodityTeamBuyData> commodityTeamBuyData = stringStatusCode.getData().getCommodityTeamBuyData1();
                    List<CommodityTeamBuyData> list = commodityTeamBuyData.getList();

//                    List<CommodityTeamBuyData> list = new ArrayList<>();
//                    CommodityTeamBuyData commodityTeamBuyData1 = new CommodityTeamBuyData();
//                    commodityTeamBuyData1.setPeopleNum("5");
//                    commodityTeamBuyData1.setTeamBuyCode("421412241");
//                    commodityTeamBuyData1.setEndTime(System.currentTimeMillis() +  150 * 1000+"");
//                    List<TeamBuyMembers> teamBuyMembers = new ArrayList<>();
//                    TeamBuyMembers teamBuyMembers1 = new TeamBuyMembers();
//                    teamBuyMembers1.setTeamBuyMember("小红花");
//                    teamBuyMembers1.setMemberHeadurl("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1558492134&di=eaae773f0eba54655b531aed30db34c7&src=http://pic2.52pk.com/files/allimg/090626/1553504U2-2.jpg");
//                    teamBuyMembers.add(teamBuyMembers1);
//                    commodityTeamBuyData1.setTeamBuyMembers(teamBuyMembers);
//                    list.add(commodityTeamBuyData1);
//
//                    CommodityTeamBuyData commodityTeamBuyData2 = new CommodityTeamBuyData();
//                    commodityTeamBuyData2.setEndTime(System.currentTimeMillis() +  150 * 1000+"");
//                    commodityTeamBuyData2.setPeopleNum("5");
//                    commodityTeamBuyData1.setTeamBuyCode("11412241");
//                    List<TeamBuyMembers> teamBuyMembers3 = new ArrayList<>();
//                    TeamBuyMembers teamBuyMembers2 = new TeamBuyMembers();
//                    teamBuyMembers2.setTeamBuyMember("花花花");
//                    teamBuyMembers2.setMemberHeadurl("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1558492134&di=eaae773f0eba54655b531aed30db34c7&src=http://pic2.52pk.com/files/allimg/090626/1553504U2-2.jpg");
//                    teamBuyMembers3.add(teamBuyMembers2);
//                    commodityTeamBuyData2.setTeamBuyMembers(teamBuyMembers3);
//                    list.add(commodityTeamBuyData2);

                    datas.addAll(list);
                    total = commodityTeamBuyData.getTotal();
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
                madpater = new AssembleTimeAdpater(mContext,datas);
                recylerview.setLayoutManager(new LinearLayoutManager(this));
                recylerview.setItemAnimator(new DefaultItemAnimator());
                recylerview.setAdapter(madpater);

                madpater.setListOnclicklister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {
                        switch (v.getId()){
                            case R.id.text_addassemble:
                                //我要参团
                                GoodsDetailActivity.startactivity(mContext,true,commodityId,datas.get(position));
                                finish();
                                break;

                            default:
                                AssmbleListDetail.startactivity(mContext,datas.get(position));
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void jumpTeachers(UpdateAddress event) {
        refreshData();
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
        if (madpater != null) {
            madpater.cancelAllTimers();
        }
    }





    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }
    public static void startactivity(Context mCOntext, String commodityId){
        Intent mIntent = new Intent(mCOntext,AssmbleListActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,commodityId);
        mCOntext.startActivity(mIntent);
    }
}
