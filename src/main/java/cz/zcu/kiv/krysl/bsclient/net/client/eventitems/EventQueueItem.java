package cz.zcu.kiv.krysl.bsclient.net.client.eventitems;

public abstract class EventQueueItem {
    public final boolean isOffline() {
        return this instanceof EventQueueItemOffline;
    }

    public final boolean isDisconnected() {
        return this instanceof EventQueueItemDisconnected;
    }

    public final boolean isEvent() {
        return this instanceof EventQueueItemEvent;
    }
}
