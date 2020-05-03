package org.oktanauts;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import org.oktanauts.model.Practitioner;

public class UserLoginController {

    private Practitioner user;

    @FXML
    public TextField idInput;
    @FXML
    private void switchToSecondary(ActionEvent e) throws IOException {
        user = new Practitioner(idInput.getText());
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/org/oktanauts/panel.fxml"));
        PanelController panelController = new PanelController(user);
        //set controller for panel
        fxmlLoader.setController(panelController);
        App.setRoot(fxmlLoader);

    }
}
