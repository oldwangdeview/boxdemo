package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.MyOrderListPagerAdpater;
import com.oldwang.boxdemo.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyOrderListActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;

    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.view1)
    View view1;

    @BindView(R.id.text_share)
    TextView text_share;
    @BindView(R.id.view_share)
    View view_share;

    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.view2)
    View view2;


    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.view3)
    View view3;

    @BindView(R.id.text4)
    TextView text4;
    @BindView(R.id.view4)
    View view4;

    @BindView(R.id.text5)
    TextView text5;
    @BindView(R.id.view5)
    View view5;

    @BindView(R.id.text6)
    TextView text6;
    @BindView(R.id.view6)
    View view6;

    @BindView(R.id.mviewpager)
    ViewPager mviewpager;


    List<TextView> textlist = new ArrayList<>();
    List<View> viewlist = new ArrayList<>();
    private MyOrderListPagerAdpater mpageradpater;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_myorderlist);
    }


    @Override
    protected void initData() {
        super.initData();
        titlename.setText("我的订单");
        textlist.add(text1);
        textlist.add(text_share);
        textlist.add(text2);
        textlist.add(text3);
        textlist.add(text4);
        textlist.add(text5);
        textlist.add(text6);

        viewlist.add(view1);
        viewlist.add(view_share);
        viewlist.add(view2);
        viewlist.add(view3);
        viewlist.add(view4);
        viewlist.add(view5);
        viewlist.add(view6);

        mpageradpater = new MyOrderListPagerAdpater(getSupportFragmentManager());
        mviewpager.setOffscreenPageLimit(7);
        mviewpager.setAdapter(mpageradpater);
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
                seletupater(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.layout_1,R.id.layout_2,R.id.layout_3,R.id.layout_4,R.id.layout_5,R.id.layout_6,R.id.layout_share})
    public void titleclick(View v){
        switch (v.getId()){
            case R.id.layout_1:
                mviewpager.setCurrentItem(0);
                break;
            case R.id.layout_share:
                mviewpager.setCurrentItem(1);
                break;
            case R.id.layout_2:
                mviewpager.setCurrentItem(2);
                break;
            case R.id.layout_3:
                mviewpager.setCurrentItem(3);
                break;
            case R.id.layout_4:
                mviewpager.setCurrentItem(4);
                break;
            case R.id.layout_5:
                mviewpager.setCurrentItem(5);
                break;
            case R.id.layout_6:
                mviewpager.setCurrentItem(6);
                break;
        }

    }


    private void seletupater(int index){
        for(int i =0;i<textlist.size();i++){
            textlist.get(i).setTextColor(getResources().getColor(R.color.c_595959));
            viewlist.get(i).setVisibility(View.INVISIBLE);
        }

        textlist.get(index).setTextColor(getResources().getColor(R.color.c_d52e21));
        viewlist.get(index).setVisibility(View.VISIBLE);
    }


    public static void startactivity(Context mContext, int i){
        Intent mIntent = new Intent(mContext,MyOrderListActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }
}
