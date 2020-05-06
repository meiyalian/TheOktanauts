package org.oktanauts.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

public interface GetPatientService {
    Patient retrievePatient(String patientId, GetPatientCallback callback) throws IOException, ParseException;
}
