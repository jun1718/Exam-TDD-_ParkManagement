package com.nhnacademy.exam.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.main.AccessPayco;
import com.nhnacademy.exam.main.Bacode;
import com.nhnacademy.exam.main.Car;
import com.nhnacademy.exam.main.CarType;
import com.nhnacademy.exam.main.Exit;
import com.nhnacademy.exam.main.Money;
import com.nhnacademy.exam.main.ParkingLot;
import com.nhnacademy.exam.main.PayPolicy;
import com.nhnacademy.exam.main.Structor;
import com.nhnacademy.exam.main.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy(timeList, payList);

        assertThat(exit.pay(parkingLot.getEntrance().getInputRecords(), payPolicy))
            .isTrue();
        assertThat(user.getMoney().getAmount())
            .isEqualTo(9000L);
    }

    @DisplayName("주차요금이 천원일 때 손님이 지갑에 가진 금액보다 주차요금이 큰 경우 감산없이 예외를 출력하고 밖으로 나갈 수 없다.")
    @Test
    void payTest_basicEx() {
        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy(timeList, payList);

        user.getMoney().setAmount(800L);

        assertThatThrownBy(() -> exit.pay(parkingLot.getEntrance().getInputRecords(), payPolicy))
            .isInstanceOf(ArithmeticException.class)
            .hasMessageContaining("돈이 없으면 나갈 수 없습니다.");
        assertThat(user.getMoney().getAmount())
            .isEqualTo(800L);

        int index = parkingLot.getIndexRepositoryForSpeedUp().get("12가0001");
        assertThat(parkingLot.getParkingSpaces().get(index))
            .isNotNull();
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
        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy(timeList, payList);

        user.addTimeMinutes(9);
        exit.pay(parkingLot.getEntrance().getInputRecords(), payPolicy);
        assertThat(user.getMoney().getAmount())
            .isEqualTo(9000L);
    }

    @DisplayName("사용시간 30분 1초일때 1500원 요금발생한다. 10000 -> 8500")
    @Test
    void payTest_30Minutes1Second() {
        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy(timeList, payList);

        user.addTimeMinutes(10);
        user.addTimeSeconds(1);
        exit.pay(parkingLot.getEntrance().getInputRecords(), payPolicy);
        assertThat(user.getMoney().getAmount())
            .isEqualTo(8500L);
    }

    @DisplayName("사용시간 50분일때 2000원 요금발생한다. 10000 -> 8000")
    @Test
    void payTest_50Minutes() {
        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy(timeList, payList);

        user.addTimeMinutes(30);
        exit.pay(parkingLot.getEntrance().getInputRecords(), payPolicy);
        assertThat(user.getMoney().getAmount())
            .isEqualTo(8000L);
    }

    @DisplayName("사용시간 61분일때 3000원 요금발생한다. 10000 -> 7000")
    @Test
    void payTest_61Minutes() {
        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy(timeList, payList);

        user.addTimeMinutes(41);
        exit.pay(parkingLot.getEntrance().getInputRecords(), payPolicy);
        assertThat(user.getMoney().getAmount())
            .isEqualTo(7000L);
    }

    @DisplayName("사용시간 6시간일때 3000원 요금발생한다. 10000 -> 7000")
    @Test
    void payTest_6Hours() {
        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy(timeList, payList);

        user.addTimeMinutes(41);
        exit.pay(parkingLot.getEntrance().getInputRecords(), payPolicy);
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
        testCase(12L, 0L, 1L, 12000L, 1500L);
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

        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy(timeList, payList);

        exit.pay(map, payPolicy);
        assertThat(user.getMoney().getAmount())
            .isEqualTo(remainingMoney);
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


    @DisplayName("주차요금 정책이 변경되고 요금이 만원 이상인 경우")
    @Test
    void payTest_over10000Won_policyChange() {
        testCase2(60L, 0L, 1L, 45500L, 0L);
        testCase2(60L, 0L, 0L, 45000L, 0L);
        testCase2(48L, 0L, 0L, 45000L, 0L);
        testCase2(14L, 0L, 0L, 21000L, 0L);
        testCase2(12L, 0L, 1L, 15500L, 0L);
        testCase2(12L, 0L, 0L, 15000L, 0L);
        testCase2(6L, 0L, 0L, 16000L, 1000L);
        testCase2(5L, 0L, 0L, 13000L, 0L);
        testCase2(4L, 0L, 1L, 10500L, 0L);
        testCase2(4L, 0L, 0L, 10000L, 0L);
    }

    private void testCase2(long hour, long minute, long second, long initMoney, long remainingMoney) {
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

        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial2(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy2(timeList, payList);

        exit.pay(map, payPolicy);
        assertThat(user.getMoney().getAmount())
            .isEqualTo(remainingMoney);
    }

    private void makeMockMaterial2(List<String> timeList, List<Long> payList) {
        timeList.add("first 1800");
        timeList.add("first 1800");
        timeList.add("each 600");
        timeList.add("day 86400");

        payList.add(0L);
        payList.add(1000L);
        payList.add(500L);
        payList.add(15000L);
    }

    private PayPolicy mockPayPolicy2(List<String> timeList, List<Long> payList) {
        PayPolicy payPolicy = mock(PayPolicy.class);
        when(payPolicy.getTimeList())
            .thenReturn(timeList);

        when(payPolicy.getPayList())
            .thenReturn(payList);

        return payPolicy;
    }

    @DisplayName("주차요금 정책이 변경되고 주차요금이 만원 이하인 경우")
    @Test
    void payTest_less10000Won_policyChange() {
        testCase2(4L, 0L, 0L, 10000L, 0L);
        testCase2(3L, 20L, 1L, 8500L, 0L);
        testCase2(3L, 20L, 0L, 8000L, 0L);
        testCase2(3L, 10L, 1L, 8000L, 0L);
        testCase2(2L, 0L, 1L, 4500L, 0L);
        testCase2(2L, 0L, 0L, 4000L, 0L);
        testCase2(1L, 59L, 0L, 4000L, 0L);
        testCase2(1L, 0L, 1L, 1500L, 0L);
        testCase2(1L, 0L, 0L, 1000L, 0L);
        testCase2(0L, 59L, 0L, 10000L, 9000L);
        testCase2(0L, 50L, 0L, 10000L, 9000L);
        testCase2(0L, 30L, 1L, 1000L, 0L);
        testCase2(0L, 30L, 0L, 10000L, 10000L);
        testCase2(0L, 0L, 1L, 10000L, 10000L);
    }

    @DisplayName("경차의 경우 계산시 요금이 50%감면된다. 500 -> 0")
    @Test
    void payTest_LightCar() {
        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy(timeList, payList);

        user.getMoney().setAmount(500L);
        user.addTimeMinutes(10);
        user.getCar().setType(CarType.LIGHT);

        exit.pay(parkingLot.getEntrance().getInputRecords(), payPolicy);
        assertThat(user.getMoney().getAmount())
            .isEqualTo(0L);
    }

    @DisplayName("페이코 인증회원이면 10% 할인된다. 1000 -> 100")
    @Test
    void payTest_PaycoAccess() {
        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy(timeList, payList);

        user.getMoney().setAmount(1000L);
        user.addTimeMinutes(10);

        AccessPayco payco = mock(AccessPayco.class);
        when(payco.access(any()))
            .thenReturn(true);

        exit.setAccessPayco(payco);
        user.getBacode().setBacode("1234");
        exit.pay(parkingLot.getEntrance().getInputRecords(), payPolicy);

        assertThat(user.getMoney().getAmount())
            .isEqualTo(100);
    }

    @DisplayName("3시간 주차 후 2시간 주차권을 제시하면 1시간 요금만 발생한다.")
    @Test
    void payTest_coupon2Hours() {
        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy(timeList, payList);

        user.getMoney().setAmount(2500L);

        user.addTimeHours(2);
        user.addTimeMinutes(40);
        user.setCoupon(2);
        exit.pay(parkingLot.getEntrance().getInputRecords(), payPolicy);

        assertThat(user.getMoney().getAmount())
            .isEqualTo(0);
    }

    @DisplayName("59분 주차 후 1시간 주차권을 제시하면 무료다.")
    @Test
    void payTest_coupon1Hours() {
        List<String> timeList = new ArrayList<>();
        List<Long> payList = new ArrayList<>();

        makeMockMaterial(timeList, payList);
        PayPolicy payPolicy = mockPayPolicy(timeList, payList);

        user.getMoney().setAmount(2000L);

        user.addTimeMinutes(39);
        user.setCoupon(1);
        exit.pay(parkingLot.getEntrance().getInputRecords(), payPolicy);

        assertThat(user.getMoney().getAmount())
            .isEqualTo(2000);
    }
}
