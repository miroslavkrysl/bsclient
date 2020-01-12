package cz.zcu.kiv.krysl.bsclient.net.client;

public abstract class IncomingMessageQueueItem {
    public final boolean isOffline() {
        return this instanceof OfflineItem;
    }

    public final boolean isDisconnected() {
        return this instanceof DisconnectedItem;
    }

    public final boolean isResponse() {
        return this instanceof ResponseItem;
    }
}
