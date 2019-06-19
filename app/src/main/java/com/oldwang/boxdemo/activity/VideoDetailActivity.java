package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.VideoCommentAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.CommentDetailData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.MemberInfo;
import com.oldwang.boxdemo.bean.NewsData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.VideoData;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.Api;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.KeybordS;
import com.oldwang.boxdemo.util.SoftKeyboardStateHelper;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.TextEditTextView;
import com.oldwang.boxdemo.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class VideoDetailActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    YRecycleview recylerview;

//    @BindView(R.id.iv_image)
//    ImageView iv_image;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.iv_user_head)
    ImageView iv_user_head;

    @BindView(R.id.tv_user_name)
    TextView tv_user_name;

    @BindView(R.id.iv_follow)
    ImageView iv_follow;

    @BindView(R.id.tv_follow)
    TextView tv_follow;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_total_comment)
    TextView tv_total_comment;

    @BindView(R.id.et_comment)
    TextEditTextView et_comment;


    @BindView(R.id.iv_one)
    ImageView iv_one;

    @BindView(R.id.iv_two)
    ImageView iv_two;


    @BindView(R.id.iv_collection)
    ImageView iv_collection;

    @BindView(R.id.iv_logo)
    ImageView iv_logo;



    @BindView(R.id.jz_video)
    JzvdStd jzvdStd;

    private int page = 1;
    private final int size = 20;
    private int total;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;       //正常情况

    private VideoCommentAdpater mcommentadpater;


    private ListData<NewsData> newsData;
    private VideoData newsInfo;

    private List<NewsData> datas = new ArrayList<>();

    private String boxingVideoCommentRepayDetail;
    private String boxingVideoCommentId;
    private String repayMemberId;

    private String videoDataId;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_videodetail);
    }

    @Override
    protected void initData() {
        super.initData();

        videoDataId = getIntent().getStringExtra(Contans.INTENT_DATA);

        et_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String content = et_comment.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    boxingVideoCommentRepayDetail = content;
                    et_comment.setText("");
                    if (!TextUtils.isEmpty(boxingVideoCommentId)) {
                        videoCommentRepaySave();
                    } else {
                        videoCommentSave(content);
                    }
                    //键盘关闭
                    //对回复进行点赞
                    //对回复进行点赞
                    boxingVideoCommentId = "";
                    boxingVideoCommentRepayDetail = "";
                    repayMemberId = "";
                    et_comment.setHint("写点感想");
                    KeybordS.closeKeybord(et_comment, mContext);

                    return false;
                }
                return true;

            }
        });
        et_comment.setOnKeyBoardHideListener(new TextEditTextView.OnKeyBoardHideListener() {
            @Override
            public void onKeyHide(int keyCode, KeyEvent event) {
                //键盘关闭
                //对回复进行点赞
                //对回复进行点赞
                boxingVideoCommentId = "";
                boxingVideoCommentRepayDetail = "";
                repayMemberId = "";
                et_comment.setHint("写点感想");
            }
        });
        setListenerFotEditText(et_comment);
        UIUtils.loadImageView(mContext,BaseActivity.getmUserHeandImage(),iv_logo);


        setListenerFotEditText(et_comment);
    }

    private void setListenerFotEditText(View view) {
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(view);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                //键盘打开
            }

            @Override
            public void onSoftKeyboardClosed() {
                //键盘关闭
                //对回复进行点赞
                boxingVideoCommentId = "";
                boxingVideoCommentRepayDetail = "";
                repayMemberId = "";
                et_comment.setHint("写点感想");
            }
        });
    }

    public static void startactivity(Context mContext, String videoDataId) {
        Intent mIntent = new Intent(mContext, VideoDetailActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA, videoDataId);
        mContext.startActivity(mIntent);
    }

    @OnClick({R.id.ll_report, R.id.iv_one, R.id.iv_two, R.id.iv_three, R.id.iv_download, R.id.iv_collection, R.id.tv_follow,R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_report:
                //举报
                ReportActivity.startactivity(mContext, newsInfo.getBoxingVideoId(), 1);
                break;
            case R.id.iv_one:
//                if (!TextUtils.isEmpty(newsInfo.getIsPraise())){
//                    ToastUtils.makeText("你已经赞过或踩过");
//                    return;
//                }
                //赞
                clickType = 0;
                updateVideoInfo();
                break;
            case R.id.iv_two:
                //踩
//                if (!TextUtils.isEmpty(newsInfo.getIsPraise())){
//                    ToastUtils.makeText("你已经赞过或踩过");
//                    return;
//                }
                clickType = 1;
                updateVideoInfo();
                break;
            case R.id.iv_three:
                //分享
                share(1, "", "", "");
                break;

            case R.id.iv_download:
                //下载
                break;
            case R.id.iv_collection:

                if (!TextUtils.isEmpty(newsInfo.getIsCollection()) && newsInfo.getIsCollection().equals("1")) {
                    ToastUtils.makeText("你已经收藏");
                    return;
                }

                //收藏
                clickType = 4;
                updateVideoInfo();
                break;
            case R.id.tv_follow:

                if (!TextUtils.isEmpty(newsInfo.getIsAttention()) && newsInfo.getIsAttention().equals("1")) {
                    AlertView alertView = new AlertView("提示", "确定取消关注吗？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position == 1) {
                                memberRelation(2
                                        , newsInfo.getMemberInfo().getMemberId());
                            }

                        }
                    });
                    alertView.show();
                } else {
                    AlertView alertView = new AlertView("提示", "确定关注吗？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position == 1) {
                                memberRelation(1, newsInfo.getMemberInfo().getMemberId());
                            }

                        }
                    });
                    alertView.show();
                }
                break;


        }
    }


    @Override
    protected void initEvent() {
        super.initEvent();
        recylerview.setReFreshEnabled(false);
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
        videoCommentList();
    }

    private int clickType;


    /**
     * 用户关注
     */
    private void memberRelation(int flag, String relationMemberId) {

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

    /**
     *
     */
    private void videoCommentSave(String boxingVideoCommentDetail) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("boxingVideoCommentDetail", boxingVideoCommentDetail);
        jsonObject.addProperty("boxingVideoId", videoDataId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().videoCommentSave(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                dismissLoadingDialog();
                refreshData();
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);
    }

    /**
     * 0点赞 1踩 2分享(取消) 3播放 4收藏
     */
    private void updateVideoInfo() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("boxingVideoId", videoDataId);
        jsonObject.addProperty("clickType", clickType);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().updateVideoInfo(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                dismissLoadingDialog();
                refreshData();
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);
    }

    /**
     * 回复评论
     */
    private void videoCommentRepaySave() {

        JsonObject jsonObject = new JsonObject();
        if (!TextUtils.isEmpty(boxingVideoCommentId)) {
            jsonObject.addProperty("boxingVideoCommentId", boxingVideoCommentId);
        }

        if (!TextUtils.isEmpty(boxingVideoCommentRepayDetail)) {
            jsonObject.addProperty("boxingVideoCommentRepayDetail", boxingVideoCommentRepayDetail);
        }
        jsonObject.addProperty("boxingVideoId", videoDataId);

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        if (!TextUtils.isEmpty(repayMemberId)) {
            jsonObject.addProperty("repayMemberId", repayMemberId);
        }


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().videoCommentRepaySave(requestBean)
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
                refreshData();
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);
    }


    /**
     * 视频l评论点赞踩
     */
    private void videoCommentUp() {

        JsonObject jsonObject = new JsonObject();
        if (!TextUtils.isEmpty(boxingVideoCommentId)) {
            jsonObject.addProperty("boxingVideoCommentId", boxingVideoCommentId);
        }
        //1踩 0点赞
        jsonObject.addProperty("type", 0);

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().videoCommentUp(requestBean)
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
                refreshData();
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);
    }


    /**
     */
    private void videoCommentList() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("boxingVideoId", videoDataId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().videoCommentList(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                dismissLoadingDialog();

                if (state != STATE_MORE) {
                    datas.clear();
                }

                if (stringStatusCode != null) {
                    DataInfo1 data = stringStatusCode.getData();
                    newsData = data.getBoxingVideoCommentData();
                    newsInfo = data.getVideoInfo();
                    datas.addAll(newsData.getList());
                    total = newsData.getTotal();
                    setData();
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                if (state != STATE_MORE) {
                    datas.clear();
                }
                setData();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);
    }

    private void setData() {

        if (newsInfo != null) {
            String imagUrl = newsInfo.getBoxingVideoImg();
            String boxingVideoUrl = newsInfo.getBoxingVideoUrl();

            if (!TextUtils.isEmpty(imagUrl)) {
//                UIUtils.loadImageView(mContext, newsInfo.getBoxingVideoImg(), iv_image);

//                jzvdStd.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
//                        , "饺子快长大", JzvdStd.SCREEN_NORMAL);
//                Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(jzvdStd.thumbImageView);

                if (!imagUrl.startsWith("http")){
                    imagUrl = Api.imageUrl + imagUrl;
                }
                Glide.with(this).load(imagUrl).into(jzvdStd.thumbImageView);


            }
            if (!TextUtils.isEmpty(boxingVideoUrl)) {

                if (!boxingVideoUrl.startsWith("http")){
                    boxingVideoUrl = Api.imageUrl + boxingVideoUrl;
                }
                jzvdStd.setUp(boxingVideoUrl
                        , "", JzvdStd.SCREEN_NORMAL);

            }


            if (!TextUtils.isEmpty(newsInfo.getIsAttention()) && newsInfo.getIsAttention().equals("1")) {
                tv_follow.setText("已关注");
                tv_follow.setTextColor(Color.parseColor("#FF8C8C8C"));
                iv_follow.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.boxvideo_gz));
            } else {
                tv_follow.setTextColor(Color.parseColor("#FFD52E21"));
                tv_follow.setText("关注");
                iv_follow.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_add));
            }

            if (!TextUtils.isEmpty(newsInfo.getIsCollection()) && newsInfo.getIsCollection().equals("1")) {
                iv_collection.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.already_collcet_icon));
            } else {
                iv_collection.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.vodeodetail_shouchang));
            }

            if (!TextUtils.isEmpty(newsInfo.getIsPraise())) {

                if (newsInfo.getIsPraise().equals("1")) {
                    iv_one.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.news_zan_hei));
                    iv_two.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.alreay_cai_icon));
                } else if (newsInfo.getIsPraise().equals("0")) {
                    iv_one.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.alreay_prise_icon));
                    iv_two.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.news_lowzan_hei));
                } else {
                    iv_one.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.news_zan_hei));
                    iv_two.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.news_lowzan_hei));
                }

            } else {
                iv_one.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.news_zan_hei));
                iv_two.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.news_lowzan_hei));
            }


            if (!TextUtils.isEmpty(newsInfo.getBoxingVideoCommentCount())) {
                tv_total_comment.setText(newsInfo.getBoxingVideoCommentCount());
            }

            if (!TextUtils.isEmpty(newsInfo.getBoxingVideoTitle())) {
                tv_name.setText(newsInfo.getBoxingVideoTitle());
            }

            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setMemberId(newsInfo.getMemberId());
            memberInfo.setMemberNickname(newsInfo.getMemberName());
            memberInfo.setMemberHeadLogo(newsInfo.getMemberHeadLogo());

            newsInfo.setMemberInfo(memberInfo);


            if (memberInfo != null) {
                if (!TextUtils.isEmpty(memberInfo.getMemberHeadLogo())) {
                    UIUtils.loadImageView(mContext, memberInfo.getMemberHeadLogo(), iv_user_head);
                }
                if (!TextUtils.isEmpty(memberInfo.getMemberNickname())) {
                    tv_user_name.setText(memberInfo.getMemberNickname());
                }
            }
            if (!TextUtils.isEmpty(newsInfo.getCreateTime())) {
                tv_time.setText(newsInfo.getCreateTime());
            }


        }
        showData();
    }


    /**
     * 加载更多
     */
    private void loadMoreData() {
        state = STATE_MORE;
        page = ++page;
        videoCommentList();
    }

    /**
     * 刷新
     */
    private void refreshData() {
        state = STATE_REFREH;
        page = 1;
        videoCommentList();
    }


    /**
     * 展示数据
     */
    private void showData() {
        switch (state) {
            case STATE_NORMAL:
                mcommentadpater = new VideoCommentAdpater(mContext, datas);
                recylerview.setLayoutManager(new LinearLayoutManager(this));
                recylerview.setItemAnimator(new DefaultItemAnimator());
                recylerview.setAdapter(mcommentadpater);
                mcommentadpater.setlistonclicklister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {
                        NewsData newsData = datas.get(position);

                        switch (v.getId()) {
                            case R.id.ll_repay:
                                //进入全部回复界面

                                CommentDetailData commentDetailData = new CommentDetailData();
                                commentDetailData.setId(newsData.getBoxingVideoCommentId());
                                commentDetailData.setType(1);
                                CommentDetailActivity.startactivity(mContext, commentDetailData);
                                break;
                            case R.id.ll_prise:
//                                if (!TextUtils.isEmpty(newsData.getIsPraise())) {
//                                    ToastUtils.makeText("你已经赞过");
//                                    return;
//                                }
                                //对回复进行点赞
                                boxingVideoCommentId = newsData.getBoxingVideoCommentId();
                                videoCommentUp();
                                break;
                            case R.id.ll_comment:
                                //对回复进行评论
                                repayMemberId = newsData.getMemberId();
                                boxingVideoCommentId = newsData.getBoxingVideoCommentId();
                                et_comment.setHint("回复" + newsData.getMemberName() + ":");
                                KeybordS.openKeybord(et_comment, mContext);
                                break;
                        }
                    }
                });

                break;
            case STATE_REFREH:
                mcommentadpater.notifyDataSetChanged();
                recylerview.scrollToPosition(0);
                recylerview.setReFreshComplete();
                break;
            case STATE_MORE:
                mcommentadpater.notifyDataSetChanged();
                recylerview.setloadMoreComplete();
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //home back
        JzvdStd.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //     Jzvd.clearSavedProgress(this, null);
        //home back
        JzvdStd.goOnPlayOnPause();
    }


    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

}
