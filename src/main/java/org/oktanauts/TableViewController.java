package org.oktanauts;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.oktanauts.model.*;
import java.net.URL;
import java.util.*;

/**
 * This class is the controller class for the monitor table of the app
 */

public class TableViewController implements Initializable, GetMeasurementCallback {
    @FXML private TableView<Patient> monitorTable;
    @FXML private ListView<String> selectView;
    @FXML private ListView<String> modifyView;
    @FXML private Label patientToAdd;
    @FXML private Button addToTable;

    private TableColumn<Patient, String> nameCol = new TableColumn<>("Patient Name");
    private TableColumn<Patient, String> cholCol = new TableColumn<>("Total\nCholesterol");
    private TableColumn<Patient, String> cholTimeCol = new TableColumn<>("Time");
    private TableColumn<Patient, String> bpDiastolicCol = new TableColumn<>("Diastolic\nBlood\nPressure");
    private TableColumn<Patient, String> bpSystolicCol = new TableColumn<>("Systolic\nBlood\nPressure");
    private TableColumn<Patient, String> bpTimeCol = new TableColumn<>("Time");

    private ObservableList<Patient> monitoredPatients = FXCollections.observableArrayList();
    private GetMeasurementService getMeasurementService = new GetMeasurementService();
    private double averageCholesterol = 0;

    private ArrayList<Patient> patientQueue = new ArrayList<>();
    private ArrayList<String> selectableObservations = new ArrayList<>();
    private HashMap<Patient, ArrayList<String>> monitorManager = new HashMap<>();

    private static String CHOLESTEROL_LEVEL = "2093-3";

    private static String BLOOD_PRESSURE = "55284-4";
    private static String DIASTOLIC_BLOOD_PRESSURE = "8462-4";
    private static String SYSTOLIC_BLOOD_PRESSURE = "8480-6";


    /**
     * Initialises the table for the view
     *
     * @param location the url location
     * @param resources the resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        updateView();

        selectableObservations.add(CHOLESTEROL_LEVEL);
        selectableObservations.add(BLOOD_PRESSURE);

        selectView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        nameCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getName()));

        cholCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(CHOLESTEROL_LEVEL) ?
                        (p.getValue().hasMeasurement(CHOLESTEROL_LEVEL, CHOLESTEROL_LEVEL) ?
                                (p.getValue().getObservation(CHOLESTEROL_LEVEL).getMeasurement(CHOLESTEROL_LEVEL)
                                        .toString()) : "-") : "-"));

        cholTimeCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(CHOLESTEROL_LEVEL) ?
                        (p.getValue().hasObservation(CHOLESTEROL_LEVEL) ?
                                p.getValue().getObservation(CHOLESTEROL_LEVEL).getTimestamp() : "-") : "-"));

        bpDiastolicCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(BLOOD_PRESSURE) ?
                        (p.getValue().hasMeasurement(BLOOD_PRESSURE, DIASTOLIC_BLOOD_PRESSURE) ?
                                (p.getValue().getObservation(BLOOD_PRESSURE).getMeasurement(DIASTOLIC_BLOOD_PRESSURE)
                                        .toString()) : "-") : "-"));

        bpSystolicCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(BLOOD_PRESSURE) ?
                        (p.getValue().hasMeasurement(BLOOD_PRESSURE, SYSTOLIC_BLOOD_PRESSURE) ?
                                (p.getValue().getObservation(BLOOD_PRESSURE).getMeasurement(SYSTOLIC_BLOOD_PRESSURE)
                                        .toString()) : "-") : "-"));

        bpTimeCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(BLOOD_PRESSURE) ?
                        (p.getValue().hasObservation(BLOOD_PRESSURE) ?
                                p.getValue().getObservation(BLOOD_PRESSURE).getTimestamp() : "-") : "-"));


        // Needs to be updated to highlight cells instead of rows
//        monitorTable.setRowFactory(row -> new TableRow<>() {
//            @Override
//            public void updateItem(Patient item, boolean empty) {
//                super.updateItem(item, empty);
//                if (item == null || empty || item.getMeasurement(Measurement.MeasurementType.CHOLESTEROL_LEVEL) == null) {
//                    setStyle("");
//                } else if (monitorManager.get(item).contains(GetMeasurementService.measurementName.CHOLESTEROL_LEVEL) && item.getMeasurement(Measurement.MeasurementType.CHOLESTEROL_LEVEL).getValue() > averageCholesterol) {
//                    setStyle("-fx-background-color: #F08888;");
//                }
//            }
//        });

        nameCol.setMinWidth(170);
        cholCol.setMinWidth(100);
        cholTimeCol.setMinWidth(170);
        bpSystolicCol.setMinWidth(100);
        bpDiastolicCol.setMinWidth(100);
        bpTimeCol.setMinWidth(170);

        monitorTable.setItems(monitoredPatients);
        monitorTable.getColumns().addAll(nameCol, cholCol, cholTimeCol, bpSystolicCol, bpDiastolicCol, bpTimeCol);
        monitorTable.setPlaceholder(new Label("No patients being monitored"));
        monitorTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        monitorTable.setOnMousePressed(e -> {
            if(e.isPrimaryButtonDown()) {
                modifyView.getItems().clear();
                modifyView.getItems().addAll(selectableObservations);
                ArrayList<String> monitorItems = monitorManager.get(monitorTable.getSelectionModel().getSelectedItem());
                for (String item : monitorItems) {
                    modifyView.getSelectionModel().select(item);
                }
            }
        });


    }

    @FXML
    public synchronized void applyChange(){
        ObservableList<String> monitorItems = modifyView.getSelectionModel().getSelectedItems();
        Patient p = monitorTable.getSelectionModel().getSelectedItem();
        ArrayList<String> monitoring = monitorManager.get(p);
        monitoring.clear();
        for (String item : monitorItems){
            monitoring.add(item);
            getMeasurementService.updatePatientMeasurement(p, item, this, null);
        }
        System.out.println("apply ");
        System.out.println(monitorManager.get(p));

        updateView();
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
    public synchronized void addMonitoredPatient(Patient p) {
//        monitoredPatients.add(p);
//        getMeasurementService.updateMonitoredPatientMeasurement(p, GetMeasurementService.measurementName.CHOLESTEROL_LEVEL, this, null);
          patientQueue.add(0, p);
          if (patientQueue.size() == 1) {
              patientToAdd.setText(p.getName());
              initializingSelectMenu();
          }
    }

    private void initializingSelectMenu() {
        for (String observation : selectableObservations) {
            selectView.getItems().add(observation);
        }
    }

    @FXML
    public synchronized void addToTable() {
        if (patientQueue.size() > 0) {
            Patient patient = patientQueue.get(patientQueue.size() - 1);
            ObservableList<String> monitorItems = selectView.getSelectionModel().getSelectedItems();

            monitoredPatients.add(patient);
            monitorManager.put(patient, new ArrayList<>());
            for (String observation : monitorItems) {
                // breaks
                getMeasurementService.updatePatientMeasurement(patient, observation, this, null);
                monitorManager.get(patient).add(observation);
            }
            updateView();
            patientQueue.remove(patientQueue.size() - 1);

            selectView.getSelectionModel().clearSelection();
            if (patientQueue.size() == 0) {
                selectView.getItems().clear();
                patientToAdd.setText("");
            }
            else {
                patientToAdd.setText(patientQueue.get(patientQueue.size() - 1).getName());
            }
        }
    }


    /**
     * Removes a patient from being monitored
     *
     * @param p the patient to be removed
     */
    public synchronized void removeMonitoredPatient(Patient patient) {
//        int index = 0;
//        boolean isFound = false;
//        while (index < monitoredPatients.size()){
//            if (monitoredPatients.get(index).equals(p)){
//                isFound = true;
//                break;
//            }
//            index ++;
//        }
//        if (isFound) {
//            monitoredPatients.remove(index);
//            updateView();
//        }
        if (patientQueue.size() > 0) {
            patientQueue.remove(patient);
            selectView.getSelectionModel().clearSelection();
            if (patientQueue.size() == 0) {
                selectView.getItems().clear();
                patientToAdd.setText("");
            }
            else {
                patientToAdd.setText(patientQueue.get(patientQueue.size() - 1).getName());
            }
        }
        if (monitoredPatients.size() > 0 ) {
            monitoredPatients.remove(patient);
        }

        monitorManager.remove(patient);

        updateView();
    }

    /**
     * Refreshes the monitored measurements of all of the patients currently being monitored
     */
    public synchronized void refreshMeasurementsData() {
        for (Map.Entry<Patient, ArrayList<String>> entry : monitorManager.entrySet()) {
            Patient p = entry.getKey();
            ArrayList<String> monitorItems = entry.getValue();
            for (String item : monitorItems){
                getMeasurementService.updatePatientMeasurement(p, item, this ,null);
            }
//        ArrayList<Patient> pList = entry.getValue();
//        GetMeasurementService.measurementName key = entry.getKey();
//        for (Patient p: pList) {
//            getMeasurementService.updateMonitoredPatientMeasurement(p, key, this ,null);
//        }
        }

    }

    /**
     * Updates the warning highlighting of the table
     */
    @Override
    public void updateView() {
        updateHighlight();
    }

    /**
     * Calculates the average measurement of the monitored patients and highlights any patients that are above it
     */
    public void updateHighlight() {
        double sum = 0.0;
        int count = 0;
        for (Map.Entry<Patient, ArrayList<String>> entry: monitorManager.entrySet()) {
            Patient patient = entry.getKey();
            ArrayList<String> monitorItems = entry.getValue();
            if (monitorItems.contains(CHOLESTEROL_LEVEL)) {
                Measurement measurement = patient.getObservation(CHOLESTEROL_LEVEL).getMeasurement(CHOLESTEROL_LEVEL);
                if (measurement != null) {
                    sum += measurement.getValue();
                    count += 1;
                }
            }
        }
//        for (Patient patient : monitorManager.get(GetMeasurementService.measurementName.CHOLESTEROL_LEVEL)) {
//            System.out.println("monitored cholesterol level patient name :" + patient.getName());
//            Measurement m = patient.getMeasurement(Measurement.MeasurementType.CHOLESTEROL_LEVEL);
//            if (m != null){
//                sum += m.getValue();
//                count += 1;
//            }
//        }

        double average = 0.0;
        if (count > 0) {
            average = sum / count;
        }

        System.out.println("count: "+ count);
        System.out.println("average value: " + average);

        this.averageCholesterol = average;
    }
}

