package co.casterlabs.twitchapi.helix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import co.casterlabs.apiutil.auth.ApiAuthException;
import co.casterlabs.apiutil.web.ApiException;
import co.casterlabs.apiutil.web.AuthenticatedWebRequest;
import co.casterlabs.twitchapi.HttpUtil;
import co.casterlabs.twitchapi.TwitchApi;
import co.casterlabs.twitchapi.helix.HelixGetUsersRequest.HelixUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import okhttp3.Response;

public class HelixGetUsersRequest extends AuthenticatedWebRequest<List<HelixUser>, TwitchHelixAuth> {
    private Set<String> logins = new HashSet<>();
    private Set<String> ids = new HashSet<>();

    public HelixGetUsersRequest(@NonNull TwitchHelixAuth auth) {
        super(auth);
    }

    public HelixGetUsersRequest addLogin(@NonNull String login) {
        this.logins.add(login);
        return this;
    }

    public HelixGetUsersRequest addId(@NonNull String id) {
        this.ids.add(id);
        return this;
    }

    @Override
    public List<HelixUser> execute() throws ApiException, ApiAuthException, IOException {
        this.auth.getRateLimiter().block();

        if (!this.ids.isEmpty() || !this.logins.isEmpty()) {
            Set<HelixUser> users = new HashSet<>();
            StringBuilder sb = new StringBuilder("https://api.twitch.tv/helix/users?");

            this.ids.forEach((id) -> sb.append("&id=").append(id));
            this.logins.forEach((login) -> sb.append("&login=").append(login));

            String url = sb.toString().replaceFirst("&", "");
            Response response = HttpUtil.sendHttpGet(url, null, auth);
            JsonObject json = TwitchApi.GSON.fromJson(response.body().string(), JsonObject.class);

            if (response.code() == 200) {
                JsonArray data = json.getAsJsonArray("data");

                for (JsonElement e : data) {
                    users.add(TwitchApi.GSON.fromJson(e, HelixUser.class));
                }
            } else {
                throw new ApiException("Unable to get users: " + json.get("message").getAsString());
            }

            return new ArrayList<>(users);
        } else {
            return Collections.emptyList();
        }
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    public static class HelixUser {
        private @NonNull String id;
        private @NonNull String login;
        @SerializedName("display_name")
        private @NonNull String displayName;
        private @NonNull String type;
        @SerializedName("broadcaster_type")
        private @NonNull String broadcasterType;
        private @NonNull String description;
        @SerializedName("profile_image_url")
        private @NonNull String profileImageUrl;
        @SerializedName("offline_image_url")
        private @NonNull String offlineImageUrl;
        @SerializedName("view_count")
        private long viewCount;
        private @Nullable String email;

    }

}
