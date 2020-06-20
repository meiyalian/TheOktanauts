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

    /**
     * Constructor for AverageTracker
     * 
     * @param observationCode the LOINC code for the observation
     * @param measurementCode the LOINC code for the measurement
     */
    public AverageTracker(String observationCode, String measurementCode) {
        this.observationCode = observationCode;
        this.measurementCode = measurementCode;
    }

    /**
     * Gets the observation code of the measurement being tracked
     *
     * @return the string of the LOINC code
     */
    public String getObservationCode() {
        return this.observationCode;
    }

    /**
     * Gets the code of the measurement being tracked
     *
     * @return the string of the LOINC code
     */
    public String getMeasurementCode() {
        return this.measurementCode;
    }

    /**
     * Gets the current average value
     *
     * @return the double representation of the current average
     */
    public double getAverage() {
        return this.average;
    }

    /**
     * Resets the current count and average to 0/0.0
     */
    public void reset() {
        this.count = 0;
        this.average = 0.0;
    }

    /**
     * Adds a new value to the tracker and calculates the new average
     *
     * @param newValue the new double value to add to the tracker
     */
    public void add(double newValue) {
        this.count++;
        this.average = this.average * (this.count - 1) / this.count + newValue/this.count;
    }
}
