package org.oktanauts;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.oktanauts.model.Objects.Patient;
import org.oktanauts.model.Objects.PatientList;
import org.oktanauts.model.Objects.Practitioner;
import org.oktanauts.model.Objects.Iterator;

public class MainPanelController  {
    private Practitioner practitioner;

    @FXML Label IDdisplay;
    @FXML ListView patientListView;



    public void initData(Practitioner practitioner){
        this.practitioner = practitioner;
        IDdisplay.setText("PractitionerID: " + practitioner.getId());
        PatientList patients= practitioner.getPatients();
        Iterator iter = patients.getIterator();
        while (iter.hasNext()){
            Patient p = (Patient) iter.next();
            String pID = p.getId();
            patientListView.getItems().add(pID);
        }


    }



}
