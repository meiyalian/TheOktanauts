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


    public ArrayList<Patient> getAllPatients(){
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
