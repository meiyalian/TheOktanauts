package org.oktanauts;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import org.oktanauts.model.Patient;

public class BPTrackingPageController {
    @FXML
    ListView patientList;

    private ObservableList<Patient> highBPPatient;


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
}
