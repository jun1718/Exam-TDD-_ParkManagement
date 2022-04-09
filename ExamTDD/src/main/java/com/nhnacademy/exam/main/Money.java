package com.nhnacademy.exam.main;

public class Money {
    private long amount = 10000L;
    private String currency = "WON";

    public Money() {}
    public Money(Long account, String currency) {
        this.amount = account;
        this.currency = currency;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
