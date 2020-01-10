package cz.zcu.kiv.krysl.bsclient.net.message.items;

import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

public interface ISerializableItem {
    void serialize(PayloadSerializer serializer);
}
