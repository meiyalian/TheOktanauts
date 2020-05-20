package org.oktanauts.model;

import java.util.ArrayList;

/**
 * This class is the practitioner model class
 * There is only one practitioner object exist at a time
 */
public class Practitioner {
    private String identifier;
    private PatientList patients ;

    /**
     * Practitioner Constructor
     *
     * @param identifier the unique identifier of the practitioner
     * @param patientList a cache of the list of patients that belong to the practitioner
     */
    public Practitioner(String identifier, PatientList patientList) {
        this.identifier = identifier;
        this.patients = patientList;
    }

    /**
     * Gets the unique identifier of the practitioner
     *
     * @return the unique identifier string
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Gets all of the patients in the practitioner's cache
     *
     * @return the ArrayList of the patients
     */
    public ArrayList<Patient> getPatients() {
        if (patients != null){
            return patients.getAllPatients();
        }
        else{
            return new ArrayList<Patient>();
        }

    }

}

