package org.oktanauts;


import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.oktanauts.model.Measurement;
import org.oktanauts.model.Observation;
import org.oktanauts.model.Patient;


import static org.oktanauts.model.GetMeasurementService.CHOLESTEROL_LEVEL;
/**
 * This class is the view(controller) class for the graph view of cholesterol level patients
 */
public class GraphicalCLController {

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis y;

    @FXML
    private BarChart<String, Double> graph;

    private ObservableList<Patient> monitoredPatients;
    private XYChart.Series dataSeries = new XYChart.Series();


    /**
     * Gets the list of monitored patients
     *
     * @return an observable list of the currently monitored patients
     */
    public ObservableList<Patient> getMonitorList(){
        return monitoredPatients;
    }

    /**
     * Initialises the data for the cholesterol level graph
     *
     * @param patients an observable list of patients to be graphed
     */
    public void initData(ObservableList<Patient> patients){
        this.monitoredPatients = patients;

        updateView();

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
                            graph.setMaxWidth(graph.getWidth() + 100);
                        }
                    }
                }
                else if (change.wasRemoved()) {
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

    /**
     * Updates the view
     */
    public void updateView(){
        // update data of the graph
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
    }
}

