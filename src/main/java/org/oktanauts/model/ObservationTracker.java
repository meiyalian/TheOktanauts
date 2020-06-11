package org.oktanauts.model;

import java.sql.Timestamp;
import java.util.ArrayList;



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
     * Adds the new latest measurement
     *
     * @param observation the latest measurement
     */
    public void addObservation(Observation observation, int position) {
        if (position < maxNumOfRecords){
            records.add(position,observation);
        }
    }




    public void setMaxNumberOfRecords(int numberOfRecords){
        this.maxNumOfRecords = numberOfRecords;
        if (numberOfRecords < records.size()){
            for (int i = 0; i < records.size()-numberOfRecords;i++){
                records.remove(records.size()-1);
            }
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


    public String display ( String measurementCode){
        StringBuilder retString = new StringBuilder(patient.getName() + ":");
        for (Observation o: records ) {
            retString.append(" ").append(o.getMeasurement(measurementCode).toString()).append(" ").append("(").append(o.getTimestamp()).append(")");
        }
        return retString.toString();
    }

    public Observation getLatest() {
        if (records.size() == 0){
            return null;
        }
        return this.records.get(0);
    }

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
