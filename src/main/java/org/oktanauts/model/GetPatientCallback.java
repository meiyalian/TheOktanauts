package org.oktanauts.model;

/**
 * This class is interface for class that uses the getPatientService
 */

public interface GetPatientCallback {
    void getPatientSuccess(Patient patient);
}
