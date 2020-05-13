package org.oktanauts;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.oktanauts.model.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class TableViewController implements Initializable, GetMeasurementCallback {
    @FXML  private TableView<Patient> monitorTable;
    private TableColumn<Patient, String> nameColumn = new TableColumn<>("Patient Name");
    private TableColumn<Patient, String> valColumn = new TableColumn<>("Val");
    private TableColumn<Patient, String> timeColumn = new TableColumn<>("Time");
    private ObservableList<Patient> monitoredPatients = FXCollections.observableArrayList();
    private GetMeasurementService getMeasurementService = new GetMeasurementService();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateView();

        nameColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getName()));

        valColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(p.getValue()
                        .getMeasurement("2093-3")!= null? p.getValue().getMeasurement("2093-3")
                        .toString(): ""));

        timeColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(p.getValue()
                        .getMeasurement("2093-3")!= null? p.getValue().getMeasurement("2093-3")
                        .getTimestamp():""));

        monitorTable.setRowFactory(row -> new TableRow<Patient>(){
            @Override
            public void updateItem(Patient item, boolean empty){
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else if (item.getHasWarning()) {
                    setStyle("-fx-background-color: #F08888;");
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



    public synchronized void addMonitoredPatient(Patient p){


        monitoredPatients.add(p);
        getMeasurementService.updateMonitoredPatientMeasurement(p, "2093-3", this);


    }

    public synchronized void removeMonitoredPatient(Patient p){
        int index = 0;
        boolean isFound = false;
        while (index < monitoredPatients.size()){
            if (monitoredPatients.get(index).equals(p)){
                isFound = true;
                break;
            }
            index ++;
        }
        if (isFound) {
            monitoredPatients.remove(index);
            updateView();
        }
    }



    public synchronized void refreshMeasurementsData()  {
      for(Patient p: monitoredPatients){
          getMeasurementService.updateMonitoredPatientMeasurement(p, "2093-3", this);
      }

    }

    @Override
    public void updateView() {
        updateHighlight();
    }

    public void updateHighlight(){
        double sum = 0.0;
        int count = 0;
        for (Patient patient : monitoredPatients) {
            Measurement m = patient.getMeasurement("2093-3");
            if (m != null){
                sum += m.getValue();
                count += 1;
            }
        }

        double average = 0.0;
        if (count > 0){
            average = sum / count;
        }
        System.out.println("average value: " + average);

        for (Patient patient : monitoredPatients) {
            Measurement m = patient.getMeasurement("2093-3");
            patient.setHasWarning(m != null && m.getValue() > average);
        }
    }
}

