package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.OrderDetail;
import com.oldwang.boxdemo.bean.OrderInfo;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.FinishGoodsDetailEvent;
import com.oldwang.boxdemo.event.MainJumpEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class PaySuccessActivity extends BaseActivity {


    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.tv_price)
    TextView tv_price;

    private OrderInfo orderInfo;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_pay_success);
    }


    @Override
    protected void initData() {
        super.initData();
        titlename.setText("支付成功");
        orderInfo = (OrderInfo) getIntent().getSerializableExtra(Contans.INTENT_DATA);
        if (orderInfo != null){
            tv_price.setText("¥ "+orderInfo.getOrderPayPrice());
        }
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }


    @OnClick(R.id.tv_look_order)
    public void goToOrder(){
//        AbsSuperApplication.finishAllActivityReserver(MainActivity.class);
        MyOrderListActivity.startactivity(mContext,1);
        if (orderInfo.isPinTuan()){
            OrderDetailActivity.startactivity(mContext,orderInfo.getOrderNo(),-1,"");
        }else {
            OrderDetailActivity.startactivity(mContext,orderInfo.getOrderNo(),1,"");
        }
        finish();
    }

    @OnClick(R.id.tv_back_hone)
    public void goTohHome(){
//        EventBus.getDefault().post(new MainJumpEvent(0));
        finish();
        AbsSuperApplication.finishAllActivityReserver(MainActivity.class);
    }
    public static void startactivity(Context mContext, OrderInfo orderInfo){
        Intent mIntent = new Intent(mContext,PaySuccessActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,orderInfo);
        mContext.startActivity(mIntent);
    }



}
