package org.oktanauts;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.oktanauts.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.Iterator;

import static org.oktanauts.model.GetMeasurementService.*;


/**
 * This class is the controller class for the monitor table of the app
 */

public class TableViewController implements Initializable, GetMeasurementCallback {
    @FXML private TableView<Patient> monitorTable;
    @FXML private ListView<Observation> modifyView;
    @FXML TabPane trackingPane;
    @FXML Tab tab1;
    @FXML Tab tab2;

    private TableColumn<Patient, String> nameCol = new TableColumn<>("Patient Name");
    private TableColumn<Patient, String> cholCol = new TableColumn<>("Total\nCholesterol");
    private TableColumn<Patient, String> cholTimeCol = new TableColumn<>("Time");
    private TableColumn<Patient, String> bpSystolicCol = new TableColumn<>("Systolic\nBlood\nPressure");
    private TableColumn<Patient, String> bpDiastolicCol = new TableColumn<>("Diastolic\nBlood\nPressure");
    private TableColumn<Patient, String> bpTimeCol = new TableColumn<>("Time");

    private ObservableList<Patient> monitoredPatients = FXCollections.observableArrayList();
    private GetMeasurementService getMeasurementService = new GetMeasurementService();
    private int x=200, y=120; // x and y value for monitoring blood pressure
    private ObservableList<Patient> highBPPatient = FXCollections.observableArrayList();

    private ArrayList<Observation> currentObservations = new ArrayList<>();
    private HashMap<String, AverageTracker> measurementAverages = new HashMap<>();
    private HashMap<Patient, ArrayList<String>> monitorManager = new HashMap<>();

    // Blueprints for observations
    private static final Observation CL_OBSERVATION = new Observation("2093-3", "TOTAL CHOLESTEROL", null);
    private static final Observation BP_OBSERVATION = new Observation("55284-4", "BLOOD PRESSURE", null);

    private GraphicalCLController graphicalCLController;
    private BPTrackingPageController bpTrackingPageController;
    private BPGraphicalController bpGraphicalController;

    /**
     * Initialises the table for the view
     *
     * @param location the url location
     * @param resources the resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentObservations.add(CL_OBSERVATION);
        currentObservations.add(BP_OBSERVATION);
        measurementAverages.put(CHOLESTEROL_LEVEL, new AverageTracker(CHOLESTEROL_LEVEL, CHOLESTEROL_LEVEL));

        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(App.class.getResource("/org/oktanauts/bpTrackingPage.fxml"));
            AnchorPane anch1 = loader.load();
            bpTrackingPageController = loader.getController();
            bpTrackingPageController.initData(highBPPatient);
            tab1.setContent(anch1);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        loader = new FXMLLoader();
        try {
            loader.setLocation(App.class.getResource("/org/oktanauts/bpGraphPage.fxml"));
            AnchorPane anch2 =  loader.load();
            bpGraphicalController = loader.getController();
            bpGraphicalController.initData(bpTrackingPageController.getTrackingPatients());
            tab2.setContent(anch2);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        nameCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getName()));

        cholCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(CHOLESTEROL_LEVEL) ?
                        (p.getValue().hasMeasurement(CHOLESTEROL_LEVEL, CHOLESTEROL_LEVEL) ?
                                (p.getValue().getObservation(CHOLESTEROL_LEVEL).getMeasurement(CHOLESTEROL_LEVEL)
                                        .toString()) : "N/A") : "-"));

        cholCol.setCellFactory(new Callback<>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Patient, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if (!item.equals("-") && !item.equals("N/A")) {
                                if (Double.parseDouble(item.split(" ")[0]) > measurementAverages
                                        .get(CHOLESTEROL_LEVEL).getAverage()) {
                                    this.setTextFill(Color.RED);
                                }
                            }
                        }
                        this.setText(item);
                    }
                };
            }
        });

        cholTimeCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(CHOLESTEROL_LEVEL) ?
                        (p.getValue().hasObservation(CHOLESTEROL_LEVEL) ?
                                p.getValue().getObservation(CHOLESTEROL_LEVEL).getTimestamp() : "N/A") : "-"));

        bpSystolicCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(BLOOD_PRESSURE) ?
                        (p.getValue().hasMeasurement(BLOOD_PRESSURE, SYSTOLIC_BLOOD_PRESSURE) ?
                                (p.getValue().getObservation(BLOOD_PRESSURE).getMeasurement(SYSTOLIC_BLOOD_PRESSURE)
                                        .toString()) : "N/A") : "-"));

        bpSystolicCol.setCellFactory(new Callback<>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Patient, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if (!item.equals("-") && !item.equals("N/A")) {
                                if (Double.parseDouble(item.split(" ")[0]) > x) {
                                    this.setTextFill(Color.DARKVIOLET);
                                }
                            }
                        }
                        this.setText(item);
                    }
                };
            }
        });

        bpDiastolicCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(BLOOD_PRESSURE) ?
                        (p.getValue().hasMeasurement(BLOOD_PRESSURE, DIASTOLIC_BLOOD_PRESSURE) ?
                                (p.getValue().getObservation(BLOOD_PRESSURE).getMeasurement(DIASTOLIC_BLOOD_PRESSURE)
                                        .toString()) : "N/A") : "-"));

        bpDiastolicCol.setCellFactory(new Callback<>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Patient, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if (!item.equals("-") && !item.equals("N/A")) {
                                if (Double.parseDouble(item.split(" ")[0]) > y) {
                                    this.setTextFill(Color.DARKVIOLET);
                                }
                            }
                        }
                        this.setText(item);
                    }
                };
            }
        });

        bpTimeCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(BLOOD_PRESSURE) ?
                        (p.getValue().hasObservation(BLOOD_PRESSURE) ?
                                p.getValue().getObservation(BLOOD_PRESSURE).getTimestamp() : "N/A") : "-"));

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

        bpTimeCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(BLOOD_PRESSURE) ?
                        (p.getValue().hasObservation(BLOOD_PRESSURE) ?
                                p.getValue().getObservation(BLOOD_PRESSURE).getTimestamp() : "N/A") : "-"));

        // set monitored measurements list
        monitorTable.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                modifyView.getItems().clear();

                //set listener to checkbox cell
                ObservableList<Observation> allObservations = FXCollections.observableArrayList(currentObservations);

                allObservations.forEach(observation -> {
                    observation.setSelected(monitorManager.get(monitorTable.getSelectionModel().getSelectedItem())
                            .contains(observation.getCode()));

                    observation.selectedProperty().addListener((observableValue, wasSelected, isSelected) -> {
                        if (isSelected) {
                            monitorManager.get(monitorTable.getSelectionModel().getSelectedItem()).add(observation.getCode());

                        }
                        else if (wasSelected) {
                            monitorManager.get(monitorTable.getSelectionModel().getSelectedItem()).remove(observation.getCode());
                        }

                        updateView();
                    });
                });


                modifyView.getItems().addAll(allObservations);

                // initialize the list view
                modifyView.setCellFactory(CheckBoxListCell.forListView(Observation::selectedProperty, new StringConverter<Observation>() {
                    @Override
                    public String toString(Observation observation) {
                        return observation.toString();
                    }

                    @Override
                    public Observation fromString(String s) {
                        return null;
                    }
                }));
            }
        });
    }

    /**
     * Updates the monitored measurements of the patients
     */
    @FXML
    public synchronized void applyChange(){
        ObservableList<Observation> monitorItems = modifyView.getSelectionModel().getSelectedItems();
        Patient p = monitorTable.getSelectionModel().getSelectedItem();
        ArrayList<String> monitoring = monitorManager.get(p);
        monitoring.clear();
        for (Observation item : monitorItems){
            monitoring.add(item.getCode());
            getMeasurementService.updatePatientMeasurement(p, item.getCode(), this);
        }

        updateView();
    }

    /**
     * Loads up the cholesterol level graph window
     *
     * @throws IOException
     */
    @FXML
    public synchronized void CLGraphWindow() throws IOException {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        for (Map.Entry<Patient, ArrayList<String>> entry: monitorManager.entrySet()){
            Patient p = entry.getKey();
            ArrayList<String> list = entry.getValue();
            for(String val : list) {
                if (val.equals(CHOLESTEROL_LEVEL)) {
                    patients.add(p);
                }
            }
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("/org/oktanauts/graphicalCholesterol.fxml"));
        Parent root = loader.load();
        graphicalCLController = loader.getController();
        graphicalCLController.initData(patients);
        Scene graphPage = new Scene(root);
        Stage newWindow = new Stage();
        newWindow.setScene(graphPage);
        newWindow.setResizable(false);
        newWindow.show();
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
     * @param patient the patient to be monitored
     */
    public synchronized void addMonitoredPatient(Patient patient) {
//        monitoredPatients.add(p);
//        getMeasurementService.updatePatientMeasurement(p, CHOLESTEROL_LEVEL, this, null);
//        getMeasurementService.updatePatientMeasurement(p, BLOOD_PRESSURE, this, null);

        ArrayList<String> monitorItems = new ArrayList<>();
        monitorItems.add(CHOLESTEROL_LEVEL);
        monitorItems.add(BLOOD_PRESSURE);

        monitoredPatients.add(patient);
        monitorManager.put(patient, monitorItems);

        for (String observation : monitorItems) {
            getMeasurementService.updatePatientMeasurement(patient, observation, null);
        }

        // if the graph view is opened, update the graph view
        if (graphicalCLController != null){
            graphicalCLController.getMonitorList().add(patient);
        }

        this.updateView();
    }

    /**
     * Removes a patient from being monitored
     *
     * @param patient the patient to be removed
     */
    public synchronized void removeMonitoredPatient(Patient patient) {
        if (monitoredPatients.size() > 0) {
            monitoredPatients.remove(patient);
            highBPPatient.remove(patient);
        }

        monitorManager.remove(patient);
        if(graphicalCLController != null){
            graphicalCLController.getMonitorList().remove(patient);
        }

        updateView();
    }

    /**
     * Refreshes the monitored measurements of all of the patients currently being monitored
     */
    public synchronized void refreshMeasurementsData() {
        for (Map.Entry<Patient, ArrayList<String>> entry : monitorManager.entrySet()) {
            Patient p = entry.getKey();
            ArrayList<String> monitorItems = entry.getValue();
            Iterator<String> iterator = monitorItems.iterator();

            while (iterator.hasNext()) {
                getMeasurementService.updatePatientMeasurement(p, iterator.next(), this);
            }
        }
    }

    /**
     * set x and y value for bloodpressure
     *
     * @param x,y the new x and y value
     */
    public void setXYVal(int x, int y){
        this.x = x;
        this.y = y;
        
        updateView();

    }

    /**
     * Updates the view
     */
    @Override
    public void updateView() {
        monitorTable.refresh();
        Platform.runLater(this::updateHighBPPatient);
        updateHighlight();

        if(graphicalCLController != null){ // update graph view
            Platform.runLater(() -> graphicalCLController.updateView());

        }
    }

    /**
     * Updates the list of patients with high blood pressure
     */
    private void updateHighBPPatient(){
        highBPPatient.clear();
        for (Map.Entry<Patient, ArrayList<String>> entry: monitorManager.entrySet()) {
            Patient patient = entry.getKey();
            ArrayList<String> monitorItems = entry.getValue();
            if (monitorItems.contains((BLOOD_PRESSURE))){
                if (patient.hasMeasurement(BLOOD_PRESSURE,SYSTOLIC_BLOOD_PRESSURE)){
                    Measurement sysMeasurement = patient.getObservation(BLOOD_PRESSURE).getMeasurement(SYSTOLIC_BLOOD_PRESSURE);
                    Measurement diaMeasurement = patient.getObservation(BLOOD_PRESSURE).getMeasurement(DIASTOLIC_BLOOD_PRESSURE);
                    if(sysMeasurement.getValue() > x || diaMeasurement.getValue() > y){
                        highBPPatient.add(patient);
                    }
                }
            }
        }
        bpTrackingPageController.updateView();
        bpGraphicalController.updateView();
    }

    /**
     * Calculates the average measurement of the monitored patients and highlights any patients that are above it
     */
    public void updateHighlight() {
        for (String measurement: measurementAverages.keySet()) {
            measurementAverages.get(measurement).reset();
        }

        for (Map.Entry<Patient, ArrayList<String>> entry: monitorManager.entrySet()) {
            Patient patient = entry.getKey();

            for (String measurement: measurementAverages.keySet()) {
                String observationCode = measurementAverages.get(measurement).getObservationCode();
                if (entry.getValue().contains(measurement) && patient.hasMeasurement(observationCode, measurement)) {
                    measurementAverages.get(measurement).add(patient.getObservation(observationCode)
                            .getMeasurement(measurement).getValue());
                }
            }
        }
    }
}

