package org.oktanauts;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import org.oktanauts.model.GetMeasurementCallback;
import org.oktanauts.model.GetMeasurementService;
import org.oktanauts.model.Patient;


import static org.oktanauts.model.GetMeasurementService.BLOOD_PRESSURE;
import static org.oktanauts.model.GetMeasurementService.SYSTOLIC_BLOOD_PRESSURE;

/**
 * This class is the controller class for tracking patients with high blood pressure
 */
public class BPTrackingPageController implements GetMeasurementCallback {
    @FXML ListView patientList;
    @FXML ListView historyView;

    private ObservableList<Patient> highBPPatient; //patients that can be tracked
    private ObservableList<Patient> patientsCanAdd = FXCollections.observableArrayList(); // patients that haven't been tracked
    private ObservableList<Patient> trackingPatients=FXCollections.observableArrayList();
    private GetMeasurementService getMeasurementService= new GetMeasurementService();


    /**
     * Initialises the data for the blood pressure tracking page
     * @param patients
     */
    public void initData(ObservableList<Patient> patients) {
        this.highBPPatient = patients;
        patientsCanAdd.addAll(highBPPatient);
        patientList.setItems(patientsCanAdd);


        historyView.setCellFactory(cell -> new ListCell<Patient>() {
            @Override
            protected void updateItem(Patient p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setText(null);
                } else {
                    setText(p.getObservationTracker(BLOOD_PRESSURE).display(SYSTOLIC_BLOOD_PRESSURE));
                }
            }
        });
        historyView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        patientList.setCellFactory(cell -> new ListCell<Patient>() {
            @Override
            protected void updateItem(Patient p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setText(null);
                } else {
                    setText(p.getName());
                }
            }
        });
        patientList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        highBPPatient.addListener((ListChangeListener<Patient>) change ->{
            while (change.next()){
                if(change.wasAdded()){
                    Patient p = change.getAddedSubList().get(0);
                    if(!patientsCanAdd.contains(p) && !trackingPatients.contains(p)){
                        patientsCanAdd.add(p);
                    }
                }else if (change.wasRemoved()){
                    Patient p = change.getRemoved().get(0);
                    patientsCanAdd.remove(p);
                }
            }
        });

        historyView.setItems(trackingPatients);


    }

    /**
     * Adds a patient to be tracked
     *
     * @param e the action event of the add
     */
    @FXML
    private synchronized void add(ActionEvent e)  {
        ObservableList<Patient> chosenPatients = patientList.getSelectionModel().getSelectedItems();
        for (Patient p: chosenPatients) {
            p.getObservationTracker(BLOOD_PRESSURE).setMaxNumberOfRecords(5);
            if(!trackingPatients.contains(p)){
                getMeasurementService.updatePatientMeasurement(p,BLOOD_PRESSURE,this);
                trackingPatients.add(p);
            }
        }

        patientsCanAdd.removeAll(chosenPatients);
    }

    /**
     * Removes a patient from being tracked
     *
     * @param e the action event of the removal
     */
    @FXML
    private synchronized void remove(ActionEvent e)  {
        ObservableList<Patient> chosenPatients = historyView.getSelectionModel().getSelectedItems();

        for (Patient p: chosenPatients){
            p.getObservationTracker(BLOOD_PRESSURE).setMaxNumberOfRecords(1);
            if(!patientsCanAdd.contains(p) && highBPPatient.contains(p)){
                patientsCanAdd.add(p);
            }
        }
        trackingPatients.removeAll(chosenPatients);
    }

    /**
     * Updates the view
     */
    @Override
    public void updateView() {
        historyView.setItems(null);
        historyView.setItems(trackingPatients);
    }

    /**
     * Gets an observable list of all of the currently tracked patient
     *
     * @return an observable list of patients
     */
    public ObservableList<Patient> getTrackingPatients(){
        return this.trackingPatients;
    }

}
