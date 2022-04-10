package com.nhnacademy.exam.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.main.AccessPayco;
import com.nhnacademy.exam.main.Car;
import com.nhnacademy.exam.main.CarType;
import com.nhnacademy.exam.main.Exit;
import com.nhnacademy.exam.main.Money;
import com.nhnacademy.exam.main.ParkingLot;
import com.nhnacademy.exam.main.ParkingSystem;
import com.nhnacademy.exam.main.PayPolicy;
import com.nhnacademy.exam.main.Structor;
import com.nhnacademy.exam.main.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParkingShellTest {
    ParkingSystem parkingSystem = new ParkingSystem();
    ParkingLot parkingLot = parkingSystem.getParkingLot();
    PayPolicy payPolicy;
    Exit exit = null;
    User user = null;

    @BeforeEach
    void beforeAll() {
        exeInitStructorAsMockingMap();
        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        payPolicy = mockPayPolicy(timeList, payList);

        user = new User("0001", new Money());
        Car car = new Car("12가0001", "A-1");
        user.setCar(car);
    }

    private void exeInitStructorAsMockingMap() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("A-1", 20);
        map.put("A-2", 20);
        map.put("B-1", 10);

        Structor structor = mock(Structor.class);
        when(structor.getStructor()).thenReturn(map);

        parkingLot.initStructor(structor);
    }


    private void makeMockMaterial(List<String> timeList, List<Long> payList) {
        timeList.add("first 1800");
        timeList.add("each 600");
        timeList.add("day 86400");

        payList.add(1000L);
        payList.add(500L);
        payList.add(10000L);
    }

    private PayPolicy mockPayPolicy(List<String> timeList, List<Long> payList) {
        PayPolicy payPolicy = mock(PayPolicy.class);
        when(payPolicy.getTimeList())
            .thenReturn(timeList);

        when(payPolicy.getPayList())
            .thenReturn(payList);

        return payPolicy;
    }

    @Test
    void test1() {
        parkingLot.receiveCar(user.getCar());
        parkingLot.parking();

        user.initTime(parkingLot.getEntrance().getInputRecords().get(user.getCar().getNumber()));
        user.addTimeMinutes(180);
        user.getMoney().setAmount(10000L);

        exit = new Exit(user, parkingLot.getParkingSpaces(), 0);

        parkingLot.setPayPolicy(payPolicy);
        parkingLot.leaveParkingLot(user);

        assertThat(user.getMoney().getAmount())
            .isEqualTo(1500);

    }

    @Test
    void test2() {
        parkingLot.receiveCar(user.getCar());
        parkingLot.parking();

        user.initTime(parkingLot.getEntrance().getInputRecords().get(user.getCar().getNumber()));
        user.addTimeMinutes(180);
        user.getMoney().setAmount(10000L);

        user.setCoupon(1);

        exit = new Exit(user, parkingLot.getParkingSpaces(), 0);

        parkingLot.setPayPolicy(payPolicy);
        parkingLot.leaveParkingLot(user);

        assertThat(user.getMoney().getAmount())
            .isEqualTo(4500);

    }

    @Test
    void test3() {
        parkingLot.receiveCar(user.getCar());
        parkingLot.parking();

        user.initTime(parkingLot.getEntrance().getInputRecords().get(user.getCar().getNumber()));
        user.addTimeMinutes(10);
        user.getMoney().setAmount(10000L);


        user.getCar().setType(CarType.LIGHT);

        exit = new Exit(user, parkingLot.getParkingSpaces(), 0);
        parkingLot.setPayPolicy(payPolicy);
        parkingLot.leaveParkingLot(user);

        assertThat(user.getMoney().getAmount())
            .isEqualTo(9500);
    }
}
