package org.oktanauts;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import org.oktanauts.model.GetMeasurementCallback;
import org.oktanauts.model.GetMeasurementService;
import org.oktanauts.model.ObservationTracker;
import org.oktanauts.model.Patient;


import java.util.HashMap;

import static org.oktanauts.model.GetMeasurementService.BLOOD_PRESSURE;
import static org.oktanauts.model.GetMeasurementService.SYSTOLIC_BLOOD_PRESSURE;

public class BPTrackingPageController implements GetMeasurementCallback {
    @FXML ListView patientList;
    @FXML ListView historyView;

    private ObservableList<Patient> highBPPatient; //patients that can be tracked
    private ObservableList<Patient> patientsCanAdd = FXCollections.observableArrayList(); // patients that haven't been tracked
    private ObservableList<Patient> trackingPatients=FXCollections.observableArrayList();
    private GetMeasurementService getMeasurementService= new GetMeasurementService();


    public void initData(ObservableList<Patient> patients) {

        this.highBPPatient = patients;
        patientsCanAdd.addAll(highBPPatient);
        System.out.println("patients can add: "+ patientsCanAdd);
        patientList.setItems(patientsCanAdd);
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
                    System.out.println("add" + p.getName());
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

    }

    @FXML
    private synchronized void add(ActionEvent e)  {

        ObservableList<Patient> chosenPatients = patientList.getSelectionModel().getSelectedItems();
        for (Patient p: chosenPatients) {
            p.getObservationTracker(BLOOD_PRESSURE).setMaxNumberOfRecords(5);
            if(!trackingPatients.contains(p)){
                trackingPatients.add(p);
                getMeasurementService.updatePatientMeasurement(p,BLOOD_PRESSURE,this);

            }

        }
        patientsCanAdd.removeAll(chosenPatients);
    }

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

    @Override
    public void updateView() {
        historyView.setItems(null);
        historyView.setItems(trackingPatients);

    }
}
