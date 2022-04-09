package com.nhnacademy.exam.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private Entrance entrance = new Entrance();
    private List<ParkingSpace> parkingSpaces = new ArrayList<>();
    private Map<String, Integer> indexRepositoryForSpeedUp = new HashMap<>();
    private static int parkingCount = 0;
    Map<String, Integer> structorMap = null;

    public ParkingLot() {}
    public ParkingLot(Structor structor) {
        initStructor(structor);
    }

    public void initStructor(Structor structor) {
        parkingSpaces.clear();
        structorMap = structor.getStructor();
        for (String code : structorMap.keySet()) {
            inputParkingSpace(code);
        }
    }

    private void inputParkingSpace(String code) {
        for (int i = 0; i < structorMap.get(code); i++) {
            ParkingSpace parkingSpace = new ParkingSpace(code, Car.getEmptyCar());
            parkingSpaces.add(parkingSpace);
        }
    }

    public void receiveCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException();
        }

        entrance.receiveCar(car);
    }

    public void parking() {
        // TODO : 나중에 차주가 원하는 주차구역에 자리가 없을 경우 다른 구역으로 배정하고 차주의 주차구역정보를 변경
        Car car = entrance.takeCar();
        String locationHoping = car.getLocationHoping();

        FindAvailableSpace findAvailableSpace = new FindAvailableSpace(parkingSpaces);
        findAvailableSpace.findAvailableSpace(locationHoping);

        // TODO: test
        System.out.println(findAvailableSpace.isAreAnyAvailableSpace());
        if (findAvailableSpace.isAreAnyAvailableSpace()) {
            car.setLocation(locationHoping);
            inputCar(findAvailableSpace.getAvailableParkingSpace(), car, findAvailableSpace.getIndex());
        }
    }

    private void inputCar(ParkingSpace parkingSpaceOfIndex, Car car, int index) {
        parkingSpaceOfIndex.setCar(car);

        String carNumber = parkingSpaceOfIndex.getCar().getNumber();
        indexRepositoryForSpeedUp.put(carNumber, index);
        parkingCount++;
    }

    public Car findCar(String number) {
        // TODO: test
        System.out.println(indexRepositoryForSpeedUp.get(number));

        ParkingSpace parkingSpace = parkingSpaces.get(indexRepositoryForSpeedUp.get(number));

        if (parkingSpace.equals(new ParkingSpace("", Car.getEmptyCar()))) {
            throw new IllegalArgumentException();
        }

        return parkingSpace.getCar();
    }

    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }

    public static int getParkingCount() {
        return parkingCount;
    }

    public void carOut(Car car) {
        parkingSpaces.set(indexRepositoryForSpeedUp.get(car.getNumber()), new ParkingSpace("", new Car("", "")));
    }
}
