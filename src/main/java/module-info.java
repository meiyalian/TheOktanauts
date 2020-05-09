module org.oktanauts {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.sql;

    opens org.oktanauts to javafx.fxml;
    exports org.oktanauts;
}