package com.oldwang.boxdemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.MyCommentGoodsAdpater;
import com.oldwang.boxdemo.adpater.MyCommentNewsAdptaer;
import com.oldwang.boxdemo.adpater.MyCommentVenueAdpater;
import com.oldwang.boxdemo.adpater.MyCommentVideoAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.MyCommentGoodsBean;
import com.oldwang.boxdemo.bean.MyCommentNewsBean;
import com.oldwang.boxdemo.bean.MyCommentVenueBean;
import com.oldwang.boxdemo.bean.NewsData;
import com.oldwang.boxdemo.bean.RequestBean;
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

public class MyCommentFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    YRecycleview recyclerview;


    @BindView(R.id.recyclerview1)
    YRecycleview recyclerview1;


    @BindView(R.id.recyclerview2)
    YRecycleview recyclerview2;


    @BindView(R.id.recyclerview3)
    YRecycleview recyclerview3;

    @BindView(R.id.choice_image)
    ImageView choice_image;
    private boolean choiceall = false;
    private int page = 1 ;
    private final int size = 20;
    private int total;


    List<YRecycleview> mviewlist = new ArrayList<>();
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state;       //正常情况
    private Dialog mLoadingDialog;
    List<MyCommentGoodsBean> commodityCommentData = new ArrayList<>();
    List<MyCommentVenueBean> venueCommentData = new ArrayList<>();
    List<MyCommentNewsBean> newsCommentData = new ArrayList<>();
    List<NewsData> boxingVideoCommentData = new ArrayList<>();

    private MyCommentGoodsAdpater mygoodsadpater;


    private MyCommentVenueAdpater myvenueadpater ;


    private MyCommentVideoAdpater myvideoadpater;


    private MyCommentNewsAdptaer mynewsadpater;



    private int GetDataType = 0;


    @Override
    public View initView(Context context) {
        TitleChoiceBarView titlechoiceview  = new TitleChoiceBarView(context, new String[]{"商品评价","场馆评价","视频评论","快讯评论"}, R.color.c_d52e21, R.color.c_595959, new TitleChoiceBarLister() {
            @Override
            public void chicepositiob(int position, String data) {

                if(GetDataType==position){
                    page = 1;
                }else {
                    GetDataType = position;
                    state = STATE_REFREH;
                    getData();
                }
                seleteposition(position);
                switch (GetDataType){
                    case 0:
                        choiceall = mygoodsadpater.GetAlldataChoiceType();
                        break;
                    case 1:
                        choiceall = myvenueadpater.GetAlldataChoiceType();
                        break;
                    case 2:
                        choiceall = myvideoadpater.GetAlldataChoiceType();
                        break;
                    case 3:
                        choiceall = mynewsadpater.GetAlldataChoiceType();
                        break;
                }

                if(choiceall){
                    choice_image.setImageResource(R.mipmap.addresslist_choice);
                }else{
                    choice_image.setImageResource(R.mipmap.addresslist_unchoice);
                }

            }
        });
        LinearLayout mview =(LinearLayout)UIUtils.inflate(mContext, R.layout.fragment_myvideo);
        mview.addView(titlechoiceview,0);
        return mview;
    }

    @Override
    protected void initData() {
        state = STATE_NORMAL;
        super.initData();
        mviewlist.add(recyclerview);
        mviewlist.add(recyclerview1);
        mviewlist.add(recyclerview2);
        mviewlist.add(recyclerview3);

        mygoodsadpater = new MyCommentGoodsAdpater(mContext,commodityCommentData);
        mygoodsadpater.setlistonclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {

            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mygoodsadpater);


        myvenueadpater = new MyCommentVenueAdpater(mContext ,venueCommentData);
        recyclerview1.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview1.setItemAnimator(new DefaultItemAnimator());
        recyclerview1.setAdapter(myvenueadpater);


        myvideoadpater = new MyCommentVideoAdpater(mContext,boxingVideoCommentData);
        recyclerview2.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview2.setItemAnimator(new DefaultItemAnimator());
        recyclerview2.setAdapter(myvideoadpater);

        mynewsadpater = new MyCommentNewsAdptaer(mContext,newsCommentData);
        recyclerview3.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview3.setItemAnimator(new DefaultItemAnimator());
        recyclerview3.setAdapter(mynewsadpater);


        getData();
    }

    @Override
    protected void initEvent() {
        super.initEvent();

        YRecycleview.OnRefreshAndLoadMoreListener mlister = new  YRecycleview.OnRefreshAndLoadMoreListener() {
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
        };
        recyclerview.setRefreshAndLoadMoreListener(mlister);
        recyclerview1.setRefreshAndLoadMoreListener(mlister);
        recyclerview2.setRefreshAndLoadMoreListener(mlister);
        recyclerview3.setRefreshAndLoadMoreListener(mlister);

    }

    @OnClick(R.id.choice_layout)
    public void choiceall(){
        choiceall = !choiceall;
        if(choiceall){
            choice_image.setImageResource(R.mipmap.addresslist_choice);
        }else{
            choice_image.setImageResource(R.mipmap.addresslist_unchoice);
        }
        switch (GetDataType){
            case 0:
                mygoodsadpater.setAddDataType(choiceall);
                break;
            case 1:
                myvenueadpater.setAddDataType(choiceall);
                break;
            case 2:
                myvideoadpater.setAddDataType(choiceall);
                break;
            case 3:
                mynewsadpater.setAddDataType(choiceall);
                break;
        }
    }


    ListOnclickLister mlisterchoice = new ListOnclickLister() {
        @Override
        public void onclick(View v, int position) {


            if(v.getId()==R.id.choice_image)

                switch (GetDataType){
                    case 0:
                        choiceall = mygoodsadpater.GetAlldataChoiceType();
                        break;
                    case 1:
                        choiceall = myvenueadpater.GetAlldataChoiceType();
                        break;
                    case 2:
                        choiceall = myvideoadpater.GetAlldataChoiceType();
                        break;
                    case 3:
                        choiceall = mynewsadpater.GetAlldataChoiceType();
                        break;
                }

                if(choiceall){
                    choice_image.setImageResource(R.mipmap.addresslist_choice);
                }else{
                    choice_image.setImageResource(R.mipmap.addresslist_unchoice);
                }



        }
    };

    /**
     * 我的发布-视频
     */
    private void getData() {

        Observable observable = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", page);
        jsonObject.addProperty("pageSize", size);
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);

        switch (GetDataType){
            case 0:
                observable =ApiUtils.getApi().myCommodityCommentList(requestBean);
                break;
            case 1:
                observable =ApiUtils.getApi().venuemyCommentList(requestBean);
                break;
            case 2:
                observable =ApiUtils.getApi().videomyCommentList(requestBean);

                break;
            case 3:
                observable =ApiUtils.getApi().newsCommentList(requestBean);
                break;
        }

        observable.compose(RxHelper.getObservaleTransformer())
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {

                if (state != STATE_MORE){
                    switch (GetDataType){
                        case 0:
                            commodityCommentData.clear();
                            break;
                        case 1:
                            venueCommentData.clear();
                            break;
                        case 2:
                            boxingVideoCommentData.clear();
                            break;
                        case 3:
                            newsCommentData.clear();
                            break;
                    }
                }

                if (stringStatusCode != null) {
                    DataInfo1 data = stringStatusCode.getData();
                    if (data != null ){
                        switch (GetDataType){
                            case 0:
                                if(data.getCommodityCommentData()!=null&&data.getCommodityCommentData().getList().size()>0){
                                    commodityCommentData.addAll(data.getCommodityCommentData().getList());
                                    total = data.getCommodityCommentData().getTotal();
                                }
                                break;
                            case 1:
                                if(data.getVenueCommentData()!=null&&data.getVenueCommentData().getList().size()>0){
                                    venueCommentData.addAll(data.getVenueCommentData().getList());
                                    total = data.getVenueCommentData().getTotal();
                                }
                                break;
                            case 2:
                                if(data.getBoxingVideoCommentData()!=null&&data.getBoxingVideoCommentData().getList().size()>0){
                                    boxingVideoCommentData.addAll(data.getBoxingVideoCommentData().getList());
                                    total = data.getBoxingVideoCommentData().getTotal();

                                }
                                break;
                            case 3:
                                if(data.getNewsCommentData()!=null&&data.getNewsCommentData().getList().size()>0){
                                    newsCommentData.addAll(data.getNewsCommentData().getList());
                                    total = data.getNewsCommentData().getTotal();

                                }
                                break;
                        }


                    }
                }
                showData();
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                if (state != STATE_MORE){
                    switch (GetDataType){
                        case 0:
                            commodityCommentData.clear();
                            break;
                        case 1:
                            venueCommentData.clear();
                            break;
                        case 2:
                            boxingVideoCommentData.clear();
                            break;
                        case 3:
                            newsCommentData.clear();
                            break;
                    }
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

            case STATE_REFREH:
                switch (GetDataType){
                    case 0:
                        recyclerview.setReFreshComplete();
                        break;
                    case 1:
                        recyclerview1.setReFreshComplete();
                        break;
                    case 2:
                        recyclerview2.setReFreshComplete();
                        break;
                    case 3:
                        recyclerview3.setReFreshComplete();
                        break;
                }

                break;
            case STATE_MORE:
                switch (GetDataType){
                    case 0:
                        recyclerview.setloadMoreComplete();
                        break;
                    case 1:
                        recyclerview1.setloadMoreComplete();
                        break;
                    case 2:
                        recyclerview2.setloadMoreComplete();
                        break;
                    case 3:
                        recyclerview3.setloadMoreComplete();
                        break;
                }
                break;
        }

        myvenueadpater.notifyDataSetChanged();
        mygoodsadpater.notifyDataSetChanged();
        mynewsadpater.notifyDataSetChanged();
        myvideoadpater.notifyDataSetChanged();

    }


    private void seleteposition(int position){
        for (int i = 0; i < mviewlist.size(); i++) {
            if(position==i){
                mviewlist.get(i).setVisibility(View.VISIBLE);
            }else{
                mviewlist.get(i).setVisibility(View.GONE);
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

    @OnClick(R.id.delete_btn)
    public void deletetmessage(){
        Observable observable = null;
        RequestBean requestBean = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        switch (GetDataType){
            case 0:
                List<MyCommentGoodsBean> listdata = mygoodsadpater.GetChoiceData();

                if(listdata.size()==0){
                    return;
                }
                String commodityCommentId = "";
                for (int i = 0; i < listdata.size(); i++) {

                    if(!TextUtils.isEmpty(listdata.get(i).getCommodityCommentId())){
                        commodityCommentId  = commodityCommentId+listdata.get(i).getCommodityCommentId()+",";
                    }
                }
                commodityCommentId = commodityCommentId.substring(0,commodityCommentId.length()-1);
                jsonObject.addProperty("commodityCommentId",commodityCommentId);
                  requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
                 observable = ApiUtils.getApi().delCommodityComment(requestBean);
                break;
            case 1:
                List<MyCommentVenueBean> mlistdata =   myvenueadpater.GetChoiceData();

                if(mlistdata.size()==0){
                    return;
                }
                String venueCommentId = "";
                for (int i = 0; i < mlistdata.size(); i++) {

                    if(!TextUtils.isEmpty(mlistdata.get(i).getCommentId())){
                        venueCommentId  = venueCommentId+mlistdata.get(i).getCommentId()+",";
                    }
                }
                venueCommentId = venueCommentId.substring(0,venueCommentId.length()-1);
                jsonObject.addProperty("venueCommentId",venueCommentId);
                requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
                observable = ApiUtils.getApi().delVenueComment(requestBean);

                break;
            case 2:
                List<NewsData> videolsit = myvideoadpater.GetChoiceData();
                if(videolsit.size()==0){
                    return;
                }
                String videoCommentId = "";
                for (int i = 0; i < videolsit.size(); i++) {
                    if(!TextUtils.isEmpty(videolsit.get(i).getBoxingVideoCommentId())){
                        videoCommentId = videoCommentId+videolsit.get(i).getBoxingVideoCommentId()+",";
                    }
                }

                videoCommentId = videoCommentId.substring(0,videoCommentId.length()-1);
                jsonObject.addProperty("videoCommentId",videoCommentId);
                requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
                observable = ApiUtils.getApi().delVideoComment(requestBean);

                break;
            case 3:
                List<MyCommentNewsBean>  newslist =  mynewsadpater.GetChoiceData();

                if(newslist.size()==0){
                    return;
                }
                String newsCommentId = "";
                for (int i = 0; i < newslist.size(); i++) {
                    if(!TextUtils.isEmpty(newslist.get(i).getNewsCommentId())){
                        newsCommentId = newsCommentId+newslist.get(i).getNewsCommentId()+",";
                    }
                }

                newsCommentId = newsCommentId.substring(0,newsCommentId.length()-1);
                jsonObject.addProperty("newsCommentId",newsCommentId);
                requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
                observable = ApiUtils.getApi().delNewsComment(requestBean);

                break;
        }






        observable .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "删除中");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(mContext) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                switch (GetDataType){
                    case 0:
                        mygoodsadpater.setAddDataType(false);
                        break;
                    case 1:
                        myvenueadpater.setAddDataType(false);
                        break;
                    case 2:
                        myvideoadpater.setAddDataType(false);
                        break;
                    case 3:
                        mynewsadpater.setAddDataType(false);
                        break;
                }
                getData();
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }
}
