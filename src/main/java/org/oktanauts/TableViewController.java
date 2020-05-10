package org.oktanauts;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.oktanauts.model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TableViewController implements Initializable, GetMeasurementCallback {
    @FXML  private TableView monitorTable;
    private TableColumn<Patient, String> nameColumn = new TableColumn<>("Patient Name");
    private TableColumn<Patient, String> valColumn = new TableColumn<>("Val");
    private TableColumn<Patient, String> timeColumn = new TableColumn<>("Time");
//    private GetMeasurementService getMeasurementService = new GetMeasurementTestModel();


//    private ObservableList<Patient> monitoredPatient = FXCollections.observableArrayList();
//
//    public ObservableList<Patient> getMonitoredPatient() {
//        return monitoredPatient;
//    }
    private ObservableList<Patient> monitoredPatients = FXCollections.observableArrayList();




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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        nameColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getName()));

        valColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getCholesterolLevel()));

        timeColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getCholesterolMeasuredTime()));

        nameColumn.setMinWidth(170);
        valColumn.setMinWidth(100);
        timeColumn.setMinWidth(170);

        monitorTable.setItems(monitoredPatients);
        monitorTable.getColumns().addAll(nameColumn, valColumn, timeColumn);
        monitorTable.setPlaceholder(new Label("No patients being monitored"));
        monitorTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);




    }

    public void addMonitoredPatient(Patient p){
        p.updateCholesterol(this);
        monitoredPatients.add(p);


    }

    public void removeMonitoredPatient(Patient p){
        int index = 0;
        while( index < monitoredPatients.size()){
            if(monitoredPatients.get(index).equals(p)){
                break;
            }
            index ++;
        }
        monitoredPatients.remove(index);

    }


    @Override
    // this method is for adding a new monitor patient
    public void updateView(Measurement measurement) {
//        observation.add(measurement);
//
//        //loop through all measurements and highlight the one that has higher value
//        hightlightWarning();

    }


//
//    private  void hightlightWarning(){
//        //add highlight
//        monitorTable.
//
//    }

}

