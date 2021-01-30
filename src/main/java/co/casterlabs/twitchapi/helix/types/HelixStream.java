package co.casterlabs.twitchapi.helix.types;

import com.google.gson.annotations.SerializedName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class HelixStream {
    private @NonNull String id;

    private @NonNull String language;

    private @NonNull String title;

    private @NonNull String type;

    @SerializedName("user_name")
    private @NonNull String userName;

    @SerializedName("started_at")
    private @NonNull String startedAt;

    @SerializedName("viewer_count")
    private @NonNull String viewerCount;

    @SerializedName("thumbnail_url")
    private @NonNull String thumbnailUrl;

}