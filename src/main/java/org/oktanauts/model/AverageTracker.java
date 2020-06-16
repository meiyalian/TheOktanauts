package org.oktanauts.model;


import java.util.HashMap;

/**
 * This class is the tracking the average values of a measurement for the table view controller
 */
public class AverageTracker {
    private String observationCode;
    private String measurementCode;
    private int count = 0;
    private double average = 0.0;

    public AverageTracker(String observationCode, String measurementCode) {
        this.observationCode = observationCode;
        this.measurementCode = measurementCode;
    }

    public String getObservationCode() {
        return this.observationCode;
    }

    public String getMeasurementCode() {
        return this.measurementCode;
    }

    public double getAverage() {
        return this.average;
    }

    public void reset() {
        this.count = 0;
        this.average = 0.0;
    }

    public void add(double newValue) {
        this.count++;
        this.average = this.average * (this.count - 1) / this.count + newValue/this.count;
    }
}
