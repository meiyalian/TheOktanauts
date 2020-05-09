package org.oktanauts.model;

import java.util.ArrayList;

public interface GetMeasurementCallback {
    void updateSingleMeasurement(Measurement measurement);
    void updateMeasurements(ArrayList<Measurement> measurements);
}
