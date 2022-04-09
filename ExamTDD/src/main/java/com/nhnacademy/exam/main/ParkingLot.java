package com.nhnacademy.exam.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private Car carEntering = null;
    private Entrance entrance = new Entrance();
    private List<ParkingSpace> parkingSpaces = new ArrayList<>();
    private Map<String, Integer> indexRepositoryForSpeedUp = new HashMap<>();
    private static int parkingCount = 0;
    Map<String, Integer> structorMap = null;

    public ParkingLot() {}
    public ParkingLot(Structor structor) {
        initStructor(structor);
    }

    // TODO: 생성자 만들어서 해당부분 자동실행되게하고 접근지정자 priavete로 변경
    public void initStructor(Structor structor) {
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
        this.carEntering = car;
    }

    public void parking() {
        //TODO : 나중에 차주가 원하는 주차구역에 자리가 없을 경우 다른 구역으로 배정하고 차주의 주차구역정보를 변경
        for (int i = 0; i < parkingSpaces.size(); i++) {
            if (!isEqualCode(i) || !isEmptyAtParkingSpaceToHope(i)) {
                continue;
            }
            inputCar(i);
        }
    }

    private boolean isEmptyAtParkingSpaceToHope(int index) {
        return parkingSpaces.get(index).getCar().equals(Car.getEmptyCar());
    }

    private boolean isEqualCode(int index) {
        return parkingSpaces.get(0).getCode().equals(carEntering.getLocationHoping());
    }

    private void inputCar(int index) {
        ParkingSpace parkingSpace= parkingSpaces.get(index);

        parkingSpace.setCar(carEntering);
        String carNumber = parkingSpace.getCar().getNumber();
        indexRepositoryForSpeedUp.put(carNumber, index);
        parkingCount++;
    }

    public Car findCar(String number) {
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
