package org.oktanauts.model;

import org.oktanauts.model.Practitioner;

import java.io.IOException;

public interface GetPractitionerCallback {
    void updateUI(Practitioner p) throws IOException;
    void getPractitionerFail();
}
