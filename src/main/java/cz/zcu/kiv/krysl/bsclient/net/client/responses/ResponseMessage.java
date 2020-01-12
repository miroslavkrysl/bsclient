package cz.zcu.kiv.krysl.bsclient.net.client.responses;

import cz.zcu.kiv.krysl.bsclient.net.messages.server.ServerMessage;

public class ResponseMessage extends Response {
    private ServerMessage response;

    public ResponseMessage(ServerMessage response) {
        this.response = response;
    }

    public ServerMessage getResponse() {
        return response;
    }
}
