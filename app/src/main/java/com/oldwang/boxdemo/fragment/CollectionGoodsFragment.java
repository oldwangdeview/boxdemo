package com.oldwang.boxdemo.fragment;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.GoodsDetailActivity;
import com.oldwang.boxdemo.activity.GoodsOrderActivity;
import com.oldwang.boxdemo.activity.VideoDetailActivity;
import com.oldwang.boxdemo.adpater.CollectionGoosAdpater;
import com.oldwang.boxdemo.adpater.GetCashAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.VenueData;
import com.oldwang.boxdemo.event.UpdateCollectionEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 收藏商品
 */
public class CollectionGoodsFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<ScoreData> mlistdata = new ArrayList<>();
    private CollectionGoosAdpater madpater ;

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_item_home_other1);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        favoriteListByType(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateListData(UpdateCollectionEvent event){

        if(event.position==0){
            switch (event.updatatype){
                case 0:
                    madpater.setAddDatetype(false);
                    break;
                case 1:
                    madpater.setAddDatetype(true);
                    break;
                case 2:
                    String favoriteId = "";
                    for (ScoreData scoreData : mlistdata) {

                        if (scoreData.isCheck()){
                            favoriteId += scoreData.getFavoriteId() + ",";
                        }
                    }
                    if (TextUtils.isEmpty(favoriteId)){
                        ToastUtils.makeText("请选择需要删除的收藏商品");
                        return;
                    }
                    delFavorite(favoriteId);

                    break;
            }
        }

    }
    /**
     * @param favoriteId 收藏id
     * 删除收藏
     */
    private void delFavorite(String favoriteId) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("favoriteId", favoriteId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(getContext(), jsonObject);
        Observable observable =
                ApiUtils.getApi().delFavorite(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(getContext()) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                if (stringStatusCode != null) {
                    ToastUtils.makeText("删除成功");
                    madpater.DeletAllData();
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    /**
     * @param favoriteType 0商品1视频
     * 我的收藏
     */
    private void favoriteListByType(final int favoriteType) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("favoriteType", favoriteType);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(getContext(), jsonObject);
        Observable observable =
                ApiUtils.getApi().favoriteListByType(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(getContext()) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    ListData<ScoreData> scoreData = data.getScoreData();
                    List<ScoreData> list = scoreData.getList();
                    mlistdata.clear();;
                    mlistdata.addAll(list);
                    madpater = new CollectionGoosAdpater(mContext,mlistdata);
                    madpater.setAddDatetype(false);
                    recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                    recyclerview.setItemAnimator(new DefaultItemAnimator());
                    recyclerview.setAdapter(madpater);

                    madpater.setListOnclickLister(new ListOnclickLister() {
                        @Override
                        public void onclick(View v, int position) {
                            GoodsDetailActivity.startactivity(mContext,false,mlistdata.get(position).getFavoriteObjectId(),null);
                        }
                    });


                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}