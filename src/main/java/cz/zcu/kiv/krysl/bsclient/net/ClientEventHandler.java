package cz.zcu.kiv.krysl.bsclient.net;

public interface ClientEventHandler {
    void handleDisconnected();
    void handleServerRequest();
    void handleOnlineChange(boolean isOnline);
}
