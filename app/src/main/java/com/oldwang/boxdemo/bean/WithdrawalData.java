package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class WithdrawalData implements Serializable{



    private String createTime;
    private String memberId;
    private String withdrawalAmount;
    private String withdrawalId;
    private String withdrawalScoresType;
    private String withdrawalStates;

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

    public String getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(String withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public String getWithdrawalId() {
        return withdrawalId;
    }

    public void setWithdrawalId(String withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    public String getWithdrawalScoresType() {
        return withdrawalScoresType;
    }

    public void setWithdrawalScoresType(String withdrawalScoresType) {
        this.withdrawalScoresType = withdrawalScoresType;
    }

    public String getWithdrawalStates() {
        return withdrawalStates;
    }

    public void setWithdrawalStates(String withdrawalStates) {
        this.withdrawalStates = withdrawalStates;
    }
}
