package com.nhnacademy.exam.main;

import java.util.List;

public class FindAvailableSpace {
    private final List<ParkingSpace> parkingSpaces;

    private ParkingSpace availableParkingSpace = null;
    private int index = 0;
    private boolean areAnyAvailableSpace = false;

    public FindAvailableSpace(List<ParkingSpace> parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }

    public void findAvailableSpace(String locationHoping) {
        ParkingSpace parkingSpaceOfIndex = null;

        for (int i = 0; i < this.parkingSpaces.size(); i++) {
            parkingSpaceOfIndex = this.parkingSpaces.get(i);

            if (!isEqualCode(locationHoping, parkingSpaceOfIndex) || !isEmptyAtParkingSpaceToHope(parkingSpaceOfIndex)) {
                continue;
            }

            this.availableParkingSpace = parkingSpaceOfIndex;
            this.index = i;
            this.areAnyAvailableSpace = true;
            break;
        }
    }

    private boolean isEqualCode(String locationHoping, ParkingSpace parkingSpaceOfIndex) {
        return parkingSpaceOfIndex.getCode().equals(locationHoping);
    }

    private boolean isEmptyAtParkingSpaceToHope(ParkingSpace parkingSpaceOfIndex) {
        return parkingSpaceOfIndex.getCar().equals(Car.getEmptyCar());
    }

    public ParkingSpace getAvailableParkingSpace() {
        return this.availableParkingSpace;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean isAreAnyAvailableSpace() {
        return this.areAnyAvailableSpace;
    }
}
