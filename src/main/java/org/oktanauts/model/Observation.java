package org.oktanauts.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is the measurement model class
 */
public class Observation {
    private String code;
    private String type;
    private Timestamp timestamp;
    public HashMap<String, Measurement> components;
    private BooleanProperty isMonitored = new SimpleBooleanProperty(false);

    /**
     * Measurement Constructor
     *
     * @param code the LOINC code of the measurement
     * @param type the name of the measurement
     * @param timestamp the timestamp of when the measurement was taken
     */
    public Observation(String code, String type, Timestamp timestamp) {
        this.code = code;
        this.type = type;
        this.timestamp = timestamp;
        this.components = new HashMap<String, Measurement>();
    }

    /**
     * Gets the LOINC code of the measurement
     *
     * @return the LOINC code of the measurement
     */
    public String getCode() { return this.code; }

    /**
     * Gets the timestamp of when the measurement was taken
     *
     * @return the timestamp of when the measurement was taken
     */
    public Timestamp getTimestamp() { return this.timestamp; }

    /**
     * Gets the specific measurement from the Observation
     *
     * @param code the LOINC code of the measurement
     * @return the specified measurement
     */
    public Measurement getMeasurement(String code) {
        return this.components.get(code);
    }

    public boolean hasMeasurement(String code) {
        return this.components.containsKey(code);
    }

    /**
     * Gets all measurements in the Observation
     *
     * @return a hash map of the measurements indexed by their LOINC codes
     */
    public HashMap<String, Measurement> getAllMeasurements() {
        return this.components;
    }

    public void addMeasurement(Measurement measurement) {
        components.put(measurement.getCode(), measurement);
    }

    public String getType(){
        return type;
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

    public void setSelected(boolean isSelected) {
        this.isMonitored = new SimpleBooleanProperty(isSelected);
    }

    @Override
    public String toString() {
        return this.type;
    }

}
