package com.oldwang.boxdemo.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainCouPonDialog extends Dialog {

    /**
     * 绑定Dialog标签
     */
    private Object tag;

    public interface OnRightClickListener {
        void onRightClick(Object args);
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * 设置弹框默认风格
     */
    private MainCouPonDialog(@NonNull Context context) {
        super(context, R.style.tipsDialogStyle);
    }

    public MainCouPonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    /**
     * 创建Dialog
     *
     * @author fengzhen
     * @version v1.0, 2018/3/29
     */
    public static class Builder {

        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;
        @BindView(R.id.tv_coupon_size)
        TextView tv_coupon_size;
        @BindView(R.id.btn_returnmain)
        Button btn_returnmain;
        @BindView(R.id.colose_button)
        ImageView colose_button;

        private MainCouPonDialog dialog;
        private View layout;
        private Context mContext;


        private BaseRecycleAdapter adpater;
        private View.OnClickListener colcosebutton;
        private int couponsize = 0;


        public Builder(@NonNull Context context) {
            this.mContext = context;
            dialog = new MainCouPonDialog(context);
            dialog.setTag(this);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_maincoupon, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ButterKnife.bind(this, layout);
        }


        public MainCouPonDialog build() {

            recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerview.setItemAnimator(new DefaultItemAnimator());
            if(adpater!=null) {
                recyclerview.setAdapter(adpater);
            }

            tv_coupon_size.setText("获得"+couponsize+"张优惠券");


            btn_returnmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            colose_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.setContentView(layout);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }



        public Builder setBaseRecycleAdpater(BaseRecycleAdapter baseadpater){
            this.adpater = baseadpater;
            return this;
        }

        public Builder setCouponSize(int size){
            this.couponsize = size;
            return this;
        }



    }
}
