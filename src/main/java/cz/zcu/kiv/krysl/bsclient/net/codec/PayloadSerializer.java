package cz.zcu.kiv.krysl.bsclient.net.codec;

import cz.zcu.kiv.krysl.bsclient.util.Helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PayloadSerializer {
    private static final String CHARS_TO_ESCAPE = String.valueOf(Constants.PAYLOAD_ITEM_SEPARATOR);

    private List<String> items;

    public PayloadSerializer() {
        this.items = new ArrayList<>();
    }

    private void addItem(String item) {
        String serializedItem = Helper.escapeChars(item, CHARS_TO_ESCAPE, Constants.ESCAPE);
        items.add(serializedItem);
    }

    public void addInt(int value) {
        this.addItem(String.valueOf(value));
    }

    public void addString(String value) {
        this.addItem(value);
    }

    public String serialize() {
        StringBuilder serialized = new StringBuilder();

        Iterator<String> iterator = items.iterator();
        while (iterator.hasNext()) {
            serialized.append(iterator.next());

            if (iterator.hasNext()) {
                serialized.append(Constants.PAYLOAD_ITEM_SEPARATOR);
            }
        }

        return serialized.toString();
    }
}
