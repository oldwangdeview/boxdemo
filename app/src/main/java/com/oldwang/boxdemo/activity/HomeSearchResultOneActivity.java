package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.QuanLianListDataAdpater;
import com.oldwang.boxdemo.adpater.TrainingBaseAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.CommodityTeamBuyData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.VenueData;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateHistoryEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
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
public class HomeSearchResultOneActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    YRecycleview recylerview;

    @BindView(R.id.inpout_finddata)
    EditText inpout_finddata;


    @BindView(R.id.iv_one)
    ImageView iv_one;

    @BindView(R.id.iv_two)
    ImageView iv_two;

    @BindView(R.id.iv_three)
    ImageView iv_three;


    @BindView(R.id.ll_sort)
    LinearLayout ll_sort;


    private QuanLianListDataAdpater madpater;

    private int page = 1;
    private final int size = 20;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;       //正常情况

    private ArrayList<CommodityTeamBuyData> datas = new ArrayList<>();
    private String memberAccount;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_home_search_result);
    }

    private List<String> historyList = new ArrayList<>();

    @Override
    protected void initData() {
        super.initData();
        ll_sort.setVisibility(View.VISIBLE);
        keyword = getIntent().getStringExtra(Contans.INTENT_DATA);

        memberAccount = BaseActivity.getUserInfo(mContext).getMemberAccount();
        if (TextUtils.isEmpty(memberAccount)) {
            memberAccount = "";
        }
        String temp = PreferencesUtils.getInstance().getString(Contans.HISTORY_ONE_USER+ memberAccount, "");



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
        Intent mIntent = new Intent(mContext, HomeSearchResultOneActivity.class);
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

    //1价格2销量3评论
    private int type;

    private boolean isAsc = false;

    /**
     * 排序
     */

    @OnClick({R.id.rl_price, R.id.rl_sale, R.id.rl_comment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_price:
                type = 1;
                iv_one.setVisibility(View.VISIBLE);
                iv_two.setVisibility(View.GONE);
                iv_three.setVisibility(View.GONE);

                isAsc = !isAsc;
                if (isAsc) {
                    iv_one.setImageDrawable(getResources().getDrawable(R.mipmap.icon_up));
                } else {
                    iv_one.setImageDrawable(getResources().getDrawable(R.mipmap.iocn_down));
                }
                refreshData();

                break;
            case R.id.rl_sale:
                type = 2;
                iv_one.setVisibility(View.GONE);
                iv_two.setVisibility(View.VISIBLE);
                iv_three.setVisibility(View.GONE);
                isAsc = !isAsc;
                if (isAsc) {
                    iv_two.setImageDrawable(getResources().getDrawable(R.mipmap.icon_up));
                } else {
                    iv_two.setImageDrawable(getResources().getDrawable(R.mipmap.iocn_down));
                }
                refreshData();
                break;
            case R.id.rl_comment:
                type = 3;
                iv_one.setVisibility(View.GONE);
                iv_two.setVisibility(View.GONE);
                iv_three.setVisibility(View.VISIBLE);
                isAsc = !isAsc;
                if (isAsc) {
                    iv_three.setImageDrawable(getResources().getDrawable(R.mipmap.icon_up));
                } else {
                    iv_three.setImageDrawable(getResources().getDrawable(R.mipmap.iocn_down));
                }
                refreshData();
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
        showLoadingDialog();

        JsonObject jsonObject = new JsonObject();

        if (!TextUtils.isEmpty(keyword)) {
            jsonObject.addProperty("commodityName", keyword);
        }

        if (type != 0) {

            if (isAsc) {
                jsonObject.addProperty("isAsc", "asc");

            } else {
                jsonObject.addProperty("isAsc", "desc");

            }

            if (type == 3) {
                jsonObject.addProperty("orderByColumn", "commentCount");
            } else if (type == 1) {
                jsonObject.addProperty("orderByColumn", "samePrice");
            } else if (type == 2) {
                jsonObject.addProperty("orderByColumn", "sellCount");

            }
        }

//        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().appSearchCommodity(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {
                if (state != STATE_MORE) {
                    datas.clear();
                }

                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    DataInfo1 data = stringStatusCode.getData();
                    ListData<CommodityTeamBuyData> commodityTeamBuyData = data.getData();
                    List<CommodityTeamBuyData> list = commodityTeamBuyData.getList();
                    datas.addAll(list);
                    total = commodityTeamBuyData.getTotal();

                    //搜索成功存入本地
                    if (page == 1) {
                        if (!TextUtils.isEmpty(keyword)) {
                            if (historyList.contains(keyword)) {
                                historyList.remove(keyword);
                            }
                            historyList.add(0, keyword);
                            PreferencesUtils.getInstance().putString(Contans.HISTORY_ONE_USER+ memberAccount, new Gson().toJson(historyList));
                            EventBus.getDefault().post(new UpdateHistoryEvent());
                        }
                    }

                }
                showData();

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                if (state != STATE_MORE) {
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

                madpater = new QuanLianListDataAdpater(mContext, datas);
                recylerview.setLayoutManager(new GridLayoutManager(mContext, 2));
                recylerview.setItemAnimator(new DefaultItemAnimator());
                recylerview.setAdapter(madpater);

                madpater.setListonClickLister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {
                        CommodityTeamBuyData commodityTeamBuyData = datas.get(position);
                        String commodityId = commodityTeamBuyData.getCommodityId();
                        String commodityType = commodityTeamBuyData.getCommodityType();
                        boolean isPingTuan = false;
                        if (!TextUtils.isEmpty(commodityType) && commodityType.equals("2")) {
                            isPingTuan = true;
                        }
                        GoodsDetailActivity.startactivity(mContext, isPingTuan, commodityId, null);

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
