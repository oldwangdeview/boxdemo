package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.bean.ScreenChildData;
import com.oldwang.boxdemo.bean.ScreenData;
import com.oldwang.boxdemo.view.AutoMeasureHeightGridView;

import java.util.List;

/**
 */

public class ScreenChildAdapter extends SimpleBaseAdapter<ScreenChildData> {

    private String id = "";
    private boolean isArea;

    public void setId(String id) {
        isArea = true;
        this.id = id;
    }

    public void setArea(boolean area) {
        isArea = area;
    }

    public ScreenChildAdapter(Context context, List<ScreenChildData> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_choremore;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TextView text_content = convertView.findViewById(R.id.text_content);
        RelativeLayout m_layout = convertView.findViewById(R.id.m_layout);
        ImageView view = convertView.findViewById(R.id.view);

        ScreenChildData screenChildData = data.get(position);
        text_content.setText(screenChildData.getName());

        if (isArea){
            if(!TextUtils.isEmpty(id) && id.equals(screenChildData.getId())){
                screenChildData.setCheck(true);
                m_layout.setBackgroundResource(R.drawable.choice_more_ischoiceback);
                view.setVisibility(View.VISIBLE);
                text_content.setTextColor(context.getResources().getColor(R.color.c_d52e21));
            }else{
                screenChildData.setCheck(false);
                m_layout.setBackgroundResource(R.drawable.choice_more_back);
                view.setVisibility(View.GONE);
                text_content.setTextColor(context.getResources().getColor(R.color.c_8C8C8C));
            }
        }else {
            if(screenChildData.isCheck()){
                m_layout.setBackgroundResource(R.drawable.choice_more_ischoiceback);
                view.setVisibility(View.VISIBLE);
                text_content.setTextColor(context.getResources().getColor(R.color.c_d52e21));
            }else{
                m_layout.setBackgroundResource(R.drawable.choice_more_back);
                view.setVisibility(View.GONE);
                text_content.setTextColor(context.getResources().getColor(R.color.c_8C8C8C));
            }
        }



        return convertView;
    }


}
