package org.oktanauts;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import org.oktanauts.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainPanelController implements Initializable {
    private Practitioner practitioner;
    private TableViewController tableViewController;
//    private ObservableList<Patient> monitoredPatient = FXCollections.observableArrayList();


    @FXML
    Label IDdisplay;
    @FXML
    ListView patientListView;
    @FXML
    BorderPane tablePane;


    // initializing process after the practitioner is created
    public void initData(Practitioner practitioner) {
        this.practitioner = practitioner;
        IDdisplay.setText("PractitionerID: " + practitioner.getId());

        ObservableList<Patient> allPatients = FXCollections.observableArrayList(practitioner.getPatients());
        allPatients.forEach(patient -> patient.selectedProperty().addListener((observableValue, wasSelected, isSelected) -> {
            if (isSelected) {
                // update table
//                tableViewController.getMonitoredPatient().add(patient);
//                monitoredPatient.add(patient);
                tableViewController.addMonitoredPatient(patient);

            }
            if (wasSelected && !isSelected) {
//                tableViewController.getMonitoredPatient().remove(patient);
//                monitoredPatient.remove(patient);
                tableViewController.removeMonitoredPatient(patient);
            }

        }));

//        Iterator iter = patients.getIterator();
//        while (iter.hasNext()){
//            Patient p = (Patient) iter.next();
//            allPatients.add(p);
////            patientListView.getItems().add(p);
//        }

        // initialize the list view

        patientListView.getItems().addAll(allPatients);
        patientListView.setCellFactory(CheckBoxListCell.forListView(Patient::selectedProperty, new StringConverter<Patient>() {
            @Override
            public String toString(Patient patient) {
                return patient.getName();
            }

            @Override
            public Patient fromString(String s) {
                return null;
            }
        }));

    }

    // initialize views in the panel
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //load table view
        FXMLLoader tableLoader = new FXMLLoader();
        tableLoader.setLocation(App.class.getResource("/org/oktanauts/tableView.fxml"));
        Pane view = null;
        try {
            view = tableLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        tableLoader.setLocation(App.class.getResource("/org/oktanauts/tableView.fxml"));
        tableViewController = tableLoader.getController();
        tablePane.setCenter(view);


        // add listener to monitored patient list, whenever a new patient is added, notify table view controller
//        monitoredPatient.addListener(new ListChangeListener<Patient>() {
//            @Override
//            public void onChanged(Change<? extends Patient> change) {
//                while (change.next()) {
//                    if (change.wasAdded()) {
//                        tableViewController.addMonitoredPatient(change.getAddedSubList().get(0));
//                    }
//                    else if (change.wasRemoved()) {
//                        tableViewController.removeMonitoredPatient(change.getRemoved().get(0));
//                    }
//                }
//            }
//        });
    }

}
