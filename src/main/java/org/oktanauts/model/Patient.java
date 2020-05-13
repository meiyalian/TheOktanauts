package org.oktanauts.model;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.json.JSONObject;

public class Patient {

    private String id;
    private String firstName;
    private String surname;
    private Date birthday;
    private String gender;
    private String city;
    private String state;
    private String country;
    private BooleanProperty isMonitored = new SimpleBooleanProperty(false);
    private boolean hasWarning = false;
    private HashMap<String, Measurement> measurements;


    public Patient(String id, String firstName, String surname, Date birthday, String gender, String city, String state, String country) {
        this.id = id;
        this.birthday = birthday;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;
        this.firstName = firstName;
        this.surname = surname;
        this.measurements = new HashMap<>();
    }

    //for testing
    public Patient(String id, String firstName, String surname) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.measurements = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return getFirstName() + " " + getSurname();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return this.city + ", " + this.state + ", " + this.country;
    }

    public Boolean getHasWarning() {
        return hasWarning;
    }

    public void setHasWarning(boolean hasWarning) {
        this.hasWarning = hasWarning;
    }

    public BooleanProperty selectedProperty() {
        return isMonitored;
    }

    public boolean isSelected() {
        return isMonitored.get();
    }



    public void addMeasurement(String measurementCode, Measurement measurement) {
        measurements.put(measurementCode, measurement);
    }

    public Measurement getMeasurement(String code) {
        if (!measurements.containsKey(code)) {
            return null;
        }
        return measurements.get(code);
    }


}