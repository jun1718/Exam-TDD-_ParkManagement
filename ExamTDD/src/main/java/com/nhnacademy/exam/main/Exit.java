package com.nhnacademy.exam.main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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

//        long payAmount = getPayAmount(startTime, usingTime);

        try {
            this.user.getMoney().subAmount(0);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    public boolean pay(Map<String, LocalDateTime> inputRecords, PayPolicy payPolicy) {
        LocalDateTime startTime = inputRecords.get(this.user.getCar().getNumber());
        Duration usingTime = Duration.between(startTime, this.user.getPayTime());


        long payAmount = getPayAmount(startTime, usingTime, payPolicy.getTimeList(), payPolicy.getPayList());

        try {
            this.user.getMoney().subAmount(payAmount);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            return false;
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

    // FiXME : 시간남으면 계산알고리즘을 더욱 간단히 리팩토링
//    private long getPayAmount(LocalDateTime startTime, Duration usingTime) {
//        long money = 0;
//        long useTime = 0;
//
//        Map<String, Integer> map = new HashMap<>();
//        long a = 0;
//        for (String time : map.keySet()) {
//            a = Integer.valueOf(time.charAt(1));
//
//            char c = time.charAt(0);
//            if (c != 'c' && c != 'd') {
//                useTime -= a;
//                money += map.get(time);
//
//                if (useTime <= 0) {
//                    break;
//                }
//            } else if (c == 'c' && useTime > 0) {
//                long count = useTime / a;
//                if (useTime % a == 0) {
//                    money += map.get(time) * count;
//                } else {
//                    money += map.get(time) * (count + 1);
//                }
//
//                if (money > a) {
//                    money = a;
//                }
//            }
//        }
//
//        this.user.getMoney().subAmount(money);
//
//
//
//
//        long usingTimeSeconds = usingTime.toSeconds();
//        usingTimeSeconds -= 1800L;
//        long payAmount = 1000L;
//
//
//        payAmount = calculateAboutUsingTimeSeconds(payAmount, usingTimeSeconds);
//
//        long startTimeSeconds = startTime.getMinute() * 60;
//        startTimeSeconds += startTime.getHour() * 60 * 60;
//
//        if (startTimeSeconds + (usingTimeSeconds += 1800L) > 86400 && payAmount == 10000) {
//            usingTimeSeconds = startTimeSeconds + usingTimeSeconds - 86400;
//
//            long count = usingTimeSeconds / 86400;
//
//            payAmount += count * 10000;
//            if (usingTimeSeconds % 86400 != 0) {
//                usingTimeSeconds %= 86400;
//                payAmount += calculateAboutUsingTimeSeconds(0, usingTimeSeconds);
//            }
//        }
//        return payAmount;
//    }

    private long calculateAboutUsingTimeSeconds(long payAmount, long usingTimeSeconds) {
        if (usingTimeSeconds > 0L) {
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
