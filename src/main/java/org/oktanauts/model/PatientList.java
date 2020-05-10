package org.oktanauts.model;

import org.oktanauts.model.Aggregate;
import org.oktanauts.model.Iterator;
import org.oktanauts.model.Patient;

import java.util.ArrayList;

public class PatientList implements Aggregate {
    private ArrayList<Patient> patients= new ArrayList<>();
    private String practitionerId;

    public PatientList(String practitionerId) {
        this.practitionerId = practitionerId;
    }

    public void add(Patient patient){
        patients.add(patient);
    }

//    public Patient searchPatient(String patientId) {
//        Patient match = null;
//
//        for (Patient patient : patients) {
//            if (patient.getId().equals(patientId)) {
//                match = patient;
//            }
//        }
//
//        return match;
//    }

    public ArrayList<Patient> retriveAllPatients(){
        return patients;
    }

    @Override
    public Iterator getIterator() {
        return new PatientIterator();
    }

    private class PatientIterator implements Iterator{

        int index;
        @Override
        public boolean hasNext() {
            return index < patients.size();
        }

        @Override
        public Object next() {
            if(this.hasNext()){
                return patients.get(index++);
            }
            return null;
        }
    }
}
