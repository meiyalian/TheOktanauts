package org.oktanauts.model;

import org.oktanauts.model.Practitioner;
import java.io.IOException;

/**
 * This class is interface for class that uses the getPractitionerService
 */

public interface GetPractitionerCallback {
    void updateUI(Practitioner p) throws IOException;
    void getPractitionerFail(); // for implementing login function later
}
