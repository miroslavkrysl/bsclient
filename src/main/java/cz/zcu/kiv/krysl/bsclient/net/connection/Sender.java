package cz.zcu.kiv.krysl.bsclient.net.connection;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Sender extends Thread {

    private OutputStream stream;
    private ISerializer serializer;
    private AtomicBoolean keepRunning;
    private BlockingQueue<IMessage> outgoingQueue;

    /**
     * Create the receiver.
     *
     * @param stream The stream to write into.
     * @param serializer The serializer used for message serialization.
     * @param outgoingQueue The queue to take messages to be sent from.
     */
    public Sender(OutputStream stream,
                    ISerializer serializer,
                    BlockingQueue<IMessage> outgoingQueue) {
        super("Sender");
        this.stream = stream;
        this.serializer = serializer;
        this.outgoingQueue = outgoingQueue;
        this.keepRunning = new AtomicBoolean(true);
    }

    /**
     * Request the sender thread to stop.
     * Sets keepRunning flag to false.
     * Associated stream should be closed too to stop the blocking write call.
     */
    public void cancel() {
        this.keepRunning.set(false);
        // interrupt blocking take() call on outgoingQueue
        this.interrupt();
    }

    /**
     * Run the sending logic. It runs in a separate thread.
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
                break;
            } catch (InterruptedException e) {
                // interrupted from the blocking queue take() call;
            }
        }
    }
}
