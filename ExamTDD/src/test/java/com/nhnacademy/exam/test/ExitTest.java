package com.nhnacademy.exam.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.main.Car;
import com.nhnacademy.exam.main.Exit;
import com.nhnacademy.exam.main.Money;
import com.nhnacademy.exam.main.ParkingLot;
import com.nhnacademy.exam.main.Structor;
import com.nhnacademy.exam.main.User;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExitTest {
    Exit exit = null;
    ParkingLot parkingLot = null;
    User user = null;

    @BeforeEach
    void beforeEach() {
        parkingLot = new ParkingLot();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("A-1", 20);
        map.put("A-2", 20);
        map.put("B-1", 10);

        Structor structor = mock(Structor.class);
        when(structor.getStructor()).thenReturn(map);

        parkingLot.initStructor(structor);

        user = new User("0001", new Money());
        Car car = new Car("12가0001", "A-1");
        user.setCar(car);

        parkingLot.receiveCar(user.getCar());
        parkingLot.parking();

        LocalDateTime startTime = parkingLot.getEntrance().getInputRecords().get(user.getCar().getNumber());
        user.initTime(startTime);
        user.addTimeMinutes(20);
        exit = new Exit(user, parkingLot.getParkingSpaces(), 0);
    }

    @DisplayName("주차요금 1000원을 계산하여 손님의 지갑사정을 변경한 후 true를 리턴한다. 10000 -> 9000")
    @Test
    void payTest_basic() {
        assertThat(exit.pay(parkingLot.getEntrance().getInputRecords()))
            .isTrue();
        assertThat(user.getMoney().getAmount())
            .isEqualTo(9000L);
    }

    @DisplayName("주차요금이 천원일 때 손님이 지갑에 가진 금액보다 주차요금이 큰 경우 감산없이 예외를 출력하고 false를 리턴한다.")
    @Test
    void payTest_basicEx() {
        user.getMoney().setAmount(800L);

        assertThat(exit.pay(parkingLot.getEntrance().getInputRecords()))
            .isFalse();
        assertThat(user.getMoney().getAmount())
            .isEqualTo(800L);
    }

    @DisplayName("주차공간을 비운다.(차가 주차장을 나간다)")
    @Test
    void passTest() {
        assertThat(parkingLot.getParkingSpaces().get(0).getCar())
            .isEqualTo(user.getCar());

        exit.pass();
        assertThat(parkingLot.getParkingSpaces().get(0).getCar())
            .isEqualTo(Car.getEmptyCar());
    }

    @DisplayName("사용시간 29분 일때(30분 00.00초이내) 1000원 요금발생한다. 10000 -> 9000")
    @Test
    void payTest_before30Minutes() {
        user.addTimeMinutes(9);
        exit.pay(parkingLot.getEntrance().getInputRecords());
        assertThat(user.getMoney().getAmount())
            .isEqualTo(9000L);
    }

    @DisplayName("사용시간 30분 1초일때 1500원 요금발생한다. 10000 -> 8500")
    @Test
    void payTest_30Minutes1Second() {
        user.addTimeMinutes(10);
        user.addTimeSeconds(1);
        exit.pay(parkingLot.getEntrance().getInputRecords());
        assertThat(user.getMoney().getAmount())
            .isEqualTo(8500L);
    }

    @DisplayName("사용시간 50분일때 2000원 요금발생한다. 10000 -> 8000")
    @Test
    void payTest_50Minutes() {
        user.addTimeMinutes(30);
        exit.pay(parkingLot.getEntrance().getInputRecords());
        assertThat(user.getMoney().getAmount())
            .isEqualTo(8000L);
    }

    @DisplayName("사용시간 61분일때 3000원 요금발생한다. 10000 -> 7000")
    @Test
    void payTest_61Minutes() {
        user.addTimeMinutes(41);
        exit.pay(parkingLot.getEntrance().getInputRecords());
        assertThat(user.getMoney().getAmount())
            .isEqualTo(7000L);
    }

    @DisplayName("사용시간 6시간일때 3000원 요금발생한다. 10000 -> 7000")
    @Test
    void payTest_6Hours() {
        user.addTimeMinutes(41);
        exit.pay(parkingLot.getEntrance().getInputRecords());
        assertThat(user.getMoney().getAmount())
            .isEqualTo(7000L);
    }

    @DisplayName("요금이 만원 이상인 경우")
    @Test
    void payTest_over10000Won() {
        testCase(60L, 0L, 1L, 30500L, 0L);
        testCase(60L, 0L, 1L, 30500L, 0L);
        testCase(60L, 0L, 0L, 30000L, 0L);
        testCase(48L, 0L, 0L, 30000L, 0L);
        testCase(14L, 0L, 0L, 16000L, 0L);
        testCase(12L, 1L, 0L, 12000L, 1500L);
        testCase(12L, 0L, 0L, 12000L, 2000L);
        testCase(6L, 0L, 0L, 12000L, 2000L);
        testCase(5L, 0L, 0L, 12000L, 2000L);
        testCase(4L, 0L, 0L, 12000L, 2000L);
        testCase(3L, 30L, 1L, 12000L, 2000L);
        testCase(3L, 30L, 0L, 12000L, 2000L);
        testCase(3L, 29L, 0L, 12000L, 2000L);
        testCase(3L, 20L, 1L, 12000L, 2000L);
        testCase(3L, 20L, 0L, 12000L, 2500L);
    }

    private void testCase(long hour, long minute, long second, long initMoney, long remainingMoney) {
        Map<String, LocalDateTime> map = mock(HashMap.class);
        LocalDateTime startTime = LocalDateTime.of(2022, 04, 10, 12, 00, 00);
        user.initTime(startTime);
        user.getMoney().setAmount(initMoney);
        user.addTimeHours(hour);
        user.addTimeMinutes(minute);
        user.addTimeSeconds(second);

        when(map.get(any()))
            .thenReturn(startTime);

        exit = new Exit(user, parkingLot.getParkingSpaces(), 0);

        exit.pay(map);
        assertThat(user.getMoney().getAmount())
            .isEqualTo(remainingMoney);
    }

    @DisplayName("요금이 만원 이하인 경우")
    @Test
    void payTest_less10000Won() {
        testCase(3L, 20L, 1L, 12000L, 2000L);
        testCase(3L, 20L, 0L, 12000L, 2500L);
        testCase(3L, 10L, 1L, 12000L, 2500L);
        testCase(2L, 0L, 1L, 10000L, 4000L);
        testCase(2L, 0L, 0L, 10000L, 4500L);
        testCase(1L, 59L, 0L, 10000L, 4500L);
        testCase(1L, 0L, 1L, 10000L, 7000L);
        testCase(1L, 0L, 0L, 10000L, 7500L);
        testCase(0L, 59L, 0L, 10000L, 7500L);
        testCase(0L, 50L, 0L, 10000L, 8000L);
        testCase(0L, 30L, 1L, 10000L, 8500L);
        testCase(0L, 30L, 0L, 10000L, 9000L);
        testCase(0L, 0L, 1L, 10000L, 9000L);
    }

}
