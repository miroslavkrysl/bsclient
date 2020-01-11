package cz.zcu.kiv.krysl.bsclient.net.connection;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.net.DisconnectedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
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
public class Connection<MessageIn, MessageOut> {

    private static final int BUFFER_SIZE = 1024;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final ISerializer<MessageOut> serializer;
    private final IDeserializer<MessageIn> deserializer;
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
     * @throws IOException When error occurs during creating the socket connection.
     */
    public Connection(InetSocketAddress serverAddress,
                      IDeserializer<MessageIn> deserializer,
                      ISerializer<MessageOut> serializer) throws IOException {

        // setup socket connection
        this.socket = new Socket();
        this.socket.connect(serverAddress);
        this.socket.setTcpNoDelay(true);

        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();

        this.serializer = serializer;
        this.deserializer = deserializer;

        this.buffer = new byte[BUFFER_SIZE];
        this.incomingQueue = new ArrayDeque<>();
    }

    /**
     * Close the connection.
     */
    public void close()  {
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
    public boolean isDisconnected() {
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

        byte[] serialized = serializer.serialize(message);

        try {
            outputStream.write(serialized);
        } catch (IOException e) {
            throw new DisconnectedException("Connection was disconnected during the message sending.");
        }

        int bytesWritten = serialized.length;
    }

    /**
     * Receive a message.
     *
     * @throws DisconnectedException If the connection is disconnected before or during the call.
     */
    public MessageIn receive() throws DisconnectedException, DeserializeException {
        while (true) {
            if (!incomingQueue.isEmpty()) {
                return incomingQueue.poll();
            }

            if (isDisconnected()) {
                throw new DisconnectedException("Can't receive a message from a disconnected connection.");
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

            } catch (IOException e) {
                // stream closed
                break;
            }
        }

        throw new DisconnectedException("Connection was disconnected during the message receiving.");
    }
}
