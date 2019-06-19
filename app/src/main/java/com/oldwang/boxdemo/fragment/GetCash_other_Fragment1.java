package com.oldwang.boxdemo.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.AuthenticationIDCardActivity;
import com.oldwang.boxdemo.activity.BandingBankCardActivity;
import com.oldwang.boxdemo.activity.GetCashActivity;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.BankData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.MemberInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ScoresInfo;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.event.GetBank;
import com.oldwang.boxdemo.event.UpdateScoreEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class GetCash_other_Fragment1 extends BaseFragment {


    @BindView(R.id.tv_money)
    TextView tv_money;

    @BindView(R.id.tv_bank_name)
    TextView tv_bank_name;


    @BindView(R.id.et_money)
    EditText et_money;

    @BindView(R.id.tv_CanWithdrawalMoney)
    TextView tv_CanWithdrawalMoney;

    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.ll_score_momey)
    LinearLayout ll_score_momey;
    @BindView(R.id.et_score_money)
    TextView et_score_money;

    private Dialog mLoadingDialog;

    private MemberInfo userInfo;
    private String canWithdrawalMoney = "";
    private String bankId;

    //1积分提现 0 金额提现
    private int type;

    private String scores;
    private String cash;

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_getcashother1);
    }

    @Override
    protected void initData() {
        super.initData();
        getUserInfo();
        canWithdrawalMoney = ((GetCashActivity) mContext).getCanWithdrawalMoney();
        type = ((GetCashActivity) mContext).getType();

        if (type == 1){
            tv_money.setText("可提现积分");
            et_money.setHint("请输入提现积分数");
            tv_right.setVisibility(View.GONE);
            ll_score_momey.setVisibility(View.VISIBLE);
            et_money.setInputType(InputType.TYPE_CLASS_NUMBER);
            et_money.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String s = et_money.getText().toString();
                    if (!TextUtils.isEmpty(s)){
                        long temp = Long.valueOf(s);
                        BigDecimal bigDecimalScore = new BigDecimal(s);
                        BigDecimal money = bigDecimalScore.multiply(new BigDecimal(cash)).divide(new BigDecimal(scores),0, BigDecimal.ROUND_HALF_UP);
                        et_score_money.setText(money.toString());
                    }else {
                        et_score_money.setText("折算金额");
                    }

                }
            });
        }

        tv_CanWithdrawalMoney.setText(canWithdrawalMoney);
        scoreRuleInfo();
    }



    /**
     * 获取用户信息
     */
    private void getUserInfo() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().getUerInfo(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    userInfo = data.getMemberInfo();
                }

            }

            @Override
            protected void _onError(String message) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }


    /**
     * 获取积分折算规则
     */
    private void scoreRuleInfo() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().scoreRuleInfo(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);

                if (stringStatusCode != null){
                    DataInfo data = stringStatusCode.getData();

                    if (data != null){
                        ScoresInfo scoresInfo = data.getScoresInfo();

                        if (scoresInfo != null){
                            scores = scoresInfo.getConversion().getScores();
                            cash = scoresInfo.getConversion().getCash();
                        }
                    }
                }

            }

            @Override
            protected void _onError(String message) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }
    /**
     * 提现申请
     */
    private void memberWithdrawal(String withdrawalAmount) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("bankId", bankId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("withdrawalAmount", withdrawalAmount);
        //提现类型（0-提现余额 1-提现积分）
        jsonObject.addProperty("withdrawalScoresType", 0);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().memberWithdrawal(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if (stringStatusCode != null) {
                    ToastUtils.makeText("申请成功，等待管理员审核");

                    BigDecimal bigDecimal = new BigDecimal(canWithdrawalMoney).subtract(new BigDecimal(withdrawalAmount));
                    tv_CanWithdrawalMoney.setText(bigDecimal.toString());

                    ((GetCashActivity)mContext).setMyCurrentItem(1);
                    EventBus.getDefault().post(new UpdateScoreEvent());
                }

            }

            @Override
            protected void _onError(String message) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }


    /**
     * 提现申请积分
     */
    private void scoreWithdrawSave(String withdrawalScores,String withdrawalAmount) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("bankCardId", bankId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("withdrawalScores", withdrawalScores);
        jsonObject.addProperty("withdrawalAmount", withdrawalAmount);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().scoreWithdrawSave(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if (stringStatusCode != null) {
                    ToastUtils.makeText("申请成功，等待管理员审核");
                    ((GetCashActivity)mContext).setMyCurrentItem(1);
                    BigDecimal bigDecimal = new BigDecimal(canWithdrawalMoney).subtract(new BigDecimal(withdrawalScores));
                    tv_CanWithdrawalMoney.setText(bigDecimal.toString());
                    EventBus.getDefault().post(new UpdateScoreEvent());
                }

            }

            @Override
            protected void _onError(String message) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }


    @OnClick(R.id.ll_bank)
    public void chooseBank(){
        BandingBankCardActivity.startactivity(mContext,true);
    }


    @OnClick(R.id.btn_submit)
    public void submit(){

        String s = et_money.getText().toString();


        String identStates = userInfo.getIdentStates();





        if (!(identStates != null && identStates.equals("1"))){

            String title = "您还未进行身份认证，不能提现，是否立即前往认证？";

            if (!TextUtils.isEmpty(identStates)){
                if (identStates.equals("1")){
                     title = "您的身份认证被驳回，不能提现，是否立即前往重新认证？";
                }
                if (identStates.equals("0")){
                    title = "您的身份认证被正在审核中，暂时不能提现，是否前往查看？";
                }
            }
            AlertView alertView = new AlertView("提示", title, null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position==1){
                        //	状态(-1审核打回 0-待审核 1-审核通过)
                        if (!TextUtils.isEmpty(userInfo.getIdentStates())) {
                            AuthenticationIDCardActivity.startactivity(mContext,userInfo.getIdentStates());
                        }else {
                            AuthenticationIDCardActivity.startactivity(mContext,"");
                        }
                    }

                }
            });
            alertView.show();
            return;
        }

        if (bankData != null ){
            if (!bankData.getBankcardAccount().equals(userInfo.getRealName())){
                ToastUtils.makeText("你选择的银行卡和身份认证信息不符，请重新选择");
                return;
            }
        }

        if (type == 1){
            withdrawalScore(s);
        }else {
            withdrawalMoney(s);
        }
    }

    /***
     * 提现积分
     * @param s
     */
    private void withdrawalScore(String s) {

        String scoreMoney = et_score_money.getText().toString();

        if (TextUtils.isEmpty(s)){
            ToastUtils.makeText("请输入提现积分");
            return;
        }

        if (TextUtils.isEmpty(scoreMoney)){
            ToastUtils.makeText("请输入折算金额");
            return;
        }

        if (TextUtils.isEmpty(canWithdrawalMoney)){
            ToastUtils.makeText("未获取到可提现积分");
            return;
        }

        double canWithdrawal = 0;
        double withdrawalMoney = 0;

        try {
            canWithdrawal = Double.valueOf(canWithdrawalMoney);
            withdrawalMoney = Double.valueOf(s);

        }catch (Exception e){

        }

        if (canWithdrawal <= 0){
            ToastUtils.makeText("积分不能超过可提现积分");
            return;
        }

        if (canWithdrawal < withdrawalMoney){
            ToastUtils.makeText("积分不能超过可提现积分");
            return;
        }
        if (TextUtils.isEmpty(bankId)){
            ToastUtils.makeText("请选择提现账户");
            return;
        }

        scoreWithdrawSave(s,scoreMoney);
    }

    /***
     * 提现现金
     * @param s
     */
    private void withdrawalMoney(String s) {
        if (TextUtils.isEmpty(s)){
            ToastUtils.makeText("请输入提现金额");
            return;
        }


        if (TextUtils.isEmpty(canWithdrawalMoney)){
            ToastUtils.makeText("未获取到可提现金额");
            return;
        }

        double canWithdrawal = 0;
        double withdrawalMoney = 0;

        try {
            canWithdrawal = Double.valueOf(canWithdrawalMoney);
            withdrawalMoney = Double.valueOf(s);

        }catch (Exception e){

        }

        if (canWithdrawal <= 0){
            ToastUtils.makeText("金额不能超过可提现金额");
            return;
        }

        if (canWithdrawal < withdrawalMoney){
            ToastUtils.makeText("金额不能超过可提现金额");
            return;
        }
        if (TextUtils.isEmpty(bankId)){
            ToastUtils.makeText("请选择提现账户");
            return;
        }

        memberWithdrawal(s);
    }


    private BankData bankData;
    /**
     * 获取银行卡信息
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBank(GetBank event) {
        bankData = event.getBankData();
        bankId = bankData.getBankId();
        tv_bank_name.setText(bankData.getBankName()+bankData.getBankcardNo());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()){
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
