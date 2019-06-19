package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.bean.BankData;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

public class BankCardAdpater extends BaseAdapter {

    Context mContext;
    List<BankData> listdata;

    private ListOnclickLister mlister;

    public BankCardAdpater(Context mContext, List<BankData> listdata) {
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
        BankData bankData = listdata.get(position);


        if (convertView == null) {
            convertView = UIUtils.inflate(mContext, R.layout.item_bankcard);
        }
        ImageView iv_bank_logo = convertView.findViewById(R.id.iv_bank_logo);
        TextView tv_edit = convertView.findViewById(R.id.tv_edit);
        TextView tv_bank_name = convertView.findViewById(R.id.tv_bank_name);
        TextView tv_bank_number = convertView.findViewById(R.id.tv_bank_number);

        TextView tv_bank_type = convertView.findViewById(R.id.tv_bank_type);
        TextView tv_add_time = convertView.findViewById(R.id.tv_add_time);
        ImageView iv_delete = convertView.findViewById(R.id.iv_delete);

        if (bankData != null) {
            if (!TextUtils.isEmpty(bankData.getBankName())) {
                tv_bank_name.setText(bankData.getBankName());
            }
            if (!TextUtils.isEmpty(bankData.getBankcardNo())) {
                tv_bank_number.setText(bankData.getBankcardNo());
            }
            if (!TextUtils.isEmpty(bankData.getCreateTime())) {
                tv_add_time.setText(DateTools.getFormat(Long.parseLong(bankData.getCreateTime())));
            }

        }
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mlister != null) {
                    mlister.onclick(view, position);
                }
            }
        });


        return convertView;
    }

    public void SetListonclicklister(ListOnclickLister mlister) {
        this.mlister = mlister;
    }

}
