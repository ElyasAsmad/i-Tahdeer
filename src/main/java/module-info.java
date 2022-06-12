module com.elyasasmad.istudent {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires org.xerial.sqlitejdbc;
    requires java.sql;
    requires annotations;
    requires org.json;

    // https://stackoverflow.com/a/57592287/16475345

    opens com.elyasasmad.istudent to javafx.fxml;
    exports com.elyasasmad.istudent;
    exports com.elyasasmad.istudent.utils;
    opens com.elyasasmad.istudent.utils to javafx.fxml;

    opens com.elyasasmad.istudent.models to com.fasterxml.jackson.databind, javafx.base;
    exports com.elyasasmad.istudent.models to com.fasterxml.jackson.databind;
    exports com.elyasasmad.istudent.constants;
    opens com.elyasasmad.istudent.constants to javafx.fxml;

    exports com.elyasasmad.istudent.utils.throwable;

}