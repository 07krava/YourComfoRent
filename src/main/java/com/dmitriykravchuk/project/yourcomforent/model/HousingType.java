package com.dmitriykravchuk.project.yourcomforent.model;

public enum HousingType {
    HOUSE(1, "HOUSE"),
    APARTMENT(2, "APARTMENT"),
    ROOM_IN_APARTMENT(3, "ROOM_IN_APARTMENT"),
    ROOM_IN_HOUSE(4, "ROOM_IN_HOUSE"),
    BAD_IN_HOSTEL(5,"BAD_IN_HOSTEL");

    private int id;
    private String name;

    HousingType(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
