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
import com.oldwang.boxdemo.activity.EvaluationDeatilActivity;
import com.oldwang.boxdemo.adpater.EvalutionAdapter;
import com.oldwang.boxdemo.adpater.MasterDataAdapter;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.EvaluationData;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.MasterData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.event.UpdateMessageEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.PinyinEquipmentComparator;
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

public class OnthespotRecordFragmnet4 extends BaseFragment {

    @BindView(R.id.lv_team)
    ListView listView;

    @BindView(R.id.city_loactionbar)
    QuickLocationBar mQuicLocationBar;


    @BindView(R.id.dialog)
    TextView dialog;

    private int page = 1;
    private int size = 100000;

    private EvalutionAdapter adapter;


    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_equipment);
    }

    @Override
    protected void initData() {
        super.initData();
        mQuicLocationBar.setOnTouchLitterChangedListener(new LetterListViewListener());
        mQuicLocationBar.setTextDialog(dialog);
        getData();

    }





    private String categoryId = "";
    private String brandId = "";
    private String maxScore = "";
    private String minScore = "";


    /**
     */
    private void getData() {


        JsonObject jsonObject = new JsonObject();

        if (!TextUtils.isEmpty(brandId)) {
            jsonObject.addProperty("brandId", brandId);
        }
        if (!TextUtils.isEmpty(categoryId)) {
            jsonObject.addProperty("categoryId", categoryId);
        }
        if (!TextUtils.isEmpty(maxScore)) {
            jsonObject.addProperty("maxScore", maxScore);
        }


        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);

        if (!TextUtils.isEmpty(minScore)) {
            jsonObject.addProperty("minScore", minScore);
        }

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().evaluationList(requestBean)
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
                    ListData<EvaluationData> evaluationData = data.getEvaluationData();
                     final List<EvaluationData> list = evaluationData.getList();

                    Collections.sort(list, new PinyinEquipmentComparator());
                    adapter = new EvalutionAdapter(mContext, list);
                    listView.setAdapter(adapter);
                    adapter.setLisclicklister(new ListOnclickLister() {
                        @Override
                        public void onclick(View v, int position) {
                            EvaluationDeatilActivity.startactivity(mContext,list.get(position).getEvaluationDetailsId());
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

    private class LetterListViewListener implements
            QuickLocationBar.OnTouchLetterChangedListener {

        @Override
        public void touchLetterChanged(String s) {
            Map<String, Integer> alphaIndexer = adapter.getCityMap();
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                listView.setSelection(position);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(UpdateMessageEvent event) {
        if (event != null){
            if (event.getPostion() == 3){
                categoryId = event.getTypId();
                brandId = event.getBrandId();
                maxScore = event.getMaxScore();
                minScore = event.getMinScore();

                getData();
            }
        }
    }
}
