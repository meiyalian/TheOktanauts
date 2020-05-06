package org.oktanauts;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.oktanauts.model.Patient;
import org.oktanauts.model.PatientList;
import org.oktanauts.model.Practitioner;
import org.oktanauts.model.Iterator;

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
            String pID = p.getName();
            patientListView.getItems().add(pID);
        }


    }



}
