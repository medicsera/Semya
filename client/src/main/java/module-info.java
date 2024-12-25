module org.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires org.example.shared; // Указываем зависимость на shared

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.compiler;
    requires java.sql;
    requires com.google.gson;
    requires java.desktop;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens org.example.client to javafx.fxml;
    exports org.example.client;
}