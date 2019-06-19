package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class IntegralruleActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_integralrule);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("积分规则");
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,IntegralruleActivity.class);
        mContext.startActivity(mIntent);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }
}
