package org.oktanauts.model;

import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
 * This class is for updating a patient's latest measurement
 */
public class GetMeasurementService {

    public enum measurementName {CHOLESTEROL_LEVEL, BLOOD_PRESSURE}

    /**
     * Reads data from reader into string
     *
     * @param rd the reader of the string
     * @return the string of the data from the reader
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    /**
     * Updates the specified measurement of a patient
     *
     * @param p the patient to be updated
     * @param measurementName type of measurement
     * @param callback optional callback upon completion of update
     */
    public void updateMonitoredPatientMeasurement(Patient p, measurementName measurementName, GetMeasurementCallback callback, MeasurementTracker measurementTracker){
        String code="";
        switch (measurementName){

            case CHOLESTEROL_LEVEL:
                code = "2093-3";
                break;
            case BLOOD_PRESSURE:
                code = "55284-4";
                break;
        }

        String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Observation?subject=" + p.getId()
                + "&code=" + code + "&_sort=-date&_format=json";

        System.out.println(url);
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);

            if (json.has("entry")) {
                JSONObject valueQuantity = json.getJSONArray("entry").getJSONObject(0).getJSONObject("resource")
                        .getJSONObject("valueQuantity");
                Timestamp dateTime = new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'H:m:s.SX")
                        .parse(json.getJSONArray("entry").getJSONObject(0).getJSONObject("resource")
                                .getString("issued")).getTime());

                switch (measurementName){

                    case BLOOD_PRESSURE:
                        // add logic to query blood pressure, create 2 measurements SYSTOLIC_BP and DIASTOLIC_BP and add them to patient


                        //update measurement tracker of the patient, for the [tracking blood pressure history] requirement
//                        if (measurementTracker != null){
//                             measurementTracker.updateLatest(systolic blood measurement);
//                        }
                       break;
                    case CHOLESTEROL_LEVEL:
                        float value = valueQuantity.getFloat("value");
                        String unit = valueQuantity.getString("unit");
//                        String name = json.getJSONArray("entry").getJSONObject(0).getJSONObject("resource")
//                                .getJSONObject("code").getString("text");
                        Measurement result = new Measurement(code, Measurement.MeasurementType.CHOLESTEROL_LEVEL, value, unit, dateTime);
                        p.addMeasurement(result);
                        break;
                }
            }

            if (callback != null) {
                callback.updateView();
            }
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    // these are for the [tracking blood pressure history] requirement
    public MeasurementTracker getNewMeasurementTracker(Patient p, measurementName measurementName, int numberOfRecords){
        Measurement[] history = trackHistory(p,measurementName,numberOfRecords);
        return new MeasurementTracker(numberOfRecords, p, history);
    }


    private Measurement[] trackHistory (Patient p, measurementName measurementName, int numberOfRecords){
        Measurement[] history = new Measurement[numberOfRecords];
        // add logic to create measurements and return a list of measurements
        //should be in the order of latest -> oldest
        return history;
    }

}
