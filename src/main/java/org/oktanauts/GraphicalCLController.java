package org.oktanauts;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.oktanauts.model.Measurement;
import org.oktanauts.model.Observation;
import org.oktanauts.model.Patient;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class GraphicalCLController {

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private BarChart<?, ?> graph;

    private ObservableList<Patient> monitoredPatients;
    private XYChart.Series dataSeries = new XYChart.Series();
    private static String CHOLESTEROL_LEVEL = "2093-3";

    public ObservableList<Patient> getMonitorList(){
        return monitoredPatients;
    }


    public void initData(ObservableList<Patient> patients){
        this.monitoredPatients = patients;
        for(Patient p : monitoredPatients){
            Observation observation = p.getObservation(CHOLESTEROL_LEVEL);
            if (observation != null){
                Measurement m = observation.getMeasurement(CHOLESTEROL_LEVEL);
                if (m != null){
                    dataSeries.getData().add(new XYChart.Data<String, Double>(p.getName(),m.getValue()));
                }

            }
        }
        graph.getData().add(dataSeries);

        // add listener to the list in order to update the graph when the monitored patients change
        monitoredPatients.addListener((ListChangeListener<Patient> )change ->{
            while (change.next()){
                if(change.wasAdded()){
                   Patient p = change.getAddedSubList().get(0);
                    Observation observation = p.getObservation(CHOLESTEROL_LEVEL);
                    if (observation != null){
                        Measurement m = observation.getMeasurement(CHOLESTEROL_LEVEL);
                        if (m != null){
                            dataSeries.getData().add(new XYChart.Data<String, Double>(p.getName(),m.getValue()));
                        }
                    }

                }else if (change.wasRemoved()){
                    System.out.println("remove");
                    ObservableList<XYChart.Data> data = dataSeries.getData();
                    String patientName = change.getRemoved().get(0).getName();
                    int index = 0;
                    while (index < data.size()){
                        if (data.get(index).getXValue().equals(patientName)){
                            dataSeries.getData().remove(index);
                            break;
                        }
                        index ++;
                    }

                }
            }
        });
    }

}

