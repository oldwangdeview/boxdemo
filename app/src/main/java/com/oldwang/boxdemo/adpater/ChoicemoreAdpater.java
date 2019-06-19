package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.bean.AttributeColorBean;
import com.oldwang.boxdemo.bean.AttributeQualityBean;
import com.oldwang.boxdemo.bean.AttributeSizeBean;
import com.oldwang.boxdemo.bean.BrandData;
import com.oldwang.boxdemo.bean.RegionData;
import com.oldwang.boxdemo.bean.TypeData;
import com.oldwang.boxdemo.interfice.ChoicePathReturnLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChoicemoreAdpater<T> extends BaseAdapter {

    Context mContext;
    List<T> data = new ArrayList<>();
    //是否支持多选
    private boolean morechoice = false;
    HashMap<Integer,Boolean> clickdata = new HashMap<>();
    ChoicePathReturnLister<T> lister;
    int titleposition = 0;
    ChoicemoreAdpater madpater = this;
    public ChoicemoreAdpater(Context mContext, List<T> data){
     this.mContext = mContext;
     this.data = data;
    }

    @Override
    public int getCount() {

        return data.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = UIUtils.inflate(mContext, R.layout.item_choremore);
        }
        TextView text_content = convertView.findViewById(R.id.text_content);
        RelativeLayout m_layout = convertView.findViewById(R.id.m_layout);
        ImageView view = convertView.findViewById(R.id.view);


        Object mobjdata = data.get(position);
        if(mobjdata instanceof String){
            text_content.setText((String)mobjdata);
        }
        if(mobjdata instanceof TypeData){
            text_content.setText(((TypeData)mobjdata).getTypeName());
        }

        if(mobjdata instanceof RegionData){
            text_content.setText(((RegionData)mobjdata).getFullname());
        }

        if(mobjdata instanceof BrandData){
            text_content.setText(((BrandData)mobjdata).getBrandName());
        }

        if(mobjdata instanceof AttributeColorBean){
            text_content.setText(((AttributeColorBean)mobjdata).getColorName());
        }

        if(mobjdata instanceof AttributeQualityBean){
            text_content.setText(((AttributeQualityBean)mobjdata).getQualityName());
        }

        if(mobjdata instanceof AttributeSizeBean){
            text_content.setText(((AttributeSizeBean)mobjdata).getSizeName());
        }
        if(clickdata.get(position)==null){
            sealldataType(false);
        }else{
            if(clickdata.get(position)){
                m_layout.setBackgroundResource(R.drawable.choice_more_ischoiceback);
                view.setVisibility(View.VISIBLE);
                text_content.setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
            }else{
                m_layout.setBackgroundResource(R.drawable.choice_more_back);
                view.setVisibility(View.INVISIBLE);
                text_content.setTextColor(mContext.getResources().getColor(R.color.c_8C8C8C));
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!morechoice){
                    sealldataType(false);
                    clickdata.put(position,true);

                }else{
                    clickdata.put(position,!clickdata.get(position));
                }

                if(lister!=null){
                    lister.getChoicedata(data.get(position),titleposition,madpater);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }




    public void sealldataType(boolean type){
        if(data.size()>0){
            for(int i =0;i<data.size();i++){
                clickdata.put(i,type);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 设置是否自持多选
     * @param morechoice
     */
    public void setclickmoreddata(boolean morechoice){
        this.morechoice = morechoice;
        sealldataType(false);
        notifyDataSetChanged();
    }

    /**
     * 获取选中数据
     * @return
     */
    public List<T> getChoiceData(){
        List<T> datalsit = new ArrayList<>();
        for(int i =0;i<data.size();i++){
            if(clickdata.get(i)){
                datalsit.add(data.get(i));
            }
        }
        return  datalsit;
    }




    public void setClickdataLister(ChoicePathReturnLister<T> lister){
        this.lister = lister;
    }

    public void setTitleposition(int titleposition){
        this.titleposition = titleposition;
    }



    public void setdata(List<T> listdata){
        data.clear();
        data.addAll(listdata);
        sealldataType(false);
    }
}
