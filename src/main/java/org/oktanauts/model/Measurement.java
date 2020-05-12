package org.oktanauts.model;

import java.sql.Timestamp;

public class Measurement {

    private String code;
    private String name;
    private double value;
    private String unit;
    private Timestamp timestamp;
    private Patient patient;


    public Measurement(String code, String name, double value, String unit, Timestamp timestamp, Patient p) {
        this.code = code;
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.timestamp = timestamp;
        this.patient = p;
    }

    public Patient getPatient() {
        return patient;
    }

    public double getValue() { return this.value; }

    public Timestamp getTimestamp() { return this.timestamp; }

    public String toString() { return this.value + " " + this.unit; }
}
