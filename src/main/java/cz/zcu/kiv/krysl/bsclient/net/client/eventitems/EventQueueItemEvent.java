package cz.zcu.kiv.krysl.bsclient.net.client.eventitems;

import cz.zcu.kiv.krysl.bsclient.net.client.events.Event;

public class EventQueueItemEvent extends EventQueueItem {
    private Event event;

    public EventQueueItemEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
