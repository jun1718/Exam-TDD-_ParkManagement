package com.nhnacademy.exam.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.main.Car;
import com.nhnacademy.exam.main.ParkingLot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParkingLotTest {
    ParkingLot parkingLot = new ParkingLot();

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
        Car car = new Car("12가0001", "A-1");
        parkingLot.receiveCar(car);
        parkingLot.parking();
        assertThat(parkingLot.getParkingSpacesA1().get(car.getId()))
            .isEqualTo("12가0001");
    }

    // 계산한다
}
