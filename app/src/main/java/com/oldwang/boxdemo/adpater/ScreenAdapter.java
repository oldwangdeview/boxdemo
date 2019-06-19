package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.bean.AttrList;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ScreenChildData;
import com.oldwang.boxdemo.bean.ScreenData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.event.UpdateAreaEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ScreenOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.view.AutoMeasureHeightGridView;
import com.oldwang.boxdemo.view.OnClickListenerWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 */

public class ScreenAdapter extends SimpleBaseAdapter<ScreenData> {

    private ScreenOnclickLister screenOnclickLister;

    private String oneId = "";
    private String twoId = "";
    private String threeId = "";
    private String fourId = "";

    //0省 1 市 2 区 3乡镇
    private int type = 0;

    public void setScreenOnclickLister(ScreenOnclickLister screenOnclickLister) {
        this.screenOnclickLister = screenOnclickLister;
    }

    public ScreenAdapter(Context context, List<ScreenData> data) {
        super(context, data);
        oneId = "";
        twoId = "";
        threeId = "";
        fourId = "";
        type = 0;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_screen;
    }


    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {
        LinearLayout ll_top = holder.getView(R.id.ll_top);
        TextView tv_type = holder.getView(R.id.tv_type);


        final TextView tv_one = holder.getView(R.id.tv_one);
        final TextView tv_two = holder.getView(R.id.tv_two);
        final TextView tv_three = holder.getView(R.id.tv_three);
        final TextView tv_four = holder.getView(R.id.tv_four);

        final View line_one = holder.getView(R.id.line_one);
        final View line_two = holder.getView(R.id.line_two);
        final View line_three = holder.getView(R.id.line_three);
        final View line_four = holder.getView(R.id.line_four);


        final ScreenData screenData = data.get(position);

        final List<ScreenChildData> screenChildDataList = screenData.getScreenChildDataList();

        AutoMeasureHeightGridView gridView = holder.getView(R.id.gridview);
        final ScreenChildAdapter adapter = new ScreenChildAdapter(context, screenChildDataList);

        switch (type) {
            case 0:
                adapter.setId(oneId);
                break;
            case 1:
                adapter.setId(twoId);
                break;
            case 2:
                adapter.setId(threeId);
                break;
            case 3:
                adapter.setId(fourId);
                break;
        }
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int m = 0; m < screenChildDataList.size(); m++) {
                    ScreenChildData screenChildData = screenChildDataList.get(m);
                    if (m == i) {
                        screenChildData.setCheck(true);
                    } else {
                        screenChildData.setCheck(false);
                    }
                }
                if (screenData.isArea()) {
                    String id = screenChildDataList.get(i).getId();
                    switch (type) {
                        case 0:
                            oneId = id;
                            twoId = "";
                            threeId = "";
                            fourId = "";
                            break;
                        case 1:
                            twoId = id;
                            threeId = "";
                            fourId = "";
                            break;
                        case 2:
                            threeId = id;
                            fourId = "";
                            break;
                        case 3:
                            fourId = id;
                            break;
                    }
                    adapter.setId(id);
                }
                adapter.setArea(screenData.isArea());
                adapter.notifyDataSetChanged();

                if (screenOnclickLister != null) {
                    screenOnclickLister.onclick(view, position, i);
                }
            }
        });


        tv_type.setText(screenData.getTypeName());
        if (screenData.isArea()) {
            ll_top.setVisibility(View.VISIBLE);
        } else {
            ll_top.setVisibility(View.GONE);
        }

        holder.getView(R.id.rl_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type = 0;
                adapter.setId(oneId);
                EventBus.getDefault().post(new UpdateAreaEvent(type, ""));
                tv_one.setTextColor(context.getResources().getColor(R.color.c_FFD52E21));
                line_one.setVisibility(View.VISIBLE);


                tv_two.setTextColor(context.getResources().getColor(R.color.c_FF595959));
                tv_three.setTextColor(context.getResources().getColor(R.color.c_FF595959));
                tv_four.setTextColor(context.getResources().getColor(R.color.c_FF595959));


                line_two.setVisibility(View.GONE);
                line_three.setVisibility(View.GONE);
                line_four.setVisibility(View.GONE);
            }
        });
        holder.getView(R.id.rl_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(oneId)) {
                    ToastUtils.makeText("请先选择省份");
                    return;
                }
                type = 1;
                adapter.setId(oneId);
                EventBus.getDefault().post(new UpdateAreaEvent(type, oneId));
                tv_two.setTextColor(context.getResources().getColor(R.color.c_FFD52E21));
                line_two.setVisibility(View.VISIBLE);


                tv_one.setTextColor(context.getResources().getColor(R.color.c_FF595959));
                tv_three.setTextColor(context.getResources().getColor(R.color.c_FF595959));
                tv_four.setTextColor(context.getResources().getColor(R.color.c_FF595959));


                line_one.setVisibility(View.GONE);
                line_three.setVisibility(View.GONE);
                line_four.setVisibility(View.GONE);

            }
        });
        holder.getView(R.id.rl_thee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(twoId)) {
                    ToastUtils.makeText("请先选择城市");
                    return;
                }
                type = 2;
                adapter.setId(threeId);

                EventBus.getDefault().post(new UpdateAreaEvent(type, twoId));
                tv_three.setTextColor(context.getResources().getColor(R.color.c_FFD52E21));
                line_three.setVisibility(View.VISIBLE);


                tv_one.setTextColor(context.getResources().getColor(R.color.c_FF595959));
                tv_two.setTextColor(context.getResources().getColor(R.color.c_FF595959));
                tv_four.setTextColor(context.getResources().getColor(R.color.c_FF595959));


                line_one.setVisibility(View.GONE);
                line_two.setVisibility(View.GONE);
                line_four.setVisibility(View.GONE);
            }
        });
        holder.getView(R.id.rl_four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(threeId)) {
                    ToastUtils.makeText("请先选择区县");
                    return;
                }
                type = 3;
                adapter.setId(fourId);

                EventBus.getDefault().post(new UpdateAreaEvent(type, threeId));
                tv_four.setTextColor(context.getResources().getColor(R.color.c_FFD52E21));
                line_four.setVisibility(View.VISIBLE);


                tv_one.setTextColor(context.getResources().getColor(R.color.c_FF595959));
                tv_two.setTextColor(context.getResources().getColor(R.color.c_FF595959));
                tv_three.setTextColor(context.getResources().getColor(R.color.c_FF595959));


                line_one.setVisibility(View.GONE);
                line_two.setVisibility(View.GONE);
                line_three.setVisibility(View.GONE);
            }
        });

        return convertView;
    }


}
