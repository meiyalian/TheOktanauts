package org.oktanauts.model;

import java.sql.Timestamp;
/**
 * This class is the measurement model class
 */
public class Measurement {
    private String code;
    private MeasurementType type;
    private double value;
    private String unit;
    private Timestamp timestamp;
    public enum MeasurementType  {CHOLESTEROL_LEVEL, SYSTOLIC_BP, DIASTOLIC_BP}

    /**
     * Measurement Constructor
     *
     * @param code the LOINC code of the measurement
     * @param type the name of the measurement
     * @param value the double value of the measurement
     * @param unit the unit that the measurement value is expressed in
     * @param timestamp the timestamp of when the measurement was taken
     */
    public Measurement(String code, MeasurementType type, double value, String unit, Timestamp timestamp) {
        this.code = code;
        this.type = type;
        this.value = value;
        this.unit = unit;
        this.timestamp = timestamp;
    }

    /**
     * Gets the LOINC code of the measurement
     *
     * @return the LOINC code of the measurement
     */
    public String getCode() { return this.code; }

    /**
     * Gets the value of the measurement
     *
     * @return the float value of the measurement
     */
    public double getValue() { return this.value; }

    /**
     * Gets the timestamp of when the measurement was taken
     *
     * @return the timestamp of when the measurement was taken
     */
    public Timestamp getTimestamp() { return this.timestamp; }

    /**
     * Gets a formatted string version of the measurement
     *
     * @return the concatenated string of the value and unit
     */
    public String toString() { return this.value + " " + this.unit; }

    public MeasurementType getType(){
        return type;
    }
}
