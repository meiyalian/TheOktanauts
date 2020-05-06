package org.oktanauts;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.oktanauts.model.Patient;


public class PanelController implements Initializable{

//    private Practitioner user;
//
//    public PanelController(Practitioner currentUser) {
//        user = currentUser;
//    }
    @FXML
    Label idLabel;
    public ListView<Patient> patientListView;
    ObservableList<Patient> patients;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("userLogin");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        idLabel.setText(user.getId());
//        patients = FXCollections.observableArrayList(user.getPatients());
//        patientListView.setItems(patients);
//        patientListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


}