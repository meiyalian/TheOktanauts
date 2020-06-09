package org.oktanauts.model;

import org.json.JSONArray;
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

    public static final String CHOLESTEROL_LEVEL = "2093-3";
    public static final String BLOOD_PRESSURE = "55284-4";
    public static final String DIASTOLIC_BLOOD_PRESSURE = "8462-4";
    public static final String SYSTOLIC_BLOOD_PRESSURE = "8480-6";



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

    private static Measurement extractMeasurement(JSONObject component) throws ParseException {
        String compCode = component.getJSONObject("code").getJSONArray("coding")
                .getJSONObject(0).getString("code");
        String compName = component.getJSONObject("code").getString("text");
        JSONObject valueQuantity = component.getJSONObject("valueQuantity");
        float compValue = valueQuantity.getFloat("value");
        String compUnit = valueQuantity.getString("unit");
        return new Measurement(compCode, compName, compValue, compUnit);
    }

    /**
     * Updates the specified observation of a patient
     *
     * @param p the patient to be updated
     * @param code the LOINC code of the observation
     * @param callback optional callback upon completion of update
     */
    public Observation updatePatientMeasurement(Patient p, String code, GetMeasurementCallback callback,
                                         ObservationTracker measurementTracker){

        String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Observation?subject=" + p.getId()
                + "&code=" + code + "&_sort=-_lastUpdated&_count=1&_format=json";

        System.out.println(url);
        Observation newObservation = null;
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);

            if (json.has("entry")) {
                JSONObject resource = json.getJSONArray("entry").getJSONObject(0).getJSONObject("resource");

                String name = resource.getJSONObject("code").getString("text");
                Timestamp dateTime = new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'H:m:s.SX")
                        .parse(resource.getString("issued")).getTime());

                Observation observation = new Observation(code, name, dateTime);

                // single measurement
                if (!resource.has("component")) {
                    Measurement newMeasurement = extractMeasurement(resource);
                    observation.addMeasurement(newMeasurement);
                }
                else {
                    JSONArray component = resource.getJSONArray("component");

                    for (int i = 0; i < component.length(); i++) {
                        Measurement newMeasurement = extractMeasurement(component.getJSONObject(i));
                        observation.addMeasurement(newMeasurement);
                    }
                }

                p.addObservation(observation);
                newObservation = observation;
            }

            if (callback != null) {
                callback.updateView();
            }


        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return newObservation;
    }


    // these are for the [tracking blood pressure history] requirement
    public ObservationTracker getNewObservationTracker(Patient p, String observationName, int numberOfRecords){
        Observation[] history = trackHistory(p,observationName,numberOfRecords);
        return new ObservationTracker(numberOfRecords, p, history);
    }


    private Observation[] trackHistory (Patient p, String observationName, int numberOfRecords){
        Observation[] history = new Observation[numberOfRecords];
        // add logic to create measurements and return a list of measurements
        //should be in the order of latest -> oldest
        return history;
    }

}
