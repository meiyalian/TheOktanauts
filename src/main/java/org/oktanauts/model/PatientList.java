package org.oktanauts.model;

import java.util.ArrayList;

public class PatientList {
    private ArrayList<Patient> patients= new ArrayList<>();
    private String practitionerId;

    public PatientList(String practitionerId) {
        this.practitionerId = practitionerId;
        patients.add(new Patient("1"));
        patients.add(new Patient("2"));
    }

    private void loadPatients(){

    }

    public Patient searchPatient(String patientId) {
        Patient match = null;

        for (Patient patient : patients) {
            if (patient.getId().equals(patientId)) {
                match = patient;
            }
        }

        return match;
    }

    public ArrayList<Patient> retriveAllPatients(){
        return patients;
    }


//    public void updatePatients(){}



}
