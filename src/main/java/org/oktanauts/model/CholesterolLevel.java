package org.oktanauts.model;

import java.sql.Timestamp;

public class CholesterolLevel extends Measurement{

    public CholesterolLevel(String unit, Timestamp measuredDateTime, double value, Patient measuredPatient, String measurementName) {
        super(unit, measuredDateTime, value, measuredPatient, measurementName);
    }


}
