package com.nhnacademy.exam.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.main.Car;
import com.nhnacademy.exam.main.FindAvailableSpace;
import com.nhnacademy.exam.main.ParkingLot;
import com.nhnacademy.exam.main.Structor;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class FindAvailableSpaceTest {
    FindAvailableSpace findAvailableSpace = null;
    ParkingLot parkingLot = new ParkingLot();

    @BeforeEach
    void beforeEach() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("A-1", 20);
        map.put("A-2", 20);
        map.put("B-1", 10);

        Structor structor = mock(Structor.class);
        when(structor.getStructor()).thenReturn(map);

        parkingLot.initStructor(structor);

        findAvailableSpace =
            new FindAvailableSpace(parkingLot.getParkingSpaces());
    }

    @DisplayName("주차장을 만든다. A-1 20개, A-2 20개, B-1 10개")
    @Test
    void initStructorTest() {
        assertThat(parkingLot.getParkingSpaces().size())
            .isEqualTo(50);
        assertThat(parkingLot.getParkingSpaces().get(0).getCode())
            .isEqualTo("A-1");
        assertThat(parkingLot.getParkingSpaces().get(20).getCode())
            .isEqualTo("A-2");
        assertThat(parkingLot.getParkingSpaces().get(40).getCode())
            .isEqualTo("B-1");
    }


    @DisplayName("원하는 구역에 빈 자리를 찾고 해당 빈자리의 인덱스값, parkingSpace값, 빈자리가 있는지에 관한 boolean값을 테스트한다.")
    @Test
    void findAvailableSpaceTest() {
        //TODO:파라미터 받아서 반복하는거 resourcefile 불러와서 그거 사용
        testCase("A-1", 0);
        testCase("A-2", 20);
        testCase("B-1", 40);
    }

    private void testCase(String locationHoping, int index) {
        findAvailableSpace.findAvailableSpace(locationHoping);

        assertThat(findAvailableSpace.getAvailableParkingSpace().getCode())
            .isEqualTo(locationHoping);

        assertThat(findAvailableSpace.getAvailableParkingSpace().getCar())
            .isEqualTo(Car.getEmptyCar());

        assertThat(findAvailableSpace.getIndex())
            .isEqualTo(index);

        assertThat(findAvailableSpace.isAreAnyAvailableSpace())
            .isTrue();
    }


    @DisplayName("A-1구역의 첫번째 공간에 다른 차를 넣었을때 A-1구역을 찾으면 사용중인 공간을 제외하고 다음 index가 나온다.")
    @Test
    void findAvailableSpaceTest_caseAddedIndexChange() {
        Car car = new Car("12가0001", "A-1");
        parkingLot.receiveCar(car);
        parkingLot.parking();

        testCase("A-1", 1);
    }
}
