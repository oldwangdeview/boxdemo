package com.oldwang.boxdemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.MyVideoAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.interfice.TitleChoiceBarLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.TitleChoiceBarView;
import com.oldwang.boxdemo.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MyVideoFragment  extends BaseFragment {
    @BindView(R.id.recyclerview)
    YRecycleview recyclerview;

    @BindView(R.id.choice_image)
    ImageView choice_image;

    TitleChoiceBarView titlechoiceview;
    private boolean choiceall = false;
    private MyVideoAdpater madpater;

    private int page = 1 ;
    private final int size = 20;
    private int total;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况

    private ArrayList<ScoreData> datas = new ArrayList<>();
    private Dialog mLoadingDialog;
    private int type = 1;


    @Override
    public View initView(Context context) {
        titlechoiceview  = new TitleChoiceBarView(context, new String[]{"审核通过","审核中","审核打回"}, R.color.c_d52e21, R.color.c_595959, new TitleChoiceBarLister() {
            @Override
            public void chicepositiob(int position, String data) {

                switch (position){
                    case 0:
                        if(type!=1){
                            page=1;
                        }
                        type = 1;
                        break;
                    case 1:
                        if(type!=0){
                            page=1;
                        }
                        type = 0;
                        break;
                    case 2:
                        if(type!=-1){
                            page=1;
                        }
                        type = -1;

                        break;


                }
                getData();
            }
        });
        LinearLayout view = (LinearLayout) UIUtils.inflate(mContext, R.layout.fragment_myvideo);
        view.addView(titlechoiceview,0);
        return view;
    }


    @Override
    protected void initData() {
        state = STATE_NORMAL;
        super.initData();

    }
    @Override
    protected void initEvent() {
        super.initEvent();




        recyclerview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
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
                    recyclerview.setloadMoreComplete();
                }
            }
        });
        getData();
    }


    @OnClick(R.id.choice_layout)
    public void choiceall(){
        choiceall = !choiceall;
        if(choiceall){
            choice_image.setImageResource(R.mipmap.addresslist_choice);
        }else{
            choice_image.setImageResource(R.mipmap.addresslist_unchoice);
        }
        madpater.setAllDataClickType(choiceall);
    }
    /**
     * 我的发布-视频
     */
    private void getData() {


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("type",type);
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().myVideoList(requestBean)
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

                if (state != STATE_MORE){
                    datas.clear();
                }

                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    if (data != null && data.getScoreData() != null && data.getScoreData().getList() != null){
                        datas.addAll(data.getScoreData().getList());
                        total = data.getScoreData().getTotal();

                    }
                }
                showData();
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                if (state != STATE_MORE){
                    datas.clear();
                }
                showData();
                LoadingDialogUtils.closeDialog(mLoadingDialog);
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
                madpater = new MyVideoAdpater(mContext,datas);
                madpater. setAllDataClickType(false);
                recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.setAdapter(madpater);
                madpater.setlistonclicklister(new ListOnclickLister() {
                    @Override
                    public void onclick(View v, int position) {

                        switch (v.getId()){
                            case R.id.choice_image:
                                choiceall = madpater.GetAllDataChoiceType();
                                if(choiceall){
                                    choice_image.setImageResource(R.mipmap.addresslist_choice);
                                }else{
                                    choice_image.setImageResource(R.mipmap.addresslist_unchoice);
                                }
                                break;
                                default:

                                    break;

                        }

                    }
                });
                break;
            case STATE_REFREH:
                madpater.notifyDataSetChanged();
                recyclerview.scrollToPosition(0);
                recyclerview.setReFreshComplete();
                break;
            case STATE_MORE:
                madpater.notifyDataSetChanged();
                recyclerview.setloadMoreComplete();
                break;
        }

    }


    @OnClick(R.id.delete_btn)
    public void deletedata(){
        List<ScoreData> choicedata = madpater.getClickdata();
        if(choicedata.size()>0){
            String videoiud = "";
            for (int i = 0; i < choicedata.size(); i++) {
                videoiud = videoiud+choicedata.get(i).getVideoId()+",";
            }

            videoiud = videoiud.substring(0,videoiud.length()-1);


            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
            jsonObject.addProperty("videoId",videoiud);
            RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
            Observable observable =
                    ApiUtils.getApi().delMyVideo(requestBean)
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

            HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(mContext) {
                @Override
                protected void _onNext(StatusCode<Object> stringStatusCode) {

                    getData();
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.makeText(message);
                    if (state != STATE_MORE){
                        datas.clear();
                    }
                    showData();
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                }
            }, "", lifecycleSubject, false, true);

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
