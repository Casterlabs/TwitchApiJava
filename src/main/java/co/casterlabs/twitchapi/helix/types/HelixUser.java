package co.casterlabs.twitchapi.helix.types;

import org.jetbrains.annotations.Nullable;

import com.google.gson.annotations.SerializedName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class HelixUser {
    private @NonNull String id;

    private @NonNull String login;

    private @NonNull String description;

    private @NonNull String type;

    private @Nullable String email;

    @SerializedName("display_name")
    private @NonNull String displayName;

    @SerializedName("broadcaster_type")
    private @NonNull String broadcasterType;

    @SerializedName("profile_image_url")
    private @NonNull String profileImageUrl;

    @SerializedName("offline_image_url")
    private @NonNull String offlineImageUrl;

    @SerializedName("view_count")
    private long viewCount;

}