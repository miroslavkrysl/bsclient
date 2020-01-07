package cz.zcu.kiv.krysl.bsclient.net.connection;


import cz.zcu.kiv.krysl.bsclient.net.IMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread responsible for receiving messages from server.
 * Continuously reads from the stream and deserializes incoming message
 * which are then pushed to the provided incoming queue.
 * run() method must be called to start the receiving thread.
 */
public class Receiver extends Thread {
    private static final int BUFFER_SIZE = 1024;

    private final byte[] buffer;
    private InputStream stream;
    private IDeserializer deserializer;
    private IConnectionLossHandler connectionLossHandler;
    private AtomicBoolean keepRunning;
    private BlockingQueue<IMessage> incomingQueue;

    /**
     * Create the receiver.
     *
     * @param stream The stream to read from.
     * @param deserializer The deserializer used for message deserialization.
     * @param incomingQueue The queue to push received messages into.
     * @param connectionLossHandler The handler which will be called when the connection loss occurs.
     */
    public Receiver(InputStream stream,
                    IDeserializer deserializer,
                    BlockingQueue<IMessage> incomingQueue,
                    IConnectionLossHandler connectionLossHandler) {
        super("Receiver");
        this.stream = stream;
        this.deserializer = deserializer;
        this.incomingQueue = incomingQueue;
        this.connectionLossHandler = connectionLossHandler;
        this.buffer = new byte[BUFFER_SIZE];
        this.keepRunning = new AtomicBoolean(true);
    }

    /**
     * Request the receiver thread to stop.
     * Sets keepRunning flag to false.
     * Associated stream should be closed too to stop the blocking read call.
     */
    public void cancel() {
        this.keepRunning.set(false);
    }

    /**
     * Run the receiving logic. It runs in a separate thread.
     */
    @Override
    public void run() {
        while (keepRunning.get()) {
            try {
                // read available bytes from stream
                int bytesRead = stream.read(buffer);

                if (bytesRead <= 0) {
                    // stream closed
                    break;
                }

                try {
                    // process incoming data
                    IMessage[] messages = deserializer.deserialize(Arrays.copyOfRange(buffer, 0, bytesRead));

                    for (IMessage message : messages) {
                        incomingQueue.offer(message);
                    }
                } catch (IOException e) {
                    // stream corrupted
                    // notify upper layer
                    connectionLossHandler.handleConnectionLoss(ConnectionLossCause.CORRUPTED);
                    keepRunning.set(false);
                    break;
                }
            } catch (IOException e) {
                // stream closed
                break;
            }
        }

        if (keepRunning.get()) {
            // unwanted connection loss
            // notify upper layer
            connectionLossHandler.handleConnectionLoss(ConnectionLossCause.CLOSED);
        }
    }
}
