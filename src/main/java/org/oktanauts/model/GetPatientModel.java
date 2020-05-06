package org.oktanauts.model;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import java.io.Reader;
import java.util.Date;


public class GetPatientModel implements GetPatientService {
    @Override
    public Patient retrievePatient(String patientId, GetPatientCallback callback) throws IOException, ParseException {
        String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/" + patientId + "?_format=json";
        InputStream is = new URL(url).openStream();

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);

            String firstName = json.getJSONArray("name").getJSONObject(0)
                    .getJSONArray("given").getString(0);
            String surname = json.getJSONArray("name").getJSONObject(0)
                    .getString("family");
            JSONObject address = json.getJSONArray("address").getJSONObject(0);
            Date birthday = new SimpleDateFormat("yyyy-MM-dd").parse(json.getString("birthDate"));
            String gender = json.getString("gender");
            String city = address.getString("city");
            String state = address.getString("state");
            String country = address.getString("country");

            Patient patient= new Patient(patientId,firstName, surname, birthday, gender, city, state, country );
            if (callback != null){
                callback.getPatientSuccess(patient);
            }

            return patient;

        } finally {
            is.close();
        }


    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
