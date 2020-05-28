package org.oktanauts.model;

import java.util.ArrayList;

class MeasurementTracker {
    private ArrayList<Measurement> records;
    private Patient patient;
    private int maxNumOfRecords;

    public MeasurementTracker(int numberOfRecords, Patient p, Measurement[] measurements) {
        maxNumOfRecords = numberOfRecords;
        records = new ArrayList<>(maxNumOfRecords);
        for(int i = 0; i<measurements.length && i<maxNumOfRecords ; i++){
            records.add(measurements[i]);
        }
        patient = p;

    }


    public Patient getPatient() {
        return patient;
    }

    public void updateLatest(Measurement m){
        if (records.size() == maxNumOfRecords){
            records.remove(records.size()-1);
        }
        records.add(0,m);
    }



}
