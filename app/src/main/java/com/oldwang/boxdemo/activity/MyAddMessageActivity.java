package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.MyAddmessagePagerAdpater;
import com.oldwang.boxdemo.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MyAddMessageActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.mviewpager)
    ViewPager viewpager;
    private MyAddmessagePagerAdpater mpageradpater;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_myaddmessage);
    }


    @Override
    protected void initData() {
        super.initData();
        titlename.setText("我的发布");
        mpageradpater = new MyAddmessagePagerAdpater(getSupportFragmentManager());
        viewpager.setOffscreenPageLimit(2);
        viewpager.setAdapter(mpageradpater);
        viewpager.setCurrentItem(0);



    }

    @Override
    protected void initEvent() {
        super.initEvent();
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                seletposition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.layout_1,R.id.layout_2})
    public void titleclick(View v){
        switch (v.getId()){
            case R.id.layout_1:
                viewpager.setCurrentItem(0);
                break;
            case R.id.layout_2:

                viewpager.setCurrentItem(1);
                break;
        }
    }


    private void seletposition(int index){
        switch (index){
            case 0:
                view2.setVisibility(View.INVISIBLE);
                view1.setVisibility(View.VISIBLE);
                text1.setTextColor(getResources().getColor(R.color.c_d52e21));
                text2.setTextColor(getResources().getColor(R.color.c_8C8C8C));
                break;
            case 1:
                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.VISIBLE);
                text1.setTextColor(getResources().getColor(R.color.c_8C8C8C));
                text2.setTextColor(getResources().getColor(R.color.c_d52e21));
                break;
        }
    }

    public static void startactivity(Context mContext){

        Intent mIntent = new Intent(mContext,MyAddMessageActivity.class);
        mContext.startActivity(mIntent);

    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }
}
