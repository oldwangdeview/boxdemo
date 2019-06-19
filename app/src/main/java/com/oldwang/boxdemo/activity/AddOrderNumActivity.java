package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.AddressData;
import com.oldwang.boxdemo.bean.CommodityData;
import com.oldwang.boxdemo.bean.CouponData;
import com.oldwang.boxdemo.bean.CourierData;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.OrderDetail;
import com.oldwang.boxdemo.bean.OrderInfo;
import com.oldwang.boxdemo.bean.OrderInfoDetail;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateOrderListEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 填写单号
 */
public class AddOrderNumActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;


    @BindView(R.id.tv_express_name)
    TextView tv_express_name;

    @BindView(R.id.et_content)
    EditText et_content;


    private String orderNo;
    private String serviceNo;
    private static final int GET_EXPRESS = 0x1421;

    private CourierData courierDataTemp;
    private String courierNo;
    private OrderInfoDetail orderDetail;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_addordernum);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("填写单号");
        orderNo = getIntent().getStringExtra(Contans.INTENT_DATA);
        serviceNo = getIntent().getStringExtra(Contans.INTENT_TYPE);
        orderDetail = (OrderInfoDetail) getIntent().getSerializableExtra("orderDetail");
    }

    public static void startactivity(Context mContext, String orderNo, String serviceNo,OrderInfoDetail orderDetail){
        Intent mIntent = new Intent(mContext,AddOrderNumActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,orderNo);
        mIntent.putExtra(Contans.INTENT_TYPE,serviceNo);
        mIntent.putExtra("orderDetail",orderDetail);

        mContext.startActivity(mIntent);

    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }

    @OnClick(R.id.layout_choidekd)
    public void choiceLogistics(){

        JsonObject jsonObject = new JsonObject();

        JsonArray jsonArray = new JsonArray();

        for (CommodityData commodityData : orderDetail.getCommoditys()) {
            JsonObject commoditys = new JsonObject();
            commoditys.addProperty("commodityId", commodityData.getCommodityId());
            commoditys.addProperty("commodityNum", commodityData.getCommodityNum());
            jsonArray.add(commoditys);
        }
        jsonObject.add("commoditys", jsonArray);


//        jsonObject.addProperty("courierId", courierData.getCourierId());
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("receivingAddressId", orderDetail.getAddress().getReceivingAddressId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);

        ChoiceLogisticsActivity.startActivitForResult(this,requestBean, GET_EXPRESS);
    }


    @OnClick(R.id.btn_submit)
    public void sumbit(){
        if (courierDataTemp == null){
            ToastUtils.makeText("请选择快递");
            return;
        }
        courierNo =  et_content.getText().toString();

        if (TextUtils.isEmpty(courierNo)){
            ToastUtils.makeText("请输入快递单号");
            return;
        }
        submitCourierNumber();
    }

    /**
     * 填写单号
     */
    private void submitCourierNumber() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courierNo",courierNo);
        jsonObject.addProperty("logisticsId",courierDataTemp.getCourierId());
        jsonObject.addProperty("logisticsName",courierDataTemp.getCourierName());
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("orderNo",orderNo);
        jsonObject.addProperty("receivingAddress",orderDetail.getAddress().getReceivingAddress());
        jsonObject.addProperty("serviceId",serviceNo);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().submitCourierNumber(requestBean)
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
                    ToastUtils.makeText("提交成功");
                    EventBus.getDefault().post(new UpdateOrderListEvent());
                    finish();
                    dismissLoadingDialog();
                }
            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //与startActivityForResult配套
        super.onActivityResult(requestCode, resultCode, data); //requestCode(申请码), 用于判断

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GET_EXPRESS:
                    courierDataTemp = (CourierData) data.getSerializableExtra("courierData");
                    if (courierDataTemp != null){
                        tv_express_name.setText(courierDataTemp.getCourierName());
                    }

                    break;


            }

        }
    }
}
