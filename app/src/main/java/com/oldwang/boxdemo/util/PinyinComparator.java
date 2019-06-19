package com.oldwang.boxdemo.util;

import com.oldwang.boxdemo.bean.TeamData;

import java.util.Comparator;

public class PinyinComparator implements Comparator<TeamData> {


    @Override
    public int compare(TeamData teamData, TeamData t1) {

        String str1 = DigitUtil.getPinYinFirst(teamData.getTeamMemberName());
        String str2 =  DigitUtil.getPinYinFirst(t1.getTeamMemberName());

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
