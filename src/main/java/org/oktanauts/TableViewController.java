package org.oktanauts;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.oktanauts.model.*;

import java.net.URL;
import java.util.ResourceBundle;

public class TableViewController implements Initializable {
    @FXML  private TableView monitorTable;
    private TableColumn<Patient, String> nameColumn = new TableColumn<>("Patient Name");
    private TableColumn<Patient, String> valColumn = new TableColumn<>("Val");
    private TableColumn<Patient, String> timeColumn = new TableColumn<>("Time");
    private ObservableList<Patient> monitoredPatients = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        nameColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getName()));

        valColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue()
                .getMeasurement("2093-3").toString()));

        timeColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue()
                .getMeasurement("2093-3").getTimestamp()));

        nameColumn.setMinWidth(170);
        valColumn.setMinWidth(100);
        timeColumn.setMinWidth(170);

        monitorTable.setItems(monitoredPatients);
        monitorTable.getColumns().addAll(nameColumn, valColumn, timeColumn);
        monitorTable.setPlaceholder(new Label("No patients being monitored"));
        monitorTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void addMonitoredPatient(Patient p){
        p.updateMeasurement("2093-3", null);
        monitoredPatients.add(p);
    }

    public void removeMonitoredPatient(Patient p){
        int index = 0;
        while( index < monitoredPatients.size()){
            if(monitoredPatients.get(index).equals(p)){
                break;
            }
            index ++;
        }
        monitoredPatients.remove(index);
    }

    // this method is for adding a new monitor patient
    public void update() {
        for (Patient patient : monitoredPatients) {
            patient.updateMeasurement("2093-3", null);
        }

        //loop through all measurements and highlight the one that has higher value
        //hightlightWarning();

    }


//
//    private  void hightlightWarning(){
//        //add highlight
//        monitorTable.
//
//    }

}

