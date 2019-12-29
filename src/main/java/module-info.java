module bsclient {
    requires javafx.controls;
    requires javafx.fxml;

    opens cz.zcu.kiv.krysl.bsclient.gui to javafx.fxml;

    exports cz.zcu.kiv.krysl.bsclient;
}