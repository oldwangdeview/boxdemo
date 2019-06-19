package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.ImagePagerAdpater;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.contans.Contans;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 图文查看页面
 */
public class ImageTextLookActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.mviewpager)
    ViewPager mviewpager;

    private ImagePagerAdpater madpater;

    private List<String> listdata1;
    private List<String> listdataText1;

    private int positon;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_image_text_look);
    }

    @Override
    protected void initData() {
        super.initData();

        String stringExtra = getIntent().getStringExtra(Contans.INTENT_DATA);
        String stringExtra2 = getIntent().getStringExtra(Contans.INTENT_TYPE);
        positon = getIntent().getIntExtra(Contans.INTENT_TYPE_TWO,0);

        listdata1 = new Gson().fromJson(stringExtra,new TypeToken<List<String >>() {}.getType());
        listdataText1 = new Gson().fromJson(stringExtra2,new TypeToken<List<String >>() {}.getType());

        titlename.setText(positon+1+"/"+listdata1.size());


        madpater = new ImagePagerAdpater(getSupportFragmentManager(),listdata1,listdataText1);
        mviewpager.setOffscreenPageLimit(3);
        mviewpager.setAdapter(madpater);
        mviewpager.setCurrentItem(positon);
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
                titlename.setText(position+1+"/"+listdata1.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    public static void startactivity(Context mContext, String listdata1, String listdataText1, int position){
        Intent mIntent = new Intent(mContext,ImageTextLookActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,listdata1);
        mIntent.putExtra(Contans.INTENT_TYPE,listdataText1);
        mIntent.putExtra(Contans.INTENT_TYPE_TWO,position);
        mContext.startActivity(mIntent);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }
}
