package org.oktanauts;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.oktanauts.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TableViewController implements Initializable, MeasurementCallback {
    @FXML  private TableView<Patient> monitorTable;
    private TableColumn<Patient, String> nameColumn = new TableColumn<>("Patient Name");
    private TableColumn<Patient, String> valColumn = new TableColumn<>("Val");
    private TableColumn<Patient, String> timeColumn = new TableColumn<>("Time");
    private ObservableList<Patient> monitoredPatients = FXCollections.observableArrayList();
    private MeasurementCallback measurementCallback;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update();
        nameColumn.setCellFactory(p -> new TableCell<Patient, String>() {
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

        valColumn.setCellFactory(p -> new TableCell<Patient, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty) {
                    Patient patient = getTableRow().getItem();
                    if (patient.getHasWarning()) {
                        setTextFill(Color.RED);
                    }
                    setText(patient.getMeasurement("2093-3").toString());
                }
            }
        });

        timeColumn.setCellFactory(p -> new TableCell<Patient, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty) {
                    Patient patient = getTableRow().getItem();
                    if (patient.getHasWarning()) {
                        setTextFill(Color.RED);
                    }
                    setText(patient.getMeasurement("2093-3").getTimestamp().toString());
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
        for (Patient patient : monitoredPatients) {
            patient.updateMeasurement("2093-3", null);
            sum += patient.getMeasurement("2093-3").getValue();
        }
        
        double average = sum / monitoredPatients.size();

        for (Patient patient : monitoredPatients) {
            patient.setHasWarning(patient.getMeasurement("2093-3").getValue() > average);
        }
    }
}

