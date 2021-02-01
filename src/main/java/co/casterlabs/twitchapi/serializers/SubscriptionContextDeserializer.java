package co.casterlabs.twitchapi.serializers;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import co.casterlabs.twitchapi.pubsub.messages.SubscriptionsV1TopicMessage.SubscriptionContext;

public class SubscriptionContextDeserializer implements JsonDeserializer<SubscriptionContext> {

    @Override
    public SubscriptionContext deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return SubscriptionContext.valueOf(json.getAsString().toUpperCase());
    }

}
