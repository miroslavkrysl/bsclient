package cz.zcu.kiv.krysl.bsclient.net.client.results;

import cz.zcu.kiv.krysl.bsclient.net.types.RestoreState;

public class RestoreResultOk extends RestoreResult {
    private RestoreState restoreState;

    public RestoreResultOk(RestoreState restoreState) {
        this.restoreState = restoreState;
    }

    public RestoreState getRestoreState() {
        return restoreState;
    }
}
