package com.nhnacademy.exam.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.main.Car;
import com.nhnacademy.exam.main.Money;
import com.nhnacademy.exam.main.ParkingLot;
import com.nhnacademy.exam.main.ParkingSpace;
import com.nhnacademy.exam.main.PayPolicy;
import com.nhnacademy.exam.main.Structor;
import com.nhnacademy.exam.main.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
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

    // TODO: 이너클래스로해서 테스트결과 이쁘게 볼수있게 바꿔라
    @DisplayName("A-1에 주차한다. -차량객체가 주차공간에 들어가고 차량객체의 주차구역위치속성에 주차구역값을 넣어준다.")
    @Test
    void parkingTest() {
        exeInitStructorAsMockingMap();

        Car car = new Car("12가0001", "A-1");
        parkingLot.receiveCar(car);
        parkingLot.parking();

        assertThat(parkingLot.getParkingSpaces().get(0).getCar().getNumber())
            .isEqualTo("12가0001");
        assertThat(parkingLot.getParkingSpaces().get(0).getCar().getLocationHoping())
            .isEqualTo("A-1");
        assertThat(parkingLot.getParkingSpaces().get(0).getCode())
            .isEqualTo("A-1");
    }

    @DisplayName("A-1에 주차한다. -주차공간이 비어있어야지만 주차가 가능하다. 차량이 있다면 바로 옆공간에 주차한다.")
    @Test
    void parkingTest_mustEmpty() {
        exeInitStructorAsMockingMap();

        Car car = new Car("12가0001", "A-1");
        parkingLot.receiveCar(car);
        parkingLot.parking();

        Car car2 = new Car("12가0002", "A-1");
        parkingLot.receiveCar(car2);
        parkingLot.parking();

        testCaseParkingTest_mustEmpty(0, "12가0001");
        testCaseParkingTest_mustEmpty(1, "12가0002");
    }

    private void testCaseParkingTest_mustEmpty(int index, String carNumber) {
        assertThat(parkingLot.getParkingSpaces().get(index).getCar().getNumber())
            .isEqualTo(carNumber);
        assertThat(parkingLot.getParkingSpaces().get(index).getCar().getLocationHoping())
            .isEqualTo("A-1");
        assertThat(parkingLot.getParkingSpaces().get(index).getCar().getLocation())
            .isEqualTo("A-1");
        assertThat(parkingLot.getParkingSpaces().get(index).getCode())
            .isEqualTo("A-1");
    }

    @DisplayName("주차장에서 차객체를 찾는다. 잘못된 차량번호가 들어올시 예외를 발생한다.")
    @Test
    void findCarTest() {
        exeInitStructorAsMockingMap();

        Car car = new Car("12가0001", "A-1");
        parkingLot.receiveCar(car);
        parkingLot.parking();

        String carNumber = car.getNumber();
        assertThat(parkingLot.findCar(carNumber).getNumber())
            .isEqualTo("12가0001");

        assertThatThrownBy(() -> parkingLot.findCar("이런번호가있을까?"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("매개변수로 전달된");
    }

    @DisplayName("주차장에서 차가 나간다. -주차장에 없는 차량을 내보내려 한 경우 예외를 발생한다.")
    @Test
    public void leaveParkingLotTest_notParking() {
        exeInitStructorAsMockingMap();
        User user = new User("0001", new Money());
        Car car = new Car("12가0001", "A-1");
        user.setCar(car);

        assertThatThrownBy(() -> parkingLot.leaveParkingLot(user))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("차번호의 차량은 주차장에 없습니다.");
    }

    @DisplayName("주차장에서 차가 나간다. -주차장에 있는 차량을 내보낸 후 내보낸 처리결과를 test 한다.(list의 parkingSpace를 빈값으로 변경 후 indexRepository의 key값에 해당하는 entry 제거)")
    @Test
    public void leaveParkingLotTest_EmptyAndDelete() {
        exeInitStructorAsMockingMap();
        User user = new User("0001", new Money());
        Car car = new Car("12가0001", "A-1");
        user.setCar(car);

        parkingLot.receiveCar(user.getCar());
        parkingLot.parking();

        LocalDateTime startTime = LocalDateTime.of(2022, 04, 10, 12, 00, 00);
        user.initTime(parkingLot.getEntrance().getInputRecords().get("12가0001"));
        user.addTimeSeconds(1);

        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy(timeList, payList);

        parkingLot.setPayPolicy(payPolicy);
        parkingLot.leaveParkingLot(user);

        assertThatThrownBy(() -> parkingLot.findCar(car.getNumber()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("차번호의 차량은 주차장에 없습니다.");
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

    @AfterEach
    void afterEach() {
        parkingLot.getParkingSpaces().clear();
    }
}
