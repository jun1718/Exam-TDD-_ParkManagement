package com.nhnacademy.exam.test;

//import static jdk.internal.vm.compiler.word.LocationIdentity.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.main.Entrance;
import com.nhnacademy.exam.main.User;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    User user = new User("0001");

    @DisplayName("사용자가 주차장을 이용한 시간을 더한다.(분단위, 초단위)")
    @Test
    void addTimeTest() {
        Map<String, LocalDateTime> map = new HashMap<>();
        map.put("12가0001", LocalDateTime.now());

        Entrance entrance = mock(Entrance.class);
        when(entrance.getInputRecords())
            .thenReturn(map);

        LocalDateTime startTime = entrance.getInputRecords().get("12가0001");
        user.initTime(startTime);

        startTime = startTime.plusMinutes(60L);
        user.addTimeMinutes(60L);
        assertThat(user.getPayTime())
            .isEqualTo(startTime);

        user.addTimeSeconds(33L);
        assertThat(user.getPayTime())
            .isEqualTo(startTime.plusSeconds(33L));
    }

}
