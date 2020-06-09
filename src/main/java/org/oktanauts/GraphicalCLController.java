package org.oktanauts;

import javafx.application.Platform;
import javafx.collections.FXCollections;
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

import static org.oktanauts.model.GetMeasurementService.CHOLESTEROL_LEVEL;

public class GraphicalCLController {

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis y;

    @FXML
    private BarChart<String, Double> graph;

    private ObservableList<Patient> monitoredPatients;
    private XYChart.Series dataSeries = new XYChart.Series();



    public ObservableList<Patient> getMonitorList(){
        return monitoredPatients;
    }


    public void initData(ObservableList<Patient> patients){
        this.monitoredPatients = patients;
//        xAxis = (CategoryAxis) graph.getXAxis();

        updateView();
//        graph.setPrefWidth(1000);
//        setMaxBarWidth(40, 10);
//
//        graph.widthProperty().addListener((obs,b,b1)->{
//            Platform.runLater(()->setMaxBarWidth(40, 10));
//        });


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
//                            graph.setPrefWidth(graph.getData().size()*5);
                            graph.setMaxWidth(graph.getWidth() + 100);
//                            graph.setBarGap(10);


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
//                            graph.setPrefWidth(graph.getData().size()*5);
//                            graph.setMaxWidth(graph.getWidth() + 100);
//                            graph.setBarGap(10);
                            break;
                        }
                        index ++;
                    }

                }
            }
        });
    }

    public void updateView(){
        //update data of the graph
//        graph.getData().clear();
        graph.setData(FXCollections.observableArrayList());
        XYChart.Series newData = new XYChart.Series();
        this.dataSeries = newData;

        for(Patient p : monitoredPatients){
            Observation observation = p.getObservation(CHOLESTEROL_LEVEL);
            if (observation != null){
                Measurement m = observation.getMeasurement(CHOLESTEROL_LEVEL);
                if (m != null){
                    newData.getData().add(new XYChart.Data<String, Double>(p.getName(),m.getValue()));
                }

            }
        }


        graph.getData().add(newData);
//        graph.lookupAll(".default-color0.chart-bar").forEach(n -> n.setStyle("-fx-bar-fill: blue;"));
//        graph.setBarGap(5);
//
    }

//    private void setMaxBarWidth(double maxBarWidth, double minCategoryGap){
//        double barWidth=0;
//        do{
//            double catSpace = xAxis.getCategorySpacing();
//            double avilableBarSpace = catSpace - (graph.getCategoryGap() + graph.getBarGap());
//            barWidth = (avilableBarSpace / graph.getData().size()) - graph.getBarGap();
//            if (barWidth >maxBarWidth){
//                avilableBarSpace=(maxBarWidth + graph.getBarGap())* graph.getData().size();
//                graph.setCategoryGap(catSpace-avilableBarSpace-graph.getBarGap());
//            }
//        } while(barWidth>maxBarWidth);
//
//        do{
//            double catSpace = xAxis.getCategorySpacing();
//            double avilableBarSpace = catSpace - (minCategoryGap + graph.getBarGap());
//            barWidth = Math.min(maxBarWidth, (avilableBarSpace / graph.getData().size()) - graph.getBarGap());
//            avilableBarSpace=(barWidth + graph.getBarGap())* graph.getData().size();
//            graph.setCategoryGap(catSpace-avilableBarSpace-graph.getBarGap());
//        } while(barWidth < maxBarWidth && graph.getCategoryGap()>minCategoryGap);
//    }
//

}

