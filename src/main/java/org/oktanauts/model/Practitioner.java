package org.oktanauts.model;


import java.util.ArrayList;

/**
 * This class is the practitioner model class
 * There is only one practitioner object exist at a time
 */
public class Practitioner {

    private String id;
    private PatientList patients ;


    public Practitioner(String id, PatientList patientList) {
        this.id = id;
        this.patients = patientList;
    }

    public String getId() {
        return id;
    }


    public ArrayList<Patient> getPatients() {
        if (patients != null){
            return patients.getAllPatients();
        }
        else{
            return new ArrayList<Patient>();
        }

    }

}

