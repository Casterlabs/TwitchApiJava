package co.casterlabs.twitchapi.pubsub.networking.messages;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import co.casterlabs.twitchapi.pubsub.PubSubTopic;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SubscriptionsV1TopicMessage implements PubSubMessage {
    @SerializedName("user_name")
    private String username;

    @SerializedName("display_name")
    private String displayname;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("channel_name")
    private String channelname;

    @SerializedName("channel_id")
    private String channelId;

    @SerializedName("sub_plan")
    private SubscriptionPlan subPlan;

    @SerializedName("sub_plan_name")
    private String planName;

    private SubscriptionContext context;

    @SerializedName("multi_month_duration")
    private int monthDuration = 1;

    @SerializedName("cumulative_months")
    private int cumulativeMonths;

    @SerializedName("streak_months")
    private int streakMonths;

    @SerializedName("is_gift")
    private boolean gift;

    @SerializedName("recipient_user_name")
    private String recipientUsername;

    @SerializedName("recipient_display_name")
    private String recipientDisplayname;

    @SerializedName("recipient_id")
    private String recipientUserId;

    @SerializedName("sub_message")
    private SubMessage message;

    public boolean isAnonymous() {
        return this.userId == null;
    }

    @Override
    public PubSubTopic getType() {
        return PubSubTopic.BITS_v2;
    }

    @Getter
    @ToString
    public static class SubMessage {
        private List<MessageEmote> emotes;
        private String message;

    }

    @Getter
    @ToString
    public static class MessageEmote {
        private int start;
        private int end;
        private int id;

    }

    public static enum SubscriptionContext {
        RESUB,
        SUBGIFT,
        ANONSUBGIFT;

    }

    public static enum SubscriptionPlan {
        UNKNOWN,
        PRIME,
        TIER_1,
        TIER_2,
        TIER_3;

    }

}
