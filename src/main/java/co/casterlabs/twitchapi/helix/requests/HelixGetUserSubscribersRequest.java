package co.casterlabs.twitchapi.helix.requests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import co.casterlabs.apiutil.web.ApiException;
import co.casterlabs.apiutil.web.AuthenticatedWebRequest;
import co.casterlabs.twitchapi.HttpUtil;
import co.casterlabs.twitchapi.TwitchApi;
import co.casterlabs.twitchapi.helix.TwitchHelixAuth;
import co.casterlabs.twitchapi.helix.types.HelixSubscriber;
import co.casterlabs.twitchapi.helix.types.HelixUser;
import lombok.NonNull;
import okhttp3.Response;

public class HelixGetUserSubscribersRequest extends AuthenticatedWebRequest<List<HelixSubscriber>, TwitchHelixAuth> {
    private String id;

    public HelixGetUserSubscribersRequest(@NonNull String id, @NonNull TwitchHelixAuth auth) {
        super(auth);
        this.id = id;
    }

    public HelixGetUserSubscribersRequest(@NonNull HelixUser user, @NonNull TwitchHelixAuth auth) {
        super(auth);
        this.id = user.getId();
    }

    @Override
    public List<HelixSubscriber> execute() throws ApiException, IOException {
        List<HelixSubscriber> subscribers = new ArrayList<>();

        String after = "";
        while (true) {
            this.auth.getRateLimiter().block();

            String url = String.format("https://api.twitch.tv/helix/subscriptions?first=100&broadcaster_id=%s&after=%s", this.id, after);
            Response response = HttpUtil.sendHttpGet(url, null, this.auth);
            JsonObject json = TwitchApi.GSON.fromJson(response.body().string(), JsonObject.class);

            response.close();

            if (response.code() == 200) {
                JsonArray data = json.getAsJsonArray("data");

                for (JsonElement e : data) {
                    HelixSubscriber subscriber = TwitchApi.GSON.fromJson(e, HelixSubscriber.class);

                    if (subscriber.getUserId() != this.id) {
                        subscribers.add(subscriber);
                    }
                }

                JsonObject pagination = json.getAsJsonObject("pagination");

                if (pagination.has("cursor")) {
                    after = pagination.get("cursor").getAsString();
                } else {
                    break;
                }
            } else {
                throw new ApiException("Unable to get subscribers: " + json.get("message").getAsString());
            }
        }

        return subscribers;
    }

}
