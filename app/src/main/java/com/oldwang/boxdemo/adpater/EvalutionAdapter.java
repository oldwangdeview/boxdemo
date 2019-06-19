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
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.bean.EvaluationData;
import com.oldwang.boxdemo.bean.MasterData;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.DigitUtil;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvalutionAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<EvaluationData> list;
    private Map<String, Integer> alphaIndexer;
    private String[] sections;
    private Context context;
    private ListOnclickLister mlister;

    public EvalutionAdapter(Context context, List<EvaluationData> list) {

        //赋值初始化
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        alphaIndexer = new HashMap<String, Integer>();
        sections = new String[list.size()];

        //把相邻的相同的首字母放到一起,同时首个字母显示
        for (int i = 0; i < list.size(); i++) {
            String currentStr = DigitUtil.getPinYinFirst(list.get(i).getEvaluationDetailsName());
            String previewStr = (i - 1) >= 0 ? DigitUtil.getPinYinFirst(list.get(i - 1).getEvaluationDetailsName())
                    : " ";
            if (!previewStr.equals(currentStr)) {//前一个首字母与当前首字母不同时加入HashMap中同时显示该字母
                String name = DigitUtil.getPinYinFirst(list.get(i).getEvaluationDetailsName());
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
            convertView = inflater.inflate(R.layout.item_evlalution, null);
            holder = new ViewHolder();
            holder.alpha = (TextView) convertView
                    .findViewById(R.id.item_city_alpha);
            holder.name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            holder.rl_item = (RelativeLayout) convertView
                    .findViewById(R.id.rl_item);

            holder.iv_image = (ImageView) convertView
                    .findViewById(R.id.iv_image);

            holder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);

            holder.tv_score = (TextView) convertView
                    .findViewById(R.id.tv_score);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        EvaluationData evaluationData = list.get(position);

        holder.name.setText(evaluationData.getEvaluationDetailsName());
        String currentStr = DigitUtil.getPinYinFirst(evaluationData.getEvaluationDetailsName());
        String previewStr = (position - 1) >= 0 ? DigitUtil.getPinYinFirst(list.get(position - 1)
                .getEvaluationDetailsName()) : " ";

        if (!previewStr.equals(currentStr)) {
            holder.alpha.setVisibility(View.VISIBLE);
            holder.alpha.setText(currentStr);
        } else {
            holder.alpha.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(evaluationData.getCommodityPicUrl())) {
            UIUtils.loadImageView(AbsSuperApplication.getContext(), evaluationData.getCommodityPicUrl(), holder.iv_image);
        }

        if (!TextUtils.isEmpty(evaluationData.getEvaluationTime())) {
            holder.tv_time.setText(DateTools.getFormat(Long.parseLong(evaluationData.getEvaluationTime())));
        }

        if (!TextUtils.isEmpty(evaluationData.getEvaluationDetailsScore())) {
            holder.tv_score.setText(evaluationData.getEvaluationDetailsScore() + "分");
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });


        return convertView;
    }

    private class ViewHolder {
        RelativeLayout rl_item;
        TextView alpha;
        TextView name;
        ImageView iv_image;
        TextView tv_time;
        TextView tv_score;


    }


    public void setLisclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}