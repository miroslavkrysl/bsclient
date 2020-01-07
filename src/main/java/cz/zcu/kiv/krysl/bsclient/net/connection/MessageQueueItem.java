package cz.zcu.kiv.krysl.bsclient.net.connection;

/**
 * Class representing message queue item.
 * It could be a Message or it could be empty.
 *
 * @param <Message> A message type, than can be hold by this class.
 */
public class MessageQueueItem<Message> {
    /**
     * Message hold by this item.
     */
    private Message message;

    /**
     * Create an empty item.
     */
    public MessageQueueItem() {
        this.message = null;
    }

    /**
     * Create an item holding a message.
     *
     * @param message Message to be hold by this item.
     */
    public MessageQueueItem(Message message) {
        this.message = message;
    }

    /**
     * Get the contained message value.
     *
     * @return Message value, or null if the contained item is not a message.
     */
    public Message getMessage() {
        return message;
    }
}
