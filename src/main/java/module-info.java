module org.oktanauts {
    requires javafx.controls;
    requires javafx.fxml;
    requires hapi.fhir.base;
    requires org.hl7.fhir.r4;

    opens org.oktanauts to javafx.fxml;
    exports org.oktanauts;
}