package cz.zcu.kiv.krysl.bsclient.net.client;

/**
 * Enum containing causes of offline state.
 */
public enum OfflineCause {
    /**
     * Connection stream was closed either by remote point or by OS.
     */
    CLOSED,

    /**
     * Messages couldn't be properly deserialized from the connection stream.
     */
    CORRUPTED,

    /**
     * An unexpected message was received.
     */
    INVALID_MESSAGE,

    /**
     * Remote point is not responding for an certain period.
     */
    UNAVAILABLE;

    public String getMessage() {
        switch (this) {
            case CLOSED:
                return "Connection closed.";
            case CORRUPTED:
                return "Message stream corrupted.";
            case INVALID_MESSAGE:
                return "Invalid message received.";
            case UNAVAILABLE:
                return "Server unavailable.";
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
