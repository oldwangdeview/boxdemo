package com.oldwang.boxdemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.AssembleListQuanlianActivity;
import com.oldwang.boxdemo.adpater.QuanLianFindDataAdpater;
import com.oldwang.boxdemo.adpater.QuanLianFindGrideAdpater;
import com.oldwang.boxdemo.adpater.QuanLianFindLeftAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.CommodityTypeData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.event.UpdateBank;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.interfice.OrderAdpaterClickGoodsLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.MyGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ClassifcationFragment extends BaseFragment {


    @BindView(R.id.mlistview)
    ListView mlistview;

    @BindView(R.id.myGridView)
    MyGridView myGridView;

    QuanLianFindLeftAdpater mlefteadpater;

    QuanLianFindGrideAdpater mdatapater;

    private Dialog mLoadingDialog;

    private List<CommodityTypeData> listDatas = new ArrayList<>();

    private List<CommodityTypeData> listTwoDatas = new ArrayList<>();

    private String commodityTypePid = "0";
    private int level = 1;

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext,R.layout.fragment_classification);
    }

    @Override
    protected void initData() {
        super.initData();
        commodityTypePid = "0";
        level = 1;
        listDatas.clear();
        listTwoDatas.clear();
        mlefteadpater = new QuanLianFindLeftAdpater(mContext,listDatas);
        mlistview.setAdapter(mlefteadpater);
        mlefteadpater.setListClickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                level = 2;
                mlefteadpater.SetSeletePosition(position);
                commodityTypePid = listDatas.get(position).getCommodityTypeId();
                getData();

            }
        });

        mdatapater = new QuanLianFindGrideAdpater(mContext,listTwoDatas);
        mdatapater.setListClicklister(new ListOnclickLister() {

            @Override
            public void onclick(View v, int position) {
                AssembleListQuanlianActivity.startactivity(mContext,1,listTwoDatas.get(position).getCommodityTypeId(),listTwoDatas.get(position).getCommodityTypeName());

            }
        });
//        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerview.setItemAnimator(new DefaultItemAnimator());
        myGridView.setAdapter(mdatapater);
        getData();
    }



    /**
     * 商品分类
     */
    private void getData() {

        JsonObject jsonObject = new JsonObject();
        if (!TextUtils.isEmpty(commodityTypePid)){
            jsonObject.addProperty("commodityTypePid", commodityTypePid);
        }
        jsonObject.addProperty("level", level);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().commodityTypeList(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                }
//                                LoadingDialogUtils.show(mLoadingDialog);

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                listTwoDatas.clear();
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    List<CommodityTypeData> commodityTypeData = data.getCommodityTypeData();

                    if (level == 1 && commodityTypeData != null){
                        listDatas.addAll(commodityTypeData);
                        mlefteadpater.SetSeletePosition(0);
                        if (listDatas.size() > 0){
                            level = 2;
                            commodityTypePid = listDatas.get(0).getCommodityTypeId();
                            getData();
                        }

                    }else{
                        if (commodityTypeData != null){
                            listTwoDatas.addAll(commodityTypeData);
                        }
                    }


                }
                mdatapater.notifyDataSetChanged();
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }

            @Override
            protected void _onError(String message) {
                listTwoDatas.clear();
                mdatapater.notifyDataSetChanged();
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);

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
