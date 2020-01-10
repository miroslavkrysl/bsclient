package cz.zcu.kiv.krysl.bsclient.net.codec;

import cz.zcu.kiv.krysl.bsclient.net.DeserializeException;
import cz.zcu.kiv.krysl.bsclient.util.Helper;

import java.util.LinkedList;
import java.util.List;

public class PayloadDeserializer {
    private static final String CHARS_TO_UNESCAPE = "" + Constants.PAYLOAD_ITEM_SEPARATOR + Constants.ESCAPE;
    private final LinkedList<String> items;

    public PayloadDeserializer() {
        this.items = null;
    }

    public PayloadDeserializer(String serialized) {
        this.items = new LinkedList<>();

        List<String> parts = Helper.splitCharSequence(serialized, Constants.PAYLOAD_ITEM_SEPARATOR, Constants.ESCAPE);

        for (String part : parts) {
            items.add(Helper.unescapeChars(part, CHARS_TO_UNESCAPE, Constants.ESCAPE));
        }
    }

    private String getItem() throws DeserializeException {
        if (items == null) {
            throw new DeserializeException("No payload is present.");
        }

        if (items.isEmpty()) {
            throw new DeserializeException("No more items in payload.");
        }

        return items.removeFirst();
    }

    public int getInt() throws DeserializeException {
        try {
            return Integer.parseInt(getItem());
        } catch (NumberFormatException e) {
            throw new DeserializeException("Can't deserialize int from payload item");
        }
    }

    public String getString() throws DeserializeException {
        return getItem();
    }
}
