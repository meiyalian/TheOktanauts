package org.oktanauts.model;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private BooleanProperty isMonitored = new SimpleBooleanProperty(false);
    private boolean hasWarning = false;
    private HashMap<String, Measurement> measurements;

    public Patient(String id, String firstName, String surname, Date birthday, String gender, String city, String state, String country) {
        this.id = id;
        this.birthday = birthday;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;
        this.firstName = firstName;
        this.surname = surname;
        this.measurements = new HashMap<>();
    }

    //for testing
    public Patient(String id) {
        this.id = id;
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

    public String getAddress(){
        return this.city + ", " + this.state + ", " + this.country;
    }

    public Boolean getHasWarning() { return hasWarning; }

    public void setHasWarning(boolean hasWarning) { this.hasWarning = hasWarning; }

    public BooleanProperty selectedProperty(){
        return isMonitored;
    }

    public boolean isSelected(){
        return isMonitored.get();
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    /* error handling --when measurement doesn't exist should call callback with null
        parameter and put null in hashmap */
    public void updateMeasurement(String code, GetMeasurementCallback callback) {
        String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Observation?subject=" + this.id
                + "&code=" + code + "&_format=json";

        System.out.println(url);
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);

            if (json.has("entry")){
                JSONObject valueQuantity = json.getJSONArray("entry").getJSONObject(0).getJSONObject("resource")
                        .getJSONObject("valueQuantity");

                Timestamp dateTime = new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'H:m:s.SX")
                        .parse(json.getJSONArray("entry").getJSONObject(0).getJSONObject("resource")
                                .getString("issued")).getTime());
                Float value = valueQuantity.getFloat("value");
                String unit = valueQuantity.getString("unit");
                String name = json.getJSONArray("entry").getJSONObject(0).getJSONObject("resource")
                        .getJSONObject("code").getString("text");

                Measurement result = new Measurement(code, name, value, unit, dateTime, this);
                measurements.put(code, result);
            }
            else{
                measurements.put(code, null);
            }

            if (callback != null) {
                callback.update();
            }

        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public Measurement getMeasurement(String code) {
        if (!measurements.containsKey(code)) {
            updateMeasurement(code, null);
        }
        return measurements.get(code);
    }
}
