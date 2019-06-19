package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.TeamAdapter;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RegionData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.TeamData;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.PinyinComparator;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.view.QuickLocationBar;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/***
 * 我的团队
 */
public class MyTeamActivity extends BaseActivity {


    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.lv_team)
    ListView listView;
    @BindView(R.id.city_loactionbar)
    QuickLocationBar mQuicLocationBar;


    @BindView(R.id.dialog)
    TextView dialog;


    private int page = 1;
    private int size = 100000;

    private TeamAdapter adapter;

    @Override
    protected void initView() {
        setContentView(R.layout.actiivity_my_team);
    }


    @Override
    protected void initData() {
        super.initData();
        titlename.setText("我的团队");
        mQuicLocationBar.setOnTouchLitterChangedListener(new LetterListViewListener());
        mQuicLocationBar.setTextDialog(dialog);
        getData();
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }


    public static void startactivity(Context mContext) {
        Intent mIntent = new Intent(mContext, MyTeamActivity.class);
        mContext.startActivity(mIntent);
    }

    /**
     * 团队成员列表
     */
    private void getData() {

        showLoadingDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().teamMemberList(requestBean)
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

                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    DataInfo1 data = stringStatusCode.getData();
                    ListData<TeamData> regionData = data.getRegionData();
                    List<TeamData> list = regionData.getList();
                    Collections.sort(list,new PinyinComparator());
                    adapter = new TeamAdapter(mContext, list);
                    listView.setAdapter(adapter);

                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                dismissLoadingDialog();
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


}
