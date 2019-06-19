package com.oldwang.boxdemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.HomeFindActivity;
import com.oldwang.boxdemo.activity.HomeSearchResultFourActivity;
import com.oldwang.boxdemo.activity.HomeSearchResultOneActivity;
import com.oldwang.boxdemo.activity.HomeSearchResultThreeActivity;
import com.oldwang.boxdemo.activity.HomeSearchResultTwoActivity;
import com.oldwang.boxdemo.adpater.AppoinmentAdpater;
import com.oldwang.boxdemo.adpater.HomeFindAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.HotData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.JumpTeachers;
import com.oldwang.boxdemo.event.UpdateHistoryEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.LogUntil;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.XCFlowLayout;
import com.oldwang.boxdemo.view.YRecycleview;

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

public class HomeSearchFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private HomeFindAdpater homeFindAdpater;


    @BindView(R.id.xcflowlayout)
    XCFlowLayout xcflowlayout;

    @BindView(R.id.ll_hot)
    LinearLayout ll_hot;

    private Dialog mLoadingDialog;

    private int position = 0;
    private String memberAccount;

    public static HomeSearchFragment newInstance(int position) {
        HomeSearchFragment f = new HomeSearchFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_home_search);
    }

    @Override
    protected void initData() {
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt("position");
        }
        if (position == 0){
            getHomeSearch();
        }


        homeFindAdpater = new HomeFindAdpater(mContext, historyList);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(homeFindAdpater);
        homeFindAdpater.setonclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int zposition) {
                String content = historyList.get(zposition);
                ((HomeFindActivity)mContext).setContent(content);
                switch (v.getId()){
                    case R.id.ll_item:
                        switch (position){
                            case 0:
                                HomeSearchResultOneActivity.startactivity(mContext, content);
                                break;
                            case 1:
                                HomeSearchResultTwoActivity.startactivity(mContext, content);
                                break;
                            case 2:
                                HomeSearchResultThreeActivity.startactivity(mContext, content);
                                break;
                            case 3:
                                HomeSearchResultFourActivity.startactivity(mContext, content);
                                break;
                        }

                        break;
                    case R.id.deletet_image:
                        historyList.remove(zposition);
                        homeFindAdpater.notifyDataSetChanged();

                        switch (position){
                            case 0:
                                PreferencesUtils.getInstance().putString(Contans.HISTORY_ONE_USER + memberAccount,new Gson().toJson(historyList));
                                break;
                            case 1:
                                PreferencesUtils.getInstance().putString(Contans.HISTORY_TWO_USER + memberAccount,new Gson().toJson(historyList));
                                break;
                            case 2:
                                PreferencesUtils.getInstance().putString(Contans.HISTORY_THREE_USER + memberAccount,new Gson().toJson(historyList));
                                break;
                            case 3:
                                PreferencesUtils.getInstance().putString(Contans.HISTORY_FOUR_USER + memberAccount,new Gson().toJson(historyList));
                                break;
                        }
                        break;
                }
            }
        });
        super.initData();
    }


    /**
     * 添加搜索标签
     * @param list
     */
    private void initChildViews(List<String> list) {
        if (position != 0 || list == null || list.size() < 1){
            return;
        }
        ll_hot.setVisibility(View.VISIBLE);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        for (int i = 0; i < list.size(); i++) {
            View bqview = UIUtils.inflate(mContext,R.layout.item_homefindbq);
            TextView mtext = bqview.findViewById(R.id.text);
            mtext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = ((TextView)v).getText().toString();
                    ((HomeFindActivity)mContext).setContent(text);
                    if (position == 0){
                        HomeSearchResultOneActivity.startactivity(mContext,text);
                    }
                }
            });
            mtext.setText(list.get(i));
            xcflowlayout.addView(bqview,lp);

        }
    }

    private  List<String> historyList = new ArrayList<>();
    @Override
    protected void initEvent() {
        super.initEvent();
        memberAccount = BaseActivity.getUserInfo(mContext).getMemberAccount();
        if (TextUtils.isEmpty(memberAccount)) {
            memberAccount = "";
        }
        setHistoryData();
    }

    private void setHistoryData() {


        List<String> tempList = new ArrayList<>();
        String temp = "";
        switch (position){
            case 0:
                 temp = PreferencesUtils.getInstance().getString(Contans.HISTORY_ONE_USER+ memberAccount,"");
                break;
            case 1:
                 temp = PreferencesUtils.getInstance().getString(Contans.HISTORY_TWO_USER + memberAccount,"");
                break;
            case 2:
                 temp = PreferencesUtils.getInstance().getString(Contans.HISTORY_THREE_USER + memberAccount,"");
                break;
            case 3:
                 temp = PreferencesUtils.getInstance().getString(Contans.HISTORY_FOUR_USER + memberAccount,"");
                break;
        }

        if (!TextUtils.isEmpty(temp)){
            tempList = new Gson().fromJson(temp,new TypeToken<List<String >>() {}.getType());
        }
        historyList.clear();
        if (tempList != null && tempList.size() > 0){
            historyList.addAll(tempList);
        }

        homeFindAdpater.notifyDataSetChanged();
    }

    private void getHomeSearch() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().hotSearch(requestBean)
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

                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    List<String> list = data.getHotData();
                    initChildViews(list);
                }

                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }

            @Override
            protected void _onError(String message) {
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
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateHostory(UpdateHistoryEvent event) {
        setHistoryData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
}

