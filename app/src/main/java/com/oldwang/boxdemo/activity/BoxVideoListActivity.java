package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.AddressManagementAdpater;
import com.oldwang.boxdemo.adpater.BoxVideoListAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.AddressData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.NewsData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.VideoData;
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

public class BoxVideoListActivity  extends BaseActivity {



    @BindView(R.id.recyclerview)
    YRecycleview recylerview;

    @BindView(R.id.iv_one)
    ImageView iv_one;

    @BindView(R.id.iv_two)
    ImageView iv_two;

    private BoxVideoListAdpater madpater;
    private ArrayList<VideoData> datas = new ArrayList<>();

    private int page = 1 ;
    private final int size = 20;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况


    private String keyword;
    private boolean isAsc;
    private boolean isComment;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_boxvediolist);
    }


    @Override
    protected void initData() {
        super.initData();
        //首次默认 评论降序排列
        isAsc = false;
        isComment = true;
    }


    /**
     * 排序
     */

    @OnClick({R.id.ll_comment,R.id.ll_play_count})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_comment:
                isComment = true;
                iv_one.setVisibility(View.VISIBLE);
                iv_two.setVisibility(View.GONE);
                isAsc = !isAsc;
                if (isAsc){
                    iv_one.setImageDrawable(getResources().getDrawable(R.mipmap.icon_up));
                }else {
                    iv_one.setImageDrawable(getResources().getDrawable(R.mipmap.iocn_down));
                }
                refreshData();

                break;
            case R.id.ll_play_count:
                isComment = false;
                iv_two.setVisibility(View.VISIBLE);
                iv_one.setVisibility(View.GONE);
                isAsc = !isAsc;
                if (isAsc){
                    iv_two.setImageDrawable(getResources().getDrawable(R.mipmap.icon_up));
                }else {
                    iv_two.setImageDrawable(getResources().getDrawable(R.mipmap.iocn_down));
                }
                refreshData();
                break;
        }
    }


    /**
     * 已关注用户
     */

    @OnClick(R.id.followuser_text)
    public void gotoFollwUserActivity(){
        FollowUserActivity.startactivity(mContext);
    }

    /**
     * 搜索
     */
    @OnClick(R.id.find_image)
    public void findVidedata(){
        VideoFindAvtivity.startactivity(mContext);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,BoxVideoListActivity.class);
        mContext.startActivity(mIntent);
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
        getData();
    }


    /**
     * 视频列表
     */
    private void getData() {

        showLoadingDialog();
        JsonObject jsonObject = new JsonObject();
        if (isAsc){
            jsonObject.addProperty("isAsc", "asc");
        }else {
            jsonObject.addProperty("isAsc", "desc");
        }
        if (!TextUtils.isEmpty(keyword)){
            jsonObject.addProperty("keyword", keyword);
        }


        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        if (isComment){
            jsonObject.addProperty("orderByColumn", "boxingVideoCommentCount" );
        }else {
            jsonObject.addProperty("orderByColumn", "boxingVideoPlayCount" );
        }

        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().videoList(requestBean)
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
                    ListData<VideoData> videoData = data.getVideoData();
                    List<VideoData> list = videoData.getList();
                    datas.addAll(list);
                    total = videoData.getTotal();
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
                madpater = new BoxVideoListAdpater(mContext,datas);
                recylerview.setLayoutManager(new LinearLayoutManager(this));
                recylerview.setItemAnimator(new DefaultItemAnimator());
                recylerview.setAdapter(madpater);
                madpater.setListOnclickLister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {
                        final VideoData videoData = datas.get(position);
                        switch (v.getId()){
                            //关注
                            case R.id.tv_follow:

                                if (!TextUtils.isEmpty(videoData.getIsAttention()) && videoData.getIsAttention().equals("1")){
                                    AlertView alertView = new AlertView("提示", "确定取消关注吗？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(Object o, int position) {
                                            if (position == 1) {
                                                memberRelation(2
                                                        ,videoData.getMemberInfo().getMemberId());
                                            }

                                        }
                                    });
                                    alertView.show();
                                }else {
                                    AlertView alertView = new AlertView("提示", "确定关注吗？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(Object o, int position) {
                                            if (position == 1) {
                                                memberRelation(1,videoData.getMemberInfo().getMemberId());
                                            }

                                        }
                                    });
                                    alertView.show();
                                }

                                break;
                            //进入视频播放页面
                            case R.id.ll_item:
                                VideoDetailActivity.startactivity(mContext,videoData.getBoxingVideoId());
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

    /**
     * 用户关注
     */
    private void memberRelation(int flag,String relationMemberId) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("flag", flag);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("relationMemberId", relationMemberId);



        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().memberRelation(requestBean)
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
                    refreshData();
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }


    @OnClick(R.id.add_image)
    public void addvideo(){
        RecordvideoActivity.setartactivity(this);
    }
}
