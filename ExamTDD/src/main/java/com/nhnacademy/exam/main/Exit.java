package com.nhnacademy.exam.main;

import java.util.List;

public class Exit {
    private User user;
    private List<ParkingSpace> parkingSpaces;
    private int index;


    public Exit(User user) {
        this.user = user;
    }

    public Exit(User user, List<ParkingSpace> parkingSpaces, int index) {
        this.user = user;
        this.parkingSpaces = parkingSpaces;
        this.index = index;
    }

    public boolean pay() {
        try {
            user.getMoney().subAmount(1000);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void pass() {
        parkingSpaces.set(index, new ParkingSpace("", Car.getEmptyCar()));
    }
}
