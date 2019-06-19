package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.AppointMentPagerAdpater;
import com.oldwang.boxdemo.adpater.HomeFindAdpater;
import com.oldwang.boxdemo.adpater.HosmeSearchPagerAdpater;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.event.HomeSerachEvent;
import com.oldwang.boxdemo.util.KeybordS;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.XCFlowLayout;

import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页搜索
 */
public class HomeFindActivity extends BaseActivity {


    @BindView(R.id.inpout_finddata)
    EditText inpout_finddata;

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
    @BindView(R.id.mviewpager)
    ViewPager mviewpager;

    private HosmeSearchPagerAdpater madpater;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_homefind);
    }

    @Override
    protected void initData() {
        super.initData();
        madpater = new HosmeSearchPagerAdpater(getSupportFragmentManager());
        mviewpager.setOffscreenPageLimit(4);
        mviewpager.setAdapter(madpater);
        mviewpager.setCurrentItem(0);

        inpout_finddata.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String content = inpout_finddata.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        content = "";
                    }
                    KeybordS.closeKeybord(inpout_finddata, mContext);
                    switch (nowPostion) {
                        case 0:
                            HomeSearchResultOneActivity.startactivity(mContext, content);
                            break;
                        case 1:
                            HomeSearchResultTwoActivity.startactivity(mContext, content);
                            break;
                        case 2:
                            HomeSearchResultThreeActivity.startactivity(mContext, content);
                            break;
                        case 3:
                            HomeSearchResultFourActivity.startactivity(mContext, content);
                            break;
                    }
                    return false;
                }
                return false;

            }
        });

    }

    public void setContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            inpout_finddata.setText(content);
            inpout_finddata.setSelection(content.length());
        }
    }


    @OnClick(R.id.quxiao_btn)
    public void overactivity() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);

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

    @OnClick({R.id.layout_1, R.id.layout_2, R.id.layout_3, R.id.layout_4})
    public void choicetitle(View v) {
        switch (v.getId()) {
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

    private int nowPostion;

    private void seleteposition(int position) {
        nowPostion = position;

        switch (position) {
            case 0:
                text1.setTextColor(getResources().getColor(R.color.c_d52e21));
                view1.setVisibility(View.VISIBLE);
                text2.setTextColor(getResources().getColor(R.color.c_525259));
                view2.setVisibility(View.INVISIBLE);
                text3.setTextColor(getResources().getColor(R.color.c_525259));
                view3.setVisibility(View.INVISIBLE);
                text4.setTextColor(getResources().getColor(R.color.c_525259));
                view4.setVisibility(View.INVISIBLE);
                break;
            case 1:
                text2.setTextColor(getResources().getColor(R.color.c_d52e21));
                view2.setVisibility(View.VISIBLE);
                text1.setTextColor(getResources().getColor(R.color.c_525259));
                view1.setVisibility(View.INVISIBLE);
                text3.setTextColor(getResources().getColor(R.color.c_525259));
                view3.setVisibility(View.INVISIBLE);
                text4.setTextColor(getResources().getColor(R.color.c_525259));
                view4.setVisibility(View.INVISIBLE);
                break;
            case 2:
                text3.setTextColor(getResources().getColor(R.color.c_d52e21));
                view3.setVisibility(View.VISIBLE);
                text2.setTextColor(getResources().getColor(R.color.c_525259));
                view2.setVisibility(View.INVISIBLE);
                text1.setTextColor(getResources().getColor(R.color.c_525259));
                view1.setVisibility(View.INVISIBLE);
                text4.setTextColor(getResources().getColor(R.color.c_525259));
                view4.setVisibility(View.INVISIBLE);
                break;
            case 3:
                text4.setTextColor(getResources().getColor(R.color.c_d52e21));
                view4.setVisibility(View.VISIBLE);
                text3.setTextColor(getResources().getColor(R.color.c_525259));
                view3.setVisibility(View.INVISIBLE);
                text2.setTextColor(getResources().getColor(R.color.c_525259));
                view2.setVisibility(View.INVISIBLE);
                text1.setTextColor(getResources().getColor(R.color.c_525259));
                view1.setVisibility(View.INVISIBLE);
                break;
        }
    }


    public static void startactivity(Context mContext) {
        Intent mIntent = new Intent(mContext, HomeFindActivity.class);
        mContext.startActivity(mIntent);
    }


}