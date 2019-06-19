package com.oldwang.boxdemo.bean;

import android.support.annotation.NonNull;

import com.oldwang.boxdemo.util.DigitUtil;

import java.io.Serializable;

public class TeamData implements Serializable{

    private String memberId;
    private String teamMemberLevel;
    private String teamMemberLogo;
    private String teamMemberName;
    private String teamMemberNum;
    private String teamMemberPhone;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getTeamMemberLevel() {
        return teamMemberLevel;
    }

    public void setTeamMemberLevel(String teamMemberLevel) {
        this.teamMemberLevel = teamMemberLevel;
    }

    public String getTeamMemberLogo() {
        return teamMemberLogo;
    }

    public void setTeamMemberLogo(String teamMemberLogo) {
        this.teamMemberLogo = teamMemberLogo;
    }

    public String getTeamMemberName() {
        return teamMemberName;
    }

    public void setTeamMemberName(String teamMemberName) {
        this.teamMemberName = teamMemberName;
    }

    public String getTeamMemberNum() {
        return teamMemberNum;
    }

    public void setTeamMemberNum(String teamMemberNum) {
        this.teamMemberNum = teamMemberNum;
    }

    public String getTeamMemberPhone() {
        return teamMemberPhone;
    }

    public void setTeamMemberPhone(String teamMemberPhone) {
        this.teamMemberPhone = teamMemberPhone;
    }

//    @Override
//    public int compareTo(@NonNull Object o) {
//        TeamData teamData = (TeamData) o;
//
//        return DigitUtil.getPinYinFirst(this.getTeamMemberName()).compareTo(DigitUtil.getPinYinFirst(teamData.getTeamMemberName()));
//    }
}
