package com.example.demo;

public class Business {
    private String name;

    private String address;

    private double latitude;

    private double longitude;

    //private String[] categories;

    public Business() {

    }
    
    public Business(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        //this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public String getAddr() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // public String[] getCategories() {
    //     return categories;
    // }
}
