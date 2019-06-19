package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.BoxVideoListAdpater;
import com.oldwang.boxdemo.adpater.HomeFindAdpater;
import com.oldwang.boxdemo.adpater.VideoFindAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.VideoData;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.KeybordS;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.SoftKeyboardStateHelper;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.view.TextEditTextView;
import com.oldwang.boxdemo.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class VideoFindAvtivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.rv_video)
    YRecycleview rv_video;


    @BindView(R.id.inpout_finddata)
    EditText inpout_finddata;


    private BoxVideoListAdpater madpater;
    private ArrayList<VideoData> datas = new ArrayList<>();

    private int page = 1 ;
    private final int size = 20;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况

    private List<String> historyList = new ArrayList<>();
    private HomeFindAdpater homeFindAdpater;
    private String keyword = "";
    private String memberAccount;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_videofind);
    }


    @Override
    protected void initData() {
        super.initData();

        memberAccount = BaseActivity.getUserInfo(mContext).getMemberAccount();
        if (TextUtils.isEmpty(memberAccount)) {
            memberAccount = "";
        }
        String temp = PreferencesUtils.getInstance().getString(Contans.HISTORY_FIVE_USER + memberAccount,"");

        if (!TextUtils.isEmpty(temp)){
            historyList = new Gson().fromJson(temp,new TypeToken<List<String >>() {}.getType());
        }
        homeFindAdpater = new HomeFindAdpater(mContext,historyList);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(madpater);
        homeFindAdpater.setonclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int zposition) {
                switch (v.getId()){
                    case R.id.ll_item:
                        keyword = historyList.get(zposition);
                        if (!TextUtils.isEmpty(keyword)){
                            inpout_finddata.setText(keyword.length());
                        }
                        KeybordS.closeKeybord(inpout_finddata, mContext);
                        refreshData();
                        break;
                    case R.id.deletet_image:
                        historyList.remove(zposition);
                        homeFindAdpater.notifyDataSetChanged();
                        PreferencesUtils.getInstance().putString(Contans.HISTORY_FIVE_USER + memberAccount,new Gson().toJson(historyList));
                        break;
                }
            }
        });

        inpout_finddata.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                   keyword = inpout_finddata.getText().toString().trim();
                    if (TextUtils.isEmpty(keyword)) {
                        keyword = "";
                    }
                    KeybordS.closeKeybord(inpout_finddata, mContext);
                    recyclerview.setVisibility(View.GONE);
                    rv_video.setVisibility(View.VISIBLE);
                    refreshData();
                    return false;
                }
                return false;

            }
        });
        setListenerFotEditText(inpout_finddata);

    }
    private void setListenerFotEditText(View view){
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(view);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                //键盘打开
                recyclerview.setVisibility(View.VISIBLE);
                rv_video.setVisibility(View.GONE);
            }

            @Override
            public void onSoftKeyboardClosed() {
                //键盘关闭
                recyclerview.setVisibility(View.GONE);
                rv_video.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,VideoFindAvtivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick(R.id.quxiao_btn)
    public void overactivity() {
        finish();
    }



    @Override
    protected void initEvent() {
        super.initEvent();
        rv_video.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
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
                    rv_video.setloadMoreComplete();
                }
            }
        });
        getData();
    }

    /**
     * 视频列表
     */
    private void getData() {

        showLoadingDialog();
        JsonObject jsonObject = new JsonObject();
        if (!TextUtils.isEmpty(keyword)){
            jsonObject.addProperty("keyword", keyword);
        }

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().videoList(requestBean)
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
                if (state != STATE_MORE){
                    datas.clear();
                }

                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    ListData<VideoData> videoData = data.getVideoData();
                    List<VideoData> list = videoData.getList();
                    datas.addAll(list);
                    total = videoData.getTotal();
                }
                showData();

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                if (state != STATE_MORE){
                    datas.clear();
                }
                dismissLoadingDialog();
                showData();
            }
        }, "", lifecycleSubject, false, true);

    }


    /**
     * 加载更多
     */
    private void loadMoreData() {
        state = STATE_MORE;
        page = ++page;
        getData();
    }

    /**
     * 刷新
     */
    private void refreshData() {
        state = STATE_REFREH;
        page = 1;
        getData();
    }

    /**
     * 展示数据
     */
    private void showData() {
        switch (state) {
            case STATE_NORMAL:
                madpater = new BoxVideoListAdpater(mContext,datas);
                rv_video.setLayoutManager(new LinearLayoutManager(this));
                rv_video.setItemAnimator(new DefaultItemAnimator());
                rv_video.setAdapter(madpater);
                madpater.setListOnclickLister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {
                        final VideoData videoData = datas.get(position);
                        switch (v.getId()){
                            //进入视频播放页面
                            case R.id.ll_item:
                                VideoDetailActivity.startactivity(mContext,videoData.getBoxingVideoId());
                                break;
                        }
                    }
                });
                break;
            case STATE_REFREH:
                madpater.notifyDataSetChanged();
                rv_video.scrollToPosition(0);
                rv_video.setReFreshComplete();
                break;
            case STATE_MORE:
                madpater.notifyDataSetChanged();
                rv_video.setloadMoreComplete();
                break;
        }

    }
}
