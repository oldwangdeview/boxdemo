package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.BoxVideoListAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.MemberRelationData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.VideoData;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateFollowEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
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

public class UserBoxVideoListActivity extends BaseActivity {


    @BindView(R.id.recyclerview)
    YRecycleview recylerview;

    @BindView(R.id.iv_head)
    ImageView iv_head;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_attention)
    TextView tv_attention;

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    private BoxVideoListAdpater madpater;
    private ArrayList<VideoData> datas = new ArrayList<>();

    private int page = 1;
    private final int size = 20;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;       //正常情况


    private MemberRelationData memberRelationData;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_video_list);
    }


    @Override
    protected void initData() {
        super.initData();
        memberRelationData = (MemberRelationData) getIntent().getSerializableExtra(Contans.INTENT_DATA);

        if (memberRelationData != null) {
            if (memberRelationData.getMemberInfo() != null) {
                if (!TextUtils.isEmpty(memberRelationData.getMemberInfo().getMemberHeadurl())) {
                    UIUtils.loadImageView(mContext, memberRelationData.getMemberInfo().getMemberHeadurl(), iv_head);
                }
                if (!TextUtils.isEmpty(memberRelationData.getMemberInfo().getMemberNickname())) {
                    tv_name.setText(memberRelationData.getMemberInfo().getMemberNickname());
                    titlename.setText(memberRelationData.getMemberInfo().getMemberNickname());
                }
            }

            if (!memberRelationData.isNotAttention()) {
                tv_attention.setText("已关注");
                tv_attention.setTextColor(Color.parseColor("#FF8C8C8C"));
            } else {
                tv_attention.setTextColor(Color.parseColor("#FFD52E21"));
                tv_attention.setText("+ 关注");
            }
        }

    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }

    @OnClick(R.id.tv_attention)
    public void attention() {
        if (!memberRelationData.isNotAttention()) {
            AlertView alertView = new AlertView("提示", "确定取消关注吗？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position == 1) {
                        memberRelation(2);
                    }

                }
            });
            alertView.show();
        } else {
            AlertView alertView = new AlertView("提示", "确定关注吗？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position == 1) {
                        memberRelation(1);
                    }

                }
            });
            alertView.show();
        }

    }


    public static void startactivity(Context mContext, MemberRelationData memberRelationData) {
        Intent mIntent = new Intent(mContext, UserBoxVideoListActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA, memberRelationData);
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
                int totalPaeg = total / size + (total % size == 0 ? 0 : 1);
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
        jsonObject.addProperty("memberId", memberRelationData.getRelationMemberId());
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
                if (state != STATE_MORE) {
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
                if (state != STATE_MORE) {
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
                madpater = new BoxVideoListAdpater(mContext, datas);
                recylerview.setLayoutManager(new LinearLayoutManager(this));
                recylerview.setItemAnimator(new DefaultItemAnimator());
                recylerview.setAdapter(madpater);
                madpater.setListOnclickLister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {
                        final VideoData videoData = datas.get(position);
                        switch (v.getId()) {
                            case R.id.ll_item:
                                VideoDetailActivity.startactivity(mContext, videoData.getBoxingVideoId());
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
    private void memberRelation(final int flag) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("flag", flag);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("relationMemberId", memberRelationData.getRelationMemberId());


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
                    if (flag == 1){
                        memberRelationData.setNotAttention(false);
                    }else {
                        memberRelationData.setNotAttention(true);
                    }

                    if (!memberRelationData.isNotAttention()) {
                        tv_attention.setText("已关注");
                        tv_attention.setTextColor(Color.parseColor("#FF8C8C8C"));
                    } else {
                        tv_attention.setTextColor(Color.parseColor("#FFD52E21"));
                        tv_attention.setText("+ 关注");
                    }
                    EventBus.getDefault().post(new UpdateFollowEvent());
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }


    @OnClick(R.id.add_image)
    public void addvideo() {
        RecordvideoActivity.setartactivity(this);
    }
}
