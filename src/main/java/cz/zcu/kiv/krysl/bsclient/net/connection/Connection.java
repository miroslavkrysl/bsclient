package cz.zcu.kiv.krysl.bsclient.net.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Connection<MessageIn, MessageOut> implements IConnectionLossHandler {
    private static final int MESSAGE_QUEUE_CAPACITY = 100;

    private IConnectionLossHandler connectionLossHandler;

    private final LinkedBlockingQueue<MessageIn> incomingQueue;
    private final LinkedBlockingQueue<MessageOut> outgoingQueue;
    private Receiver<MessageIn> receiver;
    private Sender<MessageOut> sender;
    private Socket socket;

    /**
     * Create connection to the server.
     * Creates and sets up a socket and starts sender and receiver threads.
     *
     * @param serverAddress The address of the server.
     * @param codec The codec used for stream serialization and deserialization into messages.
     * @param connectionLossHandler The handler which will be called when the connection loss occurs.
     * @throws IOException When error occurs during creating the socket connection.
     */
    public Connection(InetSocketAddress serverAddress,
                      ICodec<MessageIn, MessageOut> codec,
                      IConnectionLossHandler connectionLossHandler) throws IOException {
        this.connectionLossHandler = connectionLossHandler;

        this.incomingQueue = new LinkedBlockingQueue<>(MESSAGE_QUEUE_CAPACITY);
        this.outgoingQueue = new LinkedBlockingQueue<>(MESSAGE_QUEUE_CAPACITY);

        this.socket = new Socket();
        this.socket.connect(serverAddress);
        this.socket.setTcpNoDelay(true);

        this.receiver = new Receiver<>(socket.getInputStream(), codec.newDeserializer(), incomingQueue, this);
        this.receiver.setDaemon(false);
        this.receiver.start();

        this.sender = new Sender<>(socket.getOutputStream(), codec.newSerializer(), outgoingQueue);
        this.sender.setDaemon(false);
        this.sender.start();
    }

    /**
     * Closes connection and cancels its worker threads.
     */
    public void close()  {
        this.receiver.cancel();
        this.sender.cancel();

        try {
            this.socket.close();
        } catch (IOException e) {
            // we don't care about the error.
        }
    }

    /**
     * Check whether is the connection alive.
     *
     * @return True if connected, false otherwise.
     */
    public boolean isConnected() {
        return this.socket.isConnected();
    }

    /**
     * Send a message.
     * If the internal message queue is full, blocks for timeout until
     * the there is an available space for the message.
     *
     * @param message Message to send
     * @param timeout Timeout to wait.
     * @return True or false if timeout happens.
     * @throws InterruptedException If the thread was interrupted while waiting.
     */
    public boolean send(MessageOut message, Duration timeout) throws InterruptedException {
        // TODO: handle case when not connected or disconnected during the call.
        return outgoingQueue.offer(message, timeout.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Receive a message.
     * Blocks for timeout until a message is available or null if timeout happens.
     *
     * @param timeout Timeout to wait.
     * @return A message or null if timeout happens.
     * @throws InterruptedException If the thread was interrupted while waiting for a message.
     */
    public MessageIn receive(Duration timeout) throws InterruptedException {
        // TODO: handle case when not connected or disconnected during the call.
        return incomingQueue.poll(timeout.toMillis(), TimeUnit.MILLISECONDS);
    }

    // --- ISocketClosedHandler ---

    @Override
    synchronized public void handleConnectionLoss(ConnectionLossCause cause) {
        close();
        // notify upper layer about the connection loss
        connectionLossHandler.handleConnectionLoss(cause);
    }
}
