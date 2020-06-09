package org.oktanauts;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import org.oktanauts.model.GetMeasurementService;
import org.oktanauts.model.Patient;

import java.io.IOException;

import static org.oktanauts.model.GetMeasurementService.BLOOD_PRESSURE;

public class BPTrackingPageController {
    @FXML ListView patientList;
    @FXML ListView historyView;

    private ObservableList<Patient> highBPPatient;
    private GetMeasurementService getMeasurementService= new GetMeasurementService();


    public void initData(ObservableList<Patient> patients) {
        this.highBPPatient = patients;
        patientList.setItems(highBPPatient);
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

    }

    @FXML
    private void add(ActionEvent e)  {
        ObservableList<Patient> chosenPatients = patientList.getSelectionModel().getSelectedItems();
        for (Patient p: chosenPatients) {
            getMeasurementService.getNewObservationTracker(p,BLOOD_PRESSURE, 5);

        }
    }

}
