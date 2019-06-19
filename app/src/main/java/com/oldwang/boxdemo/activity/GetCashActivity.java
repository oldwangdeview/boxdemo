package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.GetCashPagerAdpater;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.contans.Contans;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GetCashActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.viewpager)
    ViewPager mviewpager;

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
    @BindView(R.id.text4)
    TextView text4;
    @BindView(R.id.view4)
    View view4;
    List<TextView> textlist = new ArrayList<>();
    List<View> viewlist = new ArrayList<>();



    private GetCashPagerAdpater mpageradpater;

    private String canWithdrawalMoney = "";

    //1积分提现 0 提现
    private int type;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_getcash);
    }



    @Override
    protected void initData() {
        super.initData();
        type = getIntent().getIntExtra(Contans.INTENT_TYPE,0);
        canWithdrawalMoney = getIntent().getStringExtra(Contans.INTENT_DATA);

        if (type == 1){
            titlename.setText("积分提现");
        }else {
            titlename.setText("提现申请");
        }

        textlist.add(text1);
        textlist.add(text2);
        textlist.add(text3);
        textlist.add(text4);
        viewlist.add(view1);
        viewlist.add(view2);
        viewlist.add(view3);
        viewlist.add(view4);
        mpageradpater = new GetCashPagerAdpater(getSupportFragmentManager());
        mviewpager.setOffscreenPageLimit(4);
        mviewpager.setAdapter(mpageradpater);
        mviewpager.setCurrentItem(0);
    }


    public void setMyCurrentItem(int postion){
        mviewpager.setCurrentItem(postion);
    }

    @OnClick({R.id.layout_1,R.id.layout_2,R.id.layout_3,R.id.layout_4})
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
            case R.id.layout_4:
                mviewpager.setCurrentItem(3);
                break;
        }
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
                sletedata(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void sletedata(int position){
        for(int i =0;i<textlist.size();i++){
            textlist.get(i).setTextColor(getResources().getColor(R.color.c_525259));
            viewlist.get(i).setVisibility(View.INVISIBLE);
        }
        textlist.get(position).setTextColor(getResources().getColor(R.color.c_d52e21));
        viewlist.get(position).setVisibility(View.VISIBLE);

    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity(){
        finish();
    }

    public static void startactivity(Context mContext,String data,int type){
        Intent mIntent = new Intent(mContext,GetCashActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,data);
        mIntent.putExtra(Contans.INTENT_TYPE,type);

        mContext.startActivity(mIntent);
    }

    public String getCanWithdrawalMoney(){
        return canWithdrawalMoney;
    }

    public int getType(){
        return type;
    }
}
