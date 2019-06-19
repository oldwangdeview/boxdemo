package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.ChoicePathMainAdpater;
import com.oldwang.boxdemo.adpater.ChooseCityAdapter;
import com.oldwang.boxdemo.adpater.MasterDataAdapter;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.MainPathBean;
import com.oldwang.boxdemo.bean.MainPath_ListBean;
import com.oldwang.boxdemo.bean.MasterData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.UserInfo;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateOrderListEvent;
import com.oldwang.boxdemo.fragment.OnthespotRecordFragmnet3;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LogUntil;
import com.oldwang.boxdemo.util.LogUtils;
import com.oldwang.boxdemo.util.PinyinMainPathdataComparator;
import com.oldwang.boxdemo.util.PinyinMasterDataComparator;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.MyGridView;
import com.oldwang.boxdemo.view.QuickLocationBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ChoiceMainPathActivity extends BaseActivity {
    @BindView(R.id.choice_data)
    RecyclerView choicedata;
    @BindView(R.id.choice_list)
    ListView choice_list;

    @BindView(R.id.city_loactionbar)
    QuickLocationBar mQuicLocationBar;

    @BindView(R.id.ll_choose)
    LinearLayout ll_choose;


    @BindView(R.id.dialog)
    TextView dialog;

    private List<MainPath_ListBean> listdata = new ArrayList<>();

    private List<MainPath_ListBean> chooseListData = new ArrayList<>();

    private ChooseCityAdapter chooseCityAdapter;


    private ChoicePathMainAdpater adapter;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_choicepathmain);
    }

    @Override
    protected void initData() {
        super.initData();

        mQuicLocationBar.setOnTouchLitterChangedListener(new LetterListViewListener());
        mQuicLocationBar.setTextDialog(dialog);

        String city_list = PreferencesUtils.getInstance().getString("city_list", "");

        if (TextUtils.isEmpty(city_list)){
            adddata();
        }else {
            List<MainPath_ListBean> mlistdata = new Gson().fromJson(city_list,new TypeToken<List<MainPath_ListBean >>() {}.getType());
            listdata.addAll(mlistdata);
            initAdapter();
        }




    }

    private void initAdapter() {
        adapter = new ChoicePathMainAdpater(mContext, listdata);
        choice_list.setAdapter(adapter);

        adapter.setListOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                MainPath_ListBean mainPath_listBean = listdata.get(position);
                mainPath_listBean.isChoose = !mainPath_listBean.isChoose;
                if (mainPath_listBean.isChoose){
                    chooseListData.add(mainPath_listBean);
                }else {
                    chooseListData.remove(mainPath_listBean);
                }
                adapter.notifyDataSetChanged();
                chooseCityAdapter.notifyDataSetChanged();
                if (chooseListData.size() > 0){
                    ll_choose.setVisibility(View.VISIBLE);
                }else {
                    ll_choose.setVisibility(View.GONE);
                }
            }
        });

        chooseCityAdapter = new ChooseCityAdapter(mContext,chooseListData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        choicedata.setLayoutManager(layoutManager);
        choicedata.setItemAnimator(new DefaultItemAnimator());
        choicedata.setAdapter(chooseCityAdapter);


        chooseCityAdapter.setListClickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                switch (v.getId()){
                    case R.id.tv_city_name:
                        for (int i = 0; i < chooseListData.size(); i++) {
                            if (i == position){
                                chooseListData.get(i).isItmeChoose = true;
                            }else {
                                chooseListData.get(i).isItmeChoose = false;
                            }
                        }
                        chooseCityAdapter.notifyDataSetChanged();
                        break;
                    case R.id.iv_delete:
                        chooseListData.get(position).isChoose = false;
                        chooseListData.remove(position);
                        chooseCityAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                        if (chooseListData.size() > 0){
                            ll_choose.setVisibility(View.VISIBLE);
                        }else {
                            ll_choose.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        });
    }


    private void adddata(){
        showLoadingDialog();
        new Thread(){
            @Override
            public void run() {
                super.run();
                LogUntil.show(mContext,TAG,"开始时间："+new Date().getTime()+"");
                List<MainPath_ListBean> mlistdata = new Gson().fromJson(UIUtils.getJson("sys_area.json",mContext),MainPathBean.class).RECORDS;
                LogUntil.show(mContext,TAG,"获取完毕："+new Date().getTime()+"");
                List<MainPath_ListBean> testdata = new ArrayList<>();
                for(int i = 0;i<mlistdata.size();i++){
                    if(mlistdata.get(i).area_type.equals("1")) {
                        testdata.add(mlistdata.get(i));
                    }
                }
                Collections.sort(testdata, new PinyinMainPathdataComparator());
                LogUntil.show(mContext,TAG,"排序完毕"+new Date().getTime()+"数据总量："+testdata.size() );
                Message msg = new Message();
                msg.obj = testdata;
                mhandler.sendMessage(msg);
            }
        }.start();
    }

    Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissLoadingDialog();
            LogUntil.show(mContext,TAG,"接收data");
            List<MainPath_ListBean> mlistdata = (List<MainPath_ListBean>)msg.obj;
            listdata.addAll(mlistdata);
            PreferencesUtils.getInstance().putString("city_list",new Gson().toJson(listdata));
            initAdapter();

        }
    };


    @Override
    protected void initEvent() {
        super.initEvent();
        choice_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private class LetterListViewListener implements
            QuickLocationBar.OnTouchLetterChangedListener {

        @Override
        public void touchLetterChanged(String s) {

                Map<String, Integer> alphaIndexer = adapter.getCityMap();
                if (alphaIndexer.get(s) != null) {
                    int position = alphaIndexer.get(s);
                    choice_list.setSelection(position);
                }


        }

    }

    public static void startactivity(Context mContext){
        Intent mIntente = new Intent(mContext,ChoiceMainPathActivity.class);
        mContext.startActivity(mIntente);
    }

    /**
     * 保存服务区
     */
    private void saveRegion() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("resgionIds",resgionIds);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().saveRegion(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {

                if (stringStatusCode != null) {
//                    String mainId = stringStatusCode.getData().getMainId();
                    ToastUtils.makeText("保存成功");
                    finish();
                    dismissLoadingDialog();

                    String user = PreferencesUtils.getInstance().getString(Contans.userInfo, "");
                    if (!TextUtils.isEmpty(user)){
                        UserInfo userInfo = new Gson().fromJson(user,UserInfo.class);
                        userInfo.setRegionIds(resgionIds);
                        PreferencesUtils.getInstance().putString(Contans.userInfo,new Gson().toJson(userInfo));
                    }

                }
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }

    private String resgionIds = "";

    @OnClick({R.id.tv_skip,R.id.tv_save})
    public void onClick(View view){
        
        switch (view.getId()){
            case R.id.tv_skip:
                finish();
                break;
            case R.id.tv_save:
                if (chooseListData.size() <1){
                    ToastUtils.makeText("请选择地区");
                    return;
                }

                for (MainPath_ListBean chooseListDatum : chooseListData) {
                    resgionIds += chooseListDatum.area_id;
                }
                saveRegion();
                break;
        }
    }

}
