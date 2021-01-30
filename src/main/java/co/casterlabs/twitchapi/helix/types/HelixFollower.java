package co.casterlabs.twitchapi.helix.types;

import java.io.IOException;
import java.time.Instant;

import com.google.gson.annotations.SerializedName;

import co.casterlabs.apiutil.auth.ApiAuthException;
import co.casterlabs.apiutil.web.ApiException;
import co.casterlabs.twitchapi.helix.TwitchHelixAuth;
import co.casterlabs.twitchapi.helix.requests.HelixGetUsersRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class HelixFollower {
    @SerializedName("from_id")
    private @NonNull String id;

    @SerializedName("followed_at")
    private @NonNull Instant followedAt;

    public HelixUser getAsUser(@NonNull TwitchHelixAuth auth) throws ApiAuthException, ApiException, IOException {
        return new HelixGetUsersRequest(auth).addId(this.id).execute().get(0);
    }

}