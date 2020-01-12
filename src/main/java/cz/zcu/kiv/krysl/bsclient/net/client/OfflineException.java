package cz.zcu.kiv.krysl.bsclient.net.client;

public class OfflineException extends Exception {
    private OfflineCause cause;

    public OfflineException(OfflineCause cause) {
        this.cause = cause;
    }

    public OfflineCause getLossCause() {
        return cause;
    }

    @Override
    public String getMessage() {
        return cause.getMessage();
    }
}
