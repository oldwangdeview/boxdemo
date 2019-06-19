package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/***
 * 帮助与反馈
 */
public class HelpActivity extends BaseActivity {


    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.content)
    TextView content;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_help);
    }


    @Override
    protected void initData() {
        super.initData();
        titlename.setText("帮助中心");
        getInfo();
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }
    @OnClick(R.id.tv_hint)
    public void gotoCustomerActivity(){
        CustomerActivity.startactivity(mContext);
    }


    private void getInfo() {


        JsonObject jsonObject = new JsonObject();
        //0-关于我们，1-帮助信息，2-客服电话
        jsonObject.addProperty("type", 1);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().getInfo(requestBean)
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

                    if (!TextUtils.isEmpty(stringStatusCode.getData().getInfo())){
                        content.setText(stringStatusCode.getData().getInfo());
//                        content.setText(Html.fromHtml(stringStatusCode.getData().getInfo()));
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


    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,HelpActivity.class);
        mContext.startActivity(mIntent);
    }



}
