package cz.zcu.kiv.krysl.bsclient.net.connection;


import cz.zcu.kiv.krysl.bsclient.net.IMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread responsible for receiving messages from server.
 * run() method must be called in order to start the thread.
 */
public class Receiver extends Thread {
    private static final int BUFFER_SIZE = 1024;

    private final byte[] buffer;
    private InputStream stream;
    private IDeserializer deserializer;
    private ISocketClosedHandler socketClosedHandler;
    private AtomicBoolean keepRunning;
    private BlockingQueue<IMessage> incomingQueue;

    public Receiver(InputStream stream,
                    IDeserializer deserializer,
                    BlockingQueue<IMessage> incomingQueue,
                    ISocketClosedHandler socketClosedHandler) {
        super("Receiver");
        this.stream = stream;
        this.deserializer = deserializer;
        this.incomingQueue = incomingQueue;
        this.socketClosedHandler = socketClosedHandler;
        this.buffer = new byte[BUFFER_SIZE];
        this.keepRunning = new AtomicBoolean(true);
    }

    /**
     * Request receiver thread to stop.
     * Sets keepRunning flag to false.
     * Associated stream should be closed too to stop the blocking read call.
     */
    public void cancel() {
        this.keepRunning.set(false);
    }

    /**
     * Execute the reading. It runs in a separate thread.
     */
    @Override
    public void run() {
        while (keepRunning.get()) {
            try {
                // read available bytes from stream
                int bytesRead = stream.read(buffer);

                if (bytesRead <= 0) {
                    // stream closed
                    keepRunning.set(false);
                    continue;
                }

                IMessage[] messages = deserializer.deserialize(Arrays.copyOfRange(buffer, 0, bytesRead));

                for (IMessage message : messages) {
                    incomingQueue.offer(message);
                }
            } catch (IOException e) {
                // stream closed
                keepRunning.set(false);
            }
        }
    }
}
