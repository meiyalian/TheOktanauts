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

    public void initData(String errorMsg){
        errorDisplay.setText(errorMsg);
    }

    @FXML
    private void goBack(ActionEvent e){
        Stage stage = (Stage) goBack.getScene().getWindow();
        stage.close();
    }

}
