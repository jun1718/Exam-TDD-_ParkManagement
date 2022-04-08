package com.nhnacademy.exam.main;

import java.util.HashMap;
import java.util.Map;

public class InitParkingLotStructor {
    private final Map<String, Car> parkingLocationA1 = new HashMap<>(); // 최대수용 20 대
    private final Map<String, Car> parkingLocationA2 = new HashMap<>(); // 최대수용 20 대
    private final Map<String, Car> parkingLocationB1 = new HashMap<>(); // 최대수용 30 대
    private final Map<String, Car> parkingLocationB2 = new HashMap<>(); // 최대수용 30 대

    public Map<String, Car> getParkingLocationA1() {
        return parkingLocationA1;
    }

    public Map<String, Car> getParkingLocationA2() {
        return parkingLocationA2;
    }

    public Map<String, Car> getParkingLocationB1() {
        return parkingLocationB1;
    }

    public Map<String, Car> getParkingLocationB2() {
        return parkingLocationB2;
    }
}
