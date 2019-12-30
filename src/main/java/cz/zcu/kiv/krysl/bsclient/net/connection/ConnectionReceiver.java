package cz.zcu.kiv.krysl.bsclient.net.connection;


import cz.zcu.kiv.krysl.bsclient.net.connection.Connection;

import java.io.IOException;
import java.io.InputStream;

/**
 * Thread responsible for receiving messages from server.
 * run() method must be called in order to start the thread.
 */
public class ConnectionReceiver extends Thread{
    private Connection connection;
    private InputStream stream;

    public ConnectionReceiver(Connection connection) throws IOException {
        super("ConnectionReader(" + connection.getSocket().getRemoteSocketAddress() +")");
        this.connection = connection;
    }

        /**
         * Decode a stream of bytes into a Message.
         *
         * @param data
         *            data that represent the serialized message
         * @throws IOException
         */
        private synchronized void storeMessage(byte[] data)
                throws IOException {
            try {
                List<Message> messages = decoder.decode(null, data);

                if (messages != null) {
                    for (Message msg : messages) {
                        /*
                         * If logger is enable, print the message so it shows useful
                         * debugging information.
                         */
                        if (logger.isDebugEnabled()) {
                            logger.debug("build message(type=" + msg.getType() + ") from "
                                    + msg.getClientID() + " full [" + msg + "]");
                        }

                        // Once server assign us a clientid, store it for future
                        // messages.
                        if (msg.getType() == Message.MessageType.S2C_LOGIN_SENDNONCE) {
                            clientid = msg.getClientID();
                        }

                        processedMessages.add(msg);
                    }
                }
            } catch (InvalidVersionException e) {
                shouldThrowException = true;
                storedException = e;
                logger.error("Exception when processing pending packets", e);
            }

        }

        /**
         * Keep reading from TCP stack until the whole Message has been read.
         *
         * @return a byte stream representing the message or null if client has
         *         requested to exit.
         * @throws IOException
         *             if there is any problem reading.
         */
        private byte[] readByteStream() throws IOException {
            byte[] buffer = null;
            int size = -1;
            int start = -1;
            try {
                while (stream.available() < 4) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        logger.error(e, e);
                    }
                }

                if (is.read(sizebuffer) < 0) {
                    isfinished = true;
                    return null;
                }

                size = (sizebuffer[0] & 0xFF) + ((sizebuffer[1] & 0xFF) << 8)
                        + ((sizebuffer[2] & 0xFF) << 16) + ((sizebuffer[3] & 0xFF) << 24);

                buffer = new byte[size];
                System.arraycopy(sizebuffer, 0, buffer, 0, 4);

                // read until everything is received. We have to call read
                // in a loop because the data may be split across several
                // packets.
                long startTime = System.currentTimeMillis();
                start = 4;
                int read = 0;
                long waittime = 10;
                do {
                    start = start + read;
                    read = is.read(buffer, start, size - start);
                    if (read < 0) {
                        isfinished = true;
                        return null;
                    }

                    if (System.currentTimeMillis() - 2000 > startTime) {
                        logger.warn("Waiting for more data");
                        waittime = 1000;
                    }
                    try {
                        Thread.sleep(waittime);
                    } catch (InterruptedException e) {
                        logger.error(e, e);
                    }
                } while (start + read < size);

                logger.debug("Received Marauroa Packet");

                return buffer;
            } catch (IOException e) {
                logger.warn("size buffer: " + Utility.dumpByteArray(sizebuffer));
                logger.warn("size: " + size + " start: " + start);
                logger.warn("buffer: " +  Utility.dumpByteArray(buffer));
                throw e;
            }
        }

        /**
         * Execute the reading. It runs in a separate thread.
         */
        @Override
        public void run() {
            while (connection.isConnected()) {
                try {
                    byte[] buffer = readByteStream();
                    if (buffer == null) {
                        /* User has requested exit */
                        return;
                    }

                    storeMessage(buffer);
                } catch (IOException e) {
                    connection.handleLostConnection();
                }
            }
        }
    }
}
