package com.oldwang.boxdemo.util;

import com.oldwang.boxdemo.bean.EvaluationData;
import com.oldwang.boxdemo.bean.MasterData;

import java.util.Comparator;

public class PinyinEquipmentComparator implements Comparator<EvaluationData> {


    @Override
    public int compare(EvaluationData teamData, EvaluationData t1) {

        String str1 = DigitUtil.getPinYinFirst(teamData.getEvaluationDetailsName());
        String str2 =  DigitUtil.getPinYinFirst(t1.getEvaluationDetailsName());

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
