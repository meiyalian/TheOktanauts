package org.oktanauts.model;

import org.json.JSONArray;
import org.oktanauts.getPractitionerCallback;
import org.oktanauts.model.Objects.Patient;
import org.oktanauts.model.Objects.PatientList;
import org.oktanauts.model.Objects.Practitioner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashSet;


public class GetPractitionerModel implements GetPractitionerService {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    public void retrievePatients(String practitionerID, getPractitionerCallback callback) throws IOException, ParseException {

        //replace this block with http request,
        // after getting all the patients, create patient objects, add them to a patientList object
        //and then create a practitoner with practitioner id and the patientlist
        //lastely do callback.updateUI(practitioner)

        String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Encounter?participant=" + practitionerID + "&_format=json";
        boolean finished = false;
        PatientList patients = new PatientList(practitionerID);
        HashSet<String> patientIds = new HashSet<String>();

        while (!finished) {
            InputStream is = new URL(url).openStream();

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                //System.out.println(jsonText);
                JSONObject json = new JSONObject(jsonText);

                JSONArray entries = json.getJSONArray("entry");

                String patientId;
                for (int i = 0; i < entries.length(); i++) {
                    patientId = entries.getJSONObject(i).getJSONObject("resource").getJSONObject("subject").getString("reference");
                    if (!patientIds.contains(patientId)) {
                        patientIds.add(patientId);
                        patients.add(new Patient(patientId));
                    }
                }

                // Loops through all of the bundles
                JSONArray link = json.getJSONArray("link");
                if (link.length() > 1) {
                    url = link.getJSONObject(1).getString("url");
                }
                else {
                    finished = true;
                }
            } finally {
                is.close();
            }
        }

        Practitioner practitioner = new Practitioner(practitionerID, patients);
        callback.updateUI(practitioner);
    }
}
