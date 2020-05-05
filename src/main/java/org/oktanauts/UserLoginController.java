package org.oktanauts;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.oktanauts.model.GetPatientsServiceModel;
import org.oktanauts.model.Objects.Practitioner;


public class UserLoginController implements getPatientsCallback {

//    private Practitioner user;


    GetPatientsServiceModel getPatientService = new GetPatientsServiceModel();

    @FXML
    public TextField idInput;
    @FXML

    private void enter(ActionEvent e) throws IOException {
        if (isValidate(idInput)){
            getPatientService.retrievePatients(idInput.getText(), this);
        }


//        user = new Practitioner(idInput.getText());
//        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/org/oktanauts/panel.fxml"));
//        PanelController panelController = new PanelController(user);
//        //set controller for panel
//        fxmlLoader.setController(panelController);
//        App.setRoot(fxmlLoader);

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

//        App.setRoot("panel");
    }
}
