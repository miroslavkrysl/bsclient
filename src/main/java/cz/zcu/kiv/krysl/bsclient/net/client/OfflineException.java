package cz.zcu.kiv.krysl.bsclient.net.client;

public class OfflineException extends Exception {
    private ConnectionLossCause cause;

    public OfflineException(String s, ConnectionLossCause cause) {
        super(s);
        this.cause = cause;
    }

    public ConnectionLossCause getLossCause() {
        return cause;
    }
}
