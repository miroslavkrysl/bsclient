package cz.zcu.kiv.krysl.bsclient.net.message;

import cz.zcu.kiv.krysl.bsclient.util.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Payload {
    private static final char ITEM_SEPARATOR = ';';
    private static final char ESCAPE = '\\';
    private static final char[] CHARS_TO_ESCAPE = new char[] {ESCAPE};

    private ArrayList<String> items;

    public Payload() {
        this.items = new ArrayList<>();
    }

    public Payload(String serialized) {
        this.items = new ArrayList<>();

        // split payload into items
        List<String> parts = Helper.splitString(serialized, ITEM_SEPARATOR, ESCAPE);

        // unescape every item (the item separator character)
        parts = parts.stream().map(s -> Helper.unescape(s, CHARS_TO_ESCAPE, ESCAPE)).collect(Collectors.toList());

        items.addAll(parts);
    }

    public void addItem(String item) {
        items.add(item);
    }

    public List<String> getItems() {
        return Collections.unmodifiableList(items);
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();

        Iterator<String> iter = items.iterator();
        while (iter.hasNext()) {
            String item = iter.next();
            item = Helper.escape(item, CHARS_TO_ESCAPE, ESCAPE);
            sb.append(item);

            if (iter.hasNext()) {
                sb.append(ITEM_SEPARATOR);
            }
        }

        return sb.toString();
    }
}
