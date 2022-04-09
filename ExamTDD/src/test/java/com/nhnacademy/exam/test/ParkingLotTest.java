package com.nhnacademy.exam.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.main.Car;
import com.nhnacademy.exam.main.ParkingLot;
import com.nhnacademy.exam.main.Structor;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParkingLotTest {
    ParkingLot parkingLot = new ParkingLot();

    @DisplayName("주차장을 만든다. A-1 20개, A-2 20개, B-1 10개")
    @Test
    void initStructorTest() {
        exeInitStructorAsMockingMap();

        assertThat(parkingLot.getParkingSpaces().size())
            .isEqualTo(50);
        assertThat(parkingLot.getParkingSpaces().get(0).getCode())
            .isEqualTo("A-1");
        assertThat(parkingLot.getParkingSpaces().get(20).getCode())
            .isEqualTo("A-2");
        assertThat(parkingLot.getParkingSpaces().get(40).getCode())
            .isEqualTo("B-1");
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

    @DisplayName("주차장에 차가 들어온다.")
    @Test
    void receiveCarTest() {
        Car car = new Car("12가0001", "A-1");

        assertThatCode(() -> parkingLot.receiveCar(car))
            .doesNotThrowAnyException();
    }

    @DisplayName("A-1에 주차한다.")
    @Test
    void parkingTest() {
        exeInitStructorAsMockingMap();

        Car car = new Car("12가0001", "A-1");
        parkingLot.receiveCar(car);
        parkingLot.parking();

        assertThat(parkingLot.getParkingSpaces().get(0).getCar().getNumber())
            .isEqualTo("12가0001");
        assertThat(parkingLot.getParkingSpaces().get(0).getCode())
            .isEqualTo("A-1");
    }

    //    @DisplayName("주차장에서 차객체를 찾는다.")
//    @Test
//    void findCarTest() {
//        Car car = new Car("12가0001", "A-1");
//        parkingLot.receiveCar(car);
//        parkingLot.parking();
//
//        assertThat(parkingLot.findCar(car.getNumber()).getNumber())
//            .isEqualTo("12가0001");
//    }

//    @DisplayName("주차장에서 차가 나간다.")
//    @Test
//    public void dummy() {
//        Car car = new Car("12가0001", "A-1");
//        parkingLot.receiveCar(car);
//        parkingLot.parking();
//        parkingLot.getOut(car);
//
//        assertThatThrownBy(() -> parkingLot.findCar(car.getNumber()))
//            .isInstanceOf(IllegalArgumentException.class);
//    }
}
