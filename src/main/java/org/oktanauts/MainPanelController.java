package org.oktanauts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.oktanauts.model.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


/**
 * This class is the controller class for the main panel of the app
 */
public class MainPanelController implements Initializable {
    private Practitioner practitioner;
    private TableViewController tableViewController;
    private Timer refreshTimer;


    @FXML Label IDdisplay;
    @FXML ListView patientListView;
    @FXML BorderPane tablePane;
    @FXML Spinner<Integer> refreshSpinner;
    @FXML Button backToLogin;
    @FXML Button viewDetail;

    /**
     * Resets the current stage and goes back to the login view
     *
     * @param e the action event
     * @throws IOException
     */
    @FXML
    private void setBackToLogin(ActionEvent e) throws IOException {
        refreshTimer.cancel();
        App.setRoot("userLogin");
    }

    /**
     * Sets up the view containing all of the patient details
     *
     * @param e the action event
     * @throws IOException
     */
    @FXML
    private void viewPatientDetails(ActionEvent e) throws IOException {
        Patient p = tableViewController.getSelectedPatient();
        if (p!= null){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/org/oktanauts/patientDetails.fxml"));
            Parent root = loader.load();
            DetailViewController detailViewController = loader.getController();
            detailViewController.initData(p);
            Scene detailPage = new Scene(root);
            Stage newWindow = new Stage();
            newWindow.setScene(detailPage);
            newWindow.setResizable(false);
            newWindow.show();
        }
    }

    /**
     * Initialises the patient data of the practitioner for the view
     *
     * @param practitioner the practitioner whose details and patients are being displayed
     */
    // initializing process after the practitioner is created
    public void initData(Practitioner practitioner) {
        this.practitioner = practitioner;
        IDdisplay.setText("Practitioner Identifier: " + practitioner.getIdentifier());
        //set listener to checkbox cell
        ObservableList<Patient> allPatients = FXCollections.observableArrayList(practitioner.getPatients());
        allPatients.forEach(patient -> patient.selectedProperty().addListener((observableValue, wasSelected, isSelected) -> {
            if (isSelected) {
                tableViewController.addMonitoredPatient(patient);
            }
            if (wasSelected && !isSelected) {
                tableViewController.removeMonitoredPatient(patient);
            }
        }));

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

    /**
     * Resets the refresh timer when changes to the refresh spinner occur
     */
    public void onRefreshChange() {
        if (refreshSpinner.getValue() != null) {
            refreshTimer.cancel();
            refreshTimer = new Timer();
            refreshTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    tableViewController.refreshMeasurementsData();
                    System.out.print("Refreshing - " + refreshSpinner.getValue());
                }
            }, refreshSpinner.getValue() * 1000, refreshSpinner.getValue() * 1000);
        }
    }

    /**
     * Initialises the view
     *
     * @param location the url location
     * @param resources the bundle of resources
     */
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

        tableViewController = tableLoader.getController();
        tablePane.setCenter(view);

        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tableViewController.refreshMeasurementsData();
                System.out.print("Refreshing - " + refreshSpinner.getValue());
            }
        }, 0, refreshSpinner.getValue() * 1000);

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
