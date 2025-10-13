module com.example.budgetwolt {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.sql;
    requires java.naming;
    requires mysql.connector.j;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;

    opens com.example.budgetwolt to javafx.fxml, org.hibernate.orm.core, jakarta.persistence;
    exports com.example.budgetwolt;
    exports com.example.budgetwolt.mainCLI;
    opens com.example.budgetwolt.mainCLI to javafx.fxml;
    opens com.example.budgetwolt.models;
    exports com.example.budgetwolt.models to com.example.budgetwolt.mainCLI;
    opens com.example.budgetwolt.fxControllers to javafx.fxml;
    exports com.example.budgetwolt.fxControllers to javafx.fxml;
}