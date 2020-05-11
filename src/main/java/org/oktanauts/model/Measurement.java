package org.oktanauts.model;

import java.sql.Timestamp;

public class Measurement {

    private String code;
    private String name;
    private Float value;
    private String unit;
    private Patient patient;
    private Timestamp timestamp;

    public Measurement(String code, String name, Float value, String unit, Patient patient, Timestamp timestamp) {
        this.code = code;
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.patient = patient;
        this.timestamp = timestamp;
    }

    public Float getValue() { return this.value; }

    public Timestamp getTimestamp() { return this.timestamp; }

    public String toString() { return this.value + " " + this.unit; }
}
