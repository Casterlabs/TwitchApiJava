package co.casterlabs.twitchapi.helix.requests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import co.casterlabs.apiutil.auth.ApiAuthException;
import co.casterlabs.apiutil.web.ApiException;
import co.casterlabs.apiutil.web.AuthenticatedWebRequest;
import co.casterlabs.twitchapi.HttpUtil;
import co.casterlabs.twitchapi.TwitchApi;
import co.casterlabs.twitchapi.helix.TwitchHelixAuth;
import co.casterlabs.twitchapi.helix.types.HelixStream;
import lombok.NonNull;
import okhttp3.Response;

public class HelixGetStreamsRequest extends AuthenticatedWebRequest<List<HelixStream>, TwitchHelixAuth> {
    private Set<String> logins = new HashSet<>();
    private Set<String> ids = new HashSet<>();

    public HelixGetStreamsRequest(@NonNull TwitchHelixAuth auth) {
        super(auth);
    }

    public HelixGetStreamsRequest addLogin(@NonNull String login) {
        this.logins.add(login);
        return this;
    }

    public HelixGetStreamsRequest addId(@NonNull String id) {
        this.ids.add(id);
        return this;
    }

    @Override
    public List<HelixStream> execute() throws ApiException, ApiAuthException, IOException {
        this.auth.getRateLimiter().block();

        if (!this.ids.isEmpty() || !this.logins.isEmpty()) {
            Set<HelixStream> users = new HashSet<>();
            StringBuilder sb = new StringBuilder("https://api.twitch.tv/helix/streams?");

            this.ids.forEach((id) -> sb.append("&user_id=").append(id));
            this.logins.forEach((login) -> sb.append("&user_login=").append(login));

            String url = sb.toString().replaceFirst("&", "");
            Response response = HttpUtil.sendHttpGet(url, null, auth);
            JsonObject json = TwitchApi.GSON.fromJson(response.body().string(), JsonObject.class);

            response.close();

            if (response.code() == 200) {
                JsonArray data = json.getAsJsonArray("data");

                for (JsonElement e : data) {
                    users.add(TwitchApi.GSON.fromJson(e, HelixStream.class));
                }
            } else {
                throw new ApiException("Unable to get streams: " + json.get("message").getAsString());
            }

            return new ArrayList<>(users);
        } else {
            return Collections.emptyList();
        }
    }

}
