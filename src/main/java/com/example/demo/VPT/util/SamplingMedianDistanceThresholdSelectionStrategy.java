package com.example.demo.VPT.util;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.VPT.DistanceFunction;
import com.example.demo.VPT.ThresholdSelectionStrategy;


public class SamplingMedianDistanceThresholdSelectionStrategy<P, E extends P> extends MedianDistanceThresholdSelectionStrategy<P, E> implements ThresholdSelectionStrategy<P, E> {

    private final int numberOfSamples;

    public static final int DEFAULT_NUMBER_OF_SAMPLES = 32;

    /**
     * Constructs a threshold selector that uses up to a default ({@value DEFAULT_NUMBER_OF_SAMPLES}) number of samples
     * from a list of points to choose a median distance.
     */
    public SamplingMedianDistanceThresholdSelectionStrategy() {
        this(DEFAULT_NUMBER_OF_SAMPLES);
    }

    /**
     * Constructs a threshold selector that uses up to the given number of samples from a list of points to choose a
     * median distance.
     *
     * @param numberOfSamples the maximum number of samples to use when choosing a median distance
     */
    public SamplingMedianDistanceThresholdSelectionStrategy(final int numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }

    /**
     * Returns the median distance of a subset of the given points from the given origin. The given list of points may
     * be partially sorted in the process.
     *
     * @param points the list of points from which a median distance will be chosen
     * @param origin the point from which distances to other points will be calculated
     * @param distanceFunction the function to be used to calculate the distance between the origin and other points
     *
     * @return the median distance from the origin to the given list of points
     */
    @Override
    public double selectThreshold(final List<E> points, final P origin, final DistanceFunction<P> distanceFunction) {
        return super.selectThreshold(this.getSampledPoints(points), origin, distanceFunction);
    }

    /**
     * Chooses a subset of points from which to calculate a median by sampling the given list.
     *
     * @param points the points from which to choose a subset of points
     *
     * @return a list containing at most the number of points chosen at construction time
     */
    List<E> getSampledPoints(final List<E> points) {
        final List<E> sampledPoints;

        if (points.size() > this.numberOfSamples) {
            sampledPoints = new ArrayList<>(this.numberOfSamples);
            final int step = points.size() / this.numberOfSamples;

            for (int i = 0; i < this.numberOfSamples; i++) {
                sampledPoints.add(points.get(i * step));
            }
        } else {
            sampledPoints = points;
        }

        return sampledPoints;
    }
}
