package com.easybank.model;

import java.math.BigDecimal;

public class Deposit {

    private Account account;
    private BigDecimal amount;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
