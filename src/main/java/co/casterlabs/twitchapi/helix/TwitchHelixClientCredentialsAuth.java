package co.casterlabs.twitchapi.helix;

import java.time.Instant;

import com.google.gson.JsonObject;

import co.casterlabs.apiutil.auth.ApiAuthException;
import co.casterlabs.apiutil.web.ApiException;
import co.casterlabs.twitchapi.HttpUtil;
import co.casterlabs.twitchapi.TwitchApi;
import lombok.NonNull;
import okhttp3.Response;

public class TwitchHelixClientCredentialsAuth extends TwitchHelixAuth {
    private String clientSecret;

    public TwitchHelixClientCredentialsAuth login(@NonNull String clientSecret, @NonNull String clientId) throws ApiAuthException {
        this.clientSecret = clientSecret;
        this.clientId = clientId;

        this.refresh();

        return this;
    }

    @Override
    public void refresh() throws ApiAuthException {
        if ((this.clientId != null) && (this.clientSecret != null)) {
            try {
                String url = String.format("https://id.twitch.tv/oauth2/token?client_id=%s&client_secret=%s&grant_type=client_credentials", this.clientId, this.clientSecret);
                Response response = HttpUtil.sendHttp("{}", url, null, null, null);
                JsonObject json = TwitchApi.GSON.fromJson(response.body().string(), JsonObject.class);

                if (response.code() == 200) {
                    this.accessToken = json.get("access_token").getAsString();
                    this.expireTime = Instant.now().plusSeconds(json.get("expires_in").getAsLong());
                } else {
                    this.clientSecret = null;
                    this.expireTime = Instant.now();
                    throw new ApiException("Unable to login: " + json.get("message").getAsString());
                }
            } catch (Exception e) {
                throw new ApiAuthException(e);
            }
        }
    }

}
