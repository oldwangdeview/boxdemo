package com.oldwang.boxdemo.util;

import com.oldwang.boxdemo.bean.BrandData;
import com.oldwang.boxdemo.bean.MasterData;

import java.util.Comparator;

public class PinyinBrandDataComparator implements Comparator<BrandData> {


    @Override
    public int compare(BrandData teamData, BrandData t1) {

        String str1 = DigitUtil.getPinYinFirst(teamData.getBrandName());
        String str2 =  DigitUtil.getPinYinFirst(t1.getBrandName());

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
