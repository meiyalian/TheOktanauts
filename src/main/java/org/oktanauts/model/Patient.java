package org.oktanauts.model;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public Patient(String id, String firstName, String surname, Date birthday, String gender, String city, String state, String country) {
        this.id = id;
        this.birthday = birthday;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;
        this.firstName = firstName;
        this.surname = surname;
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

    public Measurement getMeasurement (String code, MeasurementCallback callback) {
        String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Observation?subject=" + this.id
                + "&code=" + code + "&_format=json";

        System.out.println(url);
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);

            JSONObject valueQuantity = json.getJSONArray("entry").getJSONObject(0).getJSONObject("resource")
                    .getJSONObject("valueQuantity");

            Timestamp dateTime = new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'H:m:s.SX").parse(json.getJSONArray("entry")
                    .getJSONObject(0).getJSONObject("resource")
                    .getString("issued")).getTime());
            Float value = valueQuantity.getFloat("value");
            String unit = valueQuantity.getString("unit");
            String name = json.getJSONArray("entry").getJSONObject(0).getJSONObject("resource")
                    .getJSONObject("code").getString("text");

            Measurement result = new Measurement(code, name, value, unit, this, dateTime);
            if (callback != null) {
                callback.updateView(result);
            }

            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    };
}
