package com.oldwang.boxdemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.GoodsDetailCommentActivity;
import com.oldwang.boxdemo.adpater.GoodsCommentAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.CommodityChildCommentData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
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

public class GoodsCommentFragmnet extends BaseFragment {
    @BindView(R.id.recyclerview)
    YRecycleview recyclerview;

    private GoodsCommentAdpater madpater;
    private String type = "";
    private List<String> listdata =new ArrayList<>();
    private int page = 1 ;
    private final int size = 20;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况

    private ArrayList<CommodityChildCommentData> datas = new ArrayList<>();
    private Dialog mLoadingDialog;
    private String commodityId;


    @Override
    public View initView(Context context) {
        if (getArguments() != null) {
            type = getArguments().getString("position");

        }
        return UIUtils.inflate(mContext, R.layout.fragment_yrecycleview);
    }

    @Override
    protected void initData() {
        super.initData();
        commodityId = ((GoodsDetailCommentActivity)mContext).getCommodityId();
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
        getData(type);
    }

    /**
     * @param flag
     * 商品评论列表
     */
    private void getData(String flag) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commodityId", commodityId);
        jsonObject.addProperty("flag", flag);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().commodityCommentInfo(requestBean)
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
                    if (data != null){
                        ListData<CommodityChildCommentData> commodityCommentData = data.getCommodityCommentData();
                        ((GoodsDetailCommentActivity)mContext).setCount(data.getCommodityCommentInfo());
                        if (commodityCommentData != null){
                            if (commodityCommentData.getList() != null){
                                datas.addAll(commodityCommentData.getList());
                                total = commodityCommentData.getTotal();
                            }
                        }
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
        getData(type);
    }

    /**
     * 刷新
     */
    private void refreshData() {
        state = STATE_REFREH;
        page = 1;
        getData(type);
    }

    /**
     * 展示数据
     */
    private void showData() {
        switch (state) {
            case STATE_NORMAL:
                madpater = new GoodsCommentAdpater(mContext,datas);
                recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.setAdapter(madpater);
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
