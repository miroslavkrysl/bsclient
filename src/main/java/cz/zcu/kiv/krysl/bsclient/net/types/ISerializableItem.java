package cz.zcu.kiv.krysl.bsclient.net.types;

import cz.zcu.kiv.krysl.bsclient.net.codec.PayloadSerializer;

public interface ISerializableItem {
    void serialize(PayloadSerializer serializer);
}
