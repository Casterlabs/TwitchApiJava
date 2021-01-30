package co.casterlabs.twitchapi.helix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.casterlabs.twitchapi.helix.requests.HelixGetCheermotesRequest;
import co.casterlabs.twitchapi.helix.types.HelixCheermote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class CheermoteCache {
    private static final Pattern CHEERMOTE_PATTERN = Pattern.compile("[A-Za-z]+[0-9]+");

    private static Map<String, HelixCheermote> cheermoteMap = new HashMap<>();
    private static @Getter long lastRefresh = -1;

    public static CompletableFuture<Void> update(TwitchHelixAuth auth) {
        HelixGetCheermotesRequest request = new HelixGetCheermotesRequest(auth);

        return request.sendAsync().thenAccept((cheermotes) -> {
            Map<String, HelixCheermote> newCheermotes = new HashMap<>();

            for (HelixCheermote cheermote : cheermotes) {
                newCheermotes.put(cheermote.getPrefix(), cheermote);
            }

            cheermoteMap = newCheermotes;
            lastRefresh = System.currentTimeMillis();
        });
    }

    public static Map<String, CheermoteMatch> getCheermotesInMessage(String message) {
        List<String> matches = new ArrayList<>();

        Matcher m = CHEERMOTE_PATTERN.matcher(message);
        while (m.find()) {
            matches.add(m.group());
        }

        Map<String, CheermoteMatch> cheermotes = new HashMap<>();

        for (String match : matches) {
            String[] split = getNameValueJoint(match);

            if (split != null) {
                try {
                    String prefix = split[0];

                    HelixCheermote cheermote = cheermoteMap.get(prefix);

                    if (cheermote != null) {
                        int amount = Integer.parseInt(split[1]);

                        HelixCheermote.CheermoteTier tier = cheermote.getTier(amount);

                        if (tier != null) {
                            cheermotes.put(match, new CheermoteMatch(prefix, amount, cheermote, tier));
                        }
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        return cheermotes;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class CheermoteMatch {
        private String prefix;
        private int amount;
        private HelixCheermote cheermote;
        HelixCheermote.CheermoteTier tier;

    }

    private static String[] getNameValueJoint(String match) {
        char[] chars = match.toCharArray();

        for (int i = 0; i != match.length(); i++) {
            switch (chars[i]) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    return new String[] {
                            match.substring(0, i),
                            match.substring(i, match.length())
                    };
                }

                default:
                    continue;
            }
        }

        return null;
    }

}
