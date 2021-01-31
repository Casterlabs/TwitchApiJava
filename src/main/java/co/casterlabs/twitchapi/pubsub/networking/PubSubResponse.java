package co.casterlabs.twitchapi.pubsub.networking;

import com.google.gson.JsonObject;

import co.casterlabs.twitchapi.TwitchApi;
import co.casterlabs.twitchapi.pubsub.PubSubError;
import co.casterlabs.twitchapi.pubsub.PubSubTopic;
import co.casterlabs.twitchapi.pubsub.networking.messages.BitsV2TopicMessage;
import co.casterlabs.twitchapi.pubsub.networking.messages.ChannelPointsV1TopicMessage;
import co.casterlabs.twitchapi.pubsub.networking.messages.PubSubMessage;
import co.casterlabs.twitchapi.pubsub.networking.messages.SubscriptionsV1TopicMessage;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PubSubResponse {
    private IncomingMessageType type;

    private PubSubError error;
    private String nonce;

    private PubSubData data;

    @Getter
    @ToString
    public static class PubSubData {
        private String topic;
        private String message;

        public PubSubMessage deserializeMessage() {
            JsonObject message = TwitchApi.GSON.fromJson(this.message, JsonObject.class);

            switch (this.getTopicType()) {
                case BITS_v2:
                    JsonObject bitsData = message.getAsJsonObject("data");

                    bitsData.add("messageId", message.get("message_id"));

                    return TwitchApi.GSON.fromJson(bitsData, BitsV2TopicMessage.class);

                case SUBSCRIPTIONS_v1:
                    return TwitchApi.GSON.fromJson(message, SubscriptionsV1TopicMessage.class);

                case CHANNEL_POINTS_V1:
                    JsonObject pointsData = message.getAsJsonObject("data");

                    return TwitchApi.GSON.fromJson(pointsData.get("redemption"), ChannelPointsV1TopicMessage.class);

                default:
                    return null;
            }
        }

        public PubSubTopic getTopicType() {
            for (PubSubTopic topic : PubSubTopic.values()) {
                if (this.topic.startsWith(topic.getTopic())) {
                    return topic;
                }
            }

            return null;
        }

    }

}
