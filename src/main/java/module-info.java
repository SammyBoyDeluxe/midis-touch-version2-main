module com.example.midistouchmalfunction54 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires com.google.auth;
    requires java.datatransfer;
    requires com.google.api.apicommon;
    requires annotations;
    requires com.google.common;
    requires com.google.gson;

    // Allow reflection for JavaFX FXML loading in the root package
    opens com.example.midistouchmalfunction54 to javafx.fxml;

    // Allow reflective access to the 'com.example.midistouchmalfunction54.net' package by firebase.admin module
    opens com.example.midistouchmalfunction54.net to firebase.admin;
    opens com.example.midistouchmalfunction54.net.beans;
    opens com.example.midistouchmalfunction54.net.callbacks;
    opens com.example.midistouchmalfunction54.net.tasks;
    opens com.example.midistouchmalfunction54.utilities;
    opens com.example.midistouchmalfunction54.music;

    // Export the 'com.example.midistouchmalfunction54.net' package for public access
    exports com.example.midistouchmalfunction54.net;
    exports com.example.midistouchmalfunction54.net.tasks;
    exports com.example.midistouchmalfunction54.net.beans;
    exports com.example.midistouchmalfunction54.net.callbacks;
    exports com.example.midistouchmalfunction54.music;
    exports com.example.midistouchmalfunction54.utilities;
    // Export the root package
    exports com.example.midistouchmalfunction54;


}
