package com.nhnacademy.exam.main;

public class User {
    private final String userId;
    private Car car = null;
    private Money money = new Money();

    public User(String userId) {
        this.userId = userId;
    }

    public User(String userId, Money money) {
        this.userId = userId;
        this.money = money;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    public Money getMoney() {
        return this.money;
    }
}
