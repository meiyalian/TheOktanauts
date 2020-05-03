package org.oktanauts.model;

import java.util.Date;

public class Patient {

    private String id;
    private String name;
    private String surame;
    private Date birthday;
    private String gender;
    private String city;
    private String state;
    private String country;


    public Patient(String id, Date birthday, String gender, String city, String state, String country) {
        this.id = id;
        this.birthday = birthday;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Patient(String id){}

    public String getId() {
        return id;
    }

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
