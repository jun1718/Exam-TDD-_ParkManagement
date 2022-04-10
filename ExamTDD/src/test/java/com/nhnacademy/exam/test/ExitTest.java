package com.nhnacademy.exam.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.main.Car;
import com.nhnacademy.exam.main.Exit;
import com.nhnacademy.exam.main.Money;
import com.nhnacademy.exam.main.ParkingLot;
import com.nhnacademy.exam.main.Structor;
import com.nhnacademy.exam.main.User;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
        exit = new Exit(user, parkingLot.getParkingSpaces(), 0);
    }

    @DisplayName("주차요금 1000원을 계산하여 손님의 지갑사정을 변경한 후 true를 리턴한다. 10000 -> 9000")
    @Test
    void payTest_basic() {
        assertThat(exit.pay())
            .isTrue();
        assertThat(user.getMoney().getAmount())
            .isEqualTo(9000L);
    }

    @DisplayName("주차요금이 천원일 때 손님이 지갑에 가진 금액보다 주차요금이 큰 경우 감산없이 예외를 출력하고 false를 리턴한다.")
    @Test
    void payTest_basicEx() {
        user.getMoney().setAmount(800L);

        assertThat(exit.pay())
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

    @Test
    void name() {
        
    }
}
