package org.oktanauts.model;

import java.sql.Timestamp;

public abstract class Measurement {

    private String unit;
    private Timestamp measuredDateTime;
    private double value;
    private Patient measuredPatient;
    private String measurementName;
    private boolean isWarning = false;

    public Measurement(String unit, Timestamp measuredDateTime, double value, Patient belongsTo, String measurementName) {
        this.unit = unit;
        this.measuredDateTime = measuredDateTime;
        this.value = value;
        this.measuredPatient = belongsTo;
        this.measurementName = measurementName;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }

    public Timestamp getMeasuredDateTime() {
        return measuredDateTime;
    }

    public String getDisplayValue() {
        return value+ " " + unit;
    }

    public Patient getMeasuredPatient() {
        return measuredPatient;
    }




}
