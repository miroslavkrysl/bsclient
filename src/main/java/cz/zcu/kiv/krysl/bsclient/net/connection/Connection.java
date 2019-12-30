package cz.zcu.kiv.krysl.bsclient.net.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Connection {

    private ConnectionSupervisor connectionSupervisor;
    private ConnectionReceiver connectionReceiver;
    private ConnectionSender connectionSender;
    private Socket socket;

    public Connection(InetSocketAddress serverAddress) throws IOException {
        socket = new Socket();
        socket.connect(serverAddress);
        socket.setTcpNoDelay(true); // disable Nagle's algorithm

        encoder = Encoder.get();
        decoder = Decoder.get();

        /*
         * Because we access the list from several places we create a
         * synchronized list.
         */
        processedMessages = new LinkedBlockingQueue<Message>();

        readManager = new NetworkClientManagerRead();
        readManager.start();

        writeManager = new NetworkClientManagerWrite();
    }

    Socket getSocket() {
        // TODO
        return null;
    }

    void handleLostConnection() {

    }

    public boolean isConnected() {
        // TODO
        return false;
    }
}
