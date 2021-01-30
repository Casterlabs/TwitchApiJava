package co.casterlabs.twitchapi.serializers;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import co.casterlabs.twitchapi.pubsub.PubSubError;

public class PubSubErrorDeserializer implements JsonDeserializer<PubSubError> {

    @Override
    public PubSubError deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String str = json.getAsString();

        for (PubSubError error : PubSubError.values()) {
            if (error.name().equalsIgnoreCase(str)) {
                return error;
            }
        }

        return null;
    }

}
