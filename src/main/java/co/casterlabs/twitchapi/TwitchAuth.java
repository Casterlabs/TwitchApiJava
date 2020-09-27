package co.casterlabs.twitchapi;

import java.io.IOException;

import co.casterlabs.apiutil.auth.AuthProvider;
import lombok.NonNull;

public interface TwitchAuth extends AuthProvider {

    public @NonNull ApiVersion getVersion();

    public boolean isValid() throws IOException;

}
