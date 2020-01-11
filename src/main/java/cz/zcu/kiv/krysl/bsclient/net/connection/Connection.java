package cz.zcu.kiv.krysl.bsclient.net.connection;

import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A communication channel that continuously receives and sends messages.
 * Messages can be sent using the send() method.
 * Received messages are provided to the given ConnectionManager by calling
 * the handleMessageReceive method with received message as a parameter.
 * When disconnected by external cause or the message serialization/deserialization fails,
 * the ConnectionManager handleConnectionLoss method is called.
 *
 * @param <MessageIn> Incoming messages type.
 * @param <MessageOut> Outgoing messages type.
 */
public class Connection<MessageIn, MessageOut> implements IReceiverManager<MessageIn> {

    private IConnectionManager<MessageIn> connectionManager;
    private final LinkedBlockingQueue<MessageOut> outgoingQueue;
    private Receiver<MessageIn> receiver;
    private Sender<MessageOut> sender;
    private Socket socket;

    /**
     * Create connection to the remote endpoint.
     * Creates and sets up a socket and starts sender and receiver threads.
     *
     * @param serverAddress The address of the server.
     * @param deserializer The deserializer used for stream deserialization into messages.
     * @param serializer The serializer used for messages serialization into the stream.
     * @param connectionManager The handler which will be called when the connection loss occurs.
     * @throws IOException When error occurs during creating the socket connection.
     */
    public Connection(InetSocketAddress serverAddress,
                      IDeserializer<MessageIn> deserializer,
                      ISerializer<MessageOut> serializer,
                      IConnectionManager<MessageIn> connectionManager) throws IOException {

        this.connectionManager = connectionManager;
        this.outgoingQueue = new LinkedBlockingQueue<>();

        // setup socket connection
        this.socket = new Socket();
        this.socket.connect(serverAddress);
        this.socket.setTcpNoDelay(true);

        // setup receiver
        this.receiver = new Receiver<>(socket.getInputStream(), deserializer, this);
        this.receiver.setDaemon(false);
        this.receiver.start();

        // setup sender
        this.sender = new Sender<>(socket.getOutputStream(), serializer, outgoingQueue);
        this.sender.setDaemon(false);
        this.sender.start();
    }

    /**
     * Closes connection and cancels its worker threads.
     */
    public void close()  {
        receiver.cancel();
        sender.cancel();

        try {
            socket.close();
        } catch (IOException e) {
            // we don't care about the error.
        }
    }

    /**
     * Check whether is the connection disconnected.
     *
     * @return True if disconnected, false otherwise.
     */
    public boolean isDisconnected() {
        return this.socket.isClosed();
    }

    /**
     * Send a message.
     * Internally the message is placed into the outgoing message queue where waits
     * to be sent by the connection sending worker.
     *
     * @param message Message to send.
     * @throws DisconnectedException If the connection is disconnected.
     * @throws InterruptedException If the call is interrupted while putting the message into the queue.
     */
    public void send(MessageOut message) throws InterruptedException, DisconnectedException {
        if (isDisconnected()) {
            throw new DisconnectedException("Can't send a message to a disconnected connection.");
        }

        outgoingQueue.put(message);
    }

    // --- IReceiverManagerHandler ---

    @Override
    public void handleMessageReceived(MessageIn message) {
        // just pass the message to the upper layer
        this.connectionManager.handleMessageReceived(message);
    }

    @Override
    public void handleConnectionLost(ConnectionLossCause cause) {
        close();
        // notify the upper layer about the connection loss
        connectionManager.handleConnectionLost(cause);
    }
}
