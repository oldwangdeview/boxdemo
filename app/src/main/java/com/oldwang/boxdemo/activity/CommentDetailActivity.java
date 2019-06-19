package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.VideoCommentTwoAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.BoxingVideoCommentInfo;
import com.oldwang.boxdemo.bean.CommentDetailData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.NewsData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.KeybordS;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/***
 * 评论详情
 */
public class CommentDetailActivity extends BaseActivity {


    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;


    @BindView(R.id.recyclerview)
    YRecycleview recylerview;

    @BindView(R.id.iv_head)
    ImageView iv_head;

    @BindView(R.id.tv_user_name)
    TextView tv_user_name;

    @BindView(R.id.tv_time)
    TextView tv_time;


    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.tv_total_comment)
    TextView tv_total_comment;


    @BindView(R.id.et_comment)
    EditText et_comment;



    private List<NewsData> datas = new ArrayList<>();

    private int page = 1;
    private final int size = 20;
    private int total;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;       //正常情况

    private CommentDetailData commentDetailData;
    private VideoCommentTwoAdpater mcommentadpater;
    private  BoxingVideoCommentInfo boxingVideoCommentInfo;

    private NewsData newsCommentInfo;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_comment_detail);
    }


    @Override
    protected void initData() {
        super.initData();
        titlename.setText("详情");
        commentDetailData = (CommentDetailData) getIntent().getSerializableExtra(Contans.INTENT_DATA);
        et_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String content = et_comment.getText().toString().trim();
                if (!TextUtils.isEmpty(content)){

                    if (commentDetailData.getType() == 2){
                        et_comment.setText("");
                        KeybordS.closeKeybord(et_comment,mContext);
                        newsCommentSave(content);
                    }else {
                        videoCommentRepaySave(content);
                    }
                    return false;
                }
                return true;

            }
        });
    }

    /**
     *
     * 回复评论
     */
    private void videoCommentRepaySave(String boxingVideoCommentRepayDetail){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("boxingVideoCommentId", boxingVideoCommentInfo.getBoxingVideoCommentId());

        if (!TextUtils.isEmpty(boxingVideoCommentRepayDetail)){
            jsonObject.addProperty("boxingVideoCommentRepayDetail", boxingVideoCommentRepayDetail);
        }
        jsonObject.addProperty("boxingVideoId", boxingVideoCommentInfo.getBoxingVideoId());
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("repayMemberId", boxingVideoCommentInfo.getMemberId());



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

    /** 评论、回复
     *
     *
     */
    private void newsCommentSave (String newsCommentDetail){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commentMemberId", newsCommentInfo.getMemberId());

        jsonObject.addProperty("commentType", 2);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        if (!TextUtils.isEmpty(newsCommentDetail)){
            jsonObject.addProperty("newsCommentDetail", newsCommentDetail);
        }
        jsonObject.addProperty("newsCommentId", newsCommentInfo.getNewsCommentId());

        jsonObject.addProperty("newsId", newsCommentInfo.getNewsId());
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
        videoRepayDetail();
    }
    /**
     * 加载更多
     */
    private void loadMoreData() {
        state = STATE_MORE;
        page = ++page;
        videoRepayDetail();
    }

    /**
     * 刷新
     */
    private void refreshData() {
        state = STATE_REFREH;
        page = 1;
        videoRepayDetail();
    }



    /**
     * 展示数据
     */
    private void showData() {
        switch (state) {
            case STATE_NORMAL:
                mcommentadpater = new VideoCommentTwoAdpater(mContext, datas);
                recylerview.setLayoutManager(new LinearLayoutManager(this));
                recylerview.setItemAnimator(new DefaultItemAnimator());
                recylerview.setAdapter(mcommentadpater);

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

    /**
     */
    private void videoRepayDetail() {

        JsonObject jsonObject = new JsonObject();
        if (commentDetailData.getType() == 1){
            jsonObject.addProperty("boxingVideoCommentId", commentDetailData.getId());
        }else {
            jsonObject.addProperty("newsCommentId", commentDetailData.getId());

        }
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable;
        if (commentDetailData.getType() == 1) {
            observable =
                    ApiUtils.getApi().videoRepayDetail(requestBean)
                            .compose(RxHelper.getObservaleTransformer())
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {

                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread());
        }else {
            observable =
                    ApiUtils.getApi().repayInfo(requestBean)
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

                if (state != STATE_MORE) {
                    datas.clear();
                }
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();

                    if (commentDetailData.getType() == 1){
                        boxingVideoCommentInfo = data.getBoxingVideoCommentInfo();
                        ListData<NewsData> boxingVideoCommentRepayData = data.getBoxingVideoCommentRepayData();
                        total = boxingVideoCommentRepayData.getTotal();
                        datas.addAll(boxingVideoCommentRepayData.getList());
                    }else {
                        newsCommentInfo = data.getNewsCommentInfo();
                        ListData<NewsData> repayData = data.getRepayData();
                        total = repayData.getTotal();
                        datas.addAll(repayData.getList());
                    }

                }


                setData();

            }

            @Override
            protected void _onError(String message) {
                if (state != STATE_MORE) {
                    datas.clear();
                }
                setData();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    private void setData() {
        if (boxingVideoCommentInfo != null){
            if (!TextUtils.isEmpty(boxingVideoCommentInfo.getMemberHeadLogo())){
                UIUtils.loadImageView(mContext,boxingVideoCommentInfo.getMemberHeadLogo(),iv_head);
            }

            if (!TextUtils.isEmpty(boxingVideoCommentInfo.getMemberName())){
                tv_user_name.setText(boxingVideoCommentInfo.getMemberName());
            }
            if (!TextUtils.isEmpty(boxingVideoCommentInfo.getCreateTime())){
                tv_time.setText(boxingVideoCommentInfo.getCreateTime());
            }

            if (!TextUtils.isEmpty(boxingVideoCommentInfo.getBoxingVideoCommentDetail())){
                tv_content.setText(boxingVideoCommentInfo.getBoxingVideoCommentDetail());
            }
        }
        if (newsCommentInfo != null){
            if (!TextUtils.isEmpty(newsCommentInfo.getMemberLogo())){
                UIUtils.loadImageView(mContext,newsCommentInfo.getMemberLogo(),iv_head);
            }

            if (!TextUtils.isEmpty(newsCommentInfo.getMemberName())){
                tv_user_name.setText(newsCommentInfo.getMemberName());
            }
            if (!TextUtils.isEmpty(newsCommentInfo.getCreateTime())){
                tv_time.setText(DateTools.getFormat(Long.parseLong(newsCommentInfo.getCreateTime())));
            }

            if (!TextUtils.isEmpty(newsCommentInfo.getNewsComment())){
                tv_content.setText(newsCommentInfo.getNewsComment());
            }
        }

        tv_total_comment.setText(total+"");

        showData();
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }


    public static void startactivity(Context mContext, CommentDetailData commentDetailData){
        Intent mIntent = new Intent(mContext,CommentDetailActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,commentDetailData);
        mContext.startActivity(mIntent);
    }



}
