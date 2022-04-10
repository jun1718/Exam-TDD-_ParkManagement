package com.nhnacademy.exam.main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class Exit {
    private User user;
    private List<ParkingSpace> parkingSpaces;
    private Entrance entrance = null;
    private int index;

    public Exit(User user, List<ParkingSpace> parkingSpaces, int index) {
        this.user = user;
        this.parkingSpaces = parkingSpaces;
        this.index = index;
    }

    public boolean pay(Map<String, LocalDateTime> inputRecords) {
        LocalDateTime startTime = inputRecords.get(this.user.getCar().getNumber());
        Duration usingTime = Duration.between(startTime, this.user.getPayTime());

        long payAmount = getPayAmount(startTime, usingTime);

        try {
            this.user.getMoney().subAmount(payAmount);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private long getPayAmount(LocalDateTime startTime, Duration usingTime) {
        long usingTimeSeconds = usingTime.toSeconds();
        usingTimeSeconds -= 1800L;
        long payAmount = 1000L;

        payAmount = calculateAboutUsingTimeSeconds(payAmount, usingTimeSeconds);

        long startTimeSeconds = startTime.getMinute() * 60;
        startTimeSeconds += startTime.getHour() * 60 * 60;

        if (startTimeSeconds + (usingTimeSeconds += 1800L) > 86400 && payAmount == 10000) {
            usingTimeSeconds = startTimeSeconds + usingTimeSeconds - 86400;

            long count = usingTimeSeconds / 86400;

            payAmount += count * 10000;
            if (usingTimeSeconds % 86400 != 0) {
                usingTimeSeconds %= 86400;
                payAmount += calculateAboutUsingTimeSeconds(0, usingTimeSeconds);
            }
        }
        return payAmount;
    }

    private long calculateAboutUsingTimeSeconds(long payAmount, long usingTimeSeconds) {

        if (usingTimeSeconds > 0L){
            long count = usingTimeSeconds / 600;
            if (usingTimeSeconds % 600 == 0) {
                payAmount += 500L * count;
            } else {
                payAmount += 500L * (count + 1);
            }
        }

        if (payAmount > 10000) {
            payAmount = 10000;
        }

        return payAmount;
    }

    public void pass() {
        parkingSpaces.set(index, new ParkingSpace("", Car.getEmptyCar()));
    }
}
