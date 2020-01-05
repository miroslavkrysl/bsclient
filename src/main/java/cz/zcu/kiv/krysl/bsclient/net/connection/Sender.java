package cz.zcu.kiv.krysl.bsclient.net.connection;

import cz.zcu.kiv.krysl.bsclient.net.IMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Sender extends Thread {

    private OutputStream stream;
    private ISerializer serializer;
    private IConnectionEventHandler connectionManager;
    private AtomicBoolean keepRunning;
    private BlockingQueue<IMessage> outgoingQueue;

    public Sender(OutputStream stream,
                    ISerializer serializer,
                    BlockingQueue<IMessage> outgoingQueue,
                    ISocketClosedHandler socketClosedHandler) {
        super("Sender");
        this.stream = stream;
        this.serializer = serializer;
        this.outgoingQueue = outgoingQueue;
        this.connectionManager = connectionManager;
        this.keepRunning = new AtomicBoolean(true);
    }
    /**
     * Request receiver thread to stop.
     * Sets keepRunning flag to false.
     * Associated stream should be closed too to stop the blocking write call.
     */
    public void cancel() {
        this.keepRunning.set(false);
        this.interrupt();
    }

    /**
     * Execute the writing. It runs in a separate thread.
     */
    @Override
    public void run() {
        while (keepRunning.get()) {
            try {
                IMessage message = outgoingQueue.take();
                byte[] serialized = serializer.serialize(message);
                stream.write(serialized);

                int bytesWritten = serialized.length;
            } catch (IOException e) {
                // stream closed
                keepRunning.set(false);
            } catch (InterruptedException e) {
                // interrupted from the blocking queue take() call;
            }
        }
    }
}
