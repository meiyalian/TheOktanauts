package org.oktanauts.model;


import java.util.Date;
import java.util.HashMap;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * This class is the patient model class
 */
public class Patient {
    private String id;
    private String firstName;
    private String surname;
    private Date dateOfBirth;
    private String gender;
    private String city;
    private String state;
    private String country;
    private BooleanProperty isMonitored = new SimpleBooleanProperty(false);
    private HashMap<String, Measurement> measurements;

    /**
     * Patient Constructor
     *
     * @param id the id of the patient
     * @param firstName the first name of the patient
     * @param surname the surname of the patient
     * @param dateOfBirth the date of birth of the patient
     * @param gender the gender of the patient
     * @param city the current city that the patient resides in
     * @param state the current state that the patient resides in
     * @param country the current country that the patient resides in
     */
    public Patient(String id, String firstName, String surname, Date dateOfBirth, String gender, String city, String state, String country) {
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;
        this.firstName = firstName;
        this.surname = surname;
        this.measurements = new HashMap<>();
    }

    /**
     * Gets the patient's id
     *
     * @return the id of the patient
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the full name of the patient
     *
     * @return the patient's first name and surname concatenated together
     */
    public String getName() {
        return firstName + " " + surname;
    }

    /**
     * Gets the first name of the patient
     *
     * @return the first name of the patient
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the surname of the patient
     *
     * @return the surname of the patient
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Gets the date of birth of the patient
     *
     * @return the date of birth of the patient
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Gets the gender of the patient
     *
     * @return the gender of the patient
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the city that the patient currently resides in
     *
     * @return the city the patient currently resides in
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the state that the patient currently resides in
     *
     * @return the state that the patient currently resides in
     */
    public String getState() {
        return state;
    }

    /**
     * Gets the name of the country that the patient currently resides in
     *
     * @return the name of the country that the patient currently resides in
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the partial address of the patient
     * @return a string of the city, state, and country concatenated together
     */
    public String getAddress() {
        return this.city + ", " + this.state + ", " + this.country;
    }

    /**
     * Gets the boolean property of whether the patient is being currently monitored by the program
     *
     * @return a boolean property of whether the patient is currently being monitored
     */
    public BooleanProperty selectedProperty() {
        return isMonitored;
    }

    /**
     * Gets whether the patient is currently being monitored by the system
     *
     * @return the boolean value of whether the patient is being currently monitored by the system
     */
    public boolean isSelected() {
        return isMonitored.get();
    }

    /**
     * Adds a measurement to the patient's measurement cache
     *
     * @param measurement the measurement to be added to the patient's cache
     */
    public void addMeasurement(Measurement measurement) {
        measurements.put(measurement.getCode(), measurement);
    }

    /**
     * Gets the specified measurement from the patient's case
     *
     * @param code the LOINC code of the measurment to get
     * @return the desired measurement from the cache if it exists
     */
    public Measurement getMeasurement(String code) {
        if (!measurements.containsKey(code)) {
            return null;
        }
        return measurements.get(code);
    }


}