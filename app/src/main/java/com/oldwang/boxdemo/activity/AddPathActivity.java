package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.AddressData;
import com.oldwang.boxdemo.bean.AreaTempData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateAddress;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 添加收货地址
 */
public class AddPathActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.user_path)
    TextView user_path;


    @BindView(R.id.user_name)
    EditText user_name;

    @BindView(R.id.user_phone)
    EditText user_phone;

    @BindView(R.id.iv_def)
    ImageView iv_def;


    @BindView(R.id.user_pathdetail)
    EditText user_pathdetail;

    private String addess = "";

    private String provinceId;
    private String cityId;
    private String districtId;
//    private String townshipId;


    private String defaultStatus = "0";

    private AddressData addressData;

    @Override
    protected void initView() {
        setContentView(R.layout.actiivity_address);
    }


    @Override
    protected void initData() {
        super.initData();
        addressData = (AddressData) getIntent().getSerializableExtra(Contans.INTENT_DATA);
        if(addressData == null){
            titlename.setText("添加收货地址");
        }else{
            user_name.setText(addressData.getMemberName());
            user_name.setSelection(user_name.getText().toString().length());

            user_pathdetail.setText(addressData.getAddressDetail());
            user_pathdetail.setSelection(user_pathdetail.getText().toString().length());



            String address = "";

            if (!TextUtils.isEmpty(addressData.getProvince())){
                address = addressData.getProvince();
            }
            if (!TextUtils.isEmpty(addressData.getCity())){
                address += addressData.getCity();
            }
            if (!TextUtils.isEmpty(addressData.getDistrict())){
                address += addressData.getDistrict();
            }

            user_path.setText(address);

            user_phone.setText(addressData.getMemberPhone());
            user_phone.setSelection(user_phone.getText().toString().length());

            provinceId = addressData.getProvinceId();
            cityId = addressData.getCityId();
            districtId = addressData.getDistrictId();

//            if (!TextUtils.isEmpty(addressData.getAddressDetail())){
//                user_path.setText(addressData.getAddressFull().replace(addressData.getAddressDetail(),""));
//            }else {
//                user_path.setText(addressData.getAddressFull());
//            }

            defaultStatus = addressData.getDefaultStatus();
            if (!TextUtils.isEmpty(defaultStatus) && defaultStatus.equals("1")){
                iv_def.setImageDrawable(getResources().getDrawable(R.mipmap.swtich_open));
            }else {
                iv_def.setImageDrawable(getResources().getDrawable(R.mipmap.switch_close));
            }


            titlename.setText("编辑收货地址");
        }
    }

    @OnClick(R.id.ll_choose_area)
    public void chooseArea(){
        ChooseAreaProvinceActivity.startactivity(mContext);
    }

    @OnClick(R.id.btn_save)
    public void save(){
        saveMyAddress();
    }

    /**
     * 新增修改地址
     */
    private void saveMyAddress() {


        String addressDetail = user_pathdetail.getText().toString().trim();
        String memberName = user_name.getText().toString().trim();
        String memberPhone = user_phone.getText().toString().trim();



        if (TextUtils.isEmpty(memberName)){
            ToastUtils.makeText("请输入收货人姓名");
            return;
        }
        if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(cityId)){
            ToastUtils.makeText("请选择区域");
            return;
        }

        if (TextUtils.isEmpty(memberPhone)){
            ToastUtils.makeText("请输入手机号码");
            return;
        }
        if(!UIUtils.isPhoneNumber(memberPhone)){
            ToastUtils.makeText("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(addressDetail)){
            ToastUtils.makeText("请输入详细地址");
            return;
        }


//        String addressFull = addess+addressDetail;



        JsonObject jsonObject = new JsonObject();

        /***详细地址*/
        jsonObject.addProperty("addressDetail", addressDetail);
//        /***收货地址全名*/
//        jsonObject.addProperty("addressFull", addressFull);
        /***市级ID*/
        jsonObject.addProperty("cityId", cityId);
        /***是否为默认地址(1是，0否)*/
        jsonObject.addProperty("defaultStatus", defaultStatus);
        /***区县ID*/
        jsonObject.addProperty("districtId", districtId);
        /***收货人姓名*/
        jsonObject.addProperty("memberName", memberName);
        /***收货人姓名*/
        jsonObject.addProperty("memberPhone", memberPhone);
        /***省级ID*/
        jsonObject.addProperty("provinceId", provinceId);

        if (addressData != null){
            jsonObject.addProperty("receivingAddressId", addressData.getReceivingAddressId());
        }
        /***乡镇街道ID*/
//        jsonObject.addProperty("townshipId", townshipId);

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().saveMyAddress(requestBean)
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
                EventBus.getDefault().post(new UpdateAddress());
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }



    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }

    @OnClick(R.id.iv_def)
    public void switchDefAddr(){

        if (defaultStatus.equals("0")){
            defaultStatus = "1";
            iv_def.setImageDrawable(getResources().getDrawable(R.mipmap.swtich_open));
        }else {
            defaultStatus = "0";
            iv_def.setImageDrawable(getResources().getDrawable(R.mipmap.switch_close));

        }

    }

    /**
     * 启动当前页面
     * @param mContext
     * @param type 1为添加 2为修改
     */
    public static void startactivity(Context mContext,int type){
        Intent mIntent = new Intent(mContext,AddPathActivity.class);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mContext.startActivity(mIntent);
    }


    /**
     * 启动当前页面
     * @param mContext
     * @param type 1为添加 2为修改
     */
    public static void startactivity(Context mContext, int type, AddressData addressData){
        Intent mIntent = new Intent(mContext,AddPathActivity.class);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mIntent.putExtra(Contans.INTENT_DATA,addressData);
        mContext.startActivity(mIntent);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateAre(AreaTempData event) {
        if (event != null){
            addess = event.getName();
            provinceId = event.getProvinceId();
            user_path.setText(addess);
            cityId = event.getCityId();
            districtId = event.getCountyId();
//            townshipId = event.getTownshipId();
        }
    }
}
