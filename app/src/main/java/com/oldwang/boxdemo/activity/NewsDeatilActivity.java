package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.NewsCommentAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.CommentDetailData;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.NewsData;
import com.oldwang.boxdemo.bean.NewsInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.KeybordS;
import com.oldwang.boxdemo.util.SoftKeyboardStateHelper;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.MyGridView;
import com.oldwang.boxdemo.view.TextEditTextView;
import com.oldwang.boxdemo.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class NewsDeatilActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.tv_title)
    TextView tv_title;


    @BindView(R.id.listview)
    MyGridView listview;
    @BindView(R.id.recyclerview)
    YRecycleview recylerview;


    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.tv_comment_count)
    TextView tv_comment_count;

    @BindView(R.id.tv_goodCount)
    TextView tv_goodCount;

    @BindView(R.id.tv_bad_count)
    TextView tv_bad_count;

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

    @BindView(R.id.iv_logo)
    ImageView iv_logo;

    private int page = 1;
    private final int size = 20;
    private int total;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;       //正常情况

    private NewsCommentAdpater mcommentadpater;

    private String newsId;

    private ListData<NewsData> newsData;
    private NewsInfo newsInfo;

    private List<NewsData> datas = new ArrayList<>();

    //回复评论会员的ID
    private String commentMemberId = "";
    //评论类型（1评论2回复）
    private int commentType ;
    // //评论内容(类型1为评论内容类型2位回复内容);
    private String newsCommentDetail = "";
    // 快讯评论id;
    private String newsCommentId = "";
    //0赞1踩
    private int praiseType;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_newsdetail);
    }


    @Override
    protected void initData() {
        super.initData();
        commentType = 1;
        titlename.setText("快讯详情");
        newsId = getIntent().getStringExtra(Contans.INTENT_DATA);

        et_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String content = et_comment.getText().toString().trim();
                if (!TextUtils.isEmpty(content)){
                    newsCommentDetail = content;
                    et_comment.setText("");
                    newsCommentSave();
                    KeybordS.closeKeybord(et_comment,mContext);
                    //键盘关闭
                    //对回复进行点赞
                    commentMemberId = "";
                    commentType = 1;
                    newsCommentId = "";
                    newsCommentDetail = "";
                    et_comment.setHint("写点感想");
                    return false;
                }
                return true;

           }
        });
        et_comment.setOnKeyBoardHideListener((keyCode, event) -> {
            //键盘关闭
            //对回复进行点赞
            commentMemberId = "";
            commentType = 1;
            newsCommentId = "";
            newsCommentDetail = "";
            et_comment.setHint("写点感想");
        });

        UIUtils.loadImageView(mContext,BaseActivity.getmUserHeandImage(),iv_logo);
        setListenerFotEditText(et_comment);
        initWebView();
    }

    private void setListenerFotEditText(View view){
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(view);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                //键盘打开
            }

            @Override
            public void onSoftKeyboardClosed() {

            }
        });
    }
    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }


    private void initWebView() {
        WebSettings settings = webView.getSettings();
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

    @OnClick(R.id.bcak_image)
    public void finishnowactivity() {
        finish();
    }
    @OnClick({R.id.iv_one,R.id.iv_two,R.id.iv_three})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_one:
//                if (!TextUtils.isEmpty(newsInfo.getIsPraise())){
//                    ToastUtils.makeText("你已经赞过或踩过");
//                    return;
//                }
                //赞快讯
                praiseType = 0;
                newsCommentId = "";
                newsCommentPraise();
                break;
            case R.id.iv_two:
                //踩快讯
//                if (newsInfo.getIsPraise() != null){
//                    ToastUtils.makeText("你已经赞过或踩过");
//                    return;
//                }
                praiseType = 1;
                newsCommentId = "";
                newsCommentPraise();
                break;
            case R.id.iv_three:
                //分享
                share(1,"","","");
                break;
        }
    }



    public static void startactivity(Context mContext, String newsId) {
        Intent mIntent = new Intent(mContext, NewsDeatilActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA, newsId);
        mContext.startActivity(mIntent);
    }

    /**
     */
    private void newInfo() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("newsId", newsId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().newInfo(requestBean)
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
                    newsData = data.getNewsData();
                    newsInfo = data.getNewsInfo();
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

    /** 评论、回复
     *
     *
     */
    private void newsCommentSave (){

        JsonObject jsonObject = new JsonObject();
        if (!TextUtils.isEmpty(commentMemberId)){
            jsonObject.addProperty("commentMemberId", commentMemberId);
        }

        jsonObject.addProperty("commentType", commentType);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        if (!TextUtils.isEmpty(newsCommentDetail)){
            jsonObject.addProperty("newsCommentDetail", newsCommentDetail);
        }
        if (!TextUtils.isEmpty(newsCommentId)){
            jsonObject.addProperty("newsCommentId", newsCommentId);
        }

        jsonObject.addProperty("newsId", newsId);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().newsCommentSave(requestBean)
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
     *赞 踩
     *
     */
    private void newsCommentPraise (){

        JsonObject jsonObject = new JsonObject();
        if (!TextUtils.isEmpty(newsCommentId)){
            jsonObject.addProperty("commentId", newsCommentId);
        }

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("newsId", newsId);
        //
        jsonObject.addProperty("praiseType", praiseType);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().newsCommentPraise(requestBean)
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
                newsCommentId = "";
                refreshData();
            }

            @Override
            protected void _onError(String message) {
                newsCommentId = "";
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);
    }



    private void setData() {

        if (newsInfo != null) {
            String newsContent = newsInfo.getNewsContent();
            if (!TextUtils.isEmpty(newsContent)) {
                webView.loadDataWithBaseURL(null, getNewData(newsContent), "text/html", "UTF-8", null);
            }
            if (!TextUtils.isEmpty(newsInfo.getCommentCount())) {
                tv_comment_count.setText(newsInfo.getCommentCount());
                tv_total_comment.setText(newsInfo.getCommentCount());
            }
            if (!TextUtils.isEmpty(newsInfo.getNewsTitle())) {
                tv_title.setText(newsInfo.getNewsTitle());
            }


            if (!TextUtils.isEmpty(newsInfo.getGoodCount())) {
                tv_goodCount.setText(newsInfo.getGoodCount());
            }

            if (!TextUtils.isEmpty(newsInfo.getBadCount())) {
                tv_bad_count.setText(newsInfo.getBadCount());
            }
            if (!TextUtils.isEmpty(newsInfo.getCreateTime())) {
                tv_time.setText(DateTools.getFormat(Long.valueOf(newsInfo.getCreateTime())));
            }
            if (!TextUtils.isEmpty(newsInfo.getIsPraise())){
                //更改图片已赞 暂时无图片
                if (newsInfo.getIsPraise().equals("0")){
                    iv_one.setImageDrawable(getResources().getDrawable(R.mipmap.alreay_prise_icon));
                }else {
                    iv_one.setImageDrawable(getResources().getDrawable(R.mipmap.news_zan_hei));
                }

                if (newsInfo.getIsPraise().equals("1")){
                    iv_two.setImageDrawable(getResources().getDrawable(R.mipmap.alreay_cai_icon));
                }else {
                    iv_two.setImageDrawable(getResources().getDrawable(R.mipmap.news_lowzan_hei));
                }
            }else {
                iv_one.setImageDrawable(getResources().getDrawable(R.mipmap.news_zan_hei));
                iv_two.setImageDrawable(getResources().getDrawable(R.mipmap.news_lowzan_hei));
            }

        }

        showData();
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
        newInfo();
    }


    /**
     * 加载更多
     */
    private void loadMoreData() {
        state = STATE_MORE;
        page = ++page;
        newInfo();
    }

    /**
     * 刷新
     */
    private void refreshData() {
        state = STATE_REFREH;
        page = 1;
        newInfo();
    }



    /**
     * 展示数据
     */
    private void showData() {
        switch (state) {
            case STATE_NORMAL:
                mcommentadpater = new NewsCommentAdpater(mContext, datas);
                recylerview.setLayoutManager(new LinearLayoutManager(this));
                recylerview.setItemAnimator(new DefaultItemAnimator());
                recylerview.setAdapter(mcommentadpater);
                mcommentadpater.setlistonclicklister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {
                        NewsData newsData = datas.get(position);

                        switch (v.getId()){
                            case R.id.ll_repay:
                                //进入全部回复界面
                                CommentDetailData commentDetailData = new CommentDetailData();
                                commentDetailData.setId(newsData.getNewsCommentId());
                                commentDetailData.setType(2);
                                CommentDetailActivity.startactivity(mContext,commentDetailData);
                                break;
                            case R.id.ll_prise:
//                                if (!TextUtils.isEmpty(newsData.getIsPraise())){
//                                    ToastUtils.makeText("你已经赞过");
//                                    return;
//                                }
                                //对回复进行点赞
                                praiseType = 0;
                                newsCommentId = newsData.getNewsCommentId();
                                newsCommentPraise();
                                break;
                            case R.id.ll_comment:
                                //对回复进行评论
                                commentMemberId = newsData.getMemberId();
                                commentType = 2;
                                newsCommentId = newsData.getNewsCommentId();
                                et_comment.setHint("回复"+newsData.getMemberName()+":");
                                KeybordS.openKeybord(et_comment,mContext);
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

}
