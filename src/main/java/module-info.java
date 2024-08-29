module com.example.javaweb {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.desktop;

    opens com.example.javaweb to javafx.fxml;
    opens com.example.javaweb.alem.controller to javafx.fxml;
    exports com.example.javaweb;
    exports com.example.javaweb.alem;
    exports com.example.javaweb.alem.model;
    exports com.example.javaweb.alem.core;
    opens com.example.javaweb.alem.core;
    opens com.example.javaweb.alem.model;
    exports com.example.javaweb.alem.controller;
    opens com.example.javaweb.alem to javafx.fxml;

    opens com.example.javaweb.alem.controller.secretariat to javafx.fxml;
    exports com.example.javaweb.alem.controller.secretariat;
    opens com.example.javaweb.alem.controller.medecine to javafx.fxml;
    exports com.example.javaweb.alem.controller.medecine;
    opens com.example.javaweb.alem.controller.laboratoire to javafx.fxml;
    exports com.example.javaweb.alem.controller.laboratoire;
    opens com.example.javaweb.alem.controller.infirmerie to javafx.fxml;
    exports com.example.javaweb.alem.controller.infirmerie;
    opens com.example.javaweb.alem.controller.notifs to javafx.fxml;
    exports com.example.javaweb.alem.controller.notifs;
    opens com.example.javaweb.alem.controller.admin to javafx.fxml;
    exports com.example.javaweb.alem.controller.admin;

    exports com.example.javaweb.alem.model.secretariat;
    opens com.example.javaweb.alem.model.secretariat;
    exports com.example.javaweb.alem.model.medecine;
    opens com.example.javaweb.alem.model.medecine;
    exports com.example.javaweb.alem.model.laboratoire;
    opens com.example.javaweb.alem.model.laboratoire;
    exports com.example.javaweb.alem.model.infirmerie;
    opens com.example.javaweb.alem.model.infirmerie;
    exports com.example.javaweb.alem.model.admin;
    opens com.example.javaweb.alem.model.admin;
}