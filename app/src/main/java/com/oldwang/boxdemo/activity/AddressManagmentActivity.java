package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.AddressManagementAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.AddressData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.event.UpdateAddress;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
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

/**
 * 地址管里
 */
public class AddressManagmentActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.tv_small_title_layout_head)
    TextView rightext;
    @BindView(R.id.recyclerview)
    YRecycleview recylerview;


    private int page = 1;
    private final int size = 20;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;       //正常情况

    AddressManagementAdpater madpater;
    private ArrayList<AddressData> datas = new ArrayList<>();


    @Override
    protected void initView() {
        setContentView(R.layout.activity_addressmanagment);
    }


    @Override
    protected void initData() {
        super.initData();
        state = STATE_NORMAL;
        titlename.setText("地址管理");
        rightext.setText("添加");
        rightext.setVisibility(View.VISIBLE);
        getData();
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

    /**
     * 添加地址
     */
    @OnClick({R.id.tv_small_title_layout_head})
    public void Addpath() {
        AddPathActivity.startactivity(mContext, 1);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }


    public static void startactivity(Context mContext) {
        Intent mIntent = new Intent(mContext, AddressManagmentActivity.class);
        mContext.startActivity(mIntent);
    }


    /**
     * 地址列表
     */
    private void getData() {

        showLoadingDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().addressList(requestBean)
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
                if (state != STATE_MORE) {
                    datas.clear();
                }

                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    ListData<AddressData> addressData = data.getAddressData();
                    List<AddressData> list = addressData.getList();
                    datas.addAll(list);
                    total = data.getAddressData().getTotal();
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
                madpater = new AddressManagementAdpater(mContext, datas);
                recylerview.setLayoutManager(new LinearLayoutManager(this));
                recylerview.setItemAnimator(new DefaultItemAnimator());
                recylerview.setAdapter(madpater);
                madpater.setlistonclicklister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {
                        AddressData addressData = datas.get(position);
                        switch (v.getId()) {
                            case R.id.choice_morenlayout:
                                if (!(!TextUtils.isEmpty(addressData.getDefaultStatus()) && addressData.getDefaultStatus().equals("1"))) {
                                    saveMyAddress(addressData);
                                }
                                break;
                            case R.id.layout_uotdae:
                                AddPathActivity.startactivity(mContext, 2, addressData);
                                break;
                            case R.id.layout_delete:

                                AlertView alertView = new AlertView("提示", "确定删除地址？", null, null, new String[]{"取消", "确定"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(Object o, int position) {
                                        if (position == 1) {
                                            //调用接口
                                            datas.remove(position);
                                            madpater.notifyDataSetChanged();
                                        }

                                    }
                                });
                                alertView.show();

                                break;
                        }
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

    /**
     * 修改地址
     *
     * @param addressData
     */
    private void saveMyAddress(AddressData addressData) {


        JsonObject jsonObject = new JsonObject();

        /***详细地址*/
        jsonObject.addProperty("addressDetail", addressData.getAddressDetail());
//        /***收货地址全名*/
//        jsonObject.addProperty("addressFull", addressFull);
        /***市级ID*/
        jsonObject.addProperty("cityId", addressData.getCityId());
        /***是否为默认地址(1是，0否)*/
        jsonObject.addProperty("defaultStatus", 1);
        /***区县ID*/
        jsonObject.addProperty("districtId", addressData.getDistrictId());
        /***收货人姓名*/
        jsonObject.addProperty("memberName", addressData.getMemberName());
        /***收货人姓名*/
        jsonObject.addProperty("memberPhone", addressData.getMemberPhone());
        /***省级ID*/
        jsonObject.addProperty("provinceId", addressData.getProvinceId());
        jsonObject.addProperty("receivingAddressId", addressData.getReceivingAddressId());
        /***乡镇街道ID*/
//        jsonObject.addProperty("townshipId", townshipId);

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().saveMyAddress(requestBean)
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
                EventBus.getDefault().post(new UpdateAddress());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void jumpTeachers(UpdateAddress event) {
        refreshData();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
