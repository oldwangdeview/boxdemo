package com.oldwang.boxdemo.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.BrantDataAdapter;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.BrandData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.event.BrantEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.PinyinBrandDataComparator;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.QuickLocationBar;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AssembleFragment extends BaseFragment {

    @BindView(R.id.lv_master)
    ListView lv_master;

    @BindView(R.id.city_loactionbar)
    QuickLocationBar mQuicLocationBar;

    @BindView(R.id.dialog)
    TextView dialog;

    private Dialog mLoadingDialog;
    private BrantDataAdapter adapter;


    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_assemble);
    }

    @Override
    protected void initData() {
        super.initData();
        mQuicLocationBar.setOnTouchLitterChangedListener(new LetterListViewListener());
        mQuicLocationBar.setTextDialog(dialog);
        getData();
    }
    /**
     */
    private void getData() {


        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", 1);
        jsonObject.addProperty("pageSize", 1000000);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().getBrandList(requestBean)
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
                LoadingDialogUtils.closeDialog(mLoadingDialog);

                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    ListData<BrandData> brandData = data.getBrandData();
                    final List<BrandData> list = brandData.getList();

                    Collections.sort(list, new PinyinBrandDataComparator());
                    adapter = new BrantDataAdapter(mContext, list);
                    lv_master.setAdapter(adapter);
                    lv_master.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            EventBus.getDefault().post(new BrantEvent(list.get(i)));
                            ((Activity)mContext).finish();
                        }
                    });

                }

            }

            @Override
            protected void _onError(String message) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
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
                lv_master.setSelection(position);
            }

        }

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
    }
}
