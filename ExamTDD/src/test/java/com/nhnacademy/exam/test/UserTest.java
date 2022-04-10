package com.nhnacademy.exam.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.exam.main.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    User user = new User("0001");

    @DisplayName("사용자가 주차장을 이용한 시간을 더한다.(분단위)")
    @Test
    void addTimeTest() {
        user.addTime(60L);
        assertThat(user.getTime())
            .isEqualTo(60L);
    }

}
