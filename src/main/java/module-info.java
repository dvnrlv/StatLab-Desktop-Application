module com.example.ia_fxgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires commons.csv;
    requires lombok;

    opens com.example.ia_fxgui to javafx.fxml;
    exports com.example.ia_fxgui;
    exports com.example.ia_fxgui.login;
    opens com.example.ia_fxgui.login to javafx.fxml;
}