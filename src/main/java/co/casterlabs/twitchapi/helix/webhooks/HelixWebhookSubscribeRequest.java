package co.casterlabs.twitchapi.helix.webhooks;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;

import co.casterlabs.apiutil.auth.ApiAuthException;
import co.casterlabs.apiutil.web.ApiException;
import co.casterlabs.apiutil.web.AuthenticatedWebRequest;
import co.casterlabs.twitchapi.HttpUtil;
import co.casterlabs.twitchapi.ThreadHelper;
import co.casterlabs.twitchapi.helix.TwitchHelixAuth;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
public class HelixWebhookSubscribeRequest extends AuthenticatedWebRequest<Void, TwitchHelixAuth> {
    private long leaseSeconds = 864000; // https://dev.twitch.tv/docs/api/webhooks-reference#subscribe-tounsubscribe-from-events
    private boolean autoRefresh = false;
    private WebhookSubscribeMode mode;
    private Thread refreshThread;
    private String callback;
    private String topic;
    private String secret;

    public HelixWebhookSubscribeRequest(@NonNull WebhookSubscribeMode mode, String callback, String topic, @NonNull TwitchHelixAuth auth) {
        super(auth);

        this.mode = mode;
        this.callback = callback;
        this.topic = topic;
    }

    public HelixWebhookSubscribeRequest setLeaseMillis(long millis) {
        this.leaseSeconds = millis / 1000;
        return this;
    }

    public HelixWebhookSubscribeRequest setLease(long amount, TimeUnit unit) {
        this.leaseSeconds = unit.toSeconds(amount);
        return this;
    }

    @Override
    public Void execute() throws ApiException, ApiAuthException, IOException {
        this.auth.getRateLimiter().block();

        JsonObject payload = new JsonObject();

        payload.addProperty("hub.mode", this.mode.name().toLowerCase());
        payload.addProperty("hub.topic", this.topic);
        payload.addProperty("hub.callback", this.callback);
        payload.addProperty("hub.lease_seconds", this.leaseSeconds);

        if (this.secret != null) {
            payload.addProperty("hub.secret", this.secret);
        }

        HttpUtil.sendHttp(payload.toString(), "https://api.twitch.tv/helix/webhooks/hub", null, "application/json", this.auth).close();

        if (this.refreshThread != null) {
            this.refreshThread.interrupt();
        }

        if (this.autoRefresh) {
            this.refreshThread = ThreadHelper.executeAsyncLater(this.topic + " webhook", () -> {
                try {
                    this.execute();
                } catch (ApiException | IOException ignored) {}
            }, (this.leaseSeconds * 1000) + 500);
        }

        return null;
    }

    @Override
    protected void finalize() {
        if (this.refreshThread != null) {
            this.refreshThread.interrupt();
        }
    }

    public static enum WebhookSubscribeMode {
        SUBSCRIBE,
        UNSUBSCRIBE;
    }

}
