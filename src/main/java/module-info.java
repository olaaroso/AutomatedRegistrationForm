module com.example.registrationform {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.example.registrationform;

    opens com.example.registrationform to org.testfx.junit5;
}