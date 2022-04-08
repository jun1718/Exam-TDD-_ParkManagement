package com.nhnacademy.exam.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.main.Car;
import com.nhnacademy.exam.main.Entrance;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EntranceTest {
    Entrance entrance = new Entrance();

    @DisplayName("차량 번호를 스캔한다.")
    @Test
    void scan() {
        Car car = mock(Car.class);
        when(car.getId()).thenReturn("12가0001");

        entrance.scan(car);
        assertThat(entrance.getInputRcords().get("12가0001"))
            .isNotNull();
    }
}
