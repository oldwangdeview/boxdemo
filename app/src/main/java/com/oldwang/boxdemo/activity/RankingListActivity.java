package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.AddressManagementAdpater;
import com.oldwang.boxdemo.adpater.RanKingListAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.AddressData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.WithdrawalInfo;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RankingListActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    YRecycleview recyclerview;

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    @BindView(R.id.title_layout)
    LinearLayout title_layout;


    @BindView(R.id.tv_myRank)
    TextView tv_myRank;

    @BindView(R.id.tv_myScore)
    TextView tv_myScore;

    private RanKingListAdpater madapter;
    // type==1为总排行榜， type==2为当日排行版
    private int type;


    private int page = 1 ;
    private final int size = 20;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state;       //正常情况

    private ArrayList<ScoreData> datas = new ArrayList<>();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_rankinglist);
    }

    @Override
    protected void initData() {
        super.initData();
        state = STATE_NORMAL;
        type = getIntent().getIntExtra(Contans.INTENT_TYPE,-1);
        titlename.setText(type==1?"总排行版":type==2?"当日排行榜":"");

        if (type == 2){
            linearLayout.setBackground(getResources().getDrawable(R.mipmap.now_day_score));
        }

//        if(type==1){
//            title_layout.setVisibility(View.VISIBLE);
//        }else{
//            title_layout.setVisibility(View.GONE);
//        }

        getData();
    }


    public static void startactivity(Context mContext,int type){
        Intent mIntent = new Intent(mContext,RankingListActivity.class);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mContext.startActivity(mIntent);
    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
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
    }


    /**
     * 获取排行榜数据
     */
    private void getData() {

        showLoadingDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);

        Observable observable;
        if (type == 1){
             observable =
                    ApiUtils.getApi().rankingList(requestBean)
                            .compose(RxHelper.getObservaleTransformer())
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {

                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread());
        }else {
             observable =
                    ApiUtils.getApi().dayScoreList(requestBean)
                            .compose(RxHelper.getObservaleTransformer())
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {

                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread());
        }



        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                if (state != STATE_MORE){
                    datas.clear();
                }

                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    ListData<ScoreData> scoreData = data.getScoreData();
                    List<ScoreData> list = scoreData.getList();
                    WithdrawalInfo scoreInfo = data.getScoreInfo();

                    datas.addAll(list);
                    total = data.getScoreData().getTotal();

                    if (scoreInfo != null){
                        if (!TextUtils.isEmpty(scoreInfo.getMyScore())) {
                            tv_myScore.setText(scoreInfo.getMyScore());
                        }else {
                            tv_myScore.setText("0");
                        }

                        if (!TextUtils.isEmpty(scoreInfo.getMyRank())) {
                            tv_myRank.setText(scoreInfo.getMyRank()+"名");
                        }else {
                            tv_myRank.setText("未知");
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

                madapter = new RanKingListAdpater(mContext,datas);
                recyclerview.setLayoutManager(new LinearLayoutManager(this));
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.setAdapter(madapter);
                break;
            case STATE_REFREH:
                madapter.notifyDataSetChanged();
                recyclerview.scrollToPosition(0);
                recyclerview.setReFreshComplete();
                break;
            case STATE_MORE:
                madapter.notifyDataSetChanged();
                recyclerview.setloadMoreComplete();
                break;
        }

    }
}
