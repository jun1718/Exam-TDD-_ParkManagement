package com.nhnacademy.exam.test;

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

    @DisplayName("차를 주차하려는 user를 저장한다.")
    @Test
    void addUserTest() {
        parkingSystem.addUser(user);
        String userId = parkingSystem.getUsers().get(0).getUserId();
        userId.equals(user.getUserId());
    }
}
