package com.oldwang.boxdemo.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.util.CheckPermission;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.UIUtils;


/**
 * Created by Administrator on 2018/12/25 0025.
 */

public class FalshActivity extends BaseActivity {

    static final String[] PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.CAMERA,//相机
            Manifest.permission.RECORD_AUDIO,//相机
            Manifest.permission.ACCESS_COARSE_LOCATION,//定位
            Manifest.permission.ACCESS_FINE_LOCATION,//定位
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE

    };

   private boolean startthrend = true;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UIUtils.showFullScreen(FalshActivity.this,true);

    }

    @Override
    protected void initView() {


        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1500);
                    Message msg = new Message();
                    msg.arg1 = 101;
                    mhander.sendMessage(msg);
                }catch (Exception e){
                 e.printStackTrace();
                }
            }
        }.start();

    }


    Handler mhander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==101){
                intdata();
            }
        }
    };


    private void intdata(){

        if (!UIUtils.isMarshmallow()) {
            LoginActivity.startactivity(this);
           finish();
        } else {
            CheckPermission checkPermission = new CheckPermission(FalshActivity.this);
            if (checkPermission.permissionSet(PERMISSION)) {
                PermissionActivity.startActivityForResult(FalshActivity.this, Contans.PERMISSION_REQUST_COND, PERMISSION);
            } else {
                String userInfo = PreferencesUtils.getInstance().getString(Contans.userInfo, "");

                //未登录
                if (TextUtils.isEmpty(AbsSuperApplication.getToken()) || TextUtils.isEmpty(userInfo)){
                    LoginActivity.startactivity(this);
                }else {
                    //已登录
                    MainActivity.startactivity(this);
                }
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Contans.PERMISSION_REQUST_COND) {
            if (resultCode == PermissionActivity.PERMISSION_DENIEG) {
                //没有权限
                finish();
            } else if (resultCode == PermissionActivity.PERMISSION_GRANTED) {
                //有权限
                LoginActivity.startactivity(this);
               finish();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        startthrend = false;
    }
}
