package cz.zcu.kiv.krysl.bsclient.net.connection;

import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A communication channel.
 * Continuously receives messages and provides them to through the receive() method
 * and sends messages provided with the send() method.
 * When disconnected by external cause, a provided ConnectionHandler is called.
 *
 * @param <MessageIn> Incoming messages type.
 * @param <MessageOut> Outgoing messages type.
 */
public class Connection<MessageIn, MessageOut> implements IConnectionLossHandler, IIncomingMessageHandler<MessageIn> {
    /**
     * Outgoing and incoming message queues size.
     */
    private static final int MESSAGE_QUEUE_CAPACITY = 100;

    /**
     * The handler which will be called when the connection loss occurs.
     */
    private IConnectionLossHandler connectionLossHandler;

    private final LinkedBlockingQueue<MessageQueueItem<MessageIn>> incomingQueue;
    private final LinkedBlockingQueue<MessageOut> outgoingQueue;
    private Receiver<MessageIn> receiver;
    private Sender<MessageOut> sender;
    private Socket socket;
    private AtomicBoolean queueCleared;

    /**
     * Create connection to the remote endpoint.
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

        this.queueCleared = new AtomicBoolean();

        this.socket = new Socket();
        this.socket.connect(serverAddress);
        this.socket.setTcpNoDelay(true);

        this.receiver = new Receiver<>(socket.getInputStream(), codec.newDeserializer(), this, this);
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
        receiver.cancel();
        sender.cancel();

        try {
            socket.close();
        } catch (IOException e) {
            // we don't care about the error.
        }

        while (true) {
            try {
                // put empty item into the queue to indicate that the connection is closed
                incomingQueue.put(new MessageQueueItem<>());
                break;
            } catch (InterruptedException e) {
                // interrupted but we really need to put the
                // empty item into the queue
            }
        }

        // clear the outgoingQueue to eventually release the blocking offer() call in send() method
        // messages that have not been sent yet are discarded, but wo don't care because the connection
        // is lost anyway.
        if (outgoingQueue.size() > 0) {
            outgoingQueue.clear();
            queueCleared.set(true);
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
     * @throws DisconnectedException If the connection is disconnected before or during the call.
     */
    public boolean send(MessageOut message, Duration timeout) throws InterruptedException, DisconnectedException {
        if (!isConnected()) {
            throw new DisconnectedException("Can't send a message to a disconnected connection.");
        }

        boolean result = outgoingQueue.offer(message, timeout.toMillis(), TimeUnit.MILLISECONDS);

        if (!isConnected()) {
            // connection was disconnected
            if (!queueCleared.get()) {
                // but after successful send
                return result;
            }

            throw new DisconnectedException("Connection was lost during the message sending");
        }

        return result;
    }

    /**
     * Receive a message.
     * Blocks for timeout until a message is available or null if timeout happens.
     *
     * @param timeout Timeout to wait.
     * @return A message or null if timeout happens.
     * @throws InterruptedException If the thread was interrupted while waiting for a message.
     * @throws DisconnectedException If the connection is disconnected before or during the call.
     */
    public MessageIn receive(Duration timeout) throws InterruptedException, DisconnectedException {
        if (!isConnected()) {
            throw new DisconnectedException("Can't receive a message from a disconnected connection.");
        }

        MessageQueueItem<MessageIn> item = incomingQueue.poll(timeout.toMillis(), TimeUnit.MILLISECONDS);

        if (item == null) {
            return null;
        }

        MessageIn message = item.getMessage();

        if (message == null) {
            // empty item -> connection closed
            throw new DisconnectedException("Connection was lost during the message receiving");
        }

        return message;
    }

    // --- IConnectionLossHandler ---

    @Override
    synchronized public void handleConnectionLoss(ConnectionLossCause cause) {
        close();
        // notify upper layer about the connection loss
        connectionLossHandler.handleConnectionLoss(cause);
    }

    // --- IIncomingMessageHandler ---

    @Override
    public void handleIncomingMessage(MessageIn message) throws InterruptedException {
        this.incomingQueue.put(new MessageQueueItem<>(message));
    }
}
