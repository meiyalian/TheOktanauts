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

public class PractitionerModel {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public void retrievePractitioner(String practitionerID, PractitionerCallback callback) throws IOException, ParseException {
        String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Encounter?participant=" + practitionerID + "&_format=json";
        boolean finished = false;
        PatientList patients = new PatientList(practitionerID);
        HashSet<String> patientIds = new HashSet<>();

        while (!finished) {
            System.out.println(url);

            try (InputStream is = new URL(url).openStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                //System.out.println(jsonText);
                JSONObject json = new JSONObject(jsonText);

                JSONArray entries = json.getJSONArray("entry");

                String patientId;
                PatientModel createPatient = new PatientModel();
                for (int i = 0; i < entries.length(); i++) {
                    patientId = entries.getJSONObject(i).getJSONObject("resource").getJSONObject("subject").getString("reference");
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

        Practitioner practitioner = new Practitioner(practitionerID, patients);
        callback.updateUI(practitioner);
    }

}
