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
import co.casterlabs.twitchapi.helix.requests.HelixGetUserFollowersRequest.HelixFollowersResult;
import co.casterlabs.twitchapi.helix.types.HelixFollower;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import okhttp3.Response;

public class HelixGetUserFollowersRequest extends AuthenticatedWebRequest<HelixFollowersResult, TwitchHelixAuth> {
    private @Setter boolean getAll = false;
    private @Setter int first = 20;
    private String id;

    public HelixGetUserFollowersRequest(@NonNull String id, @NonNull TwitchHelixAuth auth) {
        super(auth);
        this.id = id;
    }

    @Override
    public HelixFollowersResult execute() throws ApiException, IOException {
        List<HelixFollower> followers = new ArrayList<>();
        long total = 0;

        String after = "";
        do {
            this.auth.getRateLimiter().block();

            String url = String.format("https://api.twitch.tv/helix/users/follows?first=%d&to_id=%s&after=%s", this.first, this.id, after);
            Response response = HttpUtil.sendHttpGet(url, null, this.auth);
            JsonObject json = TwitchApi.GSON.fromJson(response.body().string(), JsonObject.class);

            response.close();

            if (response.code() == 200) {
                JsonArray data = json.getAsJsonArray("data");

                total = json.get("total").getAsInt();

                for (JsonElement e : data) {
                    followers.add(TwitchApi.GSON.fromJson(e, HelixFollower.class));
                }

                JsonObject pagination = json.getAsJsonObject("pagination");

                if (pagination.has("cursor")) {
                    after = pagination.get("cursor").getAsString();
                } else {
                    break;
                }
            } else {
                throw new ApiException("Unable to get followers: " + json.get("message").getAsString());
            }
        } while (this.getAll);

        return new HelixFollowersResult(followers, total);
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class HelixFollowersResult {
        private List<HelixFollower> followers;
        private long total;

    }

}
