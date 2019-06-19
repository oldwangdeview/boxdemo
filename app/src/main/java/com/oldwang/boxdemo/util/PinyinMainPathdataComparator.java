package com.oldwang.boxdemo.util;

import com.oldwang.boxdemo.bean.MainPath_ListBean;
import java.util.Comparator;

public class PinyinMainPathdataComparator implements Comparator<MainPath_ListBean> {


    @Override
    public int compare(MainPath_ListBean teamData, MainPath_ListBean t1) {

        String str1 = DigitUtil.getPinYinFirst(teamData.fullname);
        String str2 =  DigitUtil.getPinYinFirst(t1.fullname);

        if (str1.equals("#")){
            str1 = "\\";
        }
        if (str2.equals("#")){
            str2 = "\\";
        }
        int flag = str1.compareTo(str2);
        return flag;
    }
}
