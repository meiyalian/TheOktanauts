package org.oktanauts.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;

public class Practitioner {

    private String id;
    private PatientList patients ;


    public Practitioner(String id, PatientList patientList) {
        this.id = id;
        this.patients = patientList;
    }

    public String getId() {
        return id;
    }

    public PatientList getPatientList() {
        return patients;
    }
    public ArrayList<Patient> getPatients() {
        if (patients != null){
            return patients.getAllPatients();
        }
        else{
            return new ArrayList<Patient>();
        }

    }

}

