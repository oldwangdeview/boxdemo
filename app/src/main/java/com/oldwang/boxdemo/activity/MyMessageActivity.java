package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.MyMessagePagerAdpater;
import com.oldwang.boxdemo.adpater.MycollectionPagerAdpater;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.event.UpdateCollectionEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class MyMessageActivity extends BaseActivity {
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
    @BindView(R.id.mviewpager)
    ViewPager mviewpager;

    private MyMessagePagerAdpater madpater;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_mymessage);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("我的消息");
        madpater = new MyMessagePagerAdpater(getSupportFragmentManager());
        mviewpager.setOffscreenPageLimit(2);
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

    @OnClick({R.id.layout_1,R.id.layout_2})
    public void choicetitle(View v){
        switch (v.getId()){
            case R.id.layout_1:
                mviewpager.setCurrentItem(0);
                break;
            case R.id.layout_2:
                mviewpager.setCurrentItem(1);
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
                break;
            case 1:
                text2.setTextColor(getResources().getColor(R.color.c_d52e21));
                view2.setVisibility(View.VISIBLE);
                text1.setTextColor(getResources().getColor(R.color.c_525259));
                view1.setVisibility(View.INVISIBLE);
                break;
        }
    }



    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,MyMessageActivity.class);
        mContext.startActivity(mIntent);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }
}
