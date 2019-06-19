package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SizeChoiceAdpater extends BaseAdapter {

    Context mContext;
    List<String> listdata = new ArrayList<>();
    HashMap<Integer,Boolean> clickdata = new HashMap<>();
    public SizeChoiceAdpater(Context mContext, List<String> listdata){
        this.mContext = mContext;
        this.listdata = listdata;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = UIUtils.inflate(mContext, R.layout.item_goodstype);
        }

        TextView text = convertView.findViewById(R.id.text);
        if(clickdata.get(position)){
            text.setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
            text.setBackgroundResource(R.drawable.goodsdetail_type_choicexz);
        }else{
            text.setTextColor(mContext.getResources().getColor(R.color.c_262626));
            text.setBackgroundResource(R.drawable.goodsdetail_type_choice);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setadlldataeType(false);
                clickdata.put(position,true);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void setadlldataeType(boolean type){
        for(int i = 0;i<listdata.size();i++){
            clickdata.put(i,type);
        }
        notifyDataSetChanged();
    }

    /**
     * 获取选择数据
     * @return
     */
    public List<String> getChoiceData(){
        List<String> data = new ArrayList<>();
        for(int i = 0;i<listdata.size();i++){
            if(clickdata.get(i)){
                data.add(listdata.get(i));
            }
        }
        return  data;
    }
}
