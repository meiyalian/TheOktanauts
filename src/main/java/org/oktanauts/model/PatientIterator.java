package org.oktanauts.model;

/**
 * This class is the default normal iterator for patientList object.
 */
public class PatientIterator implements Iterator{
    int index;
    PatientList patientList;

    /**
     * Constructor for PatientIterator
     *
     * @param patientList the list of patients to be iterated over
     */
    public PatientIterator(PatientList patientList) {
        this.patientList = patientList;
    }

    /**
     * Returns whether or not there is a patient next in the sequence
     *
     * @return a boolean value of whether there is another patient after the current index
     */
    @Override
    public boolean hasNext() {
        return index < patientList.getAllPatients().size();
    }

    /**
     * Gets the next patient in the sequence if it exists
     *
     * @return the next patient if it exists
     */
    @Override
    public Object next() {
        if(this.hasNext()){
            return patientList.getAllPatients().get(index++);
        }
        return null;
    }
}
