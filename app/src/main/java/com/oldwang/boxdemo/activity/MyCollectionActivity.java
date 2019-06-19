package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.MycollectionPagerAdpater;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.event.CollectCheckEvent;
import com.oldwang.boxdemo.event.UpdateCollectionEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的收藏
 */
public class MyCollectionActivity extends BaseActivity {
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
    @BindView(R.id.choice_image)
    ImageView choice_image;
    private MycollectionPagerAdpater madpater;
    private int nowposition = 0;

    private boolean oneCheck;
    private boolean twoCheck;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_mycollection);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("我的收藏");
        madpater = new MycollectionPagerAdpater(getSupportFragmentManager());
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
                nowposition = position;
                if (position == 0){
                    if(oneCheck){
                        choice_image.setImageResource(R.mipmap.addresslist_choice);
                    }else{
                        choice_image.setImageResource(R.mipmap.addresslist_unchoice);
                    }
                }else {
                    if(twoCheck){
                        choice_image.setImageResource(R.mipmap.addresslist_choice);
                    }else{
                        choice_image.setImageResource(R.mipmap.addresslist_unchoice);
                    }
                }
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

    @OnClick(R.id.choice_adddata)
    public void choice_alldata(){


        if (nowposition == 0){
            oneCheck = !oneCheck;
            EventBus.getDefault().post(new UpdateCollectionEvent(nowposition,oneCheck?1:0));
            if(oneCheck){
                choice_image.setImageResource(R.mipmap.addresslist_choice);
            }else{
                choice_image.setImageResource(R.mipmap.addresslist_unchoice);
            }
        }else{
            twoCheck = !twoCheck;
            EventBus.getDefault().post(new UpdateCollectionEvent(nowposition,twoCheck?1:0));
            if(twoCheck){
                choice_image.setImageResource(R.mipmap.addresslist_choice);
            }else{
                choice_image.setImageResource(R.mipmap.addresslist_unchoice);
            }
        }


    }

    @OnClick(R.id.delete_btn)
    public void deletealldata(){
        EventBus.getDefault().post(new UpdateCollectionEvent(nowposition,2));
    }


    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,MyCollectionActivity.class);
        mContext.startActivity(mIntent);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateListData(CollectCheckEvent event){

        if (event.getPostion() == 0){
            oneCheck = event.isCheckAll();
            if(oneCheck){
                choice_image.setImageResource(R.mipmap.addresslist_choice);
            }else{
                choice_image.setImageResource(R.mipmap.addresslist_unchoice);
            }
        }else {
            twoCheck = event.isCheckAll();

            if(twoCheck){
                choice_image.setImageResource(R.mipmap.addresslist_choice);
            }else{
                choice_image.setImageResource(R.mipmap.addresslist_unchoice);
            }
        }

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
