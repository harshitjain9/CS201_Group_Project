package com.example.demo.VPT;

import com.example.demo.Business;

public class DistFuncImpl implements DistanceFunction<Business> {

    @Override
    public double getDistance(Business current, Business shop) {
        return Math.max(Math.abs(current.getCoordinates()[0] - shop.getCoordinates()[0]), Math.abs(current.getCoordinates()[1] - shop.getCoordinates()[1]));
    }
}
