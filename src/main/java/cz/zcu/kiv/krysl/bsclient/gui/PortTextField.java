package cz.zcu.kiv.krysl.bsclient.gui;


import javafx.scene.control.TextField;

public class PortTextField extends TextField {
    public PortTextField() {
        bindListener();
    }

    public PortTextField(int port) {
        super();
        bindListener();
        setText("" + port);
    }

    public Integer getPort() {
        if (getText().isEmpty()) {
            return null;
        }
        return Integer.parseUnsignedInt(getText());
    }

    private void bindListener() {
        textProperty().addListener((observable, oldPort, newPort) -> {
            String toSet = newPort;

            if (newPort.isEmpty()) {
                return;
            }

            if (!newPort.matches("\\d*")) {
                toSet = oldPort;
            } else {
                try {
                    int port = Integer.parseUnsignedInt(newPort);
                    if (port > 65535) {
                        toSet = "65535";
                    }
                    if (port < 0) {
                        toSet = "0";
                    }
                }
                catch (NumberFormatException e) {
                    toSet = oldPort;
                }
            }

            setText(toSet);
        });
    }
}
