package com.eatthepath;

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

    public void printBusiness() {
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Coordinates: (" + String.valueOf(coordinates[0]) + ", " + String.valueOf(coordinates[1]) + ")");
        System.out.println("------------------------------------------");
    }

    // public void printBusiness(double[] inputCoordinates) {
    //     double distance = Distance.distance(inputCoordinates[0], inputCoordinates[1], coordinates[0], coordinates[1]);
    //     System.out.println("Name: " + name);
    //     System.out.println("Address: " + address);
    //     System.out.println("Coordinates: (" + String.valueOf(coordinates[0]) + ", " + String.valueOf(coordinates[1]) + ")");
    //     System.out.println("Distance: " + String.valueOf(distance));
    //     System.out.println("------------------------------------------");
    // }

}