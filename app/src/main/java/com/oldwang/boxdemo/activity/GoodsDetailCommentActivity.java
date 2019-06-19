package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.GoodsCommentpagerAdpater;
import com.oldwang.boxdemo.adpater.MainPagerAdpater;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.CommodityCommentInfo;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.GoodsEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商品评价更多
 */
public class GoodsDetailCommentActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.comment_btn1)
    TextView comment1;
    @BindView(R.id.comment_btn2)
    TextView comment2;
    @BindView(R.id.comment_btn3)
    TextView comment3;
    @BindView(R.id.comment_btn4)
    TextView comment4;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private GoodsCommentpagerAdpater pageradptaer;

    @BindView(R.id.tv_add_cart)
    TextView tv_add_cart;
    @BindView(R.id.tv_buy)
    TextView tv_buy;


    List<TextView> commentbtnlist = new ArrayList<>();

    private String commodityId;

    private boolean isPinTuan = false;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_goodscomment);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("商品评价");
        commentbtnlist.add(comment1);
        commentbtnlist.add(comment2);
        commentbtnlist.add(comment3);
        commentbtnlist.add(comment4);

        commodityId = getIntent().getStringExtra(Contans.INTENT_DATA);
        isPinTuan = getIntent().getBooleanExtra(Contans.INTENT_TYPE,false);
        pageradptaer = new GoodsCommentpagerAdpater(getSupportFragmentManager());
        viewpager.setOffscreenPageLimit(4);
        viewpager.setAdapter(pageradptaer);
        viewpager.setCurrentItem(0);
        if (isPinTuan){
            tv_add_cart.setText("单独购买");
            tv_buy.setText("我要开团");
        }

    }

    @OnClick({R.id.comment_btn1,R.id.comment_btn2,R.id.comment_btn3,R.id.comment_btn4})
    public void choicetitle(View v){
        switch (v.getId()){
            case R.id.comment_btn1:
                viewpager.setCurrentItem(0);
                break;
            case R.id.comment_btn2:
                viewpager.setCurrentItem(1);
                break;
            case R.id.comment_btn3:
                viewpager.setCurrentItem(2);
                break;
            case R.id.comment_btn4:
                viewpager.setCurrentItem(3);
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
                updatecommentbtntype(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updatecommentbtntype(int position){

        for(int i =0;i<commentbtnlist.size();i++){
            commentbtnlist.get(i).setBackgroundResource(R.drawable.goodsdetail_pinliun_unchoice);
            commentbtnlist.get(i).setTextColor(getResources().getColor(R.color.c_383838));
        }

        commentbtnlist.get(position).setBackgroundResource(R.drawable.goodsdetail_pinlun_choice);
        commentbtnlist.get(position).setTextColor(getResources().getColor(R.color.c_ffffff));

    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }

    //1单独购买2我要开团3加入购物车4立即购买
    @OnClick(R.id.tv_add_cart)
    public void addCart() {
        finish();
        //单独购买
        if (isPinTuan){
            EventBus.getDefault().post(new GoodsEvent(1));
        }else {
            EventBus.getDefault().post(new GoodsEvent(3));
        }
    }

    @OnClick(R.id.tv_buy)
    public void buyGoods() {
        finish();
        if (isPinTuan){
            EventBus.getDefault().post(new GoodsEvent(2));
        }else {
            EventBus.getDefault().post(new GoodsEvent(4));
        }
    }


    public static void startactivity(Context mContext,String commodityId,boolean isPinTuan){
        Intent mIntent = new Intent(mContext,GoodsDetailCommentActivity.class);
        mIntent.putExtra(Contans.INTENT_TYPE,isPinTuan);
        mIntent.putExtra(Contans.INTENT_DATA,commodityId);
        mContext.startActivity(mIntent);
    }
    public String getCommodityId(){
        return commodityId;
    }

    public void setCount(CommodityCommentInfo commodityCommentInfo){
        if (commodityCommentInfo != null){

            int allCount = 0;

            String goodCount = commodityCommentInfo.getGoodCount();
            if (!TextUtils.isEmpty(goodCount)){
                comment2.setText("好评("+goodCount+")");
                allCount += Integer.valueOf(goodCount);
            }

            String midCount = commodityCommentInfo.getMidCount();
            if (!TextUtils.isEmpty(midCount)){
                comment3.setText("中评("+midCount+")");
                allCount += Integer.valueOf(midCount);
            }

            String badCount = commodityCommentInfo.getBadCount();
            if (!TextUtils.isEmpty(badCount)){
                comment4.setText("差评("+badCount+")");
                allCount += Integer.valueOf(badCount);
            }

            comment1.setText("全部("+allCount+")");

        }
    }

}
