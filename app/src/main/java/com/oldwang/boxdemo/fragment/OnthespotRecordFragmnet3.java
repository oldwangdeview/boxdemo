package com.oldwang.boxdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.ArtsSchoolDetailActivity;
import com.oldwang.boxdemo.adpater.MasterDataAdapter;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.MasterData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.event.JumpTeachers;
import com.oldwang.boxdemo.event.UpdateMessageEvent;
import com.oldwang.boxdemo.event.UpdateSchoolEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LogUntil;
import com.oldwang.boxdemo.util.PinyinMasterDataComparator;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.QuickLocationBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class OnthespotRecordFragmnet3 extends BaseFragment {


    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;

    @BindView(R.id.lv_team)
    ListView listView;
    @BindView(R.id.lv_master)
    ListView lv_master;

    @BindView(R.id.city_loactionbar)
    QuickLocationBar mQuicLocationBar;


    @BindView(R.id.dialog)
    TextView dialog;

    private int page = 1;
    private int size = 100000;

    private MasterDataAdapter adapter;
    private MasterDataAdapter adapter1;

    private int postion = 0;

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_master);
    }

    @Override
    protected void initData() {
        super.initData();
        mQuicLocationBar.setOnTouchLitterChangedListener(new OnthespotRecordFragmnet3.LetterListViewListener());
        mQuicLocationBar.setTextDialog(dialog);
        getData(0);
        getData(1);

    }


    @OnClick({R.id.text1, R.id.text2})
    public void titleclick(View v) {
        switch (v.getId()) {
            case R.id.text1:
                postion = 0;
                listView.setVisibility(View.VISIBLE);
                lv_master.setVisibility(View.GONE);
                text1.setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
                text2.setTextColor(mContext.getResources().getColor(R.color.c_525259));
                break;

            case R.id.text2:
                postion = 1;
                lv_master.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                text2.setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
                text1.setTextColor(mContext.getResources().getColor(R.color.c_525259));
                break;
        }
    }


    //城市ID
    private String cityId = "";

    //区域ID
    private String districtId = "";

    //省ID
    private String provinceId = "";

    //乡镇ID
    private String townshipId = "";


    /**
     * @param newsMasterType 名师/战将类型(0名师1战将)
     *                       拳联纪实--名师战将-名师
     */
    private void getData(final int newsMasterType) {


        JsonObject jsonObject = new JsonObject();
        if (!TextUtils.isEmpty(cityId)) {
            jsonObject.addProperty("cityId", cityId);
        }
        if (!TextUtils.isEmpty(districtId)) {
            jsonObject.addProperty("districtId", districtId);
        }

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("newsMasterType", newsMasterType);

        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);



        if (!TextUtils.isEmpty(provinceId)) {
            jsonObject.addProperty("provinceId", provinceId);
        }
        if (!TextUtils.isEmpty(townshipId)) {
            jsonObject.addProperty("townshipId", townshipId);
        }
        if (!TextUtils.isEmpty(typeId)) {
            jsonObject.addProperty("typeId", typeId);
        }


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().masterList(requestBean)
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

                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    ListData<MasterData> masterData = data.getMasterData();
                    final List<MasterData> list = masterData.getList();
                    if (newsMasterType == 0) {
                        Collections.sort(list, new PinyinMasterDataComparator());
                        adapter = new MasterDataAdapter(mContext, list);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                ToastUtils.makeText(list.get(i).getNewsMasterName());
                                ArtsSchoolDetailActivity.startactivity(mContext, list.get(i).getNewsMasterId(),true);
                            }
                        });
                    } else {
                        Collections.sort(list, new PinyinMasterDataComparator());
                        adapter1 = new MasterDataAdapter(mContext, list);
                        lv_master.setAdapter(adapter1);
                        lv_master.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                ToastUtils.makeText(list.get(i).getNewsMasterName());
                                ArtsSchoolDetailActivity.startactivity(mContext, list.get(i).getNewsMasterId(),true);
                            }
                        });
                    }

                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }

    private class LetterListViewListener implements
            QuickLocationBar.OnTouchLetterChangedListener {

        @Override
        public void touchLetterChanged(String s) {

            if (postion == 0) {
                Map<String, Integer> alphaIndexer = adapter.getCityMap();
                if (alphaIndexer.get(s) != null) {
                    int position = alphaIndexer.get(s);
                    listView.setSelection(position);
                }

            } else {
                Map<String, Integer> alphaIndexer = adapter1.getCityMap();
                if (alphaIndexer.get(s) != null) {
                    int position = alphaIndexer.get(s);
                    lv_master.setSelection(position);
                }
            }

        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    private String typeId;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(UpdateMessageEvent event) {
        if (event != null){
            if (event.getPostion() == 2){
                typeId = event.getTypId();
                getData(postion);
            }
        }
    }


}