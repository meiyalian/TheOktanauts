package org.oktanauts.model;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

//this class is for testing purpose
public class GetMeasurementTestModel implements GetMeasurementService{

    @Override
    public Measurement retrieveSingleMeasurement(Patient p, String measurement, GetMeasurementCallback callback) {
        Measurement m = new CholesterolLevel("mg/dL", new Timestamp(new Date().getTime()), 20, p , "cholesterol level");

        if (callback != null){
            callback.updateSingleMeasurement(m);
        }
        return m;
    }

    @Override
    public ArrayList<Measurement> retrieveMeasurements(ArrayList<Patient> patients, String measurement, GetMeasurementCallback callback) {
        return null;
    }
}
