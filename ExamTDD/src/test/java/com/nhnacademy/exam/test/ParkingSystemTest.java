package com.nhnacademy.exam.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.exam.main.Car;
import com.nhnacademy.exam.main.Money;
import com.nhnacademy.exam.main.ParkingSystem;
import com.nhnacademy.exam.main.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParkingSystemTest {
    ParkingSystem parkingSystem = null;
    User user = null;

    @BeforeEach
    void beforeEach() {
        parkingSystem = new ParkingSystem();
        user = new User("0001");
    }

    @DisplayName("차를 주차하려는 손님을 등록(저장)한다. -단수")
    @Test
    void addUserTest_one() {
        parkingSystem.addUser(user);

        assertThat(parkingSystem.getUsers().get(0).getUserId())
            .isEqualTo("0001");
    }

    @DisplayName("차를 주차하려는 손님을 등록(저장)한다. -복수")
    @Test
    void addUserTest_others() {
        parkingSystem.addUser(user);
        assertThat(parkingSystem.getUsers().get(0).getUserId())
            .isEqualTo("0001");

        User user2 = new User("0002");
        parkingSystem.addUser(user2);
        assertThat(parkingSystem.getUsers().get(1).getUserId())
            .isEqualTo("0002");
    }

    @DisplayName("등록된 손님의 지갑 및 차량정보를 조회한다.")
    @Test
    void receiveUserTets() {
        Car car = new Car("12가0001", "A-1");
        user.setCar(car);
        parkingSystem.addUser(user);

        searchInfo(0, "0001", 10000L, "12가0001");

        User user2 = new User("0002", new Money(20000L, "WON"));
        Car car2 = new Car("12가0002", "A-1");
        user2.setCar(car2);
        parkingSystem.addUser(user2);

        searchInfo(1, "0002", 20000L, "12가0002");
    }

    private void searchInfo(int index, String userId, Long money, String carNumber) {
        User receiveUser = parkingSystem.getUsers().get(index);
        Car receiveCar = receiveUser.getCar();

        assertThat(receiveUser.getUserId())
            .isEqualTo(userId);
        assertThat(receiveUser.getMoney().getAmount())
            .isEqualTo(money);

        assertThat(receiveCar.getNumber())
            .isEqualTo(carNumber);
        assertThat(receiveCar.getLocationHoping())
            .isEqualTo("A-1");
        assertThat(receiveCar.getLocation())
            .isEqualTo("");
    }
}
