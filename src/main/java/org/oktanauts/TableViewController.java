package org.oktanauts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.oktanauts.model.*;

import java.net.URL;
import java.util.ResourceBundle;

public class TableViewController implements Initializable, GetMeasurementCallback {
    @FXML  private TableView<Patient> monitorTable;
    private TableColumn<Patient, String> nameColumn = new TableColumn<>("Patient Name");
    private TableColumn<Patient, String> valColumn = new TableColumn<>("Val");
    private TableColumn<Patient, String> timeColumn = new TableColumn<>("Time");
    private ObservableList<Patient> monitoredPatients = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update();
        nameColumn.setCellFactory(p -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty) {
                    Patient patient = getTableRow().getItem();
                    if (patient.getHasWarning()) {
                        setTextFill(Color.RED);
                    }
                    setText(patient.getName());
                }
            }
        });

        valColumn.setCellFactory(p -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty) {
                    Patient patient = getTableRow().getItem();
                    if (patient.getHasWarning()) {
                        setTextFill(Color.RED);
                    }
                    setText(patient.getMeasurement("2093-3")!= null ? patient.getMeasurement("2093-3").toString(): null);
                }
            }
        });

        timeColumn.setCellFactory(p -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty) {
                    Patient patient = getTableRow().getItem();
                    if (patient.getHasWarning()) {
                        setTextFill(Color.RED);
                    }
                    setText(patient.getMeasurement("2093-3")!= null ? patient.getMeasurement("2093-3").getTimestamp().toString(): null);
                }
            }
        });

        nameColumn.setMinWidth(170);
        valColumn.setMinWidth(100);
        timeColumn.setMinWidth(170);

        monitorTable.setItems(monitoredPatients);
        monitorTable.getColumns().addAll(nameColumn, valColumn, timeColumn);
        monitorTable.setPlaceholder(new Label("No patients being monitored"));
        monitorTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    public Patient selectedPatient(){
        return monitorTable.getSelectionModel().getSelectedItem();
    }

    public void addMonitoredPatient(Patient p){
        p.updateMeasurement("2093-3", this);
        monitoredPatients.add(p);
        update();
    }

    public void removeMonitoredPatient(Patient p){
        System.out.println("remove");
        int index = 0;
        boolean isFound = false;
        while( index < monitoredPatients.size()){
            if(monitoredPatients.get(index).equals(p)){
                isFound = true;
                break;
            }
            index ++;
        }
        if (isFound) {
            monitoredPatients.remove(index);
            update();
        }
    }

    public void update() {
        double sum = 0.0;
        int patientWithMeasurement = 0;
        for (Patient patient : monitoredPatients) {
            patient.updateMeasurement("2093-3", null);
            Measurement m = patient.getMeasurement("2093-3");
            if (m != null){
                sum += patient.getMeasurement("2093-3").getValue();
                patientWithMeasurement += 1;
            }
        }
        double average = 0;
        if (patientWithMeasurement >0){
            average = sum / patientWithMeasurement;
        }

        for (Patient patient : monitoredPatients) {
            Measurement m = patient.getMeasurement("2093-3");
            patient.setHasWarning(m != null && m.getValue() > average);
        }
    }
}

