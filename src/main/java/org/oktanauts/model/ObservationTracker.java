package org.oktanauts.model;

import java.sql.Timestamp;
import java.util.ArrayList;


/**
 * This class is the model class for tracking observations
 */
public class ObservationTracker {
    private ArrayList<Observation> records;
    private String observationCode;
    private Patient patient;
    private int maxNumOfRecords;

    /**
     * Constructor for the ObservationTracker
     *
     * @param observationCode the LOINC code of the observation
     * @param numberOfRecords the number of records to be recorded
     * @param patient the patient that is getting tracked
     */
    public ObservationTracker(String observationCode, int numberOfRecords, Patient patient) {
        this.observationCode = observationCode;
        this.maxNumOfRecords = numberOfRecords;
        this.records = new ArrayList<>();
        this.patient = patient;
    }

    /**
     * Adds an measurement to tracker
     *
     * @param observation the latest measurement
     * @param position the position the observation gets inserted at
     */
    public void addObservation(Observation observation, int position) {
        records.add(position,observation);

        if (this.records.size() > this.maxNumOfRecords) {
            this.records.remove(this.records.size() - 1);
        }
    }

    /**
     * Set number of records the tracker needs to track
     *
     * @param numberOfRecords number of records the tracker needs to track
     */
    public void setMaxNumberOfRecords(int numberOfRecords){
        this.maxNumOfRecords = numberOfRecords;
        while (this.records.size() > this.maxNumOfRecords) {
            this.records.remove(this.records.size() - 1);
        }
    }

    /**
     * Gets the patient that is being tracked
     *
     * @return the patient being tracked
     */
    public Patient getPatient() {
        return this.patient;
    }

    /**
     * Gets the LOINC code of the observation being tracked
     *
     * @return the string LOINC code of the observation
     */
    public String getObservationCode() {
        return this.observationCode;
    }

    /**
     * Gets the current amount of stored observations
     *
     * @return the number of records currently stored
     */
    public int getCount() {
        return this.records.size();
    }


    /**
     * Display the information of one measurement
     *
     * @param measurementCode  measurement code
     */
    public String display ( String measurementCode){
        StringBuilder retString = new StringBuilder(patient.getName() + ":");
        for (Observation o: records ) {
            retString.append(" ").append(o.getMeasurement(measurementCode).toString()).append(" ").append("(").append(o.getTimestamp()).append(")");
        }
        return retString.toString();
    }


    /**
     * Gets the latest measurement
     *
     * @return the measurement that was most recently recorded
     */
    public Observation getLatest() {
        if (records.size() == 0){
            return null;
        }
        return this.records.get(0);
    }

    /**
     * Get the timestamp of the most recent observation
     *
     * @return the timestamp of the most recent observation
     */
    public Timestamp getLastUpdated() {
        if (records.size() > 0) {
            return this.records.get(0).getTimestamp();
        }
        else {
            return null;
        }
    }

    /**
     * Gets the maximum number of records being stored
     *
     * @return the maximum number of records to be stored
     */
    public int getMaxNumOfRecords() {
        return this.maxNumOfRecords;
    }

    /**
     * Gets all of the records currently stored
     *
     * @return an array list of the N most recent observations
     */
    public ArrayList<Observation> getRecords() {
        return records;
    }
}
