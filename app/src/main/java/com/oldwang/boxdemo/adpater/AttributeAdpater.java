package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.GoodsDetailActivity;
import com.oldwang.boxdemo.bean.Attribute;
import com.oldwang.boxdemo.event.AttributeEvent;
import com.oldwang.boxdemo.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttributeAdpater extends BaseAdapter {

    Context mContext;
    List<Attribute> listdata = new ArrayList<>();
    HashMap<Integer,Boolean> clickdata = new HashMap<>();

    private ImageView dialogImage;

    int tag;
    public AttributeAdpater(Context mContext, List<Attribute> listdata, int tag){
        this.mContext = mContext;
        this.listdata = listdata;
        this.tag = tag;
    }
    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position,  View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = UIUtils.inflate(mContext, R.layout.item_goodstype);
        }


        final Attribute attribute = listdata.get(position);
        final TextView text = convertView.findViewById(R.id.text);




        switch (tag){
            case 0:
                if (!TextUtils.isEmpty(attribute.getAttributeColor())){
                    text.setText(attribute.getAttributeColor());
                }
                break;
            case 1:
                if (!TextUtils.isEmpty(attribute.getAttributeSize())){
                    text.setText(attribute.getAttributeSize());
                }
                break;
            case 2:
                if (!TextUtils.isEmpty(attribute.getAttributeQuality())){
                    text.setText(attribute.getAttributeQuality());
                }
                break;

        }


        if(clickdata.get(position)){
            text.setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
            text.setBackgroundResource(R.drawable.goodsdetail_type_choicexz);
        }else{
            text.setTextColor(mContext.getResources().getColor(R.color.c_262626));
            text.setBackgroundResource(R.drawable.goodsdetail_type_choice);
        }

        if (attribute.isChoos()){
            text.setTextColor(mContext.getResources().getColor(R.color.c_262626));
        }else {
            text.setBackgroundResource(R.drawable.goodsdetail_type_choice);
            text.setTextColor(mContext.getResources().getColor(R.color.c_ffffff));
        }


        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attribute.isChoos()) {
//                    String picUrl = attribute.getPicUrl();
//                    if (dialogImage == null){
//                        dialogImage  = ((GoodsDetailActivity)mContext).getDialogImageView();
//                    }
//                    if (!TextUtils.isEmpty(picUrl)){
//                        UIUtils.loadImageView(mContext,picUrl,dialogImage);
//                    }
                    setadlldataeType(false,-1);
                    clickdata.put(position, true);
                    EventBus.getDefault().post(new AttributeEvent(tag,attribute));

                    notifyDataSetChanged();
                }

            }
        });

        return convertView;
    }

    public void setadlldataeType(boolean type,int pos){
        for(int i = 0;i<listdata.size();i++){
            if (pos == i){
                clickdata.put(i,true);
            }else {
                clickdata.put(i,type);
            }
        }
        notifyDataSetChanged();
    }


    /**
     * 获取选择数据
     * @return
     */
    public List<Attribute> getChoiceData(){
        List<Attribute> data = new ArrayList<>();
        for(int i = 0;i<listdata.size();i++){
            if(clickdata.get(i)){
                data.add(listdata.get(i));
            }
        }
        return  data;
    }
}
