package com.nhnacademy.exam.main;

import java.util.Objects;

public class Car {
    private final String number;
    private String locationHoping = "";
    private String location = "";

    public Car(String number, String locationHoping) {
        this.number = number;
        this.locationHoping = locationHoping;
    }

    public static Car getEmptyCar() {
        return new Car("", "");
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber() {
        return number;
    }

    public String getLocationHoping() {
        return locationHoping;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        return Objects.equals(number, car.number) &&
            Objects.equals(locationHoping, car.locationHoping);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, locationHoping);
    }
}
