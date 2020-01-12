package cz.zcu.kiv.krysl.bsclient.net.client;

/**
 * Enum containing causes of unwanted loss of the connection.
 */
public enum ConnectionLossCause {
    /**
     * Connection stream was closed either by remote point or by OS.
     */
    CLOSED,

    /**
     * Messages couldn't be properly deserialized from the connection stream or no message is even expected.
     */
    CORRUPTED,

    /**
     * Remote point is not responding for an certain period.
     */
    UNAVAILABLE
}
