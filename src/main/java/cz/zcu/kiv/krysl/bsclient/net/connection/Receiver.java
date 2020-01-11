package cz.zcu.kiv.krysl.bsclient.net.connection;


import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread responsible for receiving messages from the remote point.
 * Continuously reads from the stream and deserializes incoming messages
 * which are then pushed to the provided incoming queue.
 * run() method must be called to start the receiving thread.
 */
public class Receiver<MessageIn> extends Thread {
    private static final int BUFFER_SIZE = 1024;

    private InputStream stream;
    private IDeserializer<MessageIn> deserializer;
    private IReceiverManager<MessageIn> receiverManager;

    private final byte[] buffer;
    private AtomicBoolean keepRunning;

    /**
     * Create the receiver.
     *
     * @param stream The stream to read from.
     * @param deserializer The deserializer used for message deserialization.
     * @param receiverManager The handler which will be called when the connection is lost or a new message is received.
     */
    public Receiver(InputStream stream,
                    IDeserializer<MessageIn> deserializer,
                    IReceiverManager<MessageIn> receiverManager) {
        super("Receiver");
        this.stream = stream;
        this.deserializer = deserializer;
        this.receiverManager = receiverManager;
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
        // interrupt blocking handleIncomingMessage call on messageHandler
        this.interrupt();
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

                // process incoming data
                List<MessageIn> messages = deserializer.deserialize(Arrays.copyOfRange(buffer, 0, bytesRead));

                for (MessageIn message : messages) {
                    receiverManager.handleMessageReceived(message);
                }

            } catch (DeserializeException e) {
                // stream corrupted
                // notify upper layer
                receiverManager.handleConnectionLost(ConnectionLossCause.CORRUPTED);
                keepRunning.set(false);
                break;
            } catch (IOException e) {
                // stream closed
                break;
            }
        }

        if (keepRunning.get()) {
            // unwanted connection loss
            // notify upper layer
            receiverManager.handleConnectionLost(ConnectionLossCause.CLOSED);
        }
    }
}
