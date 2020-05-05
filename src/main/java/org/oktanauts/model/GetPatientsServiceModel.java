package org.oktanauts.model;


import org.oktanauts.getPatientsCallback;
import org.oktanauts.model.Objects.Patient;
import org.oktanauts.model.Objects.PatientList;
import org.oktanauts.model.Objects.Practitioner;

import java.io.IOException;


public class GetPatientsServiceModel implements getPatientsService {
    @Override
    public void retrievePatients(String practitionerID, getPatientsCallback callback) throws IOException {

        //replace this block with http request,
        // after getting all the patients, create patient objects, add them to a patientList object
        //and then create a practitoner with practitioner id and the patientlist
        //lastely do callback.updateUI(practitioner)
        Patient p1 = new Patient("1");
        Patient p2 = new Patient("2");
        Patient p3 = new Patient("3");
        PatientList pl= new PatientList(practitionerID);
        pl.add(p1);
        pl.add(p2);
        pl.add(p3);
        Practitioner p = new Practitioner(practitionerID, pl);
        callback.updateUI(p);
    }
}
