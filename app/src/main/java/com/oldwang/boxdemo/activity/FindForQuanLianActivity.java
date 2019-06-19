package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.QuanLianFindDataAdpater;
import com.oldwang.boxdemo.adpater.QuanLianFindLeftAdpater;
import com.oldwang.boxdemo.adpater.QuanLianFindPagerAdpater;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 拳联装备搜索
 */
public class FindForQuanLianActivity extends BaseActivity {

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

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    QuanLianFindPagerAdpater mpageradpater;
    List<TextView> textlist = new ArrayList<>();
    List<View> viewlist = new ArrayList<>();



    @Override
    protected void initView() {
        setContentView(R.layout.activity_findforquanlian);
    }

    @Override
    protected void initData() {
        super.initData();
        mpageradpater = new QuanLianFindPagerAdpater(getSupportFragmentManager());
        viewpager.setOffscreenPageLimit(4);
        viewpager.setAdapter(mpageradpater);
        viewpager.setCurrentItem(0);
    }


    @OnClick({R.id.layout_1,R.id.layout_2,R.id.layout_3})
    public void clicktitle(View v){
        switch (v.getId()){
            case R.id.layout_1:
                viewpager.setCurrentItem(0);
                break;
            case R.id.layout_2:
                viewpager.setCurrentItem(1);
                break;
            case R.id.layout_3:
                viewpager.setCurrentItem(2);
                break;
        }
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

    private void seletposition(int position){
        if(textlist.size()==0){
            textlist.add(text1);
            viewlist.add(view1);
            textlist.add(text2);
            viewlist.add(view2);
            textlist.add(text3);
            viewlist.add(view3);
        }

        for(int i = 0;i<textlist.size();i++){
            textlist.get(i).setTextColor(mContext.getResources().getColor(R.color.c_525259));
            viewlist.get(i).setVisibility(View.INVISIBLE);
        }

        textlist.get(position).setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
        viewlist.get(position).setVisibility(View.VISIBLE);

    }



    @OnClick(R.id.retuen_image)
    public void returnimage(){
        finish();
    }




    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,FindForQuanLianActivity.class);
        mContext.startActivity(mIntent);
    }
}
