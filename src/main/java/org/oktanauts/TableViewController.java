package org.oktanauts;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.oktanauts.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TableViewController implements Initializable, MeasurementCallback {
    @FXML  private TableView<Patient> monitorTable;
    private TableColumn<Patient, String> nameColumn = new TableColumn<>("Patient Name");
    private TableColumn<Patient, String> valColumn = new TableColumn<>("Val");
    private TableColumn<Patient, String> timeColumn = new TableColumn<>("Time");
    private ObservableList<Patient> monitoredPatients = FXCollections.observableArrayList();
    private ArrayList<Measurement> monitoredCholesterol = new ArrayList<>();
    private float averageCholesterol = 0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        nameColumn.setCellFactory(monitorTable -> {
//
//            TableCell<Patient, String> cell = new TableCell<Patient, String>();
//                @Override
//                protected void updateItem(String item, boolean empty){
//                    super.updateItem(item, empty);
//                    if (!empty){
//                        this.setText(item);
//                        System.out.println(this.getTableRow().getIndex());
//
//
//                    }
//                }
//            }
//            cell.setOnMouseClicked(mouseEvent -> {
//                if (mouseEvent.getClickCount() == 2 && !cell.isEmpty()){
//                    cell.getTableRow().setStyle("-fx-background-color: tomato;");
//
//                }
//            });
//            return cell;
//        });
        nameColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getName()));



        valColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(p.getValue()
                .getMeasurement("2093-3")!= null? p.getValue().getMeasurement("2093-3").toString(): ""));



        timeColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper(p.getValue()
                .getMeasurement("2093-3")!= null? p.getValue().getMeasurement("2093-3").getTimestamp():""));

        nameColumn.setMinWidth(170);
        valColumn.setMinWidth(100);
        timeColumn.setMinWidth(170);

//        monitorTable.setRowFactory(tv -> {
//            TableRow<Patient> row = new TableRow<>();
//
//            if(row.getItem() == null){
//                System.out.println("is   null");
//            }else{
//                System.out.println("is   not null");
//            }
//
////            Measurement value = row.getItem().getMeasurement("2093-3");
////            if (value != null){
////                BooleanBinding highlight = new SimpleFloatProperty(value.getValue()).lessThan(averageCholesterol);
////                row.styleProperty().bind(Bindings.when(highlight)
////                        .then("-fx-background-color: red ;")
////                        .otherwise(""));
////            }
//            return row;
//        });



        monitorTable.setItems(monitoredPatients);
        monitorTable.getColumns().addAll(nameColumn, valColumn, timeColumn);
        monitorTable.setPlaceholder(new Label("No patients being monitored"));
        monitorTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    public Patient selectedPatient(){
        return monitorTable.getSelectionModel().getSelectedItem();
    }

    public void addMonitoredPatient(Patient p){
        p.updateMeasurement("2093-3", this);
        monitoredPatients.add(p);
        System.out.println("add" + monitoredPatients.size());

    }

    public void removeMonitoredPatient(Patient p){
        System.out.println("remove");
        int index = 0;
        boolean isFound = false;
        while( index < monitoredPatients.size()){
            if(monitoredPatients.get(index).equals(p)){
                isFound = true;
                break;
            }
            index ++;
        }
        if (isFound){
            monitoredPatients.remove(index);
        }

        removeOldMeasurement(p);
        updateAverageCholesterol();
        System.out.println("remove" + monitoredPatients.size());

    }

    private void updateAverageCholesterol(){

        if (monitoredCholesterol.size() ==0){
            averageCholesterol = 0;
        }
        else{
            float totalCholesterol = 0;
            for (Measurement each:monitoredCholesterol) {
                totalCholesterol += each.getValue();
            }
            averageCholesterol = totalCholesterol / monitoredCholesterol.size();
        }
    }

    private void removeOldMeasurement(Patient p){
        int index = 0;
        boolean isFound = false;
        while( index < monitoredCholesterol.size()){
            if(monitoredCholesterol.get(index).getPatient().equals(p)){
                isFound = true;
                break;
            }
            index ++;
        }
        if (isFound){
            monitoredCholesterol.remove(index);
        }

    }


    @Override
    public void updateView(Measurement measurement) {
        removeOldMeasurement(measurement.getPatient());
        monitoredCholesterol.add(measurement);
        updateAverageCholesterol();

    }


    public void updateAll() {
        for (Patient patient : monitoredPatients) {
            patient.updateMeasurement("2093-3", this);
        }
    }



    }

