package co.casterlabs.twitchapi.pubsub;

import co.casterlabs.twitchapi.pubsub.networking.messages.PubSubMessage;

public interface PubSubListener {

    public void onError(PubSubError error);

    public void onMessage(PubSubMessage message);

}
