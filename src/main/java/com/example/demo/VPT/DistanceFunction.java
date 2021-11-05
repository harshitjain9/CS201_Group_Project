package com.example.demo.VPT;


public interface DistanceFunction<T> {

    /**
     * Returns the distance between two points.
     *
     * @param firstPoint the first point
     * @param secondPoint the second point
     *
     * @return the distance between the two points
     */
    double getDistance(T firstPoint, T secondPoint);
}
