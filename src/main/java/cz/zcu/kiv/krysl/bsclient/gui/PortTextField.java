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

    public int getPort() {
        return Integer.parseUnsignedInt(getText());
    }

    private void bindListener() {
        textProperty().addListener((observable, oldPort, newPort) -> {
            boolean setOld = false;

            if (!newPort.matches("\\d*")) {
                setOld = true;
            } else {
                try {
                    int port = Integer.parseUnsignedInt(newPort);
                    if (port > 65535 || port < 0) {
                        setOld = true;
                    }
                }
                catch (NumberFormatException e) {
                    setOld = true;
                }
            }

            if (setOld) {
                ((TextField) observable).setText(oldPort);
            }
        });
    }
}
