package cz.zcu.kiv.krysl.bsclient.net;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class BsMessageSerializer implements ISerializer {
    private ByteArrayOutputStream data;

    @Override
    public void serializeString(String item) {
        data.writeBytes(item.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void serializeInt(int item) {

    }

    @Override
    public void serializeBoolean(boolean item) {

    }

    @Override
    public void serializeArray(Serializable[] items) {

    }
}
