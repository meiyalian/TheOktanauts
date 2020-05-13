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
    private String practitionerId;

    public PatientList(String practitionerId) {
        this.practitionerId = practitionerId;
    }

    public void add(Patient patient){
        patients.add(patient);
    }


    public ArrayList<Patient> getAllPatients(){
        return patients;
    }

    @Override
    public Iterator getIterator() {
        return new PatientIterator();
    }



    /**
     * This class is the default normal iterator for patientList object.
     */
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
