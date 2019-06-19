package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.AreaTempData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.MemberInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.UserInfo;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateUserEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.FileUtils;
import com.oldwang.boxdemo.util.PhotoUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.BottomMenuDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
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
 * 基地入住
 */
public class BaseCheckInActivity extends BaseActivity {
    private static final int REQUEST_CODE_ONE = 0x0001;
    private static final int REQUEST_CODE_TWO = 0x0002;
    private static final int REQUEST_CODE_THREE = 0x0003;


    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    //上传身份证试图
    @BindView(R.id.layout_view1)
    LinearLayout layout_view1;
    //正在审核试图
    @BindView(R.id.layout_view2)
    LinearLayout layout_view2;
    //审核通过视图
    @BindView(R.id.layout_view3)
    LinearLayout layout_view3;


    //负责人姓名
    @BindView(R.id.user_name)
    EditText user_name;
    //负责人电话
    @BindView(R.id.user_phone)
    EditText user_phone;

    //基地名称
    @BindView(R.id.user_basename)
    EditText user_basename;

    //所在区域
    @BindView(R.id.user_path)
    TextView user_path;

    //详细地址
    @BindView(R.id.user_pathdetail)
    EditText user_pathdetail;

    //身份证正面照
    @BindView(R.id.image_idcardz)
    ImageView image_idcardz;
    //身份证反面照
    @BindView(R.id.image_idcardf)
    ImageView image_idcardf;
    @BindView(R.id.image_yyzz)
    ImageView image_yyzz;

    @BindView(R.id.ll_choose_area)
    LinearLayout ll_choose_area;

    @BindView(R.id.btn_sumbit)
    Button btn_sumbit;


    private String addess = "";

    private String provinceId;
    private String cityId;
    private String districtId;
    private String townshipId;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_basecheckin);
    }


    @Override
    protected void initData() {
        super.initData();
        titlename.setText("基地入驻");
        baseEnterInfo();
    }

    /**
     * 获取用户信息
     */
    private void baseEnterInfo() {

        showLoadingDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().baseEnterInfo(requestBean)
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
                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    DataInfo data = stringStatusCode.getData();
                    UserInfo userInfo = data.getBaseInfo();

                    if (userInfo != null) {
                        String baseStatus = userInfo.getCheckStatus();
                        layout_view1.setVisibility(View.VISIBLE);

                        if (!TextUtils.isEmpty(userInfo.getPrincipalName())) {
                            user_name.setText(userInfo.getPrincipalName());
                        }

                        if (!TextUtils.isEmpty(userInfo.getPrincipalPhone())) {
                            user_phone.setText(userInfo.getPrincipalPhone());
                        }

                        if (!TextUtils.isEmpty(userInfo.getBaseName())) {
                            user_basename.setText(userInfo.getBaseName());
                        }

                        if (!TextUtils.isEmpty(userInfo.getProvinceId())) {
                            provinceId = userInfo.getProvinceId();
                        }
                        if (!TextUtils.isEmpty(userInfo.getCityId())) {
                            cityId = userInfo.getCityId();
                        }
                        if (!TextUtils.isEmpty(userInfo.getDistrictId())) {
                            districtId = userInfo.getDistrictId();
                        }
                        if (!TextUtils.isEmpty(userInfo.getTownshipId())) {
                            townshipId = userInfo.getTownshipId();
                        }

                        if (!TextUtils.isEmpty(userInfo.getAddress())) {
                            user_path.setText(userInfo.getAddress());
                        }
                        if (!TextUtils.isEmpty(userInfo.getBaseDetailedAddress())) {
                            user_pathdetail.setText(userInfo.getBaseDetailedAddress());
                        }

                        //为空为未提交审核
                        if (TextUtils.isEmpty(baseStatus) || baseStatus.equals("打回")) {
                            btn_sumbit.setClickable(true);
                            btn_sumbit.setFocusable(true);
                        } else {

                            ll_choose_area.setFocusable(false);
                            ll_choose_area.setClickable(false);

                            user_name.setFocusable(false);
                            user_pathdetail.setFocusable(false);
                            user_phone.setFocusable(false);
                            user_basename.setFocusable(false);
                            user_path.setFocusable(false);
                            user_pathdetail.setFocusable(false);
                            btn_sumbit.setClickable(false);
                            btn_sumbit.setFocusable(false);


                            btn_sumbit.setText(baseStatus);
                            btn_sumbit.setClickable(false);
                            btn_sumbit.setFocusable(false);
                            //	状态 '审核中' '审核通过' '打回'
//                            if (baseStatus.equals("审核中")) {
//                                layout_view2.setVisibility(View.VISIBLE);
//                            } else if (baseStatus.equals("审核通过")) {
//                                layout_view3.setVisibility(View.VISIBLE);
//                            }

                        }
                        if (!TextUtils.isEmpty(userInfo.getRealName())) {
                            user_name.setText(userInfo.getRealName());
                        }

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


    /**
     * 获取身份证正面照
     */
    @OnClick(R.id.image_idcardz)
    public void getIDcardZ() {
        Matisse.from(BaseCheckInActivity.this)
                .choose(MimeType.allOf())//图片类型
                .countable(false)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(1)//可选的最大数
                .capture(true)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.oldwang.boxdemo"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new GlideEngine())//图片加载引擎
                .forResult(REQUEST_CODE_ONE);
    }

    /**
     * 获取身份证反面照
     */
    @OnClick(R.id.image_idcardf)
    public void getIDcardF() {
        Matisse.from(BaseCheckInActivity.this)
                .choose(MimeType.allOf())//图片类型
                .countable(false)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(1)//可选的最大数
                .capture(true)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.oldwang.boxdemo"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new GlideEngine())//图片加载引擎
                .forResult(REQUEST_CODE_TWO);
    }

    @OnClick(R.id.image_yyzz)
    public void getyyzz() {
        Matisse.from(BaseCheckInActivity.this)
                .choose(MimeType.allOf())//图片类型
                .countable(false)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(1)//可选的最大数
                .capture(true)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.oldwang.boxdemo"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new GlideEngine())//图片加载引擎
                .forResult(REQUEST_CODE_THREE);
    }

    private int count = 0;

    public void uploadFileList(String businessId, List<File> files) {
        MultipartBody.Part body = null;
        count = 0;

        if (files != null && files.size() > 0) {

            for (File file : files) {
                if (file.exists()) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                    body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    Observable observable = ApiUtils.getApi().uploadfile(businessId, "b_base_info.file_data", body)
                            .compose(RxHelper.getObservaleTransformer())
                            .subscribeOn(Schedulers.io())
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {
                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread());

                    HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(mContext) {
                        @Override
                        protected void _onNext(StatusCode<Object> stringStatusCode) {
                            count++;
                            if (count == files.size()) {
                                ToastUtils.makeText("提交成功，等待后台管理员审核");
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


    }


    /**
     * 提交身份认证信息
     */
    private void upload() {

        String principalName = user_name.getText().toString();
        String principalPhone = user_phone.getText().toString();
        String baseDetailedAddress = user_pathdetail.getText().toString();
        String baseName = user_basename.getText().toString();


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("baseDetailedAddress", baseDetailedAddress);
        jsonObject.addProperty("baseName", baseName);
        jsonObject.addProperty("cityId", cityId);
        jsonObject.addProperty("districtId", districtId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("principalName", principalName);
        jsonObject.addProperty("principalPhone", principalPhone);
        jsonObject.addProperty("provinceId", provinceId);
        jsonObject.addProperty("townshipId", townshipId);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().baseEnter(requestBean)
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
                if (stringStatusCode != null && stringStatusCode.getData() != null) {
                    String mainId = stringStatusCode.getData().getMainId();
                    if (!TextUtils.isEmpty(mainId)) {
                        List<File> fileList = new ArrayList<>();
                        fileList.add(fileThree);
                        fileList.add(fileOne);
                        fileList.add(fileTwo);
                        uploadFileList(mainId, fileList);
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

    private File fileOne;
    private File fileTwo;
    private File fileThree;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ONE:
                    List<Uri> result = Matisse.obtainResult(data);
                    if (result != null && result.size() > 0) {
                        Glide.with(mContext).load(result.get(0))
                                .into(image_idcardz);
                        fileOne = FileUtils.uriToFile(result.get(0), mContext);
                    }
                    break;

                case REQUEST_CODE_TWO:
                    List<Uri> result2 = Matisse.obtainResult(data);
                    if (result2 != null && result2.size() > 0) {
                        Glide.with(mContext).load(result2.get(0))
                                .into(image_idcardf);
                        fileTwo = FileUtils.uriToFile(result2.get(0), mContext);
                    }

                    break;
                case REQUEST_CODE_THREE:
                    List<Uri> result3 = Matisse.obtainResult(data);
                    if (result3 != null && result3.size() > 0) {
                        Glide.with(mContext).load(result3.get(0))
                                .into(image_yyzz);
                        fileThree = FileUtils.uriToFile(result3.get(0), mContext);
                    }

                    break;

            }
        }
    }

    @OnClick(R.id.btn_sumbit)
    public void sumbit() {

        String principalName = user_name.getText().toString();
        String principalPhone = user_phone.getText().toString();
        String baseDetailedAddress = user_pathdetail.getText().toString();
        String baseName = user_basename.getText().toString();

        if (TextUtils.isEmpty(principalName)) {
            ToastUtils.makeText("请输入负责人姓名");
            return;
        }

        if (TextUtils.isEmpty(principalPhone)) {
            ToastUtils.makeText("请输入负责人号码");
            return;
        }

        if (!UIUtils.isPhoneNumber(principalPhone)) {
            ToastUtils.makeText("手机号不合法");
            return;
        }

        if (TextUtils.isEmpty(baseName)) {
            ToastUtils.makeText("请输入基地名称");
            return;
        }

        if (TextUtils.isEmpty(addess)) {
            ToastUtils.makeText("请选择所在区域");
            return;
        }

        if (TextUtils.isEmpty(baseDetailedAddress)) {
            ToastUtils.makeText("请输入详情地址");
            return;
        }

        if (fileOne == null) {
            ToastUtils.makeText("请上传身份证正面照片");
            return;
        }
        if (fileTwo == null) {
            ToastUtils.makeText("请上传身份证反面照片");
            return;
        }
        if (fileThree == null) {
            ToastUtils.makeText("请上传营业执照");
            return;
        }
        upload();

    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }


    @OnClick(R.id.ll_choose_area)
    public void chooseArea() {
        ChooseAreaProvinceActivity.startactivity(mContext);
    }


    public static void startactivity(Context mContext) {
        Intent mIntent = new Intent(mContext, BaseCheckInActivity.class);
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
        if (event != null) {
            addess = event.getName();
            provinceId = event.getProvinceId();
            user_path.setText(addess);
            cityId = event.getCityId();
            districtId = event.getCountyId();
            townshipId = event.getTownshipId();
        }
    }


}
