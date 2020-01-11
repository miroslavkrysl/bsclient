package cz.zcu.kiv.krysl.bsclient.net.client;

import cz.zcu.kiv.krysl.bsclient.net.connection.ISerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread responsible for sending messages to the remote point.
 * Continuously serializes available outgoing messages
 * which are then written into to the output stream.
 * run() method must be called to start the sending thread.
 */
class Sender<MessageOut> extends Thread {

    private OutputStream stream;
    private ISerializer<MessageOut> serializer;
    private AtomicBoolean keepRunning;
    private BlockingQueue<MessageOut> outgoingQueue;

    /**
     * Create the sender.
     *
     * @param stream The stream to write into.
     * @param serializer The serializer used for message serialization.
     * @param outgoingQueue The queue to take messages to be sent from.
     */
    public Sender(OutputStream stream,
                    ISerializer<MessageOut> serializer,
                    BlockingQueue<MessageOut> outgoingQueue) {
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
                MessageOut message = outgoingQueue.take();
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
