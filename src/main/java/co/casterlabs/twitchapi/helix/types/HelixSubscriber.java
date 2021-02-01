package co.casterlabs.twitchapi.helix.types;

import com.google.gson.annotations.SerializedName;

import co.casterlabs.twitchapi.pubsub.messages.SubscriptionsV1TopicMessage.SubscriptionPlan;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HelixSubscriber {
    @SerializedName("is_gift")
    private boolean gift;

    private SubscriptionPlan tier;

    @SerializedName("plan_name")
    private String planName;

    @SerializedName("user_login")
    private String login;

    @SerializedName("user_name")
    private String displayname;

    @SerializedName("user_id")
    private String userId;

}
