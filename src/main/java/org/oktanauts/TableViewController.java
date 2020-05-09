package org.oktanauts;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.oktanauts.model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TableViewController implements Initializable, GetMeasurementCallback {
    @FXML  private TableView monitorTable;
    private TableColumn<Measurement, String> nameColumn = new TableColumn<>("Patient Name");
    private TableColumn<Measurement, String> valColumn = new TableColumn<>("Val");
    private TableColumn<Measurement, String> timeColumn = new TableColumn<>("Time");
    private GetMeasurementService getMeasurementService = new GetMeasurementTestModel();


//    private ObservableList<Patient> monitoredPatient = FXCollections.observableArrayList();
//
//    public ObservableList<Patient> getMonitoredPatient() {
//        return monitoredPatient;
//    }
    private ObservableList<Measurement> observation = FXCollections.observableArrayList();



    @Override
//    public void initialize(URL location, ResourceBundle resources) {
////        nameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
//
//        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Patient, String>, ObservableValue<String>>() {
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Patient, String> p) {
//                return new ReadOnlyObjectWrapper(p.getValue().getName());
//            }
//        });
//        nameColumn.setMinWidth(170);
//        monitorTable.setItems(monitoredPatient);
//        monitorTable.getColumns().add(nameColumn);
//    }

    public void initialize(URL location, ResourceBundle resources) {
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        nameColumn.setCellValueFactory(m -> new ReadOnlyObjectWrapper(m.getValue().getMeasuredPatient().getName()));

        valColumn.setCellValueFactory(m -> new ReadOnlyObjectWrapper(m.getValue().getDisplayValue()));

        timeColumn.setCellValueFactory(m -> new ReadOnlyObjectWrapper(m.getValue().getMeasuredDateTime()));

        nameColumn.setMinWidth(170);
        valColumn.setMinWidth(100);
        timeColumn.setMinWidth(170);

        monitorTable.setItems(observation);
        monitorTable.getColumns().addAll(nameColumn, valColumn, timeColumn);

    }

    public void addMonitoredPatient(Patient p){
        getMeasurementService.retrieveSingleMeasurement(p, "CholesterolLevel", this);

    }

    public void removeMonitoredPatient(Patient p){
        int index = 0;
        while( index < observation.size()){
            if(observation.get(index).getMeasuredPatient().equals(p)){
                break;
            }
            index ++;
        }
        observation.remove(index);

    }


    @Override
    // this method is for adding a new monitor patient
    public void updateSingleMeasurement(Measurement measurement) {
        observation.add(measurement);

        //loop through all measurements and highlight the one that has higher value
        hightlightWarning();

    }

    @Override
    public void updateMeasurements(ArrayList<Measurement> measurements) {

        // update a list of patient's measurement
        //this method is used for update every n seconds
        //remove all the old measurements and add new ones
    }

    private static void hightlightWarning(){
        //add highlight
    }

}

