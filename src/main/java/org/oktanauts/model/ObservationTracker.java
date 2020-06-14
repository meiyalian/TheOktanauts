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


    public ObservationTracker(String observationCode, int numberOfRecords, Patient p) {
        this.observationCode = observationCode;
        this.maxNumOfRecords = numberOfRecords;
        this.records = new ArrayList<>();
        this.patient = p;
    }

    /**
     * Adds an measurement to tracker
     *
     * @param observation the latest measurement
     */
    public void addObservation(Observation observation, int position) {
        records.add(position,observation);

        if (this.records.size() > this.maxNumOfRecords) {
            this.records.remove(this.records.size() - 1);
        }
    }

    /**
     * set number of records the tracker needs to track
     *
     * @param numberOfRecords number of records the tracker needs to track
     */
    public void setMaxNumberOfRecords(int numberOfRecords){
        this.maxNumOfRecords = numberOfRecords;
        while (this.records.size() > this.maxNumOfRecords) {
            this.records.remove(this.records.size() - 1);
        }
    }


    public Patient getPatient() {
        return patient;
    }

    public String getObservationCode() {
        return this.observationCode;
    }

    public int getCount() {
        return this.records.size();
    }


    /**
     * display the information of one measurement
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
     * get the latest measurement
     *
     */
    public Observation getLatest() {
        if (records.size() == 0){
            return null;
        }
        return this.records.get(0);
    }

    /**
     * get the latest measurement updated time
     *
     */
    public Timestamp getLastUpdated() {
        if (records.size() > 0) {
            return this.records.get(0).getTimestamp();
        }
        else {
            return null;
        }
    }

    public int getMaxNumOfRecords() {
        return this.maxNumOfRecords;
    }

    public ArrayList<Observation> getRecords() {
        return records;
    }
}
