package org.oktanauts.model;

import java.io.IOException;
import java.text.ParseException;

public interface GetPractitionerService {
    void retrievePractitioner(String practitionerID, GetPractitionerCallback callback) throws IOException, ParseException;
}
