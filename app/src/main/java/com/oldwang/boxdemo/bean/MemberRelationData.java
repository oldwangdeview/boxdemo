package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class MemberRelationData implements Serializable{


    private String memberId;
    private MemberInfo memberInfo;
    private String relationId;
    private String relationMemberId;
    private boolean isNotAttention;
    private String memberAccount;
    private String relationState;

    public String getRelationState() {
        return relationState;
    }

    public void setRelationState(String relationState) {
        this.relationState = relationState;
    }

    public String getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(String memberAccount) {
        this.memberAccount = memberAccount;
    }

    public boolean isNotAttention() {
        return isNotAttention;
    }

    public void setNotAttention(boolean notAttention) {
        isNotAttention = notAttention;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getRelationMemberId() {
        return relationMemberId;
    }

    public void setRelationMemberId(String relationMemberId) {
        this.relationMemberId = relationMemberId;
    }
}
