package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.BankCardAdpater;
import com.oldwang.boxdemo.adpater.GetCashPagerAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.BankData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateBank;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/***
 * 新增或修改银行卡信息
 */
public class AddBankActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;



    @BindView(R.id.et_user_name)
    EditText et_user_name;

    @BindView(R.id.et_bank_name)
    EditText et_bank_name;

    @BindView(R.id.et_account)
    EditText et_account;

    private BankData bankData;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_bank);
    }



    @Override
    protected void initData() {
        super.initData();
        bankData = (BankData) getIntent().getSerializableExtra(Contans.INTENT_DATA);
        if (bankData != null){
            titlename.setText("修改银行卡信息");
            et_user_name.setText(bankData.getBankcardAccount());
            et_user_name.setSelection(et_user_name.getText().toString().length());

            et_bank_name.setText(bankData.getBankName());
            et_bank_name.setSelection(et_bank_name.getText().toString().length());

            et_account.setText(bankData.getBankcardNo());
            et_account.setSelection(et_account.getText().toString().length());

        }else {
            titlename.setText("新增银行卡信息");
        }
    }


    private void bankCardSave() {


        String bankName = et_bank_name.getText().toString().trim();
        String bankcardAccount = et_user_name.getText().toString().trim();
        String bankcardNo = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(bankName)) {
            ToastUtils.makeText("请输入银行名称");
            return;
        }

        if (TextUtils.isEmpty(bankcardAccount)) {
            ToastUtils.makeText("请输入银行名称");
            return;
        }
        if (TextUtils.isEmpty(bankcardNo)) {
            ToastUtils.makeText("请输入银行卡号");
            return;
        }
        if (!UIUtils.checkBankCard(bankcardNo)){
            ToastUtils.makeText("银行卡号不合法");
            return;
        }

        JsonObject jsonObject = new JsonObject();

        String bankId = "";
        if (bankData != null){
            bankId = bankData.getBankId();
        }

        if (!TextUtils.isEmpty(bankId)){
            jsonObject.addProperty("bankId", bankId);
        }
        jsonObject.addProperty("bankName", bankName);
        jsonObject.addProperty("bankcardAccount", bankcardAccount);
        jsonObject.addProperty("bankcardDefault", 0);
        jsonObject.addProperty("bankcardNo", bankcardNo);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().bankCardSave(requestBean)
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
                if (stringStatusCode != null) {
                    if (bankData != null){
                        ToastUtils.makeText("银行卡信息修改成功");
                    }else {
                        ToastUtils.makeText("银行卡信息添加成功");
                    }
                    EventBus.getDefault().post(new UpdateBank());
                    finish();
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }
    @OnClick(R.id.btn_submit)
    public void save(){
        bankCardSave();
    }


    public static void startactivity(Context mContext,BankData bankData){
        Intent mIntent = new Intent(mContext,AddBankActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,bankData);
        mContext.startActivity(mIntent);
    }


}
