package org.oktanauts.model.Objects;

import java.text.ParseException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import java.util.HashSet;

import org.json.JSONObject;

public class Patient {

    private String id;
    private String firstName;
    private String surname;
    private Date birthday;
    private String gender;
    private String city;
    private String state;
    private String country;
    private boolean isSelected = false;


    public Patient(String id, Date birthday, String gender, String city, String state, String country) {
        this.id = id;
        this.birthday = birthday;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public Patient(String id) throws IOException, ParseException {
        String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/" + id + "?_format=json";
        InputStream is = new URL(url).openStream();

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);

            JSONObject address = json.getJSONArray("address").getJSONObject(0);
            this.id = id;
            this.firstName = json.getJSONArray("name").getJSONObject(0)
                    .getJSONArray("given").getString(0);
            this.surname = json.getJSONArray("name").getJSONObject(0)
                    .getString("family");
            this.birthday = new SimpleDateFormat("yyyy-MM-dd").parse(json.getString("birthDate"));
            this.gender = json.getString("gender");
            this.city = address.getString("city");
            this.state = address.getString("state");
            this.country = address.getString("country");
        } finally {
            is.close();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() { return getFirstName() + " " + getSurname(); }

    public String getFirstName() { return firstName; }

    public String getSurname() { return surname; }

    public Date getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }


    }
