package org.oktanauts;

import javafx.collections.FXCollections;
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
import java.util.List;

public class BPGraphicalController {

    @FXML ScrollPane scrollPane;
    private ObservableList<Patient> trackingPatients;
    private ArrayList<LineChart<Number,Number>> graphs = new ArrayList<>();

    public void initData(ObservableList<Patient> patients){

        trackingPatients = patients;
        HBox hBox = new HBox();
        for (Patient p: trackingPatients) {

            NumberAxis x = new NumberAxis();
            NumberAxis y = new NumberAxis();
            x.setLabel("Time Period");
            y.setLabel("Blood Pressure");

            LineChart<Number,Number> lineChart = new LineChart<Number,Number>(x,y);
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

            graphs.add(lineChart);
            hBox.getChildren().add(lineChart);
        }
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(20));
        scrollPane.setContent(hBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);


//        trackingPatients.addListener();
    }

}
