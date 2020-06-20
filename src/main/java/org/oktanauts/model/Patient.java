package org.oktanauts.model;


import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

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
    private HashMap<String, ObservationTracker> observationTrackers;


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
    public Patient(String id, String firstName, String surname, Date dateOfBirth, String gender, String city,
                   String state, String country) {
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;
        this.firstName = firstName;
        this.surname = surname;
        this.observationTrackers = new HashMap<>();
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
     * Adds a observation to the patient's observation cache
     *
     * @param observation the observation to be added to the patient's cache
     * @param position the position the observation gets added in
     */
    public void addObservation(Observation observation, int position) {
        observationTrackers.get(observation.getCode()).addObservation(observation, position);
    }

    /**
     * Gets the specified observation from the patient's case
     *
     * @param observationCode the observation to get
     * @return the desired observation from the cache if it exists
     */
    public Observation getObservation(String observationCode) {
        if (!observationTrackers.containsKey(observationCode)) {
            return null;
        }
        return observationTrackers.get(observationCode).getLatest();
    }

    /**
     * Returns whether the patient has a specific type of observation recorded
     *
     * @param observationCode the LOINC code of the observation to be queried
     * @return a boolean value indicating whether the patient has the specified observation recorded
     */
    public boolean hasObservation(String observationCode) {
        if (observationTrackers.containsKey(observationCode)) {
            return observationTrackers.get(observationCode).getCount() > 0;
        }
        return false;
    }

    /**
     * Returns whether the patient has a specific type of measurement recorded
     *
     * @param observationCode the LOINC observation code of the measurement's observation
     * @param measurementCode the LOINC code of the measurement to be queried
     * @return a boolean value indicating whether the patient has the specified observation recorded
     */
    public boolean hasMeasurement(String observationCode, String measurementCode) {
        if (observationTrackers.containsKey(observationCode)) {
            if (observationTrackers.get(observationCode).getCount() > 0) {
                return observationTrackers.get(observationCode).getLatest().hasMeasurement(measurementCode);
            }
        }
        return false;
    }

    /**
     * Adds an observation tracker to the patient
     *
     * @param observationTracker the new observation tracker to be added
     */
    public void addObservationTracker(ObservationTracker observationTracker) {
        observationTrackers.put(observationTracker.getObservationCode(), observationTracker);
    }

    /**
     * Adds a newly generated tracker to the patient
     *
     * @param code the LOINC code of the observation to be tracked
     * @param numOfRecords the number of latest records to be recorded by the tracker
     */
    public void addObservationTracker(String code, int numOfRecords) {
        observationTrackers.put(code, new ObservationTracker(code, numOfRecords,this));
    }

    /**
     * Gets the tracker of the specified observation
     *
     * @param code the LOINC code of the observation tracker to get
     * @return the specified observation tracker if it exists
     */
    public ObservationTracker getObservationTracker(String code) {
        if (observationTrackers.containsKey(code)) {
            return observationTrackers.get(code);
        }
        return null;
    }
}