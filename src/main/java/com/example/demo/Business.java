package com.example.demo;

public class Business {
    private String name;

    private String address;

    private double[] coordinates;

    public Business() {

    }
    
    public Business(String name, String address, double[] coordinates) {
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public String getAddr() {
        return address;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

}
