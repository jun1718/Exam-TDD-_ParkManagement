package com.nhnacademy.exam.main;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Entrance {
    Map<String, Date> enteredTimeRecords = new HashMap<>();

    public void scan(Car car) {
        Date date = new Date();
        System.out.println(date);
        enteredTimeRecords.put(car.getId(), date);
    }

    public Map<String, Date> getInputRcords() {
        return enteredTimeRecords;
    }
}
