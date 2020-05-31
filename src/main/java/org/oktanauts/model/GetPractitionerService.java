package org.oktanauts.model;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * This class is for creating a practitioner obeject + retrieve all
 * of his/her patients through encounter data
 */
public class GetPractitionerService {
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
     * Populates a practitioner from the data present in the hapi fhir jpaserver
     *
     * @param practIdentifier the identifier of the practitioner to be queries
     * @param callback optional callback for after the practitioner is created
     * @return newly created practitioner
     */
    public void getPractitioner (String practIdentifier, GetPractitionerCallback callback) throws IOException, ParseException {
        String ids = "";

        String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Practitioner?identifier=" + practIdentifier +
                "&_summary=text&_count=9999&_format=json";

        boolean finished = false;
        PatientList patients = new PatientList(practIdentifier);
        HashSet<String> patientIds = new HashSet<>();
        Practitioner p;

        while (!finished) {
            System.out.println(url);

            try (InputStream is = new URL(url).openStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);

                // if doesn't have any encounter, return a practitioner with 0 patient
                if (!json.has("entry")){
                    p =  new Practitioner(practIdentifier, patients);
                    if (callback != null){
                        callback.updateUI(p);
                    }
                    return;
                }
                else {
                    JSONArray entries = json.getJSONArray("entry");

                    for (int i = 0; i < entries.length(); i++) {
                        ids += entries.getJSONObject(i).getJSONObject("resource").getString("id") + ",";
                    }

                    // Loops through all of the bundles
                    JSONArray link = json.getJSONArray("link");
                    if (link.length() > 1) {
                        if (link.getJSONObject(1).getString("relation").equals("next")) {
                            url = link.getJSONObject(1).getString("url");
                        } else {
                            finished = true;
                        }
                    } else {
                        finished = true;
                    }
                }
            }
        }

        ids = ids.substring(0, ids.length() - 1);

        url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Patient?_has:Encounter:patient:participant="
                + ids + "&_elements=name.family,name.given,address.city,address.state,address.country,birthDate,gender&_count=9999&_format=json";
        finished = false;

        int dupeCount = 0;
        int count = 0;
        while (!finished) {
            System.out.println(count);
            System.out.println(url);

            try (InputStream is = new URL(url).openStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);

                // if doesn't have any encounter, return a practitioner with 0 patient
                if (!json.has("entry")){
                    p =  new Practitioner(practIdentifier, patients);
                    if (callback != null){
                        callback.updateUI(p);
                    }
                    return;

                }
                else{
                    JSONArray entries = json.getJSONArray("entry");

                    String patientId;
                    GetPatientService createPatient = new GetPatientService();

                    // subject,name,address,birthDate,gender

                    for (int i = 0; i < entries.length(); i++) {
                        JSONObject entry = entries.getJSONObject(i).getJSONObject("resource");

                        if (entry.getString("resourceType").equals("Patient")) {
                            patientId = entry.getString("id");

                            if (!patientIds.contains(patientId)) {
                                patientIds.add(patientId);

                                String firstName = entry.getJSONArray("name").getJSONObject(0)
                                        .getJSONArray("given").getString(0);
                                String surname = entry.getJSONArray("name").getJSONObject(0)
                                        .getString("family");
                                JSONObject address = entry.getJSONArray("address").getJSONObject(0);
                                Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(entry.getString("birthDate"));
                                String gender = entry.getString("gender");
                                String city = address.getString("city");
                                String state = address.getString("state");
                                String country = address.getString("country");

                                Patient patient = new Patient(patientId, firstName, surname, dateOfBirth, gender, city, state, country);

                                count++;
                                patients.add(patient);

                            }
                            else {
                                dupeCount++;
                            }
                        }
                    }

                    // Loops through all of the bundles
                    JSONArray link = json.getJSONArray("link");
                    if (link.length() > 1) {
                        if (link.getJSONObject(1).getString("relation").equals("next")) {
                            url = link.getJSONObject(1).getString("url");
                        } else {
                            finished = true;
                        }
                    } else {
                        finished = true;
                    }
                }

            }
        }


        System.out.println("dupes: " + dupeCount);
        System.out.println("count: " + count);
        System.out.println("total: " + patientIds.size());
        p = new Practitioner(practIdentifier, patients);
        if (callback != null){
            callback.updateUI(p);
        }
    }
}
