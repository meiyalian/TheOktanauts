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
import org.oktanauts.model.GetPractitionerService;
import org.oktanauts.model.Practitioner;
import org.oktanauts.model.GetPractitionerCallback;

/**
 * This class is the controller class for the user login page of the app
 */
public class UserLoginController implements GetPractitionerCallback {

    GetPractitionerService getPractitionerService = new GetPractitionerService();

    @FXML public TextField idInput;

    /**
     * Handles the login attempt caused by the enter event
     *
     * @param e the action event
     * @throws IOException
     * @throws ParseException
     */
    @FXML
    public void enter(ActionEvent e) throws IOException, ParseException {
        if (isValidInput(idInput)){
            getPractitionerService.getPractitioner(idInput.getText(), this);
        }
    }

    /**
     * Validates that the inputted practitioner identifier is of a valid format
     *
     * @param textField the text field of the practitioner identifier input
     * @return a boolean value of whether the inputted practitioner identifier is valid
     * @throws IOException
     */
    private boolean isValidInput(TextField textField) throws IOException {
        try {
            int identifier = Integer.parseInt(textField.getText());
            return true;
        } catch (NumberFormatException e){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/org/oktanauts/errorView.fxml"));
            Parent root = loader.load();
            ErrorViewController errorView = loader.getController();
            errorView.initData("Input is not valid. Please enter your practitioner Id.");
            Scene errorPage = new Scene(root);
            Stage newWindow = new Stage();
            newWindow.setScene(errorPage);
//            newWindow.setResizable(false);
            newWindow.show();
            return false;
        }
    }

    /**
     * Updates the UI when the practitioner details have been correctly fetched
     *
     * @param practitioner the practitioner that has just logged in
     * @throws IOException
     */
    @Override
    public void updateUI(Practitioner practitioner ) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("/org/oktanauts/monitorPanel.fxml"));
        App.setRoot(loader);
        MainPanelController mainPage = loader.getController();
        mainPage.initData(practitioner);
        Stage window = App.getStage();
        window.show();
    }
}
