package org.oktanauts.model;

import org.oktanauts.getPractitionerCallback;

import java.io.IOException;
import java.text.ParseException;

public interface GetPractitionerService {
    void retrievePatients(String practitionerID, getPractitionerCallback callback) throws IOException, ParseException;
}
