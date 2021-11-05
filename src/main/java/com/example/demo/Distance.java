package com.example.demo;

public class Distance {
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        return Math.max(Math.abs(lat1 - lat2), Math.abs(lon1 - lon2));
	}
}
