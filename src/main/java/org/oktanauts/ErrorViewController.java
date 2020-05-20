package org.oktanauts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.oktanauts.model.Patient;

public class ErrorViewController {

    @FXML Label errorDisplay;
    @FXML Button goBack;

    /**
     * Initialises the view to display the error message
     *
     * @param errorMsg
     */
    public void initData(String errorMsg){
        errorDisplay.setText(errorMsg);
    }

    /**
     * Closes the current view and goes back to the previous one
     *
     * @param e the action event
     */
    @FXML
    private void goBack(ActionEvent e){
        Stage stage = (Stage) goBack.getScene().getWindow();
        stage.close();
    }

}
