package cz.zcu.kiv.krysl.bsclient.net.client.results;

public class RestoreResult {
    public boolean isFail() {
        return this instanceof RestoreResultFail;
    }

    public boolean isOk() {
        return this instanceof RestoreResultOk;
    }
}
