package org.oktanauts.model;

import java.util.ArrayList;

class ObservationTracker {
    private ArrayList<Observation> records;
    private Patient patient;
    private int maxNumOfRecords;

    public ObservationTracker(int numberOfRecords, Patient p, Observation[] observations) {
        maxNumOfRecords = numberOfRecords;
        records = new ArrayList<>(maxNumOfRecords);
        for(int i = 0; i < observations.length && i<maxNumOfRecords ; i++){
            records.add(observations[i]);
        }
        patient = p;

    }


    public Patient getPatient() {
        return patient;
    }

    public void updateLatest(Observation o){
        if (records.size() == maxNumOfRecords){
            records.remove(records.size()-1);
        }
        records.add(0, o);
    }
//
//    @Override
//    public String toString(){
//        String retString = patient.getName();
//        for (Measurement record: records ) {
//            retString += " " + record.getValue()
//        }
//    }


}
