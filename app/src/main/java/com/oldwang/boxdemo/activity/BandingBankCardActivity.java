package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.BankCardAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.BankData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.MemberInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.GetBank;
import com.oldwang.boxdemo.event.UpdateBank;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.view.MyGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 绑定银行卡
 */
public class BandingBankCardActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.mygridview)
    MyGridView mygridview;

    BankCardAdpater bankCardAdpater;

    private boolean isChoose;
    private MemberInfo userInfo;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_bandingbankcard);
    }

    @Override
    protected void initData() {
        super.initData();
        isChoose = getIntent().getBooleanExtra(Contans.INTENT_TYPE, isChoose);
        if (isChoose) {
            titlename.setText("选择银行卡");
        } else {
            titlename.setText("绑定银行卡");
        }
        getUserInfo();
        bankCardList();
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
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    userInfo = data.getMemberInfo();
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    /**
     * 银行卡列表
     */
    private void bankCardList() {


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().bankCardList(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<List<BankData>>(mContext) {
            @Override
            protected void _onNext(StatusCode<List<BankData>> stringStatusCode) {
                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    final List<BankData> data = stringStatusCode.getData();
                    if (data != null) {
                        bankCardAdpater = new BankCardAdpater(mContext, data);
                        mygridview.setAdapter(bankCardAdpater);
                        mygridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                BankData bankData = data.get(i);
                                if (isChoose) {
                                    EventBus.getDefault().post(new GetBank(bankData));
                                    finish();
                                } else {
                                    AddBankActivity.startactivity(mContext, bankData);
                                }
                            }
                        });

                        bankCardAdpater.SetListonclicklister(new ListOnclickLister() {
                            @Override
                            public void onclick(View v, int position) {
                                final BankData bankData = data.get(position);
                                AlertView alertView = new AlertView("提示", "是删除该条银行卡信息？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(Object o, int position) {
                                        if (position == 1) {
                                            bankCardDel(bankData.getBankId());
                                        }

                                    }
                                });
                                alertView.show();
                            }
                        });
                    }


                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }


    private void bankCardDel(String bankId) {


        JsonObject jsonObject = new JsonObject();


        jsonObject.addProperty("bankId", bankId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().bankCardDel(requestBean)
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
                    ToastUtils.makeText("删除成功");
                    bankCardList();
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    public static void startactivity(Context mContext, boolean isChoose) {
        Intent mIntent = new Intent(mContext, BandingBankCardActivity.class);
        mIntent.putExtra(Contans.INTENT_TYPE, isChoose);
        mContext.startActivity(mIntent);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }

    @OnClick(R.id.ll_add_bank)
    public void addBank() {

        if (userInfo == null) {
            ToastUtils.makeText("正在努力获取用户信息请稍后再试。");
            getUserInfo();
            return;
        }
        String identStates = userInfo.getIdentStates();


        if (!(identStates != null && (identStates.equals("1")) || identStates.equals("0")) ) {

            String title = "您还未进行身份认证，不能新增银行卡，是否立即前往认证？";

            if (!TextUtils.isEmpty(identStates)) {
                if (identStates.equals("1")) {
                    title = "您的身份认证被驳回，不能新增银行卡，是否立即前往重新认证？";
                }
                if (identStates.equals("0")) {
                    title = "您的身份认证被正在审核中，暂时不新增银行卡，是否前往查看审核情况？";
                }
            }
            AlertView alertView = new AlertView("提示", title, null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position == 1) {
                        //	状态(-1审核打回 0-待审核 1-审核通过)
                        if (!TextUtils.isEmpty(userInfo.getIdentStates())) {
                            AuthenticationIDCardActivity.startactivity(mContext, userInfo.getIdentStates());
                        } else {
                            AuthenticationIDCardActivity.startactivity(mContext, "");
                        }
                    }

                }
            });
            alertView.show();
            return;
        }

        AddBankActivity.startactivity(mContext, null);
    }

    /**
     * 更新当前页面
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBank(UpdateBank event) {
        bankCardList();
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
