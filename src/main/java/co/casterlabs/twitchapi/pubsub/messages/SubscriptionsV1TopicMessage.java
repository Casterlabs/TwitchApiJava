package co.casterlabs.twitchapi.pubsub.messages;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.gson.annotations.SerializedName;

import co.casterlabs.twitchapi.pubsub.PubSubTopic;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class SubscriptionsV1TopicMessage implements PubSubMessage {
    @Nullable
    @SerializedName("user_name")
    private String username;

    @Nullable
    @SerializedName("display_name")
    private String displayname;

    @Nullable
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

    @Nullable
    @SerializedName("recipient_user_name")
    private String recipientUsername;

    @Nullable
    @SerializedName("recipient_display_name")
    private String recipientDisplayname;

    @Nullable
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
        @Nullable
        private List<MessageEmote> emotes;
        private String message;

        public String getEmoteText(@NonNull MessageEmote emote) {
            return this.message.substring(emote.start, emote.end);
        }

    }

    @Getter
    @ToString
    public static class MessageEmote {
        private static final String EMOTE_LINK_BASE = "https://static-cdn.jtvnw.net/emoticons/v1/";

        private int start;
        private int end;
        private int id;

        public String getSmallImageLink() {
            return EMOTE_LINK_BASE + this.id + "/1.0";
        }

        public String getMediumImageLink() {
            return EMOTE_LINK_BASE + this.id + "/2.0";
        }

        public String getLargeImageLink() {
            return EMOTE_LINK_BASE + this.id + "/3.0";
        }

    }

    public static enum SubscriptionContext {
        SUB,
        RESUB,

        SUBGIFT,
        RESUBGIFT,

        ANONSUBGIFT,
        ANONRESUBGIFT;

    }

    public static enum SubscriptionPlan {
        UNKNOWN,
        PRIME,
        TIER_1,
        TIER_2,
        TIER_3;

    }

}
