package com.oldwang.boxdemo.activity;

import java.io.Serializable;

public class OrderData implements Serializable{

    /**收益金额	****/
    private String commissionAmount;

    /**发生金额	****/
    private String commissionTotalAmount;

    /**创建时间	****/
    private String createTime;

    /**会员ID****/
    private String memberId;

    /**会员级别	****/
    private String memberLevel;

    /**会员名称	****/
    private String memberName;

    public String getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getCommissionTotalAmount() {
        return commissionTotalAmount;
    }

    public void setCommissionTotalAmount(String commissionTotalAmount) {
        this.commissionTotalAmount = commissionTotalAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(String memberLevel) {
        this.memberLevel = memberLevel;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
