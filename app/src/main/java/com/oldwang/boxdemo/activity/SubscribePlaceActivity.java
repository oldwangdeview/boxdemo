package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.ChooseSpaceTypeAdapter;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ServiceData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/***
 * 训练基地预约
 */
public class SubscribePlaceActivity extends BaseActivity {


    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;


    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.et_phone)
    EditText et_phone;

    @BindView(R.id.tv_project)
    TextView tv_project;

    @BindView(R.id.tv_begin_timne)
    TextView tv_begin_timne;

    @BindView(R.id.tv_end_timne)
    TextView tv_end_timne;


    private String venueId;

    private TimePickerDialog benginTimePickerDialog;
    private TimePickerDialog endTimePickerDialog;

    private long beginTime, endTime;

    private String venueProjectId;

    private List<ServiceData> serviceData;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_subcribe_place);
    }


    @Override
    protected void initData() {
        super.initData();
        titlename.setText("预约详情");
        venueId = getIntent().getStringExtra(Contans.INTENT_DATA);

        benginTimePickerDialog = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        beginTime = millseconds;
                        tv_begin_timne.setText(DateTools.getFormat(millseconds, "yyyy-MM-dd HH:mm"));
                    }
                })
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("到店时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setMinMillseconds(System.currentTimeMillis())
                .setCyclic(false)
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(14)
                .build();

        endTimePickerDialog = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        endTime = millseconds;
                        tv_end_timne.setText(DateTools.getFormat(millseconds, "yyyy-MM-dd HH:mm"));
                    }
                })
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("离店时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setMinMillseconds(System.currentTimeMillis())
                .setCyclic(true)
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(14)
                .build();
        serviceProjectList();
    }


    @OnClick({R.id.ll_project, R.id.ll_begin_timne, R.id.ll_end_timne, R.id.tv_submit})
    public void choose(View view) {
        switch (view.getId()) {
            case R.id.ll_project:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //对他进行便宜
                    popupWindow.showAsDropDown(view, -10, 50, Gravity.BOTTOM);
                }
//                Window window = getWindow();
//                ColorDrawable cd = new ColorDrawable(0x000000);
//                window.setBackgroundDrawable(cd);
//                // 产生背景变暗效果，设置透明度
//                WindowManager.LayoutParams lp = window.getAttributes();
//                lp.alpha = 0.4f;
//
//                //之前不写这一句也是可以实现的，这次突然没效果了。网搜之后加上了这一句就好了。据说是因为popUpWindow没有getWindow()方法。
//                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                window.setAttributes(lp);
//
//                //对popupWindow进行显示
//                popupWindow.update();

                break;
            case R.id.ll_begin_timne:
                benginTimePickerDialog.show(getSupportFragmentManager(), "all");
                break;
            case R.id.ll_end_timne:
                endTimePickerDialog.show(getSupportFragmentManager(), "all");
                break;
            case R.id.tv_submit:

                String bookingPersonName = et_name.getText().toString();
                String bookingPersonPhone = et_phone.getText().toString();

                if (TextUtils.isEmpty(bookingPersonName)){
                    ToastUtils.makeText("请输入预约人姓名");
                    return;
                }

                if (TextUtils.isEmpty(bookingPersonPhone)){
                    ToastUtils.makeText("请输入预约人手机号");
                    return;
                }
                if (!UIUtils.isPhoneNumber(bookingPersonPhone)) {
                    ToastUtils.makeText("手机号不合法");
                    return;
                }
                if (TextUtils.isEmpty(venueProjectId)){
                    ToastUtils.makeText("请选择预约项目");
                    return;
                }

                if (beginTime <= 0){
                    ToastUtils.makeText("请选择到店时间");
                    return;
                }
                if (endTime <= 0){
                    ToastUtils.makeText("请选择离店时间");
                    return;
                }
                venueLookSave(bookingPersonName,bookingPersonPhone);
                break;
        }

    }


    /***
     * 场馆服务列表
     */
    private void serviceProjectList() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("venueId", venueId);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().serviceProjectList(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    serviceData = stringStatusCode.getData().getServiceData();
                    initPopupWindow();
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
                finish();

            }
        }, "", lifecycleSubject, false, true);

    }

    /***
     * 预约保存
     * @param bookingPersonName
     * @param bookingPersonPhone
     */
    private void venueLookSave(String bookingPersonName, String bookingPersonPhone) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("bookingBeginTime", beginTime);
        jsonObject.addProperty("bookingEndTime", endTime);
        jsonObject.addProperty("bookingPersonName", bookingPersonName);
        jsonObject.addProperty("bookingPersonPhone", bookingPersonPhone);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        jsonObject.addProperty("venueProjectId", venueProjectId);


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().venueLookSave(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {
                dismissLoadingDialog();
                if (stringStatusCode != null) {
                    ToastUtils.makeText("预约成功");
                    finish();
                }


            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }

    private PopupWindow popupWindow;

    private void initPopupWindow() {
        //加载布局
        if(popupWindow ==null) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.trainimg_pop, null);
            //更改背景颜色
            inflate.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            popupWindow = new PopupWindow(inflate);
            popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(UIUtils.dip2px(325));

            LinearLayout ll_top = inflate.findViewById(R.id.ll_top);
            ll_top.setVisibility(View.GONE);

           RecyclerView areaRecyclerView = inflate.findViewById(R.id.recyclerview);
           final ChooseSpaceTypeAdapter choseTypeAdapter = new ChooseSpaceTypeAdapter(mContext, serviceData);
            areaRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            areaRecyclerView.setItemAnimator(new DefaultItemAnimator());
            areaRecyclerView.setAdapter(choseTypeAdapter);


            choseTypeAdapter.setListClickLister(new ListOnclickLister() {
                @Override
                public void onclick(View v, int position) {

                    ServiceData serviceData1 = serviceData.get(position);
                    venueProjectId = serviceData1.getVenueProjectId();
                    tv_project.setText(serviceData1.getVenueProjectName());
                    choseTypeAdapter.setTypeId(venueProjectId);
                    popupWindow.dismiss();

                }
            });

            //点击其他地方隐藏,false为无反应
            popupWindow.setFocusable(true);
        }


//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp=getWindow().getAttributes();
//                lp.alpha=1f;
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                getWindow().setAttributes(lp);
//            }
//        });

    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }


    public static void startactivity(Context mContext, String venueId) {
        Intent mIntent = new Intent(mContext, SubscribePlaceActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA, venueId);
        mContext.startActivity(mIntent);
    }


}
