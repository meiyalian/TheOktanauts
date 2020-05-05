package org.oktanauts.model;

import org.oktanauts.getPatientsCallback;

import java.io.IOException;

public interface getPatientsService {
    void retrievePatients(String practitionerID, getPatientsCallback callback) throws IOException;
}
