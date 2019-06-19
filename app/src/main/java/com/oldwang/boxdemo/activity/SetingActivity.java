package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.UserInfo;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.MainEvent;
import com.oldwang.boxdemo.event.UpdateNoticeEvent;
import com.oldwang.boxdemo.event.UpdatePhoneEvent;
import com.oldwang.boxdemo.util.DataCleanManager;
import com.oldwang.boxdemo.util.LogUntil;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 */
public class SetingActivity extends BaseActivity {


    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.text_visioncode)
    EditText text_visioncode;
    @BindView(R.id.huancun_text)
    EditText huancun_text;
    @BindView(R.id.phone_num)
    TextView phone_num;



    @Override
    protected void initView() {
        setContentView(R.layout.activity_seting);

    }

    @Override
    protected void initData() {
        super.initData();

        String user = PreferencesUtils.getInstance().getString(Contans.userInfo, "");
        if (!TextUtils.isEmpty(user)){
            UserInfo userInfo = new Gson().fromJson(user,UserInfo.class);
            if (userInfo != null){
                if (!TextUtils.isEmpty(userInfo.getMemberAccount())){
                    phone_num.setText(userInfo.getMemberAccount());
                }
            }
        }

        titlename.setText("设置");
        try {
            huancun_text.setText(DataCleanManager.getTotalCacheSize(SetingActivity.this));
            text_visioncode.setText("V "+UIUtils.getAppVersionName(mContext));
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 关于我们
     */
    @OnClick(R.id.layout_aboutus)
    public void gotoAboutUs(){
        AboutUsActivity.startactivity(mContext);
    }

    /**
     * 绑定手机
     */
    @OnClick({R.id.layout_bangdingphone})
    public void bangdingshouji(){
        BandingPhoneOneActivity.startactivity(mContext,phone_num.getText().toString());
    }

    /**
     * 帮助与反馈
     */
    @OnClick({R.id.layout_helpandfeedback})
    public void toHelp(){
        HelpActivity.startactivity(mContext);
    }


    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,SetingActivity.class);
        mContext.startActivity(mIntent);

    }

    /**
     * 清除缓存
     */
    @OnClick({R.id.layout_claerdata,R.id.huancun_text})
    public void clecn_gcdata(){
        AlertView alertView = new AlertView("", "确定清除缓存～", null, null, new String[]{"取消", "确定"}, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position==1){
                    DataCleanManager.clearAllCache(SetingActivity.this);
                    try {
                        huancun_text.setText(DataCleanManager.getTotalCacheSize(SetingActivity.this));
                    } catch (Exception e) {
                        huancun_text.setText("0k");
                    }
                }

            }
        });
        alertView.show();
    }


    @OnClick(R.id.loginout_btn)
    public void loginout(){
        AlertView alertView = new AlertView("提示", "是否退出登录？", null, null, new String[]{"取消", "确定"}, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position==1){
                    AbsSuperApplication.setToken("");
                    PreferencesUtils.getInstance().putString(Contans.userInfo,"");
                    AbsSuperApplication.clearMemberId();
                    EventBus.getDefault().post(new MainEvent(2));

                    EMClient.getInstance().logout(true, new EMCallBack() {

                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub
                            LogUntil.show(mContext,TAG,"环信__"+"显示帐号已经退出登录");

                        }

                        @Override
                        public void onProgress(int progress, String status) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onError(int code, String message) {
                            // TODO Auto-generated method stub

                        }
                    });
                    finish();
                }

            }
        });
        alertView.show();
    }


    /**
     * 更新手机号码
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateList(UpdatePhoneEvent event) {
        String user = PreferencesUtils.getInstance().getString(Contans.userInfo, "");
        if (!TextUtils.isEmpty(user)){
            UserInfo userInfo = new Gson().fromJson(user,UserInfo.class);
            if (userInfo != null){
                if (!TextUtils.isEmpty(userInfo.getMemberAccount())){
                    phone_num.setText(userInfo.getMemberAccount());
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }
}
