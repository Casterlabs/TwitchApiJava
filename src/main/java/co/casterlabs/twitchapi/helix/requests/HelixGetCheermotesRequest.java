package co.casterlabs.twitchapi.helix.requests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import co.casterlabs.apiutil.auth.ApiAuthException;
import co.casterlabs.apiutil.web.ApiException;
import co.casterlabs.apiutil.web.AuthenticatedWebRequest;
import co.casterlabs.twitchapi.HttpUtil;
import co.casterlabs.twitchapi.TwitchApi;
import co.casterlabs.twitchapi.helix.TwitchHelixAuth;
import co.casterlabs.twitchapi.helix.types.HelixCheermote;
import lombok.NonNull;
import okhttp3.Response;

public class HelixGetCheermotesRequest extends AuthenticatedWebRequest<List<HelixCheermote>, TwitchHelixAuth> {
    private String id = "";

    public HelixGetCheermotesRequest(@NonNull TwitchHelixAuth auth) {
        super(auth);
    }

    public HelixGetCheermotesRequest addId(@NonNull String id) {
        this.id = "?broadcaster_id=" + id;
        return this;
    }

    @Override
    public List<HelixCheermote> execute() throws ApiException, ApiAuthException, IOException {
        this.auth.getRateLimiter().block();

        String url = "https://api.twitch.tv/helix/bits/cheermotes" + this.id;

        Response response = HttpUtil.sendHttpGet(url, null, auth);
        JsonObject json = TwitchApi.GSON.fromJson(response.body().string(), JsonObject.class);

        response.close();

        if (response.code() == 200) {
            List<HelixCheermote> cheermotes = new ArrayList<>();

            JsonArray data = json.getAsJsonArray("data");

            for (JsonElement e : data) {
                cheermotes.add(TwitchApi.GSON.fromJson(e, HelixCheermote.class));
            }

            return cheermotes;
        } else {
            throw new ApiException("Unable to get users: " + json.get("message").getAsString());
        }
    }

}
