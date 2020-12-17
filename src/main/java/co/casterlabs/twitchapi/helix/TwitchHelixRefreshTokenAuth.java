package co.casterlabs.twitchapi.helix;

import java.time.Instant;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import co.casterlabs.apiutil.auth.ApiAuthException;
import co.casterlabs.apiutil.web.ApiException;
import co.casterlabs.twitchapi.HttpUtil;
import co.casterlabs.twitchapi.TwitchApi;
import lombok.Getter;
import lombok.NonNull;
import okhttp3.Response;

public class TwitchHelixRefreshTokenAuth extends TwitchHelixAuth {
    private @Getter @Nullable String refreshToken;
    private String clientSecret;

    public TwitchHelixRefreshTokenAuth login(@NonNull String clientSecret, @NonNull String clientId, @NonNull String refreshToken) throws ApiAuthException {
        this.refreshToken = refreshToken;
        this.clientSecret = clientSecret;
        this.clientId = clientId;

        this.refresh();

        return this;
    }

    @Override
    public void refresh() throws ApiAuthException {
        if ((this.clientId != null) && (this.clientSecret != null) && (this.refreshToken != null)) {
            try {
                String url = String.format("https://id.twitch.tv/oauth2/token?client_id=%s&client_secret=%s&grant_type=refresh_token&refresh_token=%s", this.clientId, this.clientSecret, this.refreshToken);
                Response response = HttpUtil.sendHttp("{}", url, null, null, null);
                JsonObject json = TwitchApi.GSON.fromJson(response.body().string(), JsonObject.class);

                response.close();

                if (response.code() == 200) {
                    this.refreshToken = json.get("refresh_token").getAsString();
                    this.accessToken = json.get("access_token").getAsString();
                    this.expireTime = Instant.now().plusSeconds(json.get("expires_in").getAsLong());
                } else {
                    this.refreshToken = null;
                    this.clientSecret = null;
                    this.expireTime = Instant.now().minusSeconds(1);
                    throw new ApiException("Unable to login: " + json.get("message").getAsString());
                }
            } catch (Exception e) {
                throw new ApiAuthException(e);
            }
        }
    }

}
