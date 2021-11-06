package com.example.demo.VPT;


public interface PointFilter<T> {

    /**
     * Tests whether a point should be included in a spatial index's result set when searching for nearby neighbors.
     *
     * @param point the point to test
     *
     * @return {@code true} if the point may be included in the result set or {@code false} if it should be excluded
     */
    boolean allowPoint(T point);
}
