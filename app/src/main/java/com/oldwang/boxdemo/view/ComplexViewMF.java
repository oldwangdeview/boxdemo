package com.oldwang.boxdemo.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongwen.marqueen.MarqueeFactory;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.bean.CommodityTeamBuyData;
import com.oldwang.boxdemo.bean.TeamBuyMembers;
import com.oldwang.boxdemo.util.UIUtils;

public class ComplexViewMF extends MarqueeFactory<LinearLayout, CommodityTeamBuyData> {

    private LayoutInflater inflater;

    public ComplexViewMF(Context mContext) {
        super(mContext);
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    protected LinearLayout generateMarqueeItemView(CommodityTeamBuyData data) {

        LinearLayout mView = (LinearLayout) inflater.inflate(R.layout.complex_view, null);

        ImageView iv_head = mView.findViewById(R.id.iv_head);
        TextView tv_content = mView.findViewById(R.id.tv_content);

        if (data != null && data.getTeamBuyMembers() != null && data.getTeamBuyMembers().size() > 0) {

            TeamBuyMembers teamBuyMembers = null;
            for (TeamBuyMembers buyMembers : data.getTeamBuyMembers()) {
                if (!TextUtils.isEmpty(buyMembers.getIsHead()) && buyMembers.getIsHead().equals("1"))
                    teamBuyMembers = buyMembers;
            }
            if (teamBuyMembers != null){
                String head = teamBuyMembers.getMemberHeadurl();
                String teamBuyMember = teamBuyMembers.getTeamBuyMember();

                String peopleNum = data.getPeopleNum();
                int count = 1;
                if (!TextUtils.isEmpty(peopleNum)) {
                    count = Integer.valueOf(peopleNum) - data.getTeamBuyMembers().size();
                }
                String content = teamBuyMember + "的团只差" + count + "人";

                if (!TextUtils.isEmpty(head)) {
                    UIUtils.loadImageView(mContext, head, iv_head);
                }
                tv_content.setText(content);
            }




        }

        return mView;
    }
}
