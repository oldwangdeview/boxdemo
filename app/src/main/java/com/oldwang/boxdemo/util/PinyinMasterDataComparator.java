package com.oldwang.boxdemo.util;

import com.oldwang.boxdemo.bean.MasterData;
import com.oldwang.boxdemo.bean.TeamData;

import java.util.Comparator;

public class PinyinMasterDataComparator implements Comparator<MasterData> {


    @Override
    public int compare(MasterData teamData, MasterData t1) {

        String str1 = DigitUtil.getPinYinFirst(teamData.getNewsMasterName());
        String str2 =  DigitUtil.getPinYinFirst(t1.getNewsMasterName());

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
