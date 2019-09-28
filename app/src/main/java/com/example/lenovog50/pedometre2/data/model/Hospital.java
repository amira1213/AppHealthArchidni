package com.example.lenovog50.pedometre2.data.model;

import com.example.lenovog50.pedometre2.Coordinate;

public class Hospital {
    private String name;
    private String number;
    private Coordinate coordinate;


    public Hospital(String name, String number, Coordinate coordinate) {
        this.name = name;
        this.number = number;
        this.coordinate = coordinate;
    }

    public String getNumber() {
        return number;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public String getName() {
        return name;
    }
}
