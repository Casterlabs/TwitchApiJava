package co.casterlabs.twitchapi;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.casterlabs.apiutil.ratelimit.BucketRateLimiter;
import co.casterlabs.apiutil.ratelimit.RateLimiter;
import co.casterlabs.twitchapi.serializers.InstantSerializer;
import lombok.Getter;

public class TwitchApi {
    // @formatter:off
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantSerializer())
            .create();
    // @formatter:on

    private static @Getter RateLimiter unauthenticatedRateLimiter = new BucketRateLimiter(30, 1, TimeUnit.MINUTES); // https://dev.twitch.tv/docs/api/guide#rate-limits

}
