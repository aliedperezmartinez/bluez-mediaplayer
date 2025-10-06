module com.javadruid.bluez.mediaplayer.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.javadruid.bluez.mediaplayer.lib;

    exports com.javadruid.bluez.mediaplayer.ui;

    opens com.javadruid.bluez.mediaplayer.ui to javafx.fxml;
}
