package co.casterlabs.twitchapi.pubsub;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PubSubTopic {
    BITS_v2("channel-bits-events-v2"),
    CHANNEL_POINTS_V1("channel-points-channel-v1"),
    SUBSCRIPTIONS_v1("channel-subscribe-events-v1");

    private String topic;

}
