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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.oktanauts.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.oktanauts.model.GetMeasurementService.*;


/**
 * This class is the controller class for the monitor table of the app
 */

public class TableViewController implements Initializable, GetMeasurementCallback {
    @FXML private TableView<Patient> monitorTable;
    @FXML private ListView<String> modifyView;
    @FXML TabPane trackingPane;
    @FXML Button switchViewBtn;
    @FXML Tab tab1;
    @FXML Tab tab2;

    private TableColumn<Patient, String> nameCol = new TableColumn<>("Patient Name");
    private TableColumn<Patient, String> cholCol = new TableColumn<>("Total\nCholesterol");
    private TableColumn<Patient, String> cholTimeCol = new TableColumn<>("Time");
    private TableColumn<Patient, String> bpDiastolicCol = new TableColumn<>("Diastolic\nBlood\nPressure");
    private TableColumn<Patient, String> bpSystolicCol = new TableColumn<>("Systolic\nBlood\nPressure");
    private TableColumn<Patient, String> bpTimeCol = new TableColumn<>("Time");

    private ObservableList<Patient> monitoredPatients = FXCollections.observableArrayList();
    private GetMeasurementService getMeasurementService = new GetMeasurementService();
    private double averageCholesterol = 0;
    private int x=200,y=120; // x and y value for monitoring blood pressure
    private ObservableList<Patient> highBPPatient = FXCollections.observableArrayList();

    private ArrayList<Patient> patientQueue = new ArrayList<>();
    private ArrayList<Observation> currentObservations = new ArrayList<>();
    private HashMap<Patient, ArrayList<String>> monitorManager = new HashMap<>();

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

//        FXMLLoader tableLoader = new FXMLLoader();
//        tableLoader.setLocation(App.class.getResource("/org/oktanauts/bpTrackingPage.fxml"));
//        Pane view = null;
//        try {
//            view = tableLoader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        bpTrackingPageController = tableLoader.getController();
//        bpTrackingPageController.initData(highBPPatient);
//        trackingPane.setCenter(view);

        FXMLLoader loader = new FXMLLoader();
        try{
            loader.setLocation(App.class.getResource("/org/oktanauts/bpTrackingPage.fxml"));
            AnchorPane anch1 = loader.load();
            bpTrackingPageController = loader.getController();
            bpTrackingPageController.initData(highBPPatient);
            tab1.setContent(anch1);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("load fail");
        }
        loader = new FXMLLoader();
        try{
            loader.setLocation(App.class.getResource("/org/oktanauts/bpGraphPage.fxml"));
            AnchorPane anch2 =  loader.load();
            bpGraphicalController = loader.getController();
            bpGraphicalController.initData(bpTrackingPageController.getTrackingPatients());
            tab2.setContent(anch2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //selectView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        nameCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getName()));

        cholCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(CHOLESTEROL_LEVEL) ?
                        (p.getValue().hasMeasurement(CHOLESTEROL_LEVEL, CHOLESTEROL_LEVEL) ?
                                (p.getValue().getObservation(CHOLESTEROL_LEVEL).getMeasurement(CHOLESTEROL_LEVEL)
                                        .toString()) : "N/A") : "-"));

        cholTimeCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(CHOLESTEROL_LEVEL) ?
                        (p.getValue().hasObservation(CHOLESTEROL_LEVEL) ?
                                p.getValue().getObservation(CHOLESTEROL_LEVEL).getTimestamp() : "N/A") : "-"));

        bpDiastolicCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(BLOOD_PRESSURE) ?
                        (p.getValue().hasMeasurement(BLOOD_PRESSURE, DIASTOLIC_BLOOD_PRESSURE) ?
                                (p.getValue().getObservation(BLOOD_PRESSURE).getMeasurement(DIASTOLIC_BLOOD_PRESSURE)
                                        .toString()) : "N/A") : "-"));

        bpSystolicCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(BLOOD_PRESSURE) ?
                        (p.getValue().hasMeasurement(BLOOD_PRESSURE, SYSTOLIC_BLOOD_PRESSURE) ?
                                (p.getValue().getObservation(BLOOD_PRESSURE).getMeasurement(SYSTOLIC_BLOOD_PRESSURE)
                                        .toString()) : "N/A") : "-"));

        bpTimeCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(BLOOD_PRESSURE) ?
                        (p.getValue().hasObservation(BLOOD_PRESSURE) ?
                                p.getValue().getObservation(BLOOD_PRESSURE).getTimestamp() : "N/A") : "-"));


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

//        modifyView.setCellFactory(CheckBoxListCell.forListView(item -> {
//            return item.selectedProperty();
//        }));



        //set listener to checkbox cell
//        ObservableList<Observation> allObservations = FXCollections.observableArrayList(currentObservations);
//        allObservations.forEach(observation -> observation.selectedProperty()
//                .addListener((observableValue, wasSelected, isSelected) -> {
//            if (isSelected) {
//                monitorManager.get(monitorTable.getSelectionModel().getSelectedItem()).remove(observation.getCode());
//
//            }
//            if (wasSelected && !isSelected) {
//                monitorManager.get(monitorTable.getSelectionModel().getSelectedItem()).add(observation.getCode());
//            }
//        }));

        bpTimeCol.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(monitorManager.get(p.getValue()).contains(BLOOD_PRESSURE) ?
                        (p.getValue().hasObservation(BLOOD_PRESSURE) ?
                                p.getValue().getObservation(BLOOD_PRESSURE).getTimestamp() : "N/A") : "-"));


        // ArrayList<String> monitorItems = monitorManager.get(monitorTable.getSelectionModel().getSelectedItem());
        // set monitored measurements list
        monitorTable.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                modifyView.getItems().clear();


                //set listener to checkbox cell
                ObservableList<Observation> allObservations = FXCollections.observableArrayList(currentObservations);

                allObservations.forEach(observation -> modifyView.getItems().add(observation.getType()));

//                // initialize the list view
//                modifyView.setCellFactory(CheckBoxListCell.forListView(Observation::selectedProperty, new StringConverter<Observation>() {
//                    @Override
//                    public String toString(Observation observation) {
//                        return observation.toString();
//                    }
//
//                    @Override
//                    public Observation fromString(String s) {
//                        return null;
//                    }
//                }));
                allObservations.forEach(observation -> observation.selectedProperty()
                    .addListener((observableValue, wasSelected, isSelected) -> {
                        if (isSelected) {
                            monitorManager.get(monitorTable.getSelectionModel().getSelectedItem()).remove(observation.getCode());

                        }
                        if (wasSelected && !isSelected) {
                            monitorManager.get(monitorTable.getSelectionModel().getSelectedItem()).add(observation.getCode());
                        }

                        updateView();
                    }));
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
            System.out.println("Apply change");
            getMeasurementService.updatePatientMeasurement(p, item, this);
        }
        System.out.println("apply ");
        System.out.println(monitorManager.get(p));

        updateView();
    }


    @FXML
    public synchronized void CLGraphWindow() throws IOException {

//        ArrayList<Patient> patients = new ArrayList<>();


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

        System.out.println("number :" + patients.size());

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

        System.out.println("Add patient");
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
            System.out.println("Refresh measurement");
            for (String item : monitorItems){
                getMeasurementService.updatePatientMeasurement(p, item, this);
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
        System.out.println("update!! x: " + this.x);
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

    private void updateHighBPPatient(){
        highBPPatient.clear();
        for (Map.Entry<Patient, ArrayList<String>> entry: monitorManager.entrySet()) {
            Patient p = entry.getKey();
            ArrayList<String> monitorItems = entry.getValue();
            if (monitorItems.contains((BLOOD_PRESSURE))){
                if (p.hasMeasurement(BLOOD_PRESSURE,SYSTOLIC_BLOOD_PRESSURE)){
                    Measurement sysMeasurement = p.getObservation(BLOOD_PRESSURE).getMeasurement(SYSTOLIC_BLOOD_PRESSURE);
                    Measurement diaMeasurement = p.getObservation(BLOOD_PRESSURE).getMeasurement(DIASTOLIC_BLOOD_PRESSURE);
                    if(sysMeasurement.getValue() > x || diaMeasurement.getValue() > y){
                        highBPPatient.add(p);
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
        double sum = 0.0;
        int count = 0;
        for (Map.Entry<Patient, ArrayList<String>> entry: monitorManager.entrySet()) {
            Patient patient = entry.getKey();
            ArrayList<String> monitorItems = entry.getValue();
            if (monitorItems.contains(CHOLESTEROL_LEVEL)) {
                if (patient.hasMeasurement(CHOLESTEROL_LEVEL, CHOLESTEROL_LEVEL)) {
                    Measurement measurement = patient.getObservation(CHOLESTEROL_LEVEL).getMeasurement(CHOLESTEROL_LEVEL);
                    if (measurement != null) {
                        sum += measurement.getValue();
                        count += 1;
                    }
                }
            }
        }

        double average = 0.0;
        if (count > 0) {
            average = sum / count;
        }

        System.out.println("count: "+ count);
        System.out.println("average value: " + average);

        this.averageCholesterol = average;
    }
}

