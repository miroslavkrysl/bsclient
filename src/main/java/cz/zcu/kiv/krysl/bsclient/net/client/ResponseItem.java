package cz.zcu.kiv.krysl.bsclient.net.client;

import cz.zcu.kiv.krysl.bsclient.net.messages.server.ServerMessage;

public class ResponseItem extends IncomingMessageQueueItem {
    private ServerMessage response;

    public ResponseItem(ServerMessage response) {
        this.response = response;
    }

    public ServerMessage getResponse() {
        return response;
    }
}
