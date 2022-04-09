package com.nhnacademy.exam.main;

import java.util.ArrayList;
import java.util.List;

public class ParkingSystem {
    List<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }
}
