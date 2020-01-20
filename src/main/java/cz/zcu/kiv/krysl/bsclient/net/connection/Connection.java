package cz.zcu.kiv.krysl.bsclient.net.connection;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;
import cz.zcu.kiv.krysl.bsclient.net.messages.server.ServerMessage;
import cz.zcu.kiv.krysl.bsclient.net.types.RestoreState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * A communication channel.
 * Messages can be sent using the send() method and received using the receive() method.
 *
 * @param <MessageIn> Incoming messages type.
 * @param <MessageOut> Outgoing messages type.
 */
public class Connection<MessageIn, MessageOut> implements IMessageInputStream<MessageIn>, IMessageOutputStream<MessageOut> {

    private static Logger logger = LogManager.getLogger(Connection.class);
    private static final int BUFFER_SIZE = 1024;

    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final ISerializer<MessageOut> serializer;
    private final IDeserializer<MessageIn> deserializer;
    private final Duration timeout;

    private final byte[] buffer;
    private final Socket socket;
    private final Queue<MessageIn> incomingQueue;

    /**
     * Create connection to the remote endpoint.
     * Creates and sets up a socket and starts sender and receiver threads.
     *
     * @param serverAddress The address of the server.
     * @param deserializer The deserializer used for stream deserialization into messages.
     * @param serializer The serializer used for messages serialization into the stream.
     * @param timeout The timeout used when
     * @throws IOException When error occurs during creating the socket connection.
     */
    public Connection(InetSocketAddress serverAddress,
                      IDeserializer<MessageIn> deserializer,
                      ISerializer<MessageOut> serializer,
                      Duration timeout) throws IOException {

        // setup socket connection
        this.socket = new Socket();
        this.socket.setTcpNoDelay(true);

        this.timeout = timeout;

        int timeoutMillis;
        if (timeout.equals(Duration.ZERO)) {
            // infinite timeout
            timeoutMillis = 0;
        } else {
            // divide by two because of the receive call principe
            timeoutMillis = (int) timeout.toMillis() / 2;

            if (timeoutMillis == 0) {
                // very small timeout - int rounding caused result to be zero even if not
                // set minimum timeout at least
                timeoutMillis = 1;
            }
        }

        this.socket.setSoTimeout(timeoutMillis);
        this.socket.connect(serverAddress);


        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();

        this.serializer = serializer;
        this.deserializer = deserializer;

        this.buffer = new byte[BUFFER_SIZE];
        this.incomingQueue = new ArrayDeque<>();
    }

    /**
     * Get the local network connection socket address.
     *
     * @return The socket address.
     */
    public InetSocketAddress getLocalAddress() {
        return new InetSocketAddress(socket.getLocalAddress(), socket.getLocalPort());
    }

    /**
     * Close the connection.
     */
    synchronized public void close()  {
        logger.trace("closing the connection");
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
    synchronized public boolean isDisconnected() {
        return this.socket.isClosed();
    }

    /**
     * Send a message.
     *
     * @param message Message to send.
     * @throws DisconnectedException If the connection is disconnected before or during the call.
     */
    public void send(MessageOut message) throws DisconnectedException {
        if (isDisconnected()) {
            throw new DisconnectedException("Can't send a message to a disconnected connection.");
        }

        logger.debug("sending message: " + message);

        byte[] serialized = serializer.serialize(message);

        try {
            outputStream.write(serialized);
        } catch (IOException e) {
            close();
            throw new DisconnectedException("Connection was disconnected during the message sending.");
        }

        int bytesWritten = serialized.length;
    }

    /**
     * Receive a message.
     * Call timeouts after the timeout given in the constructor.
     *
     * @throws DisconnectedException If the connection is disconnected before or during the call.
     * @return Received message or null if timeout happens.
     * @throws DeserializeException When the message cant be properly deserialized from the underlying stream.
     */
    public MessageIn receive() throws DisconnectedException, DeserializeException {
        Instant start = Instant.now();

        while (true) {
            if (!incomingQueue.isEmpty()) {
                MessageIn message = incomingQueue.poll();
                logger.debug("received message: " + message);
                return message;
            }

            if (isDisconnected()) {
                throw new DisconnectedException("Connection is disconnected.");
            }

            if (timeout != Duration.ZERO) {
                // timeout is not infinite

                Instant now = Instant.now();
                Duration elapsed = Duration.between(start, now);

                if (elapsed.compareTo(timeout) >= 0) {
                    //timeout happened
                    return null;
                }
            }

            try {
                // read available bytes from stream into buffer
                int bytesRead = inputStream.read(buffer);

                if (bytesRead <= 0) {
                    // stream closed
                    break;
                }

                // deserialize incoming bytes
                List<MessageIn> messages = deserializer.deserialize(Arrays.copyOfRange(buffer, 0, bytesRead));
                incomingQueue.addAll(messages);

            } catch (SocketTimeoutException e) {
                // socket read timed out
            }
            catch (IOException e) {
                // stream closed
                break;
            }
        }

        close();
        throw new DisconnectedException("Connection was disconnected during the message receiving.");
    }
}
