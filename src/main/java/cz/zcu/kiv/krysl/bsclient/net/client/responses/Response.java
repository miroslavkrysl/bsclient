package cz.zcu.kiv.krysl.bsclient.net.client.responses;

public abstract class Response {

    public final boolean isDisconnected() {
        return this instanceof ResponseDisconnected;
    }

    public final boolean isResponse() {
        return this instanceof ResponseMessage;
    }
}
