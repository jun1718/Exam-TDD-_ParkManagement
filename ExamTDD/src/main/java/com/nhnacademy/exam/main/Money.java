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

    public void subAmount(long amount) {
        if (this.amount < amount) {
            throw new ArithmeticException("현실 세계의 현금은 음수가 없습니다. 현재금액 : " + this.amount
                + " 주차금액 : " + amount);
        }

        this.amount -= amount;
    }
}
