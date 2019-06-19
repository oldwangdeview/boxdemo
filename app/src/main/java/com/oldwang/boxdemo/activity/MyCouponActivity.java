package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.MyCouponPagerAdpater;
import com.oldwang.boxdemo.adpater.MyMessagePagerAdpater;
import com.oldwang.boxdemo.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MyCouponActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.mviewpager)
    ViewPager mviewpager;

    private MyCouponPagerAdpater madpater;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_mycoupon);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("我的优惠券");
        madpater = new MyCouponPagerAdpater(getSupportFragmentManager());
        mviewpager.setOffscreenPageLimit(3);
        mviewpager.setAdapter(madpater);
        mviewpager.setCurrentItem(0);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                seleteposition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.layout_1,R.id.layout_2,R.id.layout_3})
    public void choicetitle(View v){
        switch (v.getId()){
            case R.id.layout_1:
                mviewpager.setCurrentItem(0);
                break;
            case R.id.layout_2:
                mviewpager.setCurrentItem(1);
                break;
            case R.id.layout_3:
                mviewpager.setCurrentItem(2);
                break;
        }
    }
    private void seleteposition(int position){
        switch (position){
            case 0:
                text1.setTextColor(getResources().getColor(R.color.c_d52e21));
                view1.setVisibility(View.VISIBLE);
                text2.setTextColor(getResources().getColor(R.color.c_525259));
                view2.setVisibility(View.INVISIBLE);
                text3.setTextColor(getResources().getColor(R.color.c_525259));
                view3.setVisibility(View.INVISIBLE);
                break;
            case 1:
                text2.setTextColor(getResources().getColor(R.color.c_d52e21));
                view2.setVisibility(View.VISIBLE);
                text1.setTextColor(getResources().getColor(R.color.c_525259));
                view1.setVisibility(View.INVISIBLE);
                text3.setTextColor(getResources().getColor(R.color.c_525259));
                view3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                text3.setTextColor(getResources().getColor(R.color.c_d52e21));
                view3.setVisibility(View.VISIBLE);
                text2.setTextColor(getResources().getColor(R.color.c_525259));
                view2.setVisibility(View.INVISIBLE);
                text1.setTextColor(getResources().getColor(R.color.c_525259));
                view1.setVisibility(View.INVISIBLE);
                break;
        }
    }



    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,MyCouponActivity.class);
        mContext.startActivity(mIntent);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }
}
