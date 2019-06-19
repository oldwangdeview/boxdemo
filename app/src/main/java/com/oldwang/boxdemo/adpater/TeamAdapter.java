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
import com.oldwang.boxdemo.bean.TeamData;
import com.oldwang.boxdemo.util.DigitUtil;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<TeamData> list;
    private Map<String, Integer> alphaIndexer;
    private String[] sections;
    private Context context;

    public TeamAdapter(Context context, List<TeamData> list) {

        //赋值初始化
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        alphaIndexer = new HashMap<String, Integer>();
        sections = new String[list.size()];

        //把相邻的相同的首字母放到一起,同时首个字母显示
        for (int i = 0; i < list.size(); i++) {
            String currentStr = DigitUtil.getPinYinFirst(list.get(i).getTeamMemberName());
            String previewStr = (i - 1) >= 0 ? DigitUtil.getPinYinFirst(list.get(i - 1).getTeamMemberName())
                    : " ";
            if (!previewStr.equals(currentStr)) {//前一个首字母与当前首字母不同时加入HashMap中同时显示该字母
                String name = DigitUtil.getPinYinFirst(list.get(i).getTeamMemberName());
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
            convertView = inflater.inflate(R.layout.item_team, null);
            holder = new ViewHolder();
            holder.alpha = (TextView) convertView
                    .findViewById(R.id.item_city_alpha);
            holder.name = (TextView) convertView
                    .findViewById(R.id.item_city_name);
            holder.rl_item = (RelativeLayout) convertView
                    .findViewById(R.id.rl_item);

            holder.iv_head = (ImageView) convertView
                    .findViewById(R.id.iv_head);
            holder.item_phone = (TextView) convertView
                    .findViewById(R.id.item_phone);
            holder.tv_count = (TextView) convertView
                    .findViewById(R.id.tv_count);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TeamData teamData = list.get(position);

        holder.name.setText(teamData.getTeamMemberName());
        String currentStr = DigitUtil.getPinYinFirst(teamData.getTeamMemberName());
        String previewStr = (position - 1) >= 0 ? DigitUtil.getPinYinFirst(list.get(position - 1)
                .getTeamMemberName()) : " ";

        if (!TextUtils.isEmpty(teamData.getTeamMemberLogo())) {
            UIUtils.loadImageView(context, teamData.getTeamMemberLogo(), holder.iv_head);
        }
        if (!TextUtils.isEmpty(teamData.getTeamMemberPhone())) {
            holder.item_phone.setText(teamData.getTeamMemberPhone());
        }
        if (!TextUtils.isEmpty(teamData.getTeamMemberNum())) {
            holder.tv_count.setText(teamData.getTeamMemberNum());
        }

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
        ImageView iv_head;
        TextView item_phone;
        TextView tv_count;


    }
}