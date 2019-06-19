package com.oldwang.boxdemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.CouponDetailActivity;
import com.oldwang.boxdemo.adpater.AppoinmentAdpater;
import com.oldwang.boxdemo.adpater.CunponAdpater;
import com.oldwang.boxdemo.adpater.GetCashAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.CouponData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CouponFragmnet extends BaseFragment {
    @BindView(R.id.recyclerview)
    YRecycleview recyclerview;
    CunponAdpater madpater;
    private List<String> mlistdata = new ArrayList<>();

    private int position;


    private int page = 1 ;
    private final int size = 20;
    private int total;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况

    private ArrayList<CouponData> datas = new ArrayList<>();
    private Dialog mLoadingDialog;


    public static CouponFragmnet newInstance(int position) {
        CouponFragmnet f = new CouponFragmnet();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_yrecycleview);
    }

    @Override
    protected void initData() {
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt("position");
        }
        state = STATE_NORMAL;
        super.initData();
    }


    @Override
    protected void initEvent() {
        super.initEvent();
        recyclerview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }

            @Override
            public void onLoadMore() {
                int totalPaeg = total / size + (total % size == 0 ? 0:1);
                if (page < totalPaeg) {
                    loadMoreData();
                } else {
                    recyclerview.setloadMoreComplete();
                }
            }
        });
        getData(position);
    }

    /**
     * @param couponUseStatus 	优惠券状态（0未使用1以使用2未领取3已过期）
     * 我的预约
     */
    private void getData(int couponUseStatus) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("couponUseStatus", couponUseStatus);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().couponList(requestBean)
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

                if (state != STATE_MORE){
                    datas.clear();
                }

                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    if (data != null && data.getCouponData() != null && data.getCouponData().getList() != null){
                        datas.addAll(data.getCouponData().getList());
                        total = data.getCouponData().getTotal();

                    }
                }
                showData();
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                if (state != STATE_MORE){
                    datas.clear();
                }
                showData();
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);

    }


    /**
     * 加载更多
     */
    private void loadMoreData() {
        state = STATE_MORE;
        page = ++page;
        getData(position);
    }

    /**
     * 刷新
     */
    private void refreshData() {
        state = STATE_REFREH;
        page = 1;
        getData(position);
    }

    /**
     * 展示数据
     */
    private void showData() {
        switch (state) {
            case STATE_NORMAL:
                madpater = new CunponAdpater(mContext,datas,position);
                recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.setAdapter(madpater);
                madpater.setListClickLister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {
//                        CouponDetailActivity.startactivity(mContext);

                    }
                });
                break;
            case STATE_REFREH:
                madpater.notifyDataSetChanged();
                recyclerview.scrollToPosition(0);
                recyclerview.setReFreshComplete();
                break;
            case STATE_MORE:
                madpater.notifyDataSetChanged();
                recyclerview.setloadMoreComplete();
                break;
        }

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
    }
}
