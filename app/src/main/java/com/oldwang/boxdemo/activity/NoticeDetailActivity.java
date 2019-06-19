package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.NoticeData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateMessageEvent;
import com.oldwang.boxdemo.event.UpdateNoticeEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * 公告详情
 */
public class NoticeDetailActivity extends BaseActivity {


    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.content)
    TextView content;


    private NoticeData noticeData;

    private ScoreData messageData;

    private boolean isMessage;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_notice_detail);
    }


    @Override
    protected void initData() {
        super.initData();

        isMessage = getIntent().getBooleanExtra(Contans.INTENT_TYPE,false);

        if (isMessage){
            messageData = (ScoreData) getIntent().getSerializableExtra(Contans.INTENT_DATA);
            titlename.setText("消息详情");
            msgRead();
            if (messageData != null){
                if (!TextUtils.isEmpty(messageData.getMessageTitle())){
                    tv_title.setText(messageData.getMessageTitle());
                }
                if (!TextUtils.isEmpty(messageData.getMessageContent())){
                    content.setText(messageData.getMessageContent());
                }
                if (!TextUtils.isEmpty(messageData.getCreateTime())){
                    tv_time.setText(DateTools.getFormat(Long.parseLong(messageData.getCreateTime()), "yyyy-MM-dd HH:mm"));
                }
            }
        }else {
            noticeData = (NoticeData) getIntent().getSerializableExtra(Contans.INTENT_DATA);
            titlename.setText("场馆公告");

            if (noticeData != null){
                if (!TextUtils.isEmpty(noticeData.getNoticeTitle())){
                    tv_title.setText(noticeData.getNoticeTitle());
                }
                if (!TextUtils.isEmpty(noticeData.getNoticeReleaseTime())){
                    tv_time.setText(noticeData.getNoticeReleaseTime());
                }
                if (!TextUtils.isEmpty(noticeData.getNoticeReleaseContent())){
                    content.setText(noticeData.getNoticeReleaseContent());
                }
            }
        }

    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }

    /**
     * 消息阅读
     */
    private void msgRead() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("msgInfoId",messageData.getMsgInfoId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().msgRead(requestBean)
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

                if (stringStatusCode != null) {
                    EventBus.getDefault().post(new UpdateNoticeEvent());
                    dismissLoadingDialog();
                }
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }


    public static void startactivity(Context mContext, NoticeData noticeData) {
        Intent mIntent = new Intent(mContext, NoticeDetailActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA, noticeData);
        mContext.startActivity(mIntent);
    }

    public static void startactivity(Context mContext, ScoreData messageData, boolean isMessage) {
        Intent mIntent = new Intent(mContext, NoticeDetailActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA, messageData);
        mIntent.putExtra(Contans.INTENT_TYPE, isMessage);
        mContext.startActivity(mIntent);
    }
}
