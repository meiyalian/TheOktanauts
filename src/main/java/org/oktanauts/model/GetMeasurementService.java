package org.oktanauts.model;

import java.util.ArrayList;

public interface GetMeasurementService {
    Measurement retrieveSingleMeasurement(Patient patient, String measurement, GetMeasurementCallback callback);

    ArrayList<Measurement> retrieveMeasurements(ArrayList<Patient> patients, String measurement, GetMeasurementCallback callback);

}
