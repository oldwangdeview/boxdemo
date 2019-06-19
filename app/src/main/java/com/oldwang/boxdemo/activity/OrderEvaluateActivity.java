package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateOrderListEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.FileUtils;
import com.oldwang.boxdemo.util.PhotoUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.BottomMenuDialog;
import com.oldwang.boxdemo.view.MyGridView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
 * 评价
 */
public class OrderEvaluateActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.addimage)
    MyGridView mygridview;

    @BindView(R.id.iv_image)
    ImageView iv_image;

    @BindView(R.id.image_1)
    ImageView image_1;

    @BindView(R.id.image_2)
    ImageView image_2;

    @BindView(R.id.image_3)
    ImageView image_3;

    @BindView(R.id.et_content)
    EditText et_content;


    public static int MAX_IMAGE = 6;


    private Addimageadpater addimageAdpater;

    private ImageBean deleteImageBean;
    private List<ImageBean> filepath = new ArrayList<>();

    private String commodityId;
    private String orderNo;


    private String recommendContent;
    private String recommendScore;

    private String goodsImage;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_orderevaluate);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("商品评价");
        recommendScore = "5";

        commodityId = getIntent().getStringExtra(Contans.INTENT_DATA);
        orderNo = getIntent().getStringExtra(Contans.INTENT_TYPE);
        goodsImage = getIntent().getStringExtra(Contans.INTENT_TYPE_TWO);


        if (!TextUtils.isEmpty(goodsImage)){
            UIUtils.loadImageView(mContext,goodsImage,iv_image);
        }


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
                Matisse.from(OrderEvaluateActivity.this)
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



    public static void startactivity(Context mContext,String commodityId,String orderNo,String goodsImage){
        Intent mIntent = new Intent(mContext,OrderEvaluateActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,commodityId);
        mIntent.putExtra(Contans.INTENT_TYPE,orderNo);
        mIntent.putExtra(Contans.INTENT_TYPE_TWO,goodsImage);

        mContext.startActivity(mIntent);
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

    @OnClick({R.id.layout_1,R.id.layout_2,R.id.layout_3,R.id.btn_submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.layout_1:
                recommendScore = "5";
                image_1.setImageDrawable(getResources().getDrawable(R.mipmap.addresslist_choice));
                image_2.setImageDrawable(getResources().getDrawable(R.mipmap.addresslist_unchoice));
                image_3.setImageDrawable(getResources().getDrawable(R.mipmap.addresslist_unchoice));
                break;
            case R.id.layout_2:
                recommendScore = "3";
                image_1.setImageDrawable(getResources().getDrawable(R.mipmap.addresslist_unchoice));
                image_2.setImageDrawable(getResources().getDrawable(R.mipmap.addresslist_choice));
                image_3.setImageDrawable(getResources().getDrawable(R.mipmap.addresslist_unchoice));
                break;
            case R.id.layout_3:
                recommendScore = "1";
                image_1.setImageDrawable(getResources().getDrawable(R.mipmap.addresslist_unchoice));
                image_2.setImageDrawable(getResources().getDrawable(R.mipmap.addresslist_unchoice));
                image_3.setImageDrawable(getResources().getDrawable(R.mipmap.addresslist_choice));
                break;
            case R.id.btn_submit:
                recommendContent = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(recommendContent)){
                    ToastUtils.makeText("请输入评论信息");
                    return;
                }
                customerServiceSave();

                break;
        }

    }



    /**
     * 评价
     */
    private void customerServiceSave() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commodityId",commodityId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("orderNo",orderNo);
        jsonObject.addProperty("recommendContent",recommendContent);
        jsonObject.addProperty("recommendScore",recommendScore);
        jsonObject.addProperty("recommendType","1");


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().commodityRecommendSave(requestBean)
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
                    String mainId = stringStatusCode.getData().getRecommendId();
                    if (addimageAdpater.getFiles() != null && addimageAdpater.getFiles().size() > 0){
                        uploadFileList(addimageAdpater.getFiles(),mainId);
                    }else {
                        ToastUtils.makeText("评论成功");
                        EventBus.getDefault().post(new UpdateOrderListEvent());
                        finish();
                        dismissLoadingDialog();
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

    private int count;
    public void uploadFileList(List<File> files,String id) {
        MultipartBody.Part body = null;
        count = 0;
        for (File file : files) {
            if (file.exists()) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                Observable observable = ApiUtils.getApi().uploadfile(id, "shop_order_recommend_pics", body)
                        .compose(RxHelper.getObservaleTransformer())
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

                HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(OrderEvaluateActivity.this) {
                    @Override
                    protected void _onNext(StatusCode<Object> stringStatusCode) {

                        count++;
                        if (count == files.size()){
                            ToastUtils.makeText("评论成功");
                            EventBus.getDefault().post(new UpdateOrderListEvent());
                            finish();
                            dismissLoadingDialog();
                        }

                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.makeText(message);
                        dismissLoadingDialog();
                    }
                }, "", lifecycleSubject, false, true);            }
        }



    }
}
