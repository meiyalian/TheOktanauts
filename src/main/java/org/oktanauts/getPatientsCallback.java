package org.oktanauts;

import org.oktanauts.model.Objects.Practitioner;

import java.io.IOException;

public interface getPatientsCallback {
    void updateUI(Practitioner practitioner) throws IOException;
}
