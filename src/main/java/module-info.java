module org.oktanauts {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.oktanauts to javafx.fxml;
    exports org.oktanauts;
}