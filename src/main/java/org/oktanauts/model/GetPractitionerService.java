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
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public void getPractitioner (String practitionerID, GetPractitionerCallback callback) throws IOException, ParseException {
        String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Encounter?participant.identifier=" + practitionerID + "&_format=json";
        boolean finished = false;
        PatientList patients = new PatientList(practitionerID);
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
                       p =  new Practitioner(practitionerID, patients);
                       if (callback != null){
                           callback.updateUI(p);
                       }
                       return;

                    }else{
                        JSONArray entries = json.getJSONArray("entry");

                        String patientId;
                        GetPatientService createPatient = new GetPatientService();
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
            }

            p = new Practitioner(practitionerID, patients);
            if (callback != null){
                callback.updateUI(p);
            }

    }
//
//    public void getPractitionerTest(String practitionerID, GetPractitionerCallback callback) throws IOException {
//        PatientList patientList = new PatientList(practitionerID);
//        patientList.add(new Patient("1", "a", "b"));
//        patientList.add(new Patient("2", "c", "d"));
//        patientList.add(new Patient("3", "e", "f"));
//        if (callback != null){
//            callback.updateUI(new Practitioner(practitionerID, patientList));
//        }
//
//    }


}
