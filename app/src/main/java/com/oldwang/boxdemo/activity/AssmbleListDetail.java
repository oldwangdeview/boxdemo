package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.discussionavatarview.DiscussionAvatarView;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.CommodityTeamBuyData;
import com.oldwang.boxdemo.bean.TeamBuyMembers;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.rxjava.Api;
import com.oldwang.boxdemo.util.TimeTools;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AssmbleListDetail extends BaseActivity {


    private CommodityTeamBuyData commodityTeamBuyData;

    @BindView(R.id.tv_goods_name)
    TextView tv_goods_name;

    @BindView(R.id.iv_image)
    ImageView iv_image;

    @BindView(R.id.tv_sale_count)
    TextView tv_sale_count;


    @BindView(R.id.tv_score)
    TextView tv_score;


    @BindView(R.id.tv_now_pirce)
    TextView tv_now_pirce;


    @BindView(R.id.tv_price)
    TextView tv_price;

    @BindView(R.id.tv_pintuan_count)
    TextView tv_pintuan_count;

    @BindView(R.id.daview)
    public DiscussionAvatarView daview;


    @BindView(R.id.tv_time)
    TextView tv_time;
    private CountDownTimer countDownTimer;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_assembleadetail);
    }


    @Override
    protected void initData() {
        super.initData();
        commodityTeamBuyData = (CommodityTeamBuyData) getIntent().getSerializableExtra(Contans.INTENT_DATA);

        if (commodityTeamBuyData != null) {
            if (!TextUtils.isEmpty(commodityTeamBuyData.getCommodityName())) {
                tv_goods_name.setText(commodityTeamBuyData.getCommodityName());
            }

            if(!TextUtils.isEmpty(commodityTeamBuyData.getTeamBuyPrice())){
                tv_now_pirce.setText("¥ "+commodityTeamBuyData.getTeamBuyPrice());
            }
            if(!TextUtils.isEmpty(commodityTeamBuyData.getSalePrice())){
                tv_price.setText("¥ "+commodityTeamBuyData.getSalePrice());
            }
            String commodityUrl = commodityTeamBuyData.getCommodityImgsDetail();
            if (!TextUtils.isEmpty(commodityUrl)){
                UIUtils.loadImageView(mContext,commodityUrl, iv_image);
            }

            if(!TextUtils.isEmpty(commodityTeamBuyData.getSellCount())){
                tv_sale_count.setText("已售： "+commodityTeamBuyData.getSellCount());
            }
            if(!TextUtils.isEmpty(commodityTeamBuyData.getScore())){
                tv_score.setText("评分： "+commodityTeamBuyData.getScore());
            }

            String peopleNum = commodityTeamBuyData.getPeopleNum();
            int count = 1;
            if (!TextUtils.isEmpty(peopleNum)) {
                count = Integer.valueOf(peopleNum) - commodityTeamBuyData.getTeamBuyMembers().size();
            }
            String content = "还差" + count + "人拼团成功";

            tv_pintuan_count.setText(content);

            long time = Long.parseLong(commodityTeamBuyData.getEndTime());

            time = time - System.currentTimeMillis();
            if (time > 0) {
                int finalCount = count;
                countDownTimer = new CountDownTimer(time, 1000) {
                    public void onTick(long millisUntilFinished) {

                        String str="仅剩<font color='#FF0000'>"+ finalCount +"</font>个名额,"+TimeTools.getCountTimeByLong(millisUntilFinished)+"后结束";
                        tv_time.setText(Html.fromHtml(str));
//                        tv_time.setText(TimeTools.getCountTimeByLong(millisUntilFinished));
                    }
                    public void onFinish() {
                        String str="仅剩<font color='#FF0000'>"+ finalCount +"</font>个名额,已结束";
                        tv_time.setText(Html.fromHtml(str));
                    }
                }.start();

            } else {
                String str="仅剩<font color='#FF0000'>"+count+"</font>个名额,已结束";
                tv_time.setText(Html.fromHtml(str));
            }

            ArrayList<String> imagetest = new ArrayList<>();

            List<TeamBuyMembers> teamBuyMembers = commodityTeamBuyData.getTeamBuyMembers();

            for (int i = 0; i < teamBuyMembers.size(); i++) {
                TeamBuyMembers teamBuyMember = teamBuyMembers.get(i);
                if(!TextUtils.isEmpty(teamBuyMember.getMemberHeadurl())) {
                    String imagUrl = teamBuyMember.getMemberHeadurl();
                    if (!imagUrl.startsWith("http")) {
                        imagUrl = Api.imageUrl + imagUrl;
                    }
                    imagetest.add(imagUrl);
                }else{
                    imagetest.add("http://39.104.188.55:8080/jpgs/img/mrtx.png");
                }
                //最多8个
                if(imagetest.size() > 7){
                    break;
                }
            }

            daview.initDatas(imagetest);

        }

    }

    @OnClick(R.id.finish_image)
    public void overnowactivity() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @OnClick(R.id.iv_share)
    public void share() {
        List<String> commodityImgList = commodityTeamBuyData.getCommodityImgList();

        String imageUrl = "";
        if (commodityImgList != null && commodityImgList.size() > 0){
            imageUrl = commodityImgList.get(0);
        }
        share(2,commodityTeamBuyData.getCommodityName(),commodityTeamBuyData.getCommodityId(),imageUrl);

    }

    @OnClick(R.id.btn_join)
    public void joinTeam() {
        GoodsDetailActivity.startactivity(mContext, true, commodityTeamBuyData.getCommodityId(), commodityTeamBuyData);
        finish();
    }


    public static void startactivity(Context mCOntext, CommodityTeamBuyData commodityTeamBuyData) {
        Intent mIntent = new Intent(mCOntext, AssmbleListDetail.class);
        mIntent.putExtra(Contans.INTENT_DATA, commodityTeamBuyData);
        mCOntext.startActivity(mIntent);
    }
}
