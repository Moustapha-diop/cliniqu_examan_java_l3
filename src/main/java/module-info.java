module com.example.clinique {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires java.persistence;
    requires static lombok;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;
    requires jbcrypt;
    requires itextpdf;
    requires java.desktop;

    opens com.example.clinique.model to org.hibernate.orm.core, java.persistence, javafx.base;
    opens com.example.clinique.controller to javafx.fxml;
    opens com.example.clinique to javafx.fxml;
    exports com.example.clinique;
}
