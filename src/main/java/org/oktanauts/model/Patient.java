package org.oktanauts.model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

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
    private Measurement cholesterolLevel;




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


    public void updateCholesterol(GetMeasurementCallback callback){
        this.cholesterolLevel = retrieveSingleMeasurement("cholesterol_level", callback);

    }

    public String getCholesterolLevel(){
        if (this.cholesterolLevel != null){
            return cholesterolLevel.getDisplayValue();
        }
        return null;
    }

    public Timestamp getCholesterolMeasuredTime(){
        if (this.cholesterolLevel != null){
            return this.cholesterolLevel.getMeasuredDateTime();
        }
        return null;
    }



    private Measurement retrieveSingleMeasurement( String measurement, GetMeasurementCallback callback){
        Measurement m = new CholesterolLevel("mg/dL", new Timestamp(new Date().getTime()), 20, "cholesterol level");

        if (callback != null){
            callback.updateView(m);
        }
        return m;
    };





    }
