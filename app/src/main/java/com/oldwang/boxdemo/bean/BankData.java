package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class BankData implements Serializable{

    /**银行卡ID**/
    private String bankId;

    /**银行卡名**/
    private String bankName;

    /**银行账号**/
    private String bankcardAccount;

    /**默认银行卡**/
    private String bankcardDefault;

    /**银行卡号**/
    private String bankcardNo;

    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankcardAccount() {
        return bankcardAccount;
    }

    public void setBankcardAccount(String bankcardAccount) {
        this.bankcardAccount = bankcardAccount;
    }

    public String getBankcardDefault() {
        return bankcardDefault;
    }

    public void setBankcardDefault(String bankcardDefault) {
        this.bankcardDefault = bankcardDefault;
    }

    public String getBankcardNo() {
        return bankcardNo;
    }

    public void setBankcardNo(String bankcardNo) {
        this.bankcardNo = bankcardNo;
    }
}
