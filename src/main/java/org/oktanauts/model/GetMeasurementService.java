package org.oktanauts.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

    private static Measurement extractMeasurement(JSONObject component) {
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
     * @param patient the patient to be updated
     * @param code the LOINC code of the observation
     * @param callback optional callback upon completion of update
     */
    public void updatePatientMeasurement(Patient patient, String code, GetMeasurementCallback callback) {
        String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Observation?subject=" + patient.getId()
                + "&code=" + code + "&_sort=-_lastUpdated&_format=json";

        ObservationTracker observationTracker = patient.getObservationTracker(code);

        if (observationTracker != null) {
            if (observationTracker.getCount() != 0) {
                url += "&_lastUpdated=gt" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(observationTracker.getLastUpdated());
            }
            url += "&_count=" + observationTracker.getMaxNumOfRecords();
            System.out.println("Find " + observationTracker.getMaxNumOfRecords());
        }
        else {
            observationTracker = new ObservationTracker(code, 1,patient);
            patient.addObservationTracker(observationTracker);
            url += "&_count=1";
            System.out.println("Find 1");
        }

        boolean finished = false;
        int count = 0;
        while (!finished) {
            System.out.println(url);
            try (InputStream is = new URL(url).openStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);

                if (json.has("entry")) {
                    JSONArray entries = json.getJSONArray("entry");
                    for (int i = 0; i < entries.length(); i++) {
                        JSONObject resource = entries.getJSONObject(i).getJSONObject("resource");
                        String name = resource.getJSONObject("code").getString("text");
                        Timestamp dateTime = new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'H:m:s.SX")
                                .parse(resource.getString("issued")).getTime());

                        Observation observation = new Observation(code, name, dateTime);

                        if (!resource.has("component")) {
                            Measurement newMeasurement = extractMeasurement(resource);
                            observation.addMeasurement(newMeasurement);
                        } else {
                            JSONArray component = resource.getJSONArray("component");

                            for (int j = 0; j < component.length(); j++) {
                                Measurement newMeasurement = extractMeasurement(component.getJSONObject(j));
                                observation.addMeasurement(newMeasurement);
                            }
                        }

                        patient.addObservation(observation, count);
                        count++;

                        if (count >= observationTracker.getMaxNumOfRecords()) {
                            finished = true;
                            break;
                        }
                    }

                    // Loops through all of the bundles
                    JSONArray link = json.getJSONArray("link");
                    if (link.length() > 1) {
                        if (link.getJSONObject(1).getString("relation").equals("next")) {
                            url = link.getJSONObject(1).getString("url");
                        } else {
                            System.out.println("Time to make like a tree, and get out of here");
                            finished = true;
                        }
                    } else {
                        finished = true;
                    }
                }
                else {
                    finished = true;
                }
            }
            catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        if (callback != null) {
            callback.updateView();
        }

        System.out.println("Time to leave!");
    }

//    // these are for the [tracking blood pressure history] requirement
//    public ObservationTracker getNewObservationTracker(Patient p, String observationName, int numberOfRecords){
//        Observation[] history = trackHistory(p,observationName,numberOfRecords);
//        return new ObservationTracker(numberOfRecords, p, history);
//    }
//
//
//    private Observation[] trackHistory (Patient p, String observationName, int numberOfRecords){
//        Observation[] history = new Observation[numberOfRecords];
//        // add logic to create measurements and return a list of measurements
//        //should be in the order of latest -> oldest
//        return history;
//    }
}
