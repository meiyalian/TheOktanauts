package org.oktanauts.model;

import java.util.ArrayList;

public class Practitioner {

    private String id;
    private PatientList patients;


    public Practitioner(String id) {
        this.id = id;
        patients = new PatientList(id);
    }

    public String getId() {
        return id;
    }

    public ArrayList<Patient> getPatients() {
        return patients.retriveAllPatients();
    }

    public Patient viewPatient(String patientId){
        return patients.searchPatient(patientId);
    }

}

