package com.nhnacademy.exam.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private InitParkingLotStructor structor = new InitParkingLotStructor();
    private Entrance entrance = new Entrance();

    private List<Map<String, Car>> parkingAreaA = new ArrayList<>();
    private List<Map<String, Car>> parkingAreaB = new ArrayList<>();

    private Car carEntering = null;

    public ParkingLot() {
        initParkingLotStructor();
    }

    private void initParkingLotStructor() {
        parkingAreaA.add(structor.getParkingLocationA1());
        parkingAreaA.add(structor.getParkingLocationA2());

        parkingAreaB.add(structor.getParkingLocationB1());
        parkingAreaB.add(structor.getParkingLocationB2());
    }

    public void receiveCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException();
        }
        this.carEntering = car;
    }

    public void parking() {
        if (carEntering.getLocationHoping().equals("A-1")) {
            parkingAreaA.get(0).put(carEntering.getId(), carEntering);
        } else if (carEntering.getLocationHoping().equals("A-2")) {
            parkingAreaA.get(1).put(carEntering.getId(), carEntering);
        } else if (carEntering.getLocationHoping().equals("B-1")) {
            parkingAreaB.get(0).put(carEntering.getId(), carEntering);
        } else if (carEntering.getLocationHoping().equals("B-2")) {
            parkingAreaB.get(1).put(carEntering.getId(), carEntering);
        }
    }


    public Map<String, Car> getParkingSpaces() {
        return parkingSpacesA1;
    }
}
