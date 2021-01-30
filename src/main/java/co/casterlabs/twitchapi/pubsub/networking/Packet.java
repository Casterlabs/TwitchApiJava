package co.casterlabs.twitchapi.pubsub.networking;

import com.google.gson.JsonObject;

public abstract class Packet<T extends Enum<?>> {

    public abstract JsonObject serialize();

    public abstract T getPacketType();

}
