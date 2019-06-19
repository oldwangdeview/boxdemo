package com.oldwang.boxdemo.http;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.oldwang.boxdemo.activity.LoginActivity;
import com.oldwang.boxdemo.activity.LoginForPhoneActivity;
import com.oldwang.boxdemo.application.BaseApplication;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.event.ExitLoginSuccess;
import com.oldwang.boxdemo.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by oldwang on 2016/10/10 11:52.
 */

public class ApiException extends RuntimeException {
    private static String message;


    public ApiException(int number, StatusCode resultCode) {
        this(getApiExceptionMessage(number, resultCode));

    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @param resultCode
     * @param
     * @return
     */
    private static String getApiExceptionMessage(int code, StatusCode resultCode) {
        Log.e("resultCode", new Gson().toJson(resultCode));
        switch (code) {
            case 0:
                message = resultCode.getMessage();
                break;
            case 2002:
                message = "未知错误";
                break;
            case 2003:
                message = "参数错误";
                break;
            case 3001:
                message = "无效的token";
                break;
            case 501:
                message = "登陆已失效，请重新登陆";
                BaseApplication.isLoginSuccess = false;
                //  BaseApplication.sUserInfoBean=null;
                EventBus.getDefault().post(new ExitLoginSuccess());
                break;
            case 5000:
                message = "网络开了一个小差";
                break;
            default:
                message = "未知错误";
        }
        if (TextUtils.isEmpty(message)) {
            message = resultCode.getMsg();
        }
        if (TextUtils.isEmpty(message)) {
            message = "";
        }
        return message;
    }
}
