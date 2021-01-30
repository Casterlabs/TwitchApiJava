package co.casterlabs.twitchapi.helix.types;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HelixCheermote {
    private List<CheermoteTier> tiers;
    private String prefix;
    private String type;

    @SerializedName("is_charitable")
    private boolean charitable;

    public CheermoteTier getTier(int value) {
        CheermoteTier possible = null;

        for (CheermoteTier tier : this.tiers) {
            if ((possible != null) && (tier.minBits < possible.minBits)) {
                continue;
            }

            if (value >= tier.minBits) {
                possible = tier;
            }
        }

        return possible;
    }

    @Getter
    @ToString
    public static class CheermoteTier {
        private String color;
        private String id;

        @SerializedName("min_bits")
        private int minBits;

        private CheermoteImages images;

        @SerializedName("can_cheer")
        private boolean cheerable;

        @SerializedName("show_in_bits_card")
        private boolean inBitsCard;

    }

    @Getter
    @ToString
    public static class CheermoteImages {
        private CheermoteImageSet light;
        private CheermoteImageSet dark;

    }

    @Getter
    @ToString
    public static class CheermoteImageSet {
        @SerializedName("static")
        private CheermoteImageSizes still;
        private CheermoteImageSizes animated;

    }

    @Getter
    @ToString
    public static class CheermoteImageSizes {
        @SerializedName("1")
        private String tinyImageLink;

        @SerializedName("2")
        private String smallImageLink;

        @SerializedName("3")
        private String mediumImageLink;

        @SerializedName("4")
        private String largeImageLink;

    }

}
