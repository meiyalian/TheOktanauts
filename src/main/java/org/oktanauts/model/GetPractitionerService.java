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
        String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Encounter?participant.identifier=" +
                practIdentifier + "&_format=json";
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

                    }else{
                        JSONArray entries = json.getJSONArray("entry");

                        String patientId;
                        GetPatientService createPatient = new GetPatientService();
                        for (int i = 0; i < entries.length(); i++) {
                            patientId = entries.getJSONObject(i).getJSONObject("resource").getJSONObject("subject")
                                    .getString("reference");
                            if (!patientIds.contains(patientId)) {
                                patientIds.add(patientId);
                                Patient newPatient = createPatient.getPatient(patientId, null);

                                patients.add(newPatient);
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

            p = new Practitioner(practIdentifier, patients);
            if (callback != null){
                callback.updateUI(p);
            }
    }
}
