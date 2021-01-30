package co.casterlabs.twitchapi.pubsub;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import co.casterlabs.twitchapi.helix.TwitchHelixRefreshTokenAuth;
import co.casterlabs.twitchapi.pubsub.networking.OutgoingMessageType;
import co.casterlabs.twitchapi.pubsub.networking.Packet;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class PubSubListenRequest extends Packet<OutgoingMessageType> {
    private List<String> topics = new ArrayList<>();
    private @Setter boolean unlistenMode = false;
    private @Setter boolean sealed = false;

    private @NonNull TwitchHelixRefreshTokenAuth auth;
    private @NonNull PubSubListener listener;

    public PubSubListenRequest addTopic(@NonNull PubSubTopic topic, @NonNull String userId) {
        if (this.sealed) {
            throw new IllegalStateException("Request is sealed, create another.");
        } else {
            this.topics.add(topic.getTopic() + "." + userId);

            return this;
        }
    }

    @Override
    public OutgoingMessageType getPacketType() {
        return this.unlistenMode ? OutgoingMessageType.UNLISTEN : OutgoingMessageType.LISTEN;
    }

    @Override
    public JsonObject serialize() {
        this.sealed = true;

        JsonObject json = new JsonObject();
        JsonObject data = new JsonObject();
        JsonArray topics = new JsonArray();

        for (String topic : this.topics) {
            topics.add(topic);
        }

        data.addProperty("auth_token", this.auth.getAccessToken());
        data.add("topics", topics);

        json.addProperty("type", this.getPacketType().name());
        json.addProperty("nonce", this.topics.get(0));
        json.add("data", data);

        return json;
    }

}
