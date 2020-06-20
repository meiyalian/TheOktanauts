package org.oktanauts;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import org.oktanauts.model.Observation;
import org.oktanauts.model.Patient;

import java.util.ArrayList;
import static org.oktanauts.model.GetMeasurementService.BLOOD_PRESSURE;
import static org.oktanauts.model.GetMeasurementService.SYSTOLIC_BLOOD_PRESSURE;

import java.util.HashMap;
import java.util.Map;


/**
 * This class is the view(controller) class for the graph view of high blood pressure patients
 */

public class BPGraphicalController {

    @FXML ScrollPane scrollPane;
    private ObservableList<Patient> trackingPatients;
    private ArrayList<LineChart<Number,Number>> graphs = new ArrayList<>();
    private HashMap<Patient, LineChart<Number,Number>> graphManager = new HashMap<>();

    /**
     * Initialises the data for the blood pressure graphs
     * @param patients
     */
    public void initData(ObservableList<Patient> patients){
        trackingPatients = patients;
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(20));
        scrollPane.setContent(hBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);


        trackingPatients.addListener((ListChangeListener<Patient>) change ->{
            while (change.next()){
                if(change.wasAdded()){
                    Patient p = change.getAddedSubList().get(0);
                    NumberAxis x = new NumberAxis();
                    NumberAxis y = new NumberAxis();
                    x.setLabel("Time Period");
                    y.setLabel("Blood Pressure");

                    LineChart<Number,Number> lineChart = new LineChart<>(x, y);
                    lineChart.setTitle(p.getName());
                    XYChart.Series dataSeries = new XYChart.Series();
                    lineChart.getData().add(dataSeries);
                    //fill data
                    ArrayList<Observation> observations = p.getObservationTracker(BLOOD_PRESSURE).getRecords();
                    int index = observations.size();
                    for (Observation o: observations) {
                        dataSeries.getData().add(new XYChart.Data<>(index, o.getMeasurement(SYSTOLIC_BLOOD_PRESSURE).getValue()));
                        index --;

                    }
                    graphManager.put(p,lineChart);
                    hBox.getChildren().add(lineChart);
                }
                else if (change.wasRemoved()) {
                    Patient p = change.getRemoved().get(0);
                    LineChart<Number,Number> chart = graphManager.get(p);
                    graphManager.remove(p);
                    hBox.getChildren().remove(chart);

                }
            }
        });
    }

    /**
     * Updates the graphs in the view
     */
    public void updateView() {
        for (Map.Entry<Patient, LineChart<Number, Number>> entry : graphManager.entrySet()) {
            Patient p = entry.getKey();
            LineChart<Number, Number> chart = entry.getValue();
            chart.setData(FXCollections.observableArrayList());
            XYChart.Series dataSeries = new XYChart.Series();
            ArrayList<Observation> observations = p.getObservationTracker(BLOOD_PRESSURE).getRecords();
            int index = observations.size();
            for (Observation o : observations) {
                dataSeries.getData().add(new XYChart.Data<>(index, o.getMeasurement(SYSTOLIC_BLOOD_PRESSURE).getValue()));
                index--;
            }
            chart.getData().add(dataSeries);
        }
    }
}
