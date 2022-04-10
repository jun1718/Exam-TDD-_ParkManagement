package com.nhnacademy.exam.main;

import com.sun.jdi.request.InvalidRequestStateException;
import java.time.LocalDateTime;

public class User {
    private final String userId;
    private Car car = null;
    private Money money = new Money();
    private LocalDateTime payTime = null;
    private Bacode bacode = new Bacode();

    public User(String userId) {
        this.userId = userId;
    }

    public User(String userId, Money money) {
        this.userId = userId;
        this.money = money;
    }

    public Bacode getBacode() {
        return bacode;
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

    public void initTime(LocalDateTime initTime) {
        this.payTime = initTime;
    }

    public void addTimeHours(long hours) {
        if (this.payTime == null) {
            throw new InvalidRequestStateException("initTime으로 시간을 먼저 초기화 해주셔야 해당 메소드를 사용 가능합니다.");
        }
        this.payTime = this.payTime.plusHours(hours);
    }

    public void addTimeMinutes(long minutes) {
        if (this.payTime == null) {
            throw new InvalidRequestStateException("initTime으로 시간을 먼저 초기화 해주셔야 해당 메소드를 사용 가능합니다.");
        }
        this.payTime = this.payTime.plusMinutes(minutes);
    }

    public void addTimeSeconds(long seconds) {
        if (this.payTime == null) {
            throw new InvalidRequestStateException("initTime으로 시간을 먼저 초기화 해주셔야 해당 메소드를 사용 가능합니다.");
        }
        this.payTime = this.payTime.plusSeconds(seconds);
    }

    public LocalDateTime getPayTime() {
        return this.payTime;
    }
}
