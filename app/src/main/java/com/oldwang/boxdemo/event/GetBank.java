package com.oldwang.boxdemo.event;

import com.oldwang.boxdemo.bean.BankData;

public class GetBank {
    private BankData bankData;

    public GetBank(BankData bankData) {
        this.bankData = bankData;
    }

    public BankData getBankData() {
        return bankData;
    }
}
