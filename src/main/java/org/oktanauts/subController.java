package org.oktanauts;

import javafx.collections.ObservableList;
import org.oktanauts.model.Patient;

public interface subController {
    void initData(ObservableList<Patient> patients);
    void updateView();

}
