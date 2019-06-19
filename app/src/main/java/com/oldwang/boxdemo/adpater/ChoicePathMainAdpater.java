package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.bean.MainPath_ListBean;

import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.DigitUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChoicePathMainAdpater extends BaseAdapter {
    private LayoutInflater inflater;
    private List<MainPath_ListBean> list;
    private Map<String, Integer> alphaIndexer;
    private String[] sections;
    private Context context;
    private ListOnclickLister listOnclickLister;

    public ChoicePathMainAdpater(Context context, List<MainPath_ListBean> list) {

        //赋值初始化
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        alphaIndexer = new HashMap<String, Integer>();
        sections = new String[list.size()];

        //把相邻的相同的首字母放到一起,同时首个字母显示
        for (int i = 0; i < list.size(); i++) {
            String currentStr = DigitUtil.getPinYinFirst(list.get(i).fullname);
            String previewStr = (i - 1) >= 0 ? DigitUtil.getPinYinFirst(list.get(i - 1).fullname)
                    : " ";
            if (!previewStr.equals(currentStr)) {//前一个首字母与当前首字母不同时加入HashMap中同时显示该字母
                String name = DigitUtil.getPinYinFirst(list.get(i).fullname);
                alphaIndexer.put(name, i);
                sections[i] = name;
            }
        }

    }

    public Map<String, Integer> getCityMap() {
        return alphaIndexer;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ChoicePathMainAdpater.ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_master, null);
            holder = new ChoicePathMainAdpater.ViewHolder();
            holder.alpha = (TextView) convertView
                    .findViewById(R.id.item_city_alpha);
            holder.name = (TextView) convertView
                    .findViewById(R.id.item_city_name);
            holder.iv_choose = convertView.findViewById(R.id.iv_choose);
            holder.rl_item = (RelativeLayout) convertView
                    .findViewById(R.id.rl_item);
            convertView.setTag(holder);
        } else {
            holder = (ChoicePathMainAdpater.ViewHolder) convertView.getTag();
        }
        MainPath_ListBean masterData = list.get(position);

        holder.name.setText(masterData.fullname);
        String currentStr = DigitUtil.getPinYinFirst(masterData.fullname);
        String previewStr = (position - 1) >= 0 ? DigitUtil.getPinYinFirst(list.get(position - 1)
                .fullname) : " ";

        if (masterData.isChoose){
            holder.iv_choose.setVisibility(View.VISIBLE);
        }else {
            holder.iv_choose.setVisibility(View.GONE);
        }

        if (!previewStr.equals(currentStr)) {
            holder.alpha.setVisibility(View.VISIBLE);
            holder.alpha.setText(currentStr);
        } else {
            holder.alpha.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOnclickLister.onclick(view,position);
            }
        });

        return convertView;
    }

    public void setListOnclickLister(ListOnclickLister listOnclickLister) {
        this.listOnclickLister = listOnclickLister;
    }

    private class ViewHolder {
        RelativeLayout rl_item;
        TextView alpha;
        TextView name;
        ImageView iv_choose;


    }
}