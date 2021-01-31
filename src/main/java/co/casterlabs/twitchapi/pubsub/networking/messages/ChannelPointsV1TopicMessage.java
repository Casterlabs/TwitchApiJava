package co.casterlabs.twitchapi.pubsub.networking.messages;

import java.time.Instant;

import com.google.gson.annotations.SerializedName;

import co.casterlabs.twitchapi.pubsub.PubSubTopic;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChannelPointsV1TopicMessage implements PubSubMessage {

    @SerializedName("channel_id")
    private String channelId;

    private ChannelPointsRedemptionStatus status;

    private String id;

    private ChannelPointsUser user;

    private ChannelPointsReward reward;

    @Override
    public PubSubTopic getType() {
        return PubSubTopic.CHANNEL_POINTS_V1;
    }

    public static enum ChannelPointsRedemptionStatus {
        FULFILLED,
        UNFULFILLED
    }

    @Getter
    @ToString
    public static class ChannelPointsReward {

        @SerializedName("background_color")
        private String backgroundColor;

        @SerializedName("channel_id")
        private String channelId;

        private String id;

        @SerializedName("cooldown_expires_at")
        private Instant cooldownExpiresAt;

        private String title;

        private String prompt;

        private int cost;

        @SerializedName("is_enabled")
        private boolean enabled;

        @SerializedName("is_in_stock")
        private boolean inStock;

        @SerializedName("is_paused")
        private boolean paused;

        @SerializedName("is_sub_only")
        private boolean subOnly;

        @SerializedName("is_user_input_required")
        private boolean userInputRequired;

        private ChannelPointsImages image;

        @SerializedName("default_image")
        private ChannelPointsImages defaultImage;

        @SerializedName("max_per_stream")
        private ChannelPointsMaxPerStream maxPerStream;

        @SerializedName("max_per_user_per_stream")
        private ChannelPointsMaxPerUserPerStream maxPerUserPerStream;

        @SerializedName("global_cooldown")
        private ChannelPointsCooldown globalCooldown;

    }

    @Getter
    @ToString
    public static class ChannelPointsUser {
        @SerializedName("login")
        private String username;

        private String id;

        @SerializedName("display_name")
        private String displayname;

    }

    @Getter
    @ToString
    public static class ChannelPointsImages {

        @SerializedName("url_1x")
        private String smallImage;

        @SerializedName("url_2x")
        private String mediumImage;

        @SerializedName("url_4x")
        private String largeImage;

    }

    @Getter
    @ToString
    public static class ChannelPointsMaxPerStream {

        @SerializedName("is_enabled")
        private boolean enabled;

        @SerializedName("max_per_stream")
        private int max;

    }

    @Getter
    @ToString
    public static class ChannelPointsMaxPerUserPerStream {

        @SerializedName("is_enabled")
        private boolean enabled;

        @SerializedName("max_per_user_per_stream")
        private int max;

    }

    @Getter
    @ToString
    public static class ChannelPointsCooldown {

        @SerializedName("is_enabled")
        private boolean enabled;

        @SerializedName("global_cooldown_seconds")
        private int globalCooldownSeconds;

    }

}
