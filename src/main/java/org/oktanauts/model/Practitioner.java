package org.oktanauts.model;

import org.oktanauts.model.Patient;
import org.oktanauts.model.PatientList;

public class Practitioner {

    private String id;
    private PatientList patients;


    public Practitioner(String id, PatientList patientList) {
        this.id = id;
        this.patients = patientList;
    }

    public String getId() {
        return id;
    }

    public PatientList getPatients() {
        return patients;
    }

//    public Patient viewPatient(String patientId){
//        return patients.searchPatient(patientId);
//    }

}

