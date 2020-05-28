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

/**
 * This class is the controller class for the monitor table of the app
 */

public class TableViewController implements Initializable, GetMeasurementCallback {
    @FXML
    private TableView<Patient> monitorTable;
    private TableColumn<Patient, String> nameColumn = new TableColumn<>("Patient Name");
    private TableColumn<Patient, String> valColumn = new TableColumn<>("Total\nCholesterol");
    private TableColumn<Patient, String> CLtimeColumn = new TableColumn<>("Time");
    private TableColumn<Patient, String> systolicBP = new TableColumn<>("Systolic\nBlood\nPressure");
    private TableColumn<Patient, String> diastolicBP = new TableColumn<>("Diastolic\nBlood\nPressure");
    private TableColumn<Patient, String> BPtimeColumn = new TableColumn<>("Time");
    private ObservableList<Patient> monitoredPatients = FXCollections.observableArrayList();
    private GetMeasurementService getMeasurementService = new GetMeasurementService();
    private double averageCholesterol = 0;

    /**
     * Initialises the table for the view
     *
     * @param location the url location
     * @param resources the resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateView();

        nameColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getName()));

        valColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(p.getValue()
                        .getMeasurement("2093-3")!= null? p.getValue().getMeasurement("2093-3")
                        .toString(): ""));

        CLtimeColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(p.getValue()
                        .getMeasurement("2093-3")!= null? p.getValue().getMeasurement("2093-3")
                        .getTimestamp():""));

        monitorTable.setRowFactory(row -> new TableRow<>() {
            @Override
            public void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty || item.getMeasurement("2093-3") == null) {
                    setStyle("");
                } else if (item.getMeasurement("2093-3").getValue() > averageCholesterol) {
                    setStyle("-fx-background-color: #F08888;");
                }
            }
        });


        nameColumn.setMinWidth(170);
        valColumn.setMinWidth(100);
        CLtimeColumn.setMinWidth(170);
        systolicBP.setMinWidth(100);
        diastolicBP.setMinWidth(100);
        BPtimeColumn.setMinWidth(170);

        monitorTable.setItems(monitoredPatients);
        monitorTable.getColumns().addAll(nameColumn, valColumn, CLtimeColumn, systolicBP, diastolicBP, BPtimeColumn);
        monitorTable.setPlaceholder(new Label("No patients being monitored"));
        monitorTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /**
     * Gets the currently selection patient in the table
     *
     * @return the patient that is currently selected
     */
    public Patient getSelectedPatient(){
        return monitorTable.getSelectionModel().getSelectedItem();
    }

    /**
     * Adds a patient to be monitored
     *
     * @param p the patient to be monitored
     */
    public synchronized void addMonitoredPatient(Patient p){
        monitoredPatients.add(p);
        getMeasurementService.updateMonitoredPatientMeasurement(p, "2093-3", this);
    }

    /**
     * Removes a patient from being monitored
     *
     * @param p the patient to be removed
     */
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

    /**
     * Refreshes the monitored measurements of all of the patients currently being monitored
     */
    public synchronized void refreshMeasurementsData()  {
      for(Patient p: monitoredPatients){
          getMeasurementService.updateMonitoredPatientMeasurement(p, "2093-3", this);
      }
    }

    /**
     * Updates the warnings highlightings of the table
     */
    @Override
    public void updateView() {
        updateHighlight();
    }

    /**
     * Calculates the average measurement of the monitored patients and highlights any patients that are above it
     */
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

        this.averageCholesterol = average;
    }
}

