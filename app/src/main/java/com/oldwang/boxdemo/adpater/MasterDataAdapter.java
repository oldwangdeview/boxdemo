package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.bean.MasterData;
import com.oldwang.boxdemo.bean.TeamData;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.DigitUtil;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterDataAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<MasterData> list;
    private Map<String, Integer> alphaIndexer;
    private String[] sections;
    private Context context;


    public MasterDataAdapter(Context context, List<MasterData> list) {

        //赋值初始化
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        alphaIndexer = new HashMap<String, Integer>();
        sections = new String[list.size()];

        //把相邻的相同的首字母放到一起,同时首个字母显示
        for (int i = 0; i < list.size(); i++) {
            String currentStr = DigitUtil.getPinYinFirst(list.get(i).getNewsMasterName());
            String previewStr = (i - 1) >= 0 ? DigitUtil.getPinYinFirst(list.get(i - 1).getNewsMasterName())
                    : " ";
            if (!previewStr.equals(currentStr)) {//前一个首字母与当前首字母不同时加入HashMap中同时显示该字母
                String name = DigitUtil.getPinYinFirst(list.get(i).getNewsMasterName());
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_master, null);
            holder = new ViewHolder();
            holder.alpha = (TextView) convertView
                    .findViewById(R.id.item_city_alpha);
            holder.name = (TextView) convertView
                    .findViewById(R.id.item_city_name);
            holder.rl_item = (RelativeLayout) convertView
                    .findViewById(R.id.rl_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MasterData masterData = list.get(position);

        holder.name.setText(masterData.getNewsMasterName());
        String currentStr = DigitUtil.getPinYinFirst(masterData.getNewsMasterName());
        String previewStr = (position - 1) >= 0 ? DigitUtil.getPinYinFirst(list.get(position - 1)
                .getNewsMasterName()) : " ";

        if (!previewStr.equals(currentStr)) {
            holder.alpha.setVisibility(View.VISIBLE);
            holder.alpha.setText(currentStr);
        } else {
            holder.alpha.setVisibility(View.GONE);
        }



        return convertView;
    }

    private class ViewHolder {
        RelativeLayout rl_item;
        TextView alpha;
        TextView name;


    }

}