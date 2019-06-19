package com.oldwang.boxdemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.GetCashActivity;
import com.oldwang.boxdemo.adpater.AppoinmentAdpater;
import com.oldwang.boxdemo.adpater.GetCashAdpater;
import com.oldwang.boxdemo.adpater.GetCashScoreAdpater;
import com.oldwang.boxdemo.adpater.Home_other_adpater2;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.WithdrawalData;
import com.oldwang.boxdemo.event.UpdateNoticeEvent;
import com.oldwang.boxdemo.event.UpdateSchoolEvent;
import com.oldwang.boxdemo.event.UpdateScoreEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.YRecycleview;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class GetCash_other_Fragment2 extends BaseFragment {

    @BindView(R.id.recyclerview)
    YRecycleview recyclerview;

    private GetCashAdpater madpater ;
    private GetCashScoreAdpater scoreAdpater;


    private int page = 1 ;
    private final int size = 20;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况

    private ArrayList<WithdrawalData> datas = new ArrayList<>();
    private ArrayList<ScoreData> datas1 = new ArrayList<>();

    private Dialog mLoadingDialog;

    private int position = 0;

    //1积分提现 0 金额提现
    private int type;

    public static GetCash_other_Fragment2 newInstance(int position) {
        GetCash_other_Fragment2 f = new GetCash_other_Fragment2();
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
        type = ((GetCashActivity) mContext).getType();
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
     * @param withdrawalStates 0:待审核,1:审核失败,2:已提现
     * 我的预约
     */
    private void getData(int withdrawalStates) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        jsonObject.addProperty("withdrawalStates", withdrawalStates);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);

        if (type == 0){
            Observable observable =
                    ApiUtils.getApi().memberWithdrawalList(requestBean)
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
                        if (data != null && data.getWithdrawalData() != null && data.getWithdrawalData().getList() != null){
                            datas.addAll(data.getWithdrawalData().getList());
                            total = data.getWithdrawalData().getTotal();

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
        }else {
            Observable observable =
                    ApiUtils.getApi().scoreWithdrawalList(requestBean)
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
                        datas1.clear();
                    }

                    if (stringStatusCode != null) {
                        DataInfo data = stringStatusCode.getData();
                        if (data != null && data.getScoreData() != null && data.getScoreData().getList() != null){
                            datas1.addAll(data.getScoreData().getList());
                            total = data.getScoreData().getTotal();

                        }
                    }
                    showData();
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.makeText(message);
                    if (state != STATE_MORE){
                        datas1.clear();
                    }
                    showData();
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                }
            }, "", lifecycleSubject, false, true);
        }



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

        if (type == 0){
            switch (state) {
                case STATE_NORMAL:
                    madpater = new GetCashAdpater(mContext,datas,position);
                    recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                    recyclerview.setItemAnimator(new DefaultItemAnimator());
                    recyclerview.setAdapter(madpater);
                    madpater.setlistonclicklister(new ListOnclickLister() {
                        @Override
                        public void onclick(View v, int position) {


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
        }else {
            switch (state) {
                case STATE_NORMAL:
                    scoreAdpater = new GetCashScoreAdpater(mContext,datas1,position);
                    recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                    recyclerview.setItemAnimator(new DefaultItemAnimator());
                    recyclerview.setAdapter(scoreAdpater);
                    scoreAdpater.setlistonclicklister(new ListOnclickLister() {
                        @Override
                        public void onclick(View v, int position) {


                        }
                    });
                    break;
                case STATE_REFREH:
                    scoreAdpater.notifyDataSetChanged();
                    recyclerview.scrollToPosition(0);
                    recyclerview.setReFreshComplete();
                    break;
                case STATE_MORE:
                    scoreAdpater.notifyDataSetChanged();
                    recyclerview.setloadMoreComplete();
                    break;
            }
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateList(UpdateScoreEvent event) {
        refreshData();
    }
}
