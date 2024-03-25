module com.example.ia_fxgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires commons.csv;
    requires static lombok;
    requires itextpdf;
    requires bcrypt;
    requires bytes;
    requires java.desktop;
    requires javafx.swing;
    requires itextpdf;
    requires io;
    requires commons.math3;

    opens com.example.ia_fxgui to javafx.fxml;
    exports com.example.ia_fxgui;
    exports com.example.ia_fxgui.login;
    opens com.example.ia_fxgui.login to javafx.fxml;
    exports com.example.ia_fxgui.services;
    opens com.example.ia_fxgui.services to javafx.fxml;
}