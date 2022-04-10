package com.nhnacademy.exam.main;

import java.time.Duration;
import java.time.LocalDateTime;
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

    public boolean pay(Map<String, LocalDateTime> inputRecords, PayPolicy payPolicy) {
        LocalDateTime startTime = inputRecords.get(this.user.getCar().getNumber());
        Duration usingTime = Duration.between(startTime, this.user.getPayTime());
        long payAmount = getPayAmount(startTime, usingTime, payPolicy.getTimeList(), payPolicy.getPayList());

        if (this.user.getCar().getType() == CarType.LIGHT) {
            payAmount *= 0.5;
        }
        try {
            this.user.getMoney().subAmount(payAmount);
        }catch (ArithmeticException e) {
            e.printStackTrace();
            throw e;
        }

        return true;
    }
    private long getPayAmount(LocalDateTime startTime, Duration usingTime, List<String> timeList, List<Long> payList) {
        long usingTimeSeconds = usingTime.toSeconds();

        long money = getMoney(usingTimeSeconds, timeList, payList, true);

        long startTimeSeconds = startTime.getMinute() * 60;
        startTimeSeconds += startTime.getHour() * 60 * 60;

        usingTimeSeconds = usingTime.toSeconds();
        Long oneDayPay = payList.get(payList.size() - 1);

        if (money == oneDayPay && startTimeSeconds + usingTimeSeconds > 86400) {
            usingTimeSeconds = startTimeSeconds + usingTimeSeconds - 86400;
            long count = usingTimeSeconds / 86400;

            money += count * oneDayPay;
            if (usingTimeSeconds % 86400 != 0) {
                usingTimeSeconds %= 86400;
                money += getMoney(usingTimeSeconds, timeList, payList, false);
            }
        }
        return money;
    }

    private long getMoney(long usingTimeSeconds, List<String> timeList,
                          List<Long> payList, boolean isFirstSequence) {
        long money = 0;

        for (int i = 0; i < timeList.size(); i++) {
            String[] timeSplit = timeList.get(i).split(" ");
            String eventKey = timeSplit[0];
            long time = Long.valueOf(timeSplit[1]);
            Long pay = payList.get(i);

            if (eventKey.equals("first")) {
                if (isFirstSequence) {
                    usingTimeSeconds -= time;
                    money += pay;
                }

                if (usingTimeSeconds <= 0) {
                    break;
                }
            } else if (eventKey.equals("each")) {
                long count = usingTimeSeconds / time;
                if (usingTimeSeconds % time == 0) {
                    money += count * pay;
                } else {
                    money += (count + 1) * pay;
                }
            } else if (eventKey.equals("day")) {
                if (money > pay) {
                    money = pay;
                }
            }
        }
        return money;
    }

    public void pass() {
        parkingSpaces.set(index, new ParkingSpace("", Car.getEmptyCar()));
    }
}
