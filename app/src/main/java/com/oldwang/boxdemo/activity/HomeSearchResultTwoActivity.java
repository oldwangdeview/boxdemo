package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.OnthespotRecordAdpater;
import com.oldwang.boxdemo.adpater.QuanLianListDataAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.CommodityTeamBuyData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.NewsData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateHistoryEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.view.YRecycleview;

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
 *  首页装备搜索结果
 */
public class HomeSearchResultTwoActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    YRecycleview recylerview;

    @BindView(R.id.inpout_finddata)
    EditText inpout_finddata;

    private OnthespotRecordAdpater madpater;

    private ArrayList<NewsData> datas = new ArrayList<>();


    private int page = 1;
    private final int size = 20;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;       //正常情况

    private String  memberAccount;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_home_search_result);
    }

    private List<String> historyList = new ArrayList<>();

    @Override
    protected void initData() {
        super.initData();
        keyword = getIntent().getStringExtra(Contans.INTENT_DATA);
        memberAccount = BaseActivity.getUserInfo(mContext).getMemberAccount();
        if (TextUtils.isEmpty(memberAccount)) {
            memberAccount = "";
        }
        String temp = PreferencesUtils.getInstance().getString(Contans.HISTORY_TWO_USER + memberAccount, "");
        if (!TextUtils.isEmpty(keyword)) {
            inpout_finddata.setText(keyword);
            inpout_finddata.setSelection(keyword.length());
        }

        if (!TextUtils.isEmpty(temp)) {
            historyList = new Gson().fromJson(temp, new TypeToken<List<String>>() {
            }.getType());
        }
        inpout_finddata.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = inpout_finddata.getText().toString().trim();
                    state = STATE_REFREH;
                    page = 1;
                    getData();
                }
                return false;
            }
        });
        getData();
    }


    public static void startactivity(Context mContext, String keyword) {
        Intent mIntent = new Intent(mContext, HomeSearchResultTwoActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA, keyword);
        mContext.startActivity(mIntent);
    }

    @OnClick({R.id.quxiao_btn, R.id.iv_clear})
    public void switchSearch(View view) {
        switch (view.getId()) {
            case R.id.quxiao_btn:
                finish();
                break;
            case R.id.iv_clear:
                inpout_finddata.setText("");
                break;
        }
    }


    @Override
    protected void initEvent() {
        super.initEvent();
        recylerview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }

            @Override
            public void onLoadMore() {
                int totalPaeg = total / size + (total % size == 0 ? 0 : 1);
                if (page < totalPaeg) {
                    loadMoreData();
                } else {
                    recylerview.setloadMoreComplete();
                }
            }
        });
    }


    private String keyword;

    private void getData() {

        JsonObject jsonObject = new JsonObject();
        if (!TextUtils.isEmpty(keyword)) {
            jsonObject.addProperty("keyword", keyword);
        }
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().newsList(requestBean)
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

                if (state != STATE_MORE) {
                    datas.clear();
                }

                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();

                    if (data != null) {

                        ListData<NewsData> newsData = data.getNewsData();

                        if (newsData != null && newsData.getList() != null) {
                            datas.addAll(newsData.getList());
                            total = newsData.getTotal();
                        }
                        //搜索成功存入本地
                        if (page == 1) {
                            if (!TextUtils.isEmpty(keyword)) {
                                if (historyList.contains(keyword)) {
                                    historyList.remove(keyword);
                                }
                                historyList.add(0, keyword);

                                PreferencesUtils.getInstance().putString(Contans.HISTORY_TWO_USER+memberAccount, new Gson().toJson(historyList));
                                EventBus.getDefault().post(new UpdateHistoryEvent());
                            }

                        }


                    }
                }
                showData();
                dismissLoadingDialog();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                if (state != STATE_MORE) {
                    datas.clear();
                }
                showData();
                dismissLoadingDialog();
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

                madpater = new OnthespotRecordAdpater(mContext, datas);
                recylerview.setLayoutManager(new LinearLayoutManager(this));
                recylerview.setItemAnimator(new DefaultItemAnimator());
                recylerview.setAdapter(madpater);

                madpater.setlistonclicklister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {
                        NewsDeatilActivity.startactivity(mContext, datas.get(position).getNewsId());

                    }
                });
                break;
            case STATE_REFREH:
                madpater.notifyDataSetChanged();
                recylerview.scrollToPosition(0);
                recylerview.setReFreshComplete();
                break;
            case STATE_MORE:
                madpater.notifyDataSetChanged();
                recylerview.setloadMoreComplete();
                break;
        }

    }


}
