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
    @FXML private ListView<GetMeasurementService.measurementName> selectView;
    @FXML private ListView<GetMeasurementService.measurementName> modifyView;
    @FXML private Label patientToAdd;
    @FXML private Button addToTable;

    private TableColumn<Patient, String> nameColumn = new TableColumn<>("Patient Name");
    private TableColumn<Patient, String> valColumn = new TableColumn<>("Total\nCholesterol");
    private TableColumn<Patient, String> CLtimeColumn = new TableColumn<>("Time");
    private TableColumn<Patient, String> systolicBP = new TableColumn<>("Systolic\nBlood\nPressure");
    private TableColumn<Patient, String> diastolicBP = new TableColumn<>("Diastolic\nBlood\nPressure");
    private TableColumn<Patient, String> BPtimeColumn = new TableColumn<>("Time");
    private ObservableList<Patient> monitoredPatients = FXCollections.observableArrayList();
    private GetMeasurementService getMeasurementService = new GetMeasurementService();
    private double averageCholesterol = 0;

    private ArrayList<Patient> patientQueue = new ArrayList<>();
    private ArrayList<GetMeasurementService.measurementName> selectableMeasurements = new ArrayList<>();
    private HashMap<Patient, ArrayList<GetMeasurementService.measurementName>> monitorManager = new HashMap<>();


    /**
     * Initialises the table for the view
     *
     * @param location the url location
     * @param resources the resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        updateView();

        selectableMeasurements.add(GetMeasurementService.measurementName.CHOLESTEROL_LEVEL);
        selectableMeasurements.add(GetMeasurementService.measurementName.BLOOD_PRESSURE);
        for (GetMeasurementService.measurementName m: selectableMeasurements) {

        }
        selectView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        nameColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getName()));

        valColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(GetMeasurementService.measurementName.CHOLESTEROL_LEVEL)?
                        (p.getValue().getMeasurement(Measurement.MeasurementType.CHOLESTEROL_LEVEL)!= null? p.getValue().getMeasurement(Measurement.MeasurementType.CHOLESTEROL_LEVEL).toString(): "")
                        : ""));

        CLtimeColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(GetMeasurementService.measurementName.CHOLESTEROL_LEVEL)?
                        (p.getValue().getMeasurement(Measurement.MeasurementType.CHOLESTEROL_LEVEL)!= null? p.getValue().getMeasurement(Measurement.MeasurementType.CHOLESTEROL_LEVEL).getTimestamp():""):
                        ""));


        monitorTable.setRowFactory(row -> new TableRow<>() {
            @Override
            public void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty || item.getMeasurement(Measurement.MeasurementType.CHOLESTEROL_LEVEL) == null) {
                    setStyle("");
                } else if (monitorManager.get(item).contains(GetMeasurementService.measurementName.CHOLESTEROL_LEVEL) && item.getMeasurement(Measurement.MeasurementType.CHOLESTEROL_LEVEL).getValue() > averageCholesterol) {
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
        monitorTable.setOnMousePressed(e->{
            if(e.isPrimaryButtonDown()){
                modifyView.getItems().clear();
                modifyView.getItems().addAll(selectableMeasurements);
                ArrayList<GetMeasurementService.measurementName> monitorItems = monitorManager.get(monitorTable.getSelectionModel().getSelectedItem());
                for (GetMeasurementService.measurementName item: monitorItems) {
                    modifyView.getSelectionModel().select(item);
                }
            }
        });


    }

    @FXML
    public synchronized void applyChange(){
        ObservableList<GetMeasurementService.measurementName> monitorItems = modifyView.getSelectionModel().getSelectedItems();
        Patient p = monitorTable.getSelectionModel().getSelectedItem();
        ArrayList<GetMeasurementService.measurementName> monitoring = monitorManager.get(p);
        monitoring.clear();
        for (GetMeasurementService.measurementName item : monitorItems){
            monitoring.add(item);
            getMeasurementService.updateMonitoredPatientMeasurement(p, item, this, null);
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
    public synchronized void addMonitoredPatient(Patient p){
//        monitoredPatients.add(p);
//        getMeasurementService.updateMonitoredPatientMeasurement(p, GetMeasurementService.measurementName.CHOLESTEROL_LEVEL, this, null);
          patientQueue.add(0,p);
          if(patientQueue.size() ==1){
              patientToAdd.setText(p.getName());
              initializingSelectMenu();
          }
    }

    private void initializingSelectMenu(){
        for (GetMeasurementService.measurementName measurement: selectableMeasurements) {
            selectView.getItems().add(measurement);
        }
    }

    @FXML
    public synchronized void addToTable(){
        if(patientQueue.size() > 0){
            Patient p = patientQueue.get(patientQueue.size()-1);
            ObservableList<GetMeasurementService.measurementName> monitorItems = selectView.getSelectionModel().getSelectedItems();

            monitoredPatients.add(p);
            monitorManager.put(p,new ArrayList<>());
            for (GetMeasurementService.measurementName m:monitorItems) {
                getMeasurementService.updateMonitoredPatientMeasurement(p, m, this, null);
                monitorManager.get(p).add(m);
            }
            updateView();
            patientQueue.remove(patientQueue.size()-1);

            selectView.getSelectionModel().clearSelection();
            if (patientQueue.size() == 0){
                selectView.getItems().clear();
                patientToAdd.setText("");
            }
            else {
                patientToAdd.setText(patientQueue.get(patientQueue.size()-1).getName());
            }
        }
    }


    /**
     * Removes a patient from being monitored
     *
     * @param p the patient to be removed
     */
    public synchronized void removeMonitoredPatient(Patient p){
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
        if (patientQueue.size()>0){
            patientQueue.remove(p);
            selectView.getSelectionModel().clearSelection();
            if (patientQueue.size() ==0){
                selectView.getItems().clear();
                patientToAdd.setText("");
            }
            else{
                patientToAdd.setText(patientQueue.get(patientQueue.size()-1).getName());
            }
        }
        if (monitoredPatients.size() >0){
            monitoredPatients.remove(p);
        }

        monitorManager.remove(p);

        updateView();
    }




    /**
     * Refreshes the monitored measurements of all of the patients currently being monitored
     */
    public synchronized void refreshMeasurementsData()  {

        for (Map.Entry<Patient, ArrayList<GetMeasurementService.measurementName>> entry: monitorManager.entrySet()){
            Patient p = entry.getKey();
            ArrayList<GetMeasurementService.measurementName> monitorItems = entry.getValue();
            for (GetMeasurementService.measurementName item: monitorItems){
                getMeasurementService.updateMonitoredPatientMeasurement(p, item, this ,null);
            }
//        ArrayList<Patient> pList = entry.getValue();
//        GetMeasurementService.measurementName key = entry.getKey();
//        for (Patient p: pList) {
//            getMeasurementService.updateMonitoredPatientMeasurement(p, key, this ,null);
//        }
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
        for (Map.Entry<Patient, ArrayList<GetMeasurementService.measurementName>> entry: monitorManager.entrySet()){
            Patient p = entry.getKey();
            ArrayList<GetMeasurementService.measurementName> monitorItems = entry.getValue();
            if (monitorItems.contains(GetMeasurementService.measurementName.CHOLESTEROL_LEVEL)){
                Measurement m = p.getMeasurement(Measurement.MeasurementType.CHOLESTEROL_LEVEL);
                if (m != null){
                sum += m.getValue();
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
        if (count > 0){
            average = sum / count;
        }

        System.out.println("count: "+ count);
        System.out.println("average value: " + average);

        this.averageCholesterol = average;
    }
}

