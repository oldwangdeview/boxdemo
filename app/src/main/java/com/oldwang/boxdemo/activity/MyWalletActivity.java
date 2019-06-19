package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.VenueData;
import com.oldwang.boxdemo.bean.WithdrawalInfo;
import com.oldwang.boxdemo.event.UpdateScoreEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
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
 * 我的钱包
 */
public class MyWalletActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.tv_incomeTotal)
    TextView tv_incomeTotal;

    @BindView(R.id.tv_CanWithdrawalMoney)
    TextView tv_CanWithdrawalMoney;

    @BindView(R.id.tv_frozenMoney)
    TextView tv_frozenMoney;

    private WithdrawalInfo withdrawalInfo;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_mywallet);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("我的钱包");
        withdrawalMoneyInfo();
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,MyWalletActivity.class);
        mContext.startActivity(mIntent);
    }

    /**
     * 获取提现信息
     */
    private void withdrawalMoneyInfo() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().withdrawalMoneyInfo(requestBean)
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
                     withdrawalInfo = data.getWithdrawalInfo();
                     setWithdrawalData();
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }


    /***
     * 设置用户提现信息
     */
    private void setWithdrawalData() {
        if (withdrawalInfo != null){
            if (!TextUtils.isEmpty(withdrawalInfo.getCanWithdrawalMoney())){
                tv_CanWithdrawalMoney.setText(withdrawalInfo.getCanWithdrawalMoney());
            }

            if (!TextUtils.isEmpty(withdrawalInfo.getFrozenMoney())){
                tv_frozenMoney.setText(withdrawalInfo.getFrozenMoney());
            }

            if (!TextUtils.isEmpty(withdrawalInfo.getIncomeTotal())){
                tv_incomeTotal.setText(withdrawalInfo.getIncomeTotal());
            }
        }
    }

    /**
     * 提现
     */
    @OnClick(R.id.button_getcash)
    public void gotoGetCashActivity(){
        if (withdrawalInfo != null){
            GetCashActivity.startactivity(this,withdrawalInfo.getCanWithdrawalMoney(),0);
        }else {
            ToastUtils.makeText("未获取到可提现信息，请稍后再试");
        }
    }

    /**
     * 收益记录
     */
    @OnClick(R.id.layout_incomerecord)
    public void gotoIncomercord(){
        IncomeRecordActivity.startactivity(mContext);
    }

    /**
     * 积分提现
     */
    @OnClick(R.id.layout_intgarcash)
    public void gotoIntareCashActivity(){
        IntegralCashActivity.startactivity(mContext);
    }

    /**
     * 我的团队
     */
    @OnClick(R.id.ll_team)
    public void gotoMyTeam(){
        MyTeamActivity.startactivity(this);
    }

    /**
     * 申请代理
     */
    @OnClick(R.id.layout_getagent)
    public void gotoGetAgent(){
        GetAgentActivity.startactivity(this);
    }

    /**
     * 绑定银行卡
     */
    @OnClick(R.id.layout_bangdingbankcard)
    public void gotoBandingBankcard(){
        BandingBankCardActivity.startactivity(this,false);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateScore(UpdateScoreEvent event) {
        withdrawalMoneyInfo();
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
