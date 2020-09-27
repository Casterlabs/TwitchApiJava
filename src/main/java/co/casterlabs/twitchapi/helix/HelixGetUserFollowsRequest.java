package co.casterlabs.twitchapi.helix;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import co.casterlabs.apiutil.auth.ApiAuthException;
import co.casterlabs.apiutil.web.ApiException;
import co.casterlabs.apiutil.web.AuthenticatedWebRequest;
import co.casterlabs.twitchapi.HttpUtil;
import co.casterlabs.twitchapi.TwitchApi;
import co.casterlabs.twitchapi.helix.HelixGetUserFollowsRequest.HelixFollower;
import co.casterlabs.twitchapi.helix.HelixGetUsersRequest.HelixUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import okhttp3.Response;

public class HelixGetUserFollowsRequest extends AuthenticatedWebRequest<List<HelixFollower>, TwitchHelixAuth> {
    private String id;

    public HelixGetUserFollowsRequest(@NonNull String id, @NonNull TwitchHelixAuth auth) {
        super(auth);
        this.id = id;
    }

    @Override
    public List<HelixFollower> execute() throws ApiException, IOException {
        this.auth.getRateLimiter().block();

        List<HelixFollower> followers = new ArrayList<>();
        String after = "";

        while (after != null) {
            String url = String.format("https://api.twitch.tv/helix/users/follows?first=100&to_id=%s&after=%s", this.id, after);
            Response response = HttpUtil.sendHttpGet(url, null, this.auth);
            JsonObject json = TwitchApi.GSON.fromJson(response.body().string(), JsonObject.class);

            if (response.code() == 200) {
                JsonArray data = json.getAsJsonArray("data");

                for (JsonElement e : data) {
                    followers.add(TwitchApi.GSON.fromJson(e, HelixFollower.class));
                }

                JsonObject pagination = json.getAsJsonObject("pagination");

                if (pagination.has("cursor")) {
                    after = pagination.get("cursor").getAsString();
                } else {
                    after = null;
                }
            } else {
                throw new ApiException("Unable to get followers: " + json.get("message").getAsString());
            }
        }

        return followers;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class HelixFollower {
        @SerializedName("from_id")
        private @NonNull String id;
        @SerializedName("followed_at")
        private @NonNull Instant followedAt;

        public HelixUser getAsUser(@NonNull TwitchHelixAuth auth) throws ApiAuthException, ApiException, IOException {
            return new HelixGetUsersRequest(auth).addId(this.id).execute().get(0);
        }

    }

}
