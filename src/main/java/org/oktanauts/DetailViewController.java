package org.oktanauts;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.oktanauts.model.Patient;

/**
 * This class is the controller class for viewing a patient details from
 * selecting a patient in the monitor table
 */
public class DetailViewController{
    @FXML Label name;
    @FXML Label birthday;
    @FXML Label gender;
    @FXML Label address;

    public void initData(Patient p){
        name.setText("Name: " + p.getName());
        birthday.setText("Birthday: " + p.getBirthday());
        gender.setText("Gender: " + p.getGender());
        address.setText("Address: " + p.getAddress());
    }
}
