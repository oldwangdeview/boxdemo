package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class MemberInfo implements Serializable{


    private String agentType;
    private String beinvitateCode;
    private String identStates;
    private String memberAccount;
    private String memberHeadLogo;
    private String identStatesDes;
    private String scoresLevel;
    private String realName;
    /**昵称*/
    private String memberNickname;
    private String memberId;
    private String memberHeadurl;

    private String invitateCode;

    public String getInvitateCode() {
        return invitateCode;
    }

    public void setInvitateCode(String invitateCode) {
        this.invitateCode = invitateCode;
    }

    public String getMemberHeadurl() {
        return memberHeadurl;
    }

    public void setMemberHeadurl(String memberHeadurl) {
        this.memberHeadurl = memberHeadurl;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBeinvitateCode() {
        return beinvitateCode;
    }

    public void setBeinvitateCode(String beinvitateCode) {
        this.beinvitateCode = beinvitateCode;
    }


    public String getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(String memberAccount) {
        this.memberAccount = memberAccount;
    }

    public String getMemberHeadLogo() {
        return memberHeadLogo;
    }

    public void setMemberHeadLogo(String memberHeadLogo) {
        this.memberHeadLogo = memberHeadLogo;
    }

    public String getIdentStates() {
        return identStates;
    }

    public void setIdentStates(String identStates) {
        this.identStates = identStates;
    }

    public String getIdentStatesDes() {
        return identStatesDes;
    }

    public void setIdentStatesDes(String identStatesDes) {
        this.identStatesDes = identStatesDes;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public String getScoresLevel() {
        return scoresLevel;
    }

    public void setScoresLevel(String scoresLevel) {
        this.scoresLevel = scoresLevel;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }
}
