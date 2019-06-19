package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class TeamBuyMembers implements Serializable{

    private String memberNickname;
    private String memberHeadurl;
    private String isHead;//1团长

    public String getIsHead() {
        return isHead;
    }

    public void setIsHead(String isHead) {
        this.isHead = isHead;
    }

    public String getTeamBuyMember() {
        return memberNickname;
    }

    public void setTeamBuyMember(String teamBuyMember) {
        this.memberNickname = teamBuyMember;
    }

    public String getMemberHeadurl() {
        return memberHeadurl;
    }

    public void setMemberHeadurl(String memberHeadurl) {
        this.memberHeadurl = memberHeadurl;
    }
}
