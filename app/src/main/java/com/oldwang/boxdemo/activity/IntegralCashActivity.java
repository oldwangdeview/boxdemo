package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.WithdrawalInfo;
import com.oldwang.boxdemo.event.JumpTeachers;
import com.oldwang.boxdemo.event.UpdateScoreEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LogUntil;
import com.oldwang.boxdemo.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 积分提现
 */
public class IntegralCashActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;


    private WithdrawalInfo scoreInfo;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_integralcash);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("积分提现");
        myScore();

    }

    /**
     * 我的积分
     */
    private void myScore() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().myScore(requestBean)
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
                     scoreInfo = data.getScoreInfo();
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    /**
     * 排行榜
     * @param v
     */
    @OnClick({R.id.layout_rankinglist1,R.id.layout_rankinglist2})
    public void ClickRankinglist(View v){

        switch (v.getId()){
            case R.id.layout_rankinglist1:
                RankingListActivity.startactivity(this,1);
                break;
            case R.id.layout_rankinglist2:
                RankingListActivity.startactivity(this,2);
                break;
        }


    }



    @OnClick(R.id.layout_integralrule)
    public void gotoIntegralrueActivity(){
        IntegralruleActivity.startactivity(this);
    }


    @OnClick(R.id.ll_score)
    public void goScoreWithdrawal(){

        if (scoreInfo != null){
            GetCashActivity.startactivity(this,scoreInfo.getNowScore(),1);
        }else {
            ToastUtils.makeText("未获取到可提现信息，请稍后再试");
        }
    }



    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,IntegralCashActivity.class);
        mContext.startActivity(mIntent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateScore(UpdateScoreEvent event) {
        myScore();
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
