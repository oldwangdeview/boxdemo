package com.oldwang.boxdemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.AssembleFragmentAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.AttributeColorBean;
import com.oldwang.boxdemo.bean.AttributeQualityBean;
import com.oldwang.boxdemo.bean.AttributeSizeBean;
import com.oldwang.boxdemo.bean.BrandData;
import com.oldwang.boxdemo.bean.CommodityTeamBuyData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ChoiceDataReturnLister;
import com.oldwang.boxdemo.interfice.ChoicePriceLister;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.view.ChoiceView;
import com.oldwang.boxdemo.view.RightChoiceMoreDialog;
import com.oldwang.boxdemo.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AssembleListQuanlianActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.recyclerview)
    YRecycleview recyclerview;
    @BindArray(R.array.assble_usersize)
    String[] assemble;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_volume)
    TextView tv_volume;
    @BindView(R.id.tv_evalute)
    TextView tv_evalute;

    @BindView(R.id.im_price)
    ImageView im_price;
    @BindView(R.id.im_volume)
    ImageView im_volume;
    @BindView(R.id.im_ecalute)
    ImageView im_ecalute;



    private AssembleFragmentAdpater madpater;
    private int contentpage =1;
    private int total = 0;
    private final int size = 8;
    private Dialog mLoadingDialog;
    private int type = -1;
    private String commodityTypeId = null;
    private List<CommodityTeamBuyData> datas = new ArrayList<>();
    private List<BrandData> brandlistdat = new ArrayList<>();


    private boolean aes = false;
    private int title_buttontype = 0;
    private List<TextView> titleTextviewListdata = new ArrayList<>();
    private List<ImageView> titleimage = new ArrayList<>();
    private List<AttributeColorBean> choicecolorlistdata = new ArrayList<>();
    private List<AttributeQualityBean> choicequality = new ArrayList<>();
    private List<AttributeSizeBean> choicesize = new ArrayList<>();
    private List<String> attributeColor = new ArrayList<>();
    private List<String> attributeQuality = new ArrayList<>();
    private List<String> attributeSize = new ArrayList<>();
    private double maxBuyPrice = -1;
    private double minBuyPrice = -1;
    private String brandName = "";
    private Integer teambuypeoplesize = 0;
    private RightChoiceMoreDialog mdialog = null;

    private String commodityTypeName;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_assenblequanlian);
    }



    @Override
    protected void initData() {
        super.initData();
        type = getIntent().getIntExtra(Contans.INTENT_TYPE,-1);
        commodityTypeId = getIntent().getStringExtra(Contans.INTENT_DATA);
        commodityTypeName = getIntent().getStringExtra(Contans.INTENT_TYPE_TWO);
        if(type==-1){
            finish();
        }

        titleTextviewListdata.add(tv_price);
        titleTextviewListdata.add(tv_volume);
        titleTextviewListdata.add(tv_evalute);
        titleimage.add(im_price);
        titleimage.add(im_volume);
        titleimage.add(im_ecalute);


        madpater = new AssembleFragmentAdpater(mContext,datas,type);
        madpater.setListOnclick(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                String commodityId = datas.get(position).getCommodityId();
                GoodsDetailActivity.startactivity(mContext,type==1?false:true,commodityId, null);
            }
        });
        recyclerview.setLayoutManager(new GridLayoutManager(mContext,2));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(madpater);

        switch (type){
            case 1:
                if (!TextUtils.isEmpty(commodityTypeName)){
                    titlename.setText(commodityTypeName);
                }else {
                    titlename.setText("分类");
                }

                break;
            case 2:
                titlename.setText("拼团");
                break;
        }



        getdata(type);
        getData();
        getSpecificationsdata();
    }


    @Override
    protected void initEvent() {
        super.initEvent();
        recyclerview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                recyclerview.setReFreshComplete();
                contentpage = 1;
                getdata(type);
            }

            @Override
            public void onLoadMore() {
                int totalPaeg = total / size + (total % size == 0 ? 0 : 1);
                if (contentpage < totalPaeg) {
                    contentpage++;
                    getdata(type);
                }

            }
        });
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }
    /**
     * @param mContext 上下文
     * @param type type==1(分类) type==2(拼团)
     */
    public static void startactivity(Context mContext,int type,String commodityTypeId,String commodityTypeName){
        Intent mintent = new Intent(mContext,AssembleListQuanlianActivity.class);
        mintent.putExtra(Contans.INTENT_TYPE,type);
        mintent.putExtra(Contans.INTENT_DATA,commodityTypeId);
        mintent.putExtra(Contans.INTENT_TYPE_TWO,commodityTypeName);
        mContext.startActivity(mintent);
    }


    private void getdata(int type){
        seleteposition();
        Observable observable = null;
        RequestBean requestBean = null;
        JsonObject jsonObject = new JsonObject();


        jsonObject.addProperty("commodityTypeId", commodityTypeId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", contentpage);
        if(title_buttontype>=0) {
            jsonObject.addProperty("orderByColumn",
                             title_buttontype==0?"scp.same_price" :
                             title_buttontype==1?"sell_count" :
                             title_buttontype==2?"recommend_scores" :
                             "");


        }
        jsonObject.addProperty("isAsc", aes?"asc":"desc");


        if(maxBuyPrice>=0){
            jsonObject.addProperty("maxBuyPrice", maxBuyPrice);
        }
        if(minBuyPrice>=0){
            jsonObject.addProperty("minBuyPrice",minBuyPrice);
        }
        if(attributeColor.size()>0){

            JsonArray array = new JsonArray();
            for (String s : attributeColor) {
                array.add(s);
            }
            jsonObject.add("attributeColor",array);
        }

        if(attributeQuality.size()>0){
            JsonArray array = new JsonArray();
            for (String s : attributeQuality) {
                array.add(s);
            }
            jsonObject.add("attributeQuality",array);
        }

        if(attributeSize.size()>0){
            JsonArray array = new JsonArray();
            for (String s : attributeSize) {
                array.add(s);
            }
            jsonObject.add("attributeSize",array);
        }

        if(!TextUtils.isEmpty(brandName)){
            jsonObject.addProperty("brandName",brandName);
        }
        jsonObject.addProperty("total",size);

        requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        if(type==1){
            observable = ApiUtils.getApi().commodityList(requestBean);
        }

        if(type==2){
            observable = ApiUtils.getApi().teamBuyList(requestBean);
        }


        if(observable==null){
            return;
        }

        observable .compose(RxHelper.getObservaleTransformer())
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

                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    if (data != null && data.getCommodityTeamBuyData() != null && data.getCommodityTeamBuyData().getList() != null){
                        if (contentpage == 1){
                            datas.clear();
                        }
                        datas.addAll(data.getCommodityTeamBuyData().getList());
                        total = data.getCommodityTeamBuyData().getTotal();

                        madpater.notifyDataSetChanged();
                        recyclerview.setloadMoreComplete();


                    }
                }

                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }


    /**
     * 筛选数据
     */
    @OnClick({R.id.lin_surh})
    public void surhdata(){
        if(mdialog==null){
        List<ChoiceView> viewlist = new ArrayList<>();
        //价格筛选
        ChoiceView choiceprice = new ChoiceView(mContext, "价格区间", new ChoicePriceLister() {
            @Override
            public void returndata(String minprice, String maxprice) {
                if(!TextUtils.isEmpty(maxprice)) {
                    maxBuyPrice = Double.parseDouble(maxprice);
                }
                if(!TextUtils.isEmpty(minprice)){
                    minBuyPrice =Double.parseDouble(minprice);
                }
            }
        });
        viewlist.add(choiceprice);

        //品牌
        if(brandlistdat.size()>0){

            ChoiceView choicebrand = new ChoiceView<BrandData>(mContext, brandlistdat, "品牌", true, new ChoiceDataReturnLister<BrandData>() {
                @Override
                public void getChoicedata(List<BrandData> listdata) {
                    if(listdata.size()>0) {
                        brandName = listdata.get(0).getBrandName();
                    }else{
                        brandName = "";
                    }
                }
            },false);
            viewlist.add(choicebrand);

        }


        if(choicecolorlistdata.size()>0){

            ChoiceView choicecolor = new ChoiceView<AttributeColorBean>(mContext, choicecolorlistdata, "颜色", true, new ChoiceDataReturnLister<AttributeColorBean>() {
                @Override
                public void getChoicedata(List<AttributeColorBean> listdata) {

                    attributeColor.clear();
                    for(int i = 0;i<listdata.size();i++){
                        attributeColor.add(listdata.get(i).getColorName());
                    }

                }
            },true);
            viewlist.add(choicecolor);
        }

        if(choicequality.size()>0){
            ChoiceView choicecolor = new ChoiceView<AttributeQualityBean>(mContext, choicequality, "材质", true, new ChoiceDataReturnLister<AttributeQualityBean>() {
                @Override
                public void getChoicedata(List<AttributeQualityBean> listdata) {
                    attributeQuality.clear();
                    for(int i=0;i<listdata.size();i++){
                        attributeQuality.add(listdata.get(i).getQualityName());
                    }
                }
            },true);
            viewlist.add(choicecolor);
        }



        if(choicesize.size()>0){
            ChoiceView choicesizeciew = new ChoiceView<AttributeSizeBean>(mContext, choicesize, "尺寸", true, new ChoiceDataReturnLister<AttributeSizeBean>() {
                @Override
                public void getChoicedata(List<AttributeSizeBean> listdata) {
                    attributeSize.clear();
                    for(int i=0;i<listdata.size();i++){
                        attributeSize.add(listdata.get(i).getSizeName());
                    }
                }
            },true);
            viewlist.add(choicesizeciew);
        }


        //拼团人数
        if(assemble.length>0&&type==2){
            List<String> assemblelist = new ArrayList<>();
            for(int i=0;i<assemble.length;i++){
                assemblelist.add(assemble[i]);
            }
            ChoiceView choiceassenble = new ChoiceView<String>(mContext, assemblelist, "拼团人数", true, new ChoiceDataReturnLister<String>() {
                @Override
                public void getChoicedata(List<String> listdata) {
                    if(listdata.size()>0) {
                        String data = listdata.get(0).replace("人", "");
                        teambuypeoplesize = Integer.parseInt(data);
                    }else{
                        teambuypeoplesize = -1;
                    }
                }
            },false);
            viewlist.add(choiceassenble);
        }

         mdialog = new RightChoiceMoreDialog.Builder(mContext).setOkButtonClickLister(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getdata(type);
            }
        }).setconntentview(viewlist).create();
        }
        mdialog.show();

    }


    /**
     * 获取品牌数据
     */
    private void getData() {


        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("pageNum", 1);
        jsonObject.addProperty("total", 1000000);


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
                    brandlistdat.clear();
                    brandlistdat.addAll(list);


                }

            }

            @Override
            protected void _onError(String message) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }


    @OnClick(R.id.tv_price)
    public void choiceprice(){

        if(title_buttontype==0){
            aes = !aes;
        }else{
         title_buttontype=0;
         aes = false;
        }

        getdata(type);
    }




    @OnClick(R.id.tv_volume)
    public void choicevolume(){

        if(title_buttontype==1){
            aes = !aes;
        }else{
            title_buttontype=1;
            aes = false;
        }

        getdata(type);
    }




    @OnClick(R.id.tv_evalute)
    public void choiceevalute(){

        if(title_buttontype==2){
            aes = !aes;
        }else{
            title_buttontype=2;
            aes = false;
        }

        getdata(type);
    }



    private void seleteposition(){
        for(int i = 0;i<titleTextviewListdata.size();i++){
            if(i==title_buttontype){
                titleTextviewListdata.get(title_buttontype).setTextColor(getResources().getColor(R.color.c_d52e21));
                titleimage.get(title_buttontype).setVisibility(View.VISIBLE);
                if(aes){
                   titleimage.get(i).setImageResource(R.mipmap.arrow_topdown_red);
                }else{
                    titleimage.get(i).setImageResource(R.mipmap.arrow_down_red);
                }
            }else{
                titleTextviewListdata.get(i).setTextColor(getResources().getColor(R.color.c_595959));
                titleimage.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }


   private void getSpecificationsdata(){
        if(!TextUtils.isEmpty(commodityTypeId)){




            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
            jsonObject.addProperty("commodityTypeId", commodityTypeId);

            RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
            Observable observable =
                    ApiUtils.getApi().getAttributeList(requestBean)
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
                        if(data.getAttributeColor().size()>0){
                            choicecolorlistdata.clear();
                            choicecolorlistdata.addAll(data.getAttributeColor());
                        }
                        if(data.getAttributeQuality().size()>0){
                            choicequality.clear();
                            choicequality.addAll(data.getAttributeQuality());
                        }

                        if(data.getAttributeSize().size()>0){
                            choicesize.clear();
                            choicesize.addAll(data.getAttributeSize());
                        }

                    }

                }

                @Override
                protected void _onError(String message) {
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                    ToastUtils.makeText(message);
                }
            }, "", lifecycleSubject, false, true);



        }
   }
}
