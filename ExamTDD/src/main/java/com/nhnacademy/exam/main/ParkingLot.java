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
    private Map<String, Integer> structorMap = null;
    private PayPolicy payPolicy = null;

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
            throw new IllegalArgumentException("차 객체가 초기화되지 않았습니다.");
        }

        if (car.getType() == CarType.TRUCK) {
            throw new IllegalArgumentException("트럭은 여기 못들어옵니다.");
        }

        entrance.receiveCar(car);
    }

    public void parking() {
        // TODO : 나중에 차주가 원하는 주차구역에 자리가 없을 경우 다른 구역으로 배정하고 차주의 주차구역정보를 변경
        Car car = entrance.takeCar();
        String locationHoping = car.getLocationHoping();

        FindAvailableSpace findAvailableSpace = new FindAvailableSpace(parkingSpaces);
        findAvailableSpace.findAvailableSpace(locationHoping);

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
        Integer index = indexRepositoryForSpeedUp.get(number);
        if (index == null) {
            throw new IllegalArgumentException("매개변수로 전달된 차번호의 차량은 주차장에 없습니다.");
        }

        ParkingSpace parkingSpace = parkingSpaces.get(index);
        if (parkingSpace == null
                || parkingSpace.equals(new ParkingSpace("", Car.getEmptyCar()))) {
            throw new IllegalArgumentException("매개변수로 전달된 차번호가 있는 공간은 무슨 이유에선지 비워져 있습니다.");
        }

        return parkingSpace.getCar();
    }

    public void leaveParkingLot(User user) {
        Car car = user.getCar();
        Integer index = this.indexRepositoryForSpeedUp.get(car.getNumber());
        if (index == null) throw new IllegalArgumentException("매개변수로 전달된 차번호의 차량은 주차장에 없습니다.");

        Exit exit = new Exit(user, this.parkingSpaces, index);
        if (exit.pay(this.entrance.getInputRecords(), this.payPolicy)) exit.pass();

        this.indexRepositoryForSpeedUp.remove(car.getNumber());
    }

    public void setPayPolicy(PayPolicy payPolicy) {
        this.payPolicy = payPolicy;
    }

    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }

    public static int getParkingCount() {
        return parkingCount;
    }

    public Entrance getEntrance() {
        return entrance;
    }

    public Map<String, Integer> getIndexRepositoryForSpeedUp() {
        return indexRepositoryForSpeedUp;
    }
}
