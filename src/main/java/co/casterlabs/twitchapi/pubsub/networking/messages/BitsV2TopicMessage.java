package co.casterlabs.twitchapi.pubsub.networking.messages;

import com.google.gson.annotations.SerializedName;

import co.casterlabs.twitchapi.pubsub.PubSubTopic;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BitsV2TopicMessage implements PubSubMessage {
    @SerializedName("user_name")
    private String username;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("channel_name")
    private String channelname;

    @SerializedName("channel_id")
    private String channelId;

    @SerializedName("chat_message")
    private String chatMessage;

    @SerializedName("bits_used")
    private int bitsUsed;

    @SerializedName("total_bits_used")
    private int totalBitsUsed;

    private String context;

    // Injected in the parser.
    private String messageId;

    public boolean isAnonymous() {
        return this.userId == null;
    }

    @Override
    public PubSubTopic getType() {
        return PubSubTopic.BITS_v2;
    }

}
