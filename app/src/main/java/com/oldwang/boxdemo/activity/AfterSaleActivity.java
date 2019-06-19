package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.Addimageadpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.CommodityData;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.ImageBean;
import com.oldwang.boxdemo.bean.OrderInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateOrderListEvent;
import com.oldwang.boxdemo.event.UpdateUserEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.ChooseTypeBottomDialog;
import com.oldwang.boxdemo.view.MyGridView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 售后申请
 */
public class AfterSaleActivity extends BaseActivity {

    public static int MAX_IMAGE = 6;

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.mygridview)
    MyGridView mygridview;


    @BindView(R.id.iv_image)
    ImageView iv_image;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_goods_price)
    TextView tv_goods_price;

    @BindView(R.id.tv_attribute)
    TextView tv_attribute;

    @BindView(R.id.tv_goods_count)
    TextView tv_goods_count;

    @BindView(R.id.tv_type)
    TextView tv_type;

    @BindView(R.id.tv_price)
    EditText tv_price;

    @BindView(R.id.et_content)
    EditText et_content;

    @BindView(R.id.ll_prise)
    LinearLayout ll_prise;

    private Addimageadpater addimageAdpater;

    private CommodityData commodityData;
    private String orderNo;

    private ChooseTypeBottomDialog chooseTypeBottomDialog;

    private int type = 1;

    private int serviceType;
    private ImageBean deleteImageBean;
    private List<ImageBean> filepath = new ArrayList<>();
    private int count;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_aftersale);
    }

    private ImageView oneImageView;
    private ImageView twoImageView;
    private ImageView threeImageView;

    @Override
    protected void initData() {
        super.initData();

        commodityData  = (CommodityData) getIntent().getSerializableExtra(Contans.INTENT_DATA);
        orderNo = getIntent().getStringExtra(Contans.INTENT_TYPE);

        ChooseTypeBottomDialog.Builder builder = new ChooseTypeBottomDialog.Builder(mContext);



        chooseTypeBottomDialog =  builder.setClicklister(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ll_one:
                        type = 1;
                        oneImageView.setVisibility(View.VISIBLE);
                        twoImageView.setVisibility(View.GONE);
                        threeImageView.setVisibility(View.GONE);

                        break;
                    case R.id.ll_two:
                        type = 2;
                        oneImageView.setVisibility(View.GONE);
                        twoImageView.setVisibility(View.VISIBLE);
                        threeImageView.setVisibility(View.GONE);

                        break;
                    case R.id.ll_three:
                        type = 3;
                        oneImageView.setVisibility(View.GONE);
                        twoImageView.setVisibility(View.GONE);
                        threeImageView.setVisibility(View.VISIBLE);
                        break;
                    case R.id.ojbk_btn:
                        serviceType = type;
                        //	售后类型(01退货02换货03退款)
                        if (type == 1){
                            ll_prise.setVisibility(View.VISIBLE);
                            tv_type.setText("退款退货");
                        }else if (type == 2){
                            ll_prise.setVisibility(View.GONE);
                            tv_type.setText("换货");
                        }else {
                            ll_prise.setVisibility(View.VISIBLE);
                            tv_type.setText("仅退款");
                        }
                        chooseTypeBottomDialog.dismiss();
                        break;

                }
            }
        }).create();
        oneImageView = builder.getOneImageView();
        twoImageView = builder.getTwoImageView();
        threeImageView = builder.getThreeImageView();


        setData();

        titlename.setText("售后申请");

        deleteImageBean = new ImageBean(true, null);
        filepath.add(deleteImageBean);
        addimageAdpater = new Addimageadpater(this, filepath);
        addimageAdpater.setDeletImageBean(deleteImageBean);
        addimageAdpater.setOnclickItemLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                int size = MAX_IMAGE - (filepath.size() - 1);
                if (size <= 0) {
                    return;
                }
                Matisse.from(AfterSaleActivity.this)
                        .choose(MimeType.allOf())//图片类型
                        .countable(false)//true:选中后显示数字;false:选中后显示对号
                        .maxSelectable(size)//可选的最大数
                        .capture(true)//选择照片时，是否显示拍照
                        .captureStrategy(new CaptureStrategy(true, "com.oldwang.boxdemo"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .imageEngine(new GlideEngine())//图片加载引擎
                        .forResult(Contans.GALLERY_REQUEST_CODE);
            }
        });


        mygridview.setAdapter(addimageAdpater);
    }

    /**
     * 申请售后
     * @param price
     */
    private void customerServiceSave(String price) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        JsonObject orderInfo = new JsonObject();
        JsonArray commoditys = new JsonArray();
        JsonObject commodity = new JsonObject();
        commodity.addProperty("attributeColor",commodityData.getAttributeColor());
        commodity.addProperty("attributeId",commodityData.getAttributeId());
        if (!TextUtils.isEmpty(commodityData.getAttributeName())){
            commodity.addProperty("attributeName",commodityData.getAttributeName());
        }
        commodity.addProperty("attributeQuality",commodityData.getAttributeQuality());
        commodity.addProperty("attributeSize",commodityData.getAttributeSize());
        commodity.addProperty("brandId",commodityData.getBrandId());

        commodity.addProperty("commodityId",commodityData.getCommodityId());
        commodity.addProperty("commodityName",commodityData.getCommodityName());
        commodity.addProperty("commodityNum",commodityData.getCommodityNum());
        commodity.addProperty("priceUnit",commodityData.getPriceUnit());




        BigDecimal bigDecimal = new BigDecimal(commodityData.getPriceUnit()).multiply(new BigDecimal(commodityData.getCommodityNum())).setScale(2, BigDecimal.ROUND_HALF_UP);
        commodity.addProperty("totalPrice",bigDecimal.toString());
        if (!TextUtils.isEmpty(reallyPrice)){
            commodity.addProperty("totalPrice",reallyPrice);
        }

        commoditys.add(commodity);
        orderInfo.add("commoditys",commoditys);
        orderInfo.addProperty("orderNo",orderNo);

        String applyReason = et_content.getText().toString();


        JsonObject serviceInfo = new JsonObject();

        if (!TextUtils.isEmpty(applyReason)){
            serviceInfo.addProperty("applyReason",applyReason);
        }
        serviceInfo.addProperty("serviceType",serviceType);
        jsonObject.add("orderInfo",orderInfo);
        jsonObject.add("serviceInfo",serviceInfo);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().customerServiceSave(requestBean)
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
                    String mainId = stringStatusCode.getData().getMainId();
                    uploadFileList(addimageAdpater.getFiles(),mainId);
                }
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }

    public void uploadFileList(List<File> files,String id) {
        count = 0;
        MultipartBody.Part body = null;

        for (File file : files) {
            if (file.exists()) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                Observable observable = ApiUtils.getApi().uploadfile(id, "shop_after_sale_service", body)
                        .compose(RxHelper.getObservaleTransformer())
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

                HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(AfterSaleActivity.this) {
                    @Override
                    protected void _onNext(StatusCode<Object> stringStatusCode) {

                        count++;
                        if (count == files.size()){
                            ToastUtils.makeText("售后申请成功");
                            EventBus.getDefault().post(new UpdateOrderListEvent());
                            dismissLoadingDialog();
                            finish();
                        }

                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.makeText(message);
                        dismissLoadingDialog();
                    }
                }, "", lifecycleSubject, false, true);
            }
        }



    }

    private void setData() {

        if (!TextUtils.isEmpty(commodityData.getCommodityPicUrl())) {
            UIUtils.loadImageView(mContext, commodityData.getCommodityPicUrl(), iv_image);
        }

        if (!TextUtils.isEmpty(commodityData.getCommodityName())) {
            tv_name.setText(commodityData.getCommodityName());
        }
        if (!TextUtils.isEmpty(commodityData.getPriceUnit())) {
            tv_goods_price.setText("¥ " + commodityData.getPriceUnit());
        }


        if (!TextUtils.isEmpty(commodityData.getCommodityNum())) {
            tv_goods_count.setText("x" + commodityData.getCommodityNum());
        }else {
            commodityData.setCommodityNum("1");
        }
        if (TextUtils.isEmpty(commodityData.getPriceUnit())){
            commodityData.setPriceUnit("0");
        }

        BigDecimal bigDecimal = new BigDecimal(commodityData.getPriceUnit()).multiply(new BigDecimal(commodityData.getCommodityNum())).setScale(2, BigDecimal.ROUND_HALF_UP);


        tv_price.setText(bigDecimal.toString());

        String attribute = "";

        if (!TextUtils.isEmpty(commodityData.getAttributeColor())) {
            attribute = "颜色：" + commodityData.getAttributeColor() + " ";
        }
        if (!TextUtils.isEmpty(commodityData.getAttributeQuality())) {
            attribute += "材质：" + commodityData.getAttributeQuality() + " ";
        }
        if (!TextUtils.isEmpty(commodityData.getAttributeSize())) {
            attribute += "型号：" + commodityData.getAttributeSize();
        }
        tv_attribute.setText(attribute);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode) {
                case  Contans.GALLERY_REQUEST_CODE:
                    List<Uri> result2 = Matisse.obtainResult(data);
                    for (Uri uri : result2) {
                        if (filepath.size() >= MAX_IMAGE) {
                            filepath.remove(deleteImageBean);
                        }
                        filepath.add(new ImageBean(false, uri));
                    }
                    addimageAdpater.notifyDataSetChanged();

                    break;


            }
        }
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }

    @OnClick(R.id.ll_choose_type)
    public void chooseType() {
        chooseTypeBottomDialog.show();
    }

    private String reallyPrice;

    @OnClick(R.id.btn_submit)
    public void submit() {

        String trim = et_content.getText().toString().trim();

        if (serviceType == 0){
            ToastUtils.makeText("请选择售后类型");
            return;
        }

        if (TextUtils.isEmpty(trim)){
            ToastUtils.makeText("请输入售后说明");
            return;
        }
        String price = "";
        if (serviceType != 2){
             price = tv_price.getText().toString();
            if (TextUtils.isEmpty(price)){
                ToastUtils.makeText("退款金额不能为空");
            }

            if (new BigDecimal(price).compareTo(new BigDecimal(BigInteger.ZERO)) <= 0){
                ToastUtils.makeText("退款金额必须大于0");
                return;
            }
            BigDecimal bigDecimal = new BigDecimal(commodityData.getPriceUnit()).multiply(new BigDecimal(commodityData.getCommodityNum())).setScale(2, BigDecimal.ROUND_HALF_UP);

            if (new BigDecimal(price).compareTo(bigDecimal) > 0 ){
                ToastUtils.makeText("退款金额不能大于商品总金额");
                return;
            }
            reallyPrice = bigDecimal.toString();
        }


        if (addimageAdpater.getFiles() == null || addimageAdpater.getFiles().size() < 1){
            ToastUtils.makeText("请上传凭证");
            return;
        }
        customerServiceSave(price);
    }



    public static void startactivity(Context mContext, CommodityData commodityData,String orderNo){
        Intent mIntent = new Intent(mContext,AfterSaleActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,commodityData);
        mIntent.putExtra(Contans.INTENT_TYPE,orderNo);
        mContext.startActivity(mIntent);
    }
}
