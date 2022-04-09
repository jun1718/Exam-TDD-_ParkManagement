package com.nhnacademy.exam.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Entrance {
    Map<String, Date> enteredTimeRecords = new HashMap<>();
    Deque<Car> holdCarQue = new LinkedList<Car>();

    public void receiveCar(Car car) {
        holdCarQue.offerLast(car);
        scan();
    }

    private void scan() {
        Date date = new Date();
        enteredTimeRecords.put(holdCarQue.peekLast().getNumber(), date);
    }

    public Car takeCar() {
        if (holdCarQue.size() == 0) {
            throw new IndexOutOfBoundsException("현재 대기중인 차량이 없습니다.");
        }

        return holdCarQue.pollFirst();
    }

    public Map<String, Date> getInputRcords() {
        return enteredTimeRecords;
    }

    public Deque<Car> getHoldCarQue() {
        return holdCarQue;
    }
}
