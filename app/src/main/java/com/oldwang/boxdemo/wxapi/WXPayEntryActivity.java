package com.oldwang.boxdemo.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.WeChartPayEvent;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Time  2018/3/2 15:11
 * Dest  微信支付
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private String Tag="WXPayEntryActivity";
    private IWXAPI mIWXAPI;
    private String mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mData = intent.getStringExtra("_wxapi_payresp_extdata");
        mIWXAPI = WXAPIFactory.createWXAPI(this,Contans.WEIXIN_APP_ID,true);
        mIWXAPI.handleIntent(intent,this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
        Log.i(Tag, "onReq: "+baseReq.toString());

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mIWXAPI.handleIntent(intent, this);
    }

    @Override
    public void onResp(BaseResp resp) {
        EventBus.getDefault().post(new WeChartPayEvent(resp.errCode));
        finish();
//        if (resp.errCode == 0) {
////                       ToastUtils.makeText("支付成功");
//                       finish();
//                   } else if (resp.errCode == -2) {
//                       //用户取消
////                       ToastUtils.makeText("取消支付");
//                       finish();
//                   } else if (resp.errCode == -1) {
//                       //支付失败
////                       ToastUtils.makeText("支付失败");
//                       finish();
//        }

    }
}
