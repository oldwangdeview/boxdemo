package com.oldwang.boxdemo.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 加横线的Textview， 常用于优惠时显示原价
 */
public class DrawLineTextView extends TextView {
    public DrawLineTextView(Context context) {
        this(context, null);
    }

    public DrawLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }
}
