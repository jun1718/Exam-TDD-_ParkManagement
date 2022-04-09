package com.nhnacademy.exam.main;

import java.util.Objects;

public class ParkingSpace {
    private final String code;
    private Car car;

    public ParkingSpace(String code, Car car) {
        this.code = code;
        this.car = car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParkingSpace that = (ParkingSpace) o;
        return Objects.equals(code, that.code) && Objects.equals(car, that.car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, car);
    }
}
