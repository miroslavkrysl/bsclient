package cz.zcu.kiv.krysl.bsclient.net.connection;

import cz.zcu.kiv.krysl.bsclient.net.message.Message;

import java.io.IOException;
import java.io.OutputStream;

public class ConnectionSender {
    private OutputStream os = null;
    boolean loggedOut = false;

    /**
     * Constructor
     */
    public NetworkClientManagerWrite() {
        try {
            os = socket.getOutputStream();
            if (!registered) {
                registered = true;
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        if (!loggedOut) {
                            Message msg = new MessageC2SLogout(1);
                            write(msg);
                        }
                    }
                });
            }
        } catch (IOException e) {
            logger.error(e, e);
        }
    }

    /**
     * Send message to server.
     *
     * @param message The message to send to the server.
     * @return true, if the message was sent successfully
     */
    public synchronized boolean send(Message message) {
        try {
            if (keepRunning) {
                /* We enforce the remote endpoint */
                msg.setChannel(null);
                msg.setClientID(clientid);

                /*
                 * If we are sending a out of sync message, clear the queue
                 * of messages.
                 */
                if (msg.getType() == Message.MessageType.C2S_OUTOFSYNC) {
                    processedMessages.clear();
                }

                /*
                 * If logger is enable, print the message so it shows useful
                 * debugging information.
                 */
                if (logger.isDebugEnabled()) {
                    logger.debug("build message(type=" + msg.getType() + ") from "
                            + msg.getClientID() + " full [" + msg + "]");
                }

                os.write(encoder.encode(msg));
                return true;
            } else {
                logger.warn("Write requested not to keeprunning");
                connected = false;
                return false;
            }
        } catch (IOException e) {
            logger.error("error while sending a packet (msg=(" + msg + "))", e);
            handle;
            return false;
        }
    }
}
