package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.discussionavatarview.DiscussionAvatarView;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.bean.CommodityTeamBuyData;
import com.oldwang.boxdemo.bean.TeamBuyMembers;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.Api;
import com.oldwang.boxdemo.util.TimeTools;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.DrawLineTextView;

import java.util.ArrayList;
import java.util.List;


public  class AssembleTimeAdpater extends RecyclerView.Adapter<AssembleTimeAdpater.ViewHolder> {

    private List<CommodityTeamBuyData> mDatas;
    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownMap;
    private Context mContext;

    public AssembleTimeAdpater(Context context, List<CommodityTeamBuyData> datas) {
        mDatas = datas;
        this.mContext = context;
        countDownMap = new SparseArray<>();
    }
    ListOnclickLister mlister;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assemble, parent, false);
        return new ViewHolder(view);
    }


    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        if (countDownMap == null) {
            return;
        }
        Log.e("TAG",  "size :  " + countDownMap.size());
        for (int i = 0,length = countDownMap.size(); i < length; i++) {
            CountDownTimer cdt = countDownMap.get(countDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }

    public void addItem(CommodityTeamBuyData item){
        mDatas.add(0, item);
        notifyItemInserted(0);
//            notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CommodityTeamBuyData data = mDatas.get(position);
        long time = Long.parseLong(data.getEndTime());
        time = time - System.currentTimeMillis();
        //将前一个缓存清除
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }
        if (time > 0) {
            holder.countDownTimer = new CountDownTimer(time, 1000) {
                public void onTick(long millisUntilFinished) {
                    holder.timeTv.setText(TimeTools.getCountTimeByLong(millisUntilFinished));
                }
                public void onFinish() {
                    holder.timeTv.setText("00:00:00");
                }
            }.start();

            countDownMap.put(holder.timeTv.hashCode(), holder.countDownTimer);
        } else {
            holder.timeTv.setText("00:00:00");
        }



        if(!TextUtils.isEmpty(data.getCommodityName())){
            holder.tv_name.setText(data.getCommodityName());
        }
        if(!TextUtils.isEmpty(data.getTeamBuyPrice())){
            holder.tv_now_pirce.setText("¥ "+data.getTeamBuyPrice());
        }
        if(!TextUtils.isEmpty(data.getSalePrice())){
            holder.tv_price.setText("¥ "+data.getSalePrice());
        }
        String commodityUrl = data.getCommodityImgsDetail();

        if (!TextUtils.isEmpty(commodityUrl)){
            UIUtils.loadImageView(mContext,commodityUrl, holder.iv_goods_image);
        }


        ArrayList<String> imagetest = new ArrayList<>();

        List<TeamBuyMembers> teamBuyMembers = data.getTeamBuyMembers();

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

        holder.daview.initDatas(imagetest);
        if(position==0){
            holder.mview.setVisibility(View.VISIBLE);
        }else{
            holder.mview.setVisibility(View.GONE);
        }

        holder.text_addassemble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mDatas != null && !mDatas.isEmpty()) {
            return mDatas.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView statusTv;
        public TextView timeTv;
        public CountDownTimer countDownTimer;
        public ImageView iv_goods_image;
        public TextView tv_name;
        public TextView tv_time ;
        public TextView tv_now_pirce;
        public DrawLineTextView tv_price ;
        public TextView text_addassemble;
        public LinearLayout ll_item;
        public View mview;
        public DiscussionAvatarView daview;

        public ViewHolder(View itemView) {
            super(itemView);
            timeTv = (TextView) itemView.findViewById(R.id.tv_time);
            iv_goods_image = (ImageView) itemView.findViewById(R.id.iv_goods_image);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_now_pirce = (TextView) itemView.findViewById(R.id.tv_now_pirce);
            tv_price = (DrawLineTextView) itemView.findViewById(R.id.tv_price);
            text_addassemble = (TextView) itemView.findViewById(R.id.text_addassemble);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
            mview = (View) itemView.findViewById(R.id.view);
            daview = (DiscussionAvatarView) itemView.findViewById(R.id.daview);


        }
    }
    public void setListOnclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

}