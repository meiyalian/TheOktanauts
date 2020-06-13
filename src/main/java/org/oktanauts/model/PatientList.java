package org.oktanauts.model;

import org.oktanauts.model.Aggregate;
import org.oktanauts.model.Iterator;
import org.oktanauts.model.Patient;

import java.util.ArrayList;

/**
 * This class is a wrapper class for storing a list of patient objects.
 * Currently it's not as useful but it will be helpful if some filter functions are
 * required.
 */
public class PatientList implements Aggregate {
    private ArrayList<Patient> patients= new ArrayList<>();
    private String practIdentifier;

    /**
     * PatientList Constructor
     *
     * @param practIdentifier the string identifier of the practitioner
     */
    public PatientList(String practIdentifier) {
        this.practIdentifier = practIdentifier;
    }

    /**
     * Adds a new patient to the list
     *
     * @param patient the patient to be added
     */
    public void add(Patient patient){
        patients.add(patient);
    }

    /**
     * Gets the list of all of the patients in the list
     *
     * @return an ArrayList of all of the patients
     */
    public ArrayList<Patient> getAllPatients(){
        return patients;
    }

    /**
     * Overrides the getIterator function to get the new patient iterator
     *
     * @return a new patient iterator
     */
    @Override
    public Iterator getIterator() {
        return new PatientIterator(this);
    }




}
