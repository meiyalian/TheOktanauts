package org.oktanauts;

import java.io.IOException;
import java.text.ParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.oktanauts.model.GetPractitionerModel;
import org.oktanauts.model.Practitioner;
import org.oktanauts.model.GetPractitionerCallback;


public class UserLoginController implements GetPractitionerCallback {


    GetPractitionerModel getPractitionerService = new GetPractitionerModel();

    @FXML public TextField idInput;

    @FXML
    private void enter(ActionEvent e) throws IOException, ParseException {
        if (isValidate(idInput)){
            getPractitionerService.retrievePractitioner(idInput.getText(), this);
        }


    }

    public boolean isValidate(TextField textField){
        try{
            int ID = Integer.parseInt(textField.getText());
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }


    @Override
    public void updateUI(Practitioner practitioner ) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("/org/oktanauts/monitorPanel.fxml"));


        Parent parent = loader.load();
        Scene mainPanel = new Scene(parent);

        MainPanelController mainPage = loader.getController();
        mainPage.initData(practitioner);
        Stage window = App.getStage();
        window.setScene(mainPanel);
        window.show();


    }
}
