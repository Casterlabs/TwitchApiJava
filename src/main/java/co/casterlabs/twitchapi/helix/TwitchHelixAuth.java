package co.casterlabs.twitchapi.helix;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import co.casterlabs.apiutil.ratelimit.BucketRateLimiter;
import co.casterlabs.apiutil.ratelimit.RateLimiter;
import co.casterlabs.twitchapi.ApiVersion;
import co.casterlabs.twitchapi.HttpUtil;
import co.casterlabs.twitchapi.ThreadHelper;
import co.casterlabs.twitchapi.TwitchAuth;
import lombok.Getter;
import lombok.NonNull;
import okhttp3.Request;
import okhttp3.Response;

public abstract class TwitchHelixAuth implements TwitchAuth {
    private @Getter RateLimiter rateLimiter = new BucketRateLimiter(800, 1, TimeUnit.MINUTES); // https://dev.twitch.tv/docs/api/guide#rate-limits
    protected @NonNull @Getter Instant expireTime = Instant.now();
    protected String accessToken;
    protected String clientId;

    public void revoke() throws IOException {
        String url = String.format("https://id.twitch.tv/oauth2/revoke?client_id=%s&token=%s", this.clientId, this.accessToken);
        HttpUtil.sendHttp("{}", url, null, null, null);
        this.accessToken = null;
        this.clientId = null;
    }

    @Override
    public void authenticateRequest(@NonNull Request.Builder request) {
        request.addHeader("Authorization", "Bearer " + this.accessToken);
        request.addHeader("Client-Id", this.clientId);
    }

    public void autoRefresh() {
        ThreadHelper.executeAsyncLater("Auth refresh", () -> {
            try {
                this.refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, this.expireTime.toEpochMilli() - TimeUnit.SECONDS.toMillis(60)); // Refresh 1 minute before
    }

    @Override
    public boolean isValid() throws IOException {
        if ((this.accessToken != null) && (this.clientId != null)) {
            Response response = HttpUtil.sendHttpGet("https://id.twitch.tv/oauth2/validate", null, this);

            response.close();

            return response.code() == 200;
        }

        return false;
    }

    @Override
    public @NonNull ApiVersion getVersion() {
        return ApiVersion.HELIX;
    }

}
