package co.casterlabs.twitchapi.serializers;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import co.casterlabs.twitchapi.pubsub.networking.messages.SubscriptionsV1TopicMessage.SubscriptionPlan;

public class SubscriptionPlanDeserializer implements JsonDeserializer<SubscriptionPlan> {

    @Override
    public SubscriptionPlan deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String str = json.getAsString();

        switch (str) {
            case "1":
                return SubscriptionPlan.PRIME;

            case "1000":
                return SubscriptionPlan.TIER_1;

            case "2000":
                return SubscriptionPlan.TIER_2;

            case "3000":
                return SubscriptionPlan.TIER_3;

            default:
                return SubscriptionPlan.UNKNOWN;
        }
    }

}
