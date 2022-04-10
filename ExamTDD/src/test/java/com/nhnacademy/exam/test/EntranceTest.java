package com.nhnacademy.exam.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.main.Car;
import com.nhnacademy.exam.main.Entrance;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EntranceTest {
    Entrance entrance = new Entrance();
    Car mockedCar1;

    @BeforeEach
    void beforeEach() {
        mockedCar1 = makeMockedCar("12가0001");
        entrance.receiveCar(mockedCar1);
    }

    private Car makeMockedCar(String number) {
        Car mockedCar = mock(Car.class);
        when(mockedCar.getNumber()).thenReturn(number);

        return mockedCar;
    }

    @DisplayName("차량 번호를 스캔한다.")
    @Test
    void scanTest() {
        assertThat(entrance.getInputRecords().get("12가0001"))
            .isNotNull();
    }

    @DisplayName("입구에서 대기중인 첫번째 차량객체를 가져온다.")
    @Test
    void takeCarTest_bring() {
        Car receiveCar = entrance.takeCar();
        assertThat(mockedCar1.equals(receiveCar))
            .isTrue();
    }

    @DisplayName("입구에서 대기중인 차량이 없을경우에 대기차량 호출 시 예외 발생(처음부터 아무것도 없었던경우)")
    @Test
    void takeCarTest_bringEx() {
        entrance.takeCar(); // beforeEach에서 만들어진거 먼저 제거
        assertThatThrownBy(() -> entrance.takeCar())
            .isInstanceOf(IndexOutOfBoundsException.class)
            .hasMessageContaining("차량이 없습니다.");
    }

    @DisplayName("입력된 차량객체가 takeCar()로 호출되고 나면 해당 객체는 entrance 객체에서 삭제된다.(있었다가 없어진걸 확인하는 경우)")
    @Test
    void takeCarTest_delete() {
        Car receiveCar = entrance.takeCar();
        assertThat(mockedCar1.equals(receiveCar))
            .isTrue();

        assertThatThrownBy(() -> entrance.takeCar())
            .isInstanceOf(IndexOutOfBoundsException.class)
            .hasMessageContaining("차량이 없습니다.");
    }

    @DisplayName("통합테스트(차량 여러대가 입구로 잘들어오는지 확인, 차량 여러대가 다른 곳으로 이동하면서 삭제되는지 확인)")
    @Test
    void takeCarTest_many() {
        Car mockedCar2 = makeMockedCar("12가0002");
        Car mockedCar3 = makeMockedCar("12가0003");

        entrance.receiveCar(mockedCar2);
        entrance.receiveCar(mockedCar3);

        assertThat(entrance.takeCar().getNumber().equals("12가0001"))
            .isTrue();
        assertThat(entrance.takeCar().getNumber().equals("12가0002"))
            .isTrue();
        assertThat(entrance.takeCar().getNumber().equals("12가0003"))
            .isTrue();

        assertThatThrownBy(() -> entrance.takeCar())
            .isInstanceOf(IndexOutOfBoundsException.class)
            .hasMessageContaining("차량이 없습니다.");
    }

    @Test
    void scanTest_LocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime getTime = entrance.getInputRecords().get("12가0001");
        assertThat(getTime.compareTo(now))
            .isEqualTo(-1);

        assertThat(getTime.isAfter(now))
            .isFalse();

        assertThat(getTime.isBefore(now))
            .isTrue();

        assertThat(getTime.isEqual(now))
            .isFalse();
    }
}
