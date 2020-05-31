package org.oktanauts.model;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import java.io.Reader;
import java.util.Date;
/**
 * This class is for creating a patient object
 */
public class GetPatientService {
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
     * Populates a patient from the data in the hapi fhir jpaserver
     *
     * @param patientId id of the patient in the database to be queried
     * @param callback optional callback for after the patient is created
     * @return newly created patient
     */
    public Patient getPatient(String patientId, GetPatientCallback callback) throws IOException, ParseException {
        String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/" + patientId + "?_elements=subject,name,address,birthDate,gender&_format=json";

        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);

            String firstName = json.getJSONArray("name").getJSONObject(0)
                    .getJSONArray("given").getString(0);
            String surname = json.getJSONArray("name").getJSONObject(0)
                    .getString("family");
            JSONObject address = json.getJSONArray("address").getJSONObject(0);
            Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(json.getString("birthDate"));
            String gender = json.getString("gender");
            String city = address.getString("city");
            String state = address.getString("state");
            String country = address.getString("country");

            Patient patient = new Patient(patientId, firstName, surname, dateOfBirth, gender, city, state, country);
            if (callback != null) {
                callback.getPatientSuccess(patient);
            }

            return patient;
        }
    }
}
