package cz.zcu.kiv.krysl.bsclient.net.connection;

import cz.zcu.kiv.krysl.bsclient.net.IMessage;
import cz.zcu.kiv.krysl.bsclient.net.NotConnectedException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Connection implements ISocketClosedHandler {
    private static final int MESSAGE_QUEUE_CAPACITY = 100;

    private InetSocketAddress serverAddress;
    private ICodec codec;
    private IConnectionEventHandler connectionManager;

    private final LinkedBlockingQueue<IMessage> incomingQueue;
    private final LinkedBlockingQueue<IMessage> outgoingQueue;
    private Supervisor supervisor;
    private Receiver receiver;
    private Sender sender;
    private Socket socket;

    public Connection(InetSocketAddress serverAddress,
                      ICodec codec,
                      IConnectionEventHandler connectionManager) throws IOException {
        this.serverAddress = serverAddress;
        this.codec = codec;
        this.connectionManager = connectionManager;

        this.incomingQueue = new LinkedBlockingQueue<>(MESSAGE_QUEUE_CAPACITY);
        this.outgoingQueue = new LinkedBlockingQueue<>(MESSAGE_QUEUE_CAPACITY);

        connect();
    }

    public void reconnect() throws IOException {
        close();
        connect();
    }

    private void connect() throws IOException {
        this.socket = new Socket();
        this.socket.connect(serverAddress);
        this.socket.setTcpNoDelay(true);

        this.receiver = new Receiver(socket.getInputStream(), codec.newDeserializer(), incomingQueue, this);
        this.receiver.setDaemon(false);
        this.receiver.start();

        this.sender = new Sender(socket.getOutputStream(), codec.newSerializer(), outgoingQueue, this);
        this.sender.setDaemon(false);
        this.sender.start();
    }

    /**
     * Closes connection and cancels and joins all its worker threads.
     */
    public void close() throws IOException {
        this.receiver.cancel();
        this.sender.cancel();
        this.socket.close();

        // join worker threads
        while (true) {
            try {
                this.receiver.join();
                break;
            } catch (InterruptedException e) {
                // do nothing, just try to join again
            }
        }

        while (true) {
            try {
                this.sender.join();
                break;
            } catch (InterruptedException e) {
                // do nothing, just try to join again
            }
        }
    }

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
    public boolean send(IMessage message, Duration timeout) throws InterruptedException {
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
    public IMessage receive(Duration timeout) throws InterruptedException {
        return incomingQueue.poll(timeout.toMillis(), TimeUnit.MILLISECONDS);
    }

    // --- ISocketClosedHandler ---

    @Override
    synchronized public void handleSocketClosed(SocketCloseCause cause) {
        close();
    }
}
