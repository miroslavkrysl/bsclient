package cz.zcu.kiv.krysl.bsclient.net;

public class SessionManager {
    private String token;

    public SessionManager(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
